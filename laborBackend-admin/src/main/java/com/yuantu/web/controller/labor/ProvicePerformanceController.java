package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.ProvicePerformance;
import com.yuantu.labor.service.IProvicePerformanceService;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.ProvicePerformanceScreenVO;
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
 * 省公司考核Controller
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@RestController
@RequestMapping("/labor/properformance")
@Api("省公司考核管理")
public class ProvicePerformanceController extends BaseController
{
    @Autowired
    private IProvicePerformanceService provicePerformanceService;

    /**
     * 查询省公司考核列表
     */
    @ApiOperation("不分页查询省公司考核列表")
   // @PreAuthorize("@ss.hasPermi('labor:properformance:list')")
    @GetMapping("/list")
    public AjaxResult list(ProvicePerformance provicePerformance)
    {
      //  startPage();
        List<ProvicePerformance> list = provicePerformanceService.selectProvicePerformanceList(provicePerformance);
       // return getDataTable(list);
        return success(list);
    }

    /**
     * 筛选查询省公司考核列表
     */
    // @PreAuthorize("@ss.hasPermi('labor:properformance:list')")
    @GetMapping("/screen")
    @ApiOperation("筛选查询省公司考核列表")
    public TableDataInfo  selectProvicePerformanceListByScreen(ProvicePerformanceScreenVO provicePerformanceScreenVO){
        startPage();
        List<ProvicePerformance> list = provicePerformanceService.selectProvicePerformanceListByScreen(provicePerformanceScreenVO);
        return  getDataTable(list);
    }




  // @PreAuthorize("@ss.hasPermi('labor:properformance:import')")
    @ApiOperation("下载省公司考核导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {
        provicePerformanceService.downloadExcelTemplate(response);
    }

    @ApiOperation("导入省公司考核信息")
    @PreAuthorize("@ss.hasPermi('labor:properformance:import')")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(provicePerformanceService.uploadProvicePerformanceInfosFile(file,userId,username));
    }

    /**
     * 导出省公司考核列表
     */
    @ApiOperation("导出省公司考核列表")
    @PreAuthorize("@ss.hasPermi('labor:properformance:export')")
    @Log(title = "省公司考核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<ProvicePerformance>  list =  provicePerformanceService.findExportInfos(export);
        ExcelUtil<ProvicePerformance> util = new ExcelUtil<>(ProvicePerformance.class);
        Field[] declaredFields = ProvicePerformance.class.getDeclaredFields();
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
        util.exportExcel(response, list, "省公司考核数据");;
    }

    /**
     * 表格拆分导出省公司考核数据信息
     */
    @ApiOperation("表格拆分导出省公司考核数据信息")
    @PreAuthorize("@ss.hasPermi('labor:properformance:export')")
    @Log(title = "省公司考核数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        provicePerformanceService.exportDivide(response, export);
    }

    /**
     * 获取省公司考核详细信息
     */
    @ApiOperation("获取省公司考核详细信息")
    @PreAuthorize("@ss.hasPermi('labor:properformance:query')")
    @GetMapping(value = "/{ppId}")
    public AjaxResult getInfo(@PathVariable("ppId") Long ppId)
    {
        return success(provicePerformanceService.selectProvicePerformanceByPpId(ppId));
    }

    /**
     * 新增省公司考核
     */
    @ApiOperation("新增省公司考核")
    @PreAuthorize("@ss.hasPermi('labor:properformance:add')")
    @Log(title = "省公司考核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProvicePerformance provicePerformance)
    {
     //   provicePerformance.setUpdateBy(getUsername());
        provicePerformance.setCreateBy(getUsername());
        provicePerformance.setCreateTime(DateUtils.getNowDate());
        return toAjax(provicePerformanceService.insertProvicePerformance(provicePerformance));
    }

    /**
     * 修改省公司考核
     */
    @ApiOperation("修改省公司考核")
    @PreAuthorize("@ss.hasPermi('labor:properformance:edit')")
    @Log(title = "省公司考核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProvicePerformance provicePerformance)
    {
        provicePerformance.setUpdateBy(getUsername());
        provicePerformance.setUpdateTime(DateUtils.getNowDate());
        return toAjax(provicePerformanceService.updateProvicePerformance(provicePerformance));
    }

    /**
     * 删除省公司考核
     */
    @ApiOperation("删除省公司考核")
    @PreAuthorize("@ss.hasPermi('labor:properformance:remove')")
    @Log(title = "省公司考核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ppIds}")
    public AjaxResult remove(@PathVariable Long[] ppIds)
    {
        return toAjax(provicePerformanceService.deleteProvicePerformanceByPpIds(ppIds));
    }
}
