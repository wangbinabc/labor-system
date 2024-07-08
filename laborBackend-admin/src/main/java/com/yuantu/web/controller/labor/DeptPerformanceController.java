package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.service.IDeptPerformanceService;
import com.yuantu.labor.vo.*;
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
 * 部门绩效Controller
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@RestController
@RequestMapping("/labor/deptPerformance")
@Api("部门绩效管理")
public class DeptPerformanceController extends BaseController
{
    @Autowired
    private IDeptPerformanceService deptPerformanceService;

    /**
     * 根据条件查询部门绩效列表
     */
   @PreAuthorize("@ss.hasPermi('labor:deptPerformance:list')")
    @GetMapping("/list")
    @ApiOperation("查询部门绩效列表")
    public TableDataInfo list(DeptPerformanceParamsVO deptPerformanceParamsVO)
    {
        startPage();
        List<DeptPerformance> list = deptPerformanceService.selectDeptPerformanceListByParamsVO(deptPerformanceParamsVO);
        return getDataTable(list);
    }


    /**
     * 部门绩效统计列表-按年度季度
     * dpCycle参数值为0统计年度,否则统计季度
     */
    // @PreAuthorize("@ss.hasPermi('labor:deptPerformance:list')")
    @GetMapping("/byCycle")
    @ApiOperation("部门绩效统计列表-按年度季度:dpCycle参数值为0统计年度,不带参数统计季度")
    public AjaxResult  selectDeptPerformanceListByCycle(DeptPerformance deptPerformance){
        List<DeptPerformance> list = deptPerformanceService.selectDeptPerformanceListByCycle(deptPerformance);
        return  success(list);
    }

    /**
     * 筛选查询部门绩效列表
     */
    // @PreAuthorize("@ss.hasPermi('labor:deptPerformance:list')")
    @GetMapping("/screen")
    @ApiOperation("筛选查询部门绩效列表")
    public TableDataInfo  selectDeptPerformanceListByScreen(DeptPerformanceScreenVO deptPerformanceScreenVO){
        startPage();
        List<DeptPerformance> list = deptPerformanceService.selectDeptPerformanceListByScreen(deptPerformanceScreenVO);
        return  getDataTable(list);
    }



    /**
     * 统计查询年度和季度部门绩效列表
     */
   // @PreAuthorize("@ss.hasPermi('labor:deptPerformance:count')")
    @GetMapping("/count")
   @ApiOperation("统计查询年度和季度部门绩效列表")
    public AjaxResult count(DeptPerformance deptPerformance)
    {
      //  startPage();
        List<CountDeptPerformanceVO> list = deptPerformanceService.countDeptPerformances(deptPerformance);
      //  return getDataTable(list);
        return success(list);
    }



   // @PreAuthorize("@ss.hasPermi('labor:deptPerformance:import')")
    @ApiOperation("下载部门绩效导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {
        deptPerformanceService.downloadExcelTemplate(response);
    }

    @ApiOperation("导入部门绩效信息")
  //  @PreAuthorize("@ss.hasPermi('labor:deptPerformance:import')")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(deptPerformanceService.uploadDeptPerformanceInfosFile(file,userId,username));
    }

    /**
     * 导出部门绩效列表
     */
    @PreAuthorize("@ss.hasPermi('labor:deptPerformance:export')")
    @Log(title = "部门绩效", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出部门绩效列表")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<DeptPerformance> list = deptPerformanceService.findExportInfos(export);
        ExcelUtil<DeptPerformance> util = new ExcelUtil<DeptPerformance>(DeptPerformance.class);
        Field[] declaredFields = DeptPerformance.class.getDeclaredFields();
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
        util.exportExcel(response, list, "部门绩效数据");;
    }


    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出部门绩效数据信息")
      @PreAuthorize("@ss.hasPermi('labor:deptPerformance:export')")
    @Log(title = "部门绩效数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        deptPerformanceService.exportDivide(response, export);
    }

    /**
     * 获取部门绩效详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:deptPerformance:query')")
    @GetMapping(value = "/{dpId}")
    @ApiOperation("获取部门绩效详细信息")
    public AjaxResult getInfo(@PathVariable("dpId") Long dpId)
    {
        return success(deptPerformanceService.selectDeptPerformanceByDpId(dpId));
    }



    /**
     * 新增部门绩效
     */
    @PreAuthorize("@ss.hasPermi('labor:deptPerformance:add')")
    @Log(title = "部门绩效", businessType = BusinessType.INSERT)
    @ApiOperation("新增部门绩效")
    @PostMapping
    public AjaxResult add(@RequestBody DeptPerformance deptPerformance)
    {
     //   deptPerformance.setUpdateBy(getUsername());
        deptPerformance.setCreateBy(getUsername());
        deptPerformance.setCreateTime(DateUtils.getNowDate());
        return toAjax(deptPerformanceService.insertDeptPerformance(deptPerformance));
    }




    /**
     * 修改部门绩效
     */
    @PreAuthorize("@ss.hasPermi('labor:deptPerformance:edit')")
    @Log(title = "部门绩效", businessType = BusinessType.UPDATE)
    @ApiOperation("修改部门绩效")
    @PutMapping
    public AjaxResult edit(@RequestBody DeptPerformance deptPerformance)
    {
        deptPerformance.setUpdateBy(getUsername());
        deptPerformance.setUpdateTime(DateUtils.getNowDate());
        return toAjax(deptPerformanceService.updateDeptPerformance(deptPerformance));
    }

    /**
     * 删除部门绩效
     */
    @PreAuthorize("@ss.hasPermi('labor:deptPerformance:remove')")
    @Log(title = "部门绩效", businessType = BusinessType.DELETE)
    @ApiOperation("删除部门绩效")
	@DeleteMapping("/{dpIds}")
    public AjaxResult remove(@PathVariable Long[] dpIds)
    {
        return toAjax(deptPerformanceService.deleteDeptPerformanceByDpIds(dpIds));
    }
}
