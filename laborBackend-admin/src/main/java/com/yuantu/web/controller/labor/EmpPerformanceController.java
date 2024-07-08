package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.mapper.EmpPerformanceMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.LoanWorkerMapper;
import com.yuantu.labor.service.IEmpPerformanceService;
import com.yuantu.labor.vo.CountEmpPerformanceVO;
import com.yuantu.labor.vo.EmpPerformanceScreenVO;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
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
 * 员工绩效Controller
 * 
 * @author ruoyi
 * @date 2023-09-26
 */

@RestController
@RequestMapping("/labor/empPerformance")
@Api("员工绩效管理")
public class EmpPerformanceController extends BaseController
{
    @Autowired
    private IEmpPerformanceService empPerformanceService;

    @Autowired
    private EmpPerformanceMapper empPerformanceMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    /**
     * 查询员工绩效列表
     */
 // @PreAuthorize("@ss.hasPermi('labor:empPerformance:list')")
    @ApiOperation("查询员工绩效列表")
    @GetMapping("/list")
    public TableDataInfo list(EmpPerformance empPerformance)
    {
        List<Long> empIds = new ArrayList<>();
        List<Long> loanIds = new ArrayList<>();
        Integer query = null;
        if (empPerformance.getPerfEmpName() != null || empPerformance.getIsRelated() != null
                || empPerformance.getPerfDeptId() != null) {
            query = 1;
            String perfEmpName = empPerformance.getPerfEmpName();
            Integer isRelated = empPerformance.getIsRelated();
            Long perfDeptId = empPerformance.getPerfDeptId();
            empIds = employeeMapper.findEmpIdsByParams(perfEmpName, isRelated, null, perfDeptId);
            loanIds = loanWorkerMapper.findLoanIdsByParams(perfEmpName, isRelated, null, perfDeptId);
        }
        List<Long> performIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(empIds)) {
            performIds = empPerformanceMapper.findEmpPerformanceIdByEmpIdsAndFlag(empIds, 1);
        }
        if (!CollectionUtils.isEmpty(loanIds)) {
            performIds.addAll(empPerformanceMapper.findEmpPerformanceIdByEmpIdsAndFlag(loanIds, 2));
        }
        startPage();
        List<EmpPerformance> list = empPerformanceService.selectEmpPerformanceList(empPerformance, performIds, query);
        return getDataTable(list);
    }


    /**
     * 筛选查询员工绩效列表
     */
    // @PreAuthorize("@ss.hasPermi('labor:empPerformance:list')")
    @GetMapping("/screen")
    @ApiOperation("筛选查询员工绩效列表")
    public TableDataInfo  selectEmpPerformanceListByScreen(EmpPerformanceScreenVO empPerformanceScreenVO){

        List<Long> empIds = new ArrayList<>();
        List<Long> loanIds = new ArrayList<>();
        Integer query = null;
        if (empPerformanceScreenVO.getPerfEmpName() != null || empPerformanceScreenVO.getIsRelated() != null
                || empPerformanceScreenVO.getPerfDeptId() != null) {
            query = 1;
            String perfEmpName = empPerformanceScreenVO.getPerfEmpName();
            Integer isRelated = empPerformanceScreenVO.getIsRelated();
            Long perfDeptId = empPerformanceScreenVO.getPerfDeptId();
            empIds = employeeMapper.findEmpIdsByParams(perfEmpName, isRelated, null, perfDeptId);
            loanIds = loanWorkerMapper.findLoanIdsByParams(perfEmpName, isRelated, null, perfDeptId);
        }
        List<Long> performIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(empIds)) {
            performIds = empPerformanceMapper.findEmpPerformanceIdByEmpIdsAndFlag(empIds, 1);
        }
        if (!CollectionUtils.isEmpty(loanIds)) {
            performIds.addAll(empPerformanceMapper.findEmpPerformanceIdByEmpIdsAndFlag(loanIds, 2));
        }
        startPage();
        List<EmpPerformance> list = empPerformanceService.selectEmpPerformanceListByScreen(empPerformanceScreenVO, performIds, query);
        return  getDataTable(list);
    }

    /**
     * 统计查询员工绩效列表
     *
     * @param empPerformance 员工绩效
     * @return 绩效统计集合
     */
  //  @PreAuthorize("@ss.hasPermi('labor:empPerformance:list')")
    @ApiOperation("统计查询员工绩效列表")
    @GetMapping("/countEmps")
    public TableDataInfo  countEmps(EmpPerformance empPerformance)
    {
        startPage();
        List<CountEmpPerformanceVO> list = empPerformanceService.countEmpPerformanceList(empPerformance);
        return getDataTable(list);
    }

    /**
     * 统计查询员工绩效详情
     *
     * @param empPerformance 员工绩效 （没有使用）
     * @return 绩效统计集合
     */
   // @PreAuthorize("@ss.hasPermi('labor:empPerformance:list')")
    @ApiOperation("统计查询员工绩效详情")
    @GetMapping("/details")
    public AjaxResult  countEmpPerformanceDetails(EmpPerformance empPerformance)
    {
        return success(empPerformanceService.countEmpPerformanceDetails(empPerformance));
    }




//    @PreAuthorize("@ss.hasPermi('labor:empPerformance:import')")
    @ApiOperation("下载员工绩效导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {
        empPerformanceService.downloadExcelTemplate(response);
    }

    @ApiOperation("导入员工绩效信息")
    @PreAuthorize("@ss.hasPermi('labor:empPerformance:import')")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file, @RequestParam Integer flag) {
        String username = getUsername();
        Long userId = getUserId();
        return success(empPerformanceService.uploadEmpPerformanceInfosFile(file,userId,username, flag));
    }

    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出员工绩效信息")
   //  @PreAuthorize("@ss.hasPermi('labor:empPerformance:export')")
    @Log(title = "员工绩效", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        empPerformanceService.exportDivide(response, export);
    }


    /**
     * 导出员工绩效列表
     */
   // @PreAuthorize("@ss.hasPermi('labor:empPerformance:export')")
    @Log(title = "员工绩效", businessType = BusinessType.EXPORT)
    @ApiOperation("导出员工绩效列表")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<EmpPerformance>  list =  empPerformanceService.findExportInfos(export);
        ExcelUtil<EmpPerformance> util = new ExcelUtil<>(EmpPerformance.class);
        Field[] declaredFields = EmpPerformance.class.getDeclaredFields();
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
        util.exportExcel(response, list, "员工绩效数据");;
    }

    /**
     * 获取员工绩效详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:empPerformance:query')")
    @GetMapping(value = "/{perfId}")
    @ApiOperation("获取员工绩效详细信息")
    public AjaxResult getInfo(@PathVariable("perfId") Long perfId)
    {
        return success(empPerformanceService.selectEmpPerformanceByPerfId(perfId));
    }

    /**
     * 新增员工绩效
     */
   // @PreAuthorize("@ss.hasPermi('labor:empPerformance:add')")
    @Log(title = "员工绩效", businessType = BusinessType.INSERT)
    @ApiOperation("新增员工绩效")
    @PostMapping
    public AjaxResult add(@RequestBody EmpPerformance empPerformance)
    {
        empPerformance.setCreateBy(getUsername());
        empPerformance.setCreateTime(DateUtils.getNowDate());
        return toAjax(empPerformanceService.insertEmpPerformance(empPerformance));
    }

    /**
     * 修改员工绩效
     */
    @PreAuthorize("@ss.hasPermi('labor:empPerformance:edit')")
    @Log(title = "员工绩效", businessType = BusinessType.UPDATE)
    @ApiOperation("修改员工绩效")
    @PutMapping
    public AjaxResult edit(@RequestBody EmpPerformance empPerformance)
    {
        empPerformance.setUpdateBy(getUsername());
        empPerformance.setUpdateTime(DateUtils.getNowDate());
        return toAjax(empPerformanceService.updateEmpPerformance(empPerformance));
    }

    /**
     * 删除员工绩效
     */
    @PreAuthorize("@ss.hasPermi('labor:empPerformance:remove')")
    @Log(title = "员工绩效", businessType = BusinessType.DELETE)
    @ApiOperation("删除员工绩效")
	@DeleteMapping("/{perfIds}")
    public AjaxResult remove(@PathVariable Long[] perfIds)
    {
        return toAjax(empPerformanceService.deleteEmpPerformanceByPerfIds(perfIds));
    }
}
