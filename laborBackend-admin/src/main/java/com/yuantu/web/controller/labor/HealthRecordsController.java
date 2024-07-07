package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.HealthRecords;
import com.yuantu.labor.service.IHealthRecordsService;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.HealthRecordsSimpleVO;
import com.yuantu.labor.vo.HealthRecordsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工健康档案Controller
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
@RestController
@RequestMapping("/labor/records")
@Api("员工健康档案管理")
public class HealthRecordsController extends BaseController
{
    @Autowired
    private IHealthRecordsService healthRecordsService;

    /**
     * 查询员工健康档案列表
     */
  // @PreAuthorize("@ss.hasPermi('labor:records:list')")
    @GetMapping("/list")
    @ApiOperation("查询员工健康档案列表")
    public TableDataInfo list(HealthRecordsVO healthRecordsVO)
    {
        startPage();
        List<HealthRecords> list = healthRecordsService.selectHealthRecordsList(healthRecordsVO);
        return getDataTable(list);
    }

   //  @PreAuthorize("@ss.hasPermi('labor:records:import')")
    @ApiOperation("下载员工福利导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {

        ExcelUtil<HealthRecordsSimpleVO> util = new ExcelUtil<>(HealthRecordsSimpleVO.class);
        util.importTemplateExcel(response, "员工健康档案导入模板");
    }

    @ApiOperation("导入员工健康档案信息")
    @PostMapping("/import")
    @PreAuthorize("@ss.hasPermi('labor:records:import')")
    public AjaxResult importInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(healthRecordsService.uploadHealthRecordsInfosFile(file,userId,username));
    }

    /**
     * 导出员工健康档案列表
     */
    @PreAuthorize("@ss.hasPermi('labor:records:export')")
    @Log(title = "员工健康档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出员工健康档案列表")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<HealthRecords>  list =  healthRecordsService.findExportInfos(export);
        ExcelUtil<HealthRecords> util = new ExcelUtil<>(HealthRecords.class);
        Field[] declaredFields = HealthRecords.class.getDeclaredFields();
        List<String> fieldNamesWithExcel = new ArrayList<>();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Excel.class)) {
                fieldNamesWithExcel.add(field.getName());
            }
        }
        List<String> excludeFieldNames = new ArrayList<>();
        List<String> fieldNames = export.getFieldNames();
        if (!CollectionUtils.isEmpty(fieldNames)) {
            for (String s : fieldNamesWithExcel) {
                if (!fieldNames.contains(s)) {
                    excludeFieldNames.add(s);
                }
            }
            if (!CollectionUtils.isEmpty(excludeFieldNames)) {
                String[] excludeArr = new String[excludeFieldNames.size()];
                excludeFieldNames.toArray(excludeArr);
                util.hideColumn(excludeArr);
            }
        }
        util.exportExcel(response, list, "员工健康档案数据");;
    }

    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出员工健康档案信息")
   //  @PreAuthorize("@ss.hasPermi('labor:records:export')")
    @Log(title = "员工健康档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        healthRecordsService.exportDivide(response, export);
    }

    /**
     * 获取员工健康档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:records:query')")
    @GetMapping(value = "/{healthId}")
    @ApiOperation("获取员工健康档案详细信息")
    public AjaxResult getInfo(@PathVariable("healthId") Long healthId)
    {
        return success(healthRecordsService.selectHealthRecordsByHealthId(healthId));
    }

    /**
     * 新增员工健康档案
     */
   @PreAuthorize("@ss.hasPermi('labor:records:add')")
    @Log(title = "员工健康档案", businessType = BusinessType.INSERT)
    @ApiOperation("新增员工健康档案,annexPath参数为上传附件表的ID")
    @PostMapping
    public AjaxResult add(@RequestBody HealthRecords healthRecords)
    {
     //   healthRecords.setUpdateBy(getUsername());
        healthRecords.setCreateBy(getUsername());
        healthRecords.setCreateTime(DateUtils.getNowDate());
        return toAjax(healthRecordsService.insertHealthRecords(healthRecords));
    }

    /**
     * 修改员工健康档案
     */
    @ApiOperation("修改员工健康档案,annexPath参数为上传附件表的ID")
    @PreAuthorize("@ss.hasPermi('labor:records:edit')")
    @Log(title = "员工健康档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HealthRecords healthRecords)
    {
        healthRecords.setUpdateBy(getUsername());
        healthRecords.setUpdateTime(DateUtils.getNowDate());
        return toAjax(healthRecordsService.updateHealthRecords(healthRecords));
    }

    /**
     * 删除员工健康档案
     */
    @ApiOperation("删除员工健康档案,annexPath参数为上传附件表的ID")
    @PreAuthorize("@ss.hasPermi('labor:records:remove')")
    @Log(title = "员工健康档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{healthIds}")
    public AjaxResult remove(@PathVariable Long[] healthIds)
    {
        return toAjax(healthRecordsService.deleteHealthRecordsByHealthIds(healthIds));
    }
}
