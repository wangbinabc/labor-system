package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.mapper.EmpAttendanceMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.LoanWorkerMapper;
import com.yuantu.labor.service.IEmpAttendanceService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考勤Controller
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@RestController
@Api("考勤管理")
@RequestMapping("/labor/attendance")
public class EmpAttendanceController extends BaseController {

    @Autowired
    private IEmpAttendanceService empAttendanceService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    @Autowired
    private EmpAttendanceMapper attendanceMapper;

    /**
     * 查询考勤列表
     */
    //  @PreAuthorize("@ss.hasPermi('labor:attendance:list')")
    @GetMapping("/list")
    @ApiOperation("查询考勤列表")
    public TableDataInfo list(EmpAttendance empAttendance) {
        startPage();
        List<EmpAttendanceListVO> list = empAttendanceService.selectEmpAttendanceList(empAttendance);
        return getDataTable(list);
    }

    /**
     * 筛选查询考勤列表
     */
    // @PreAuthorize("@ss.hasPermi('labor:attendance:list')")
    @GetMapping("/screen")
    @ApiOperation("筛选查询考勤列表")
    public TableDataInfo selectEmpAttendanceListByScreen(EmpAttendanceScreenVO empAttendanceScreenVO) {

        List<Long> empIds = new ArrayList<>();
        List<Long> loanIds = new ArrayList<>();
        Integer query = null;
        if (empAttendanceScreenVO.getAttendEmpName() != null || empAttendanceScreenVO.getIsRelated() != null
                || empAttendanceScreenVO.getEmpCategory() != null || empAttendanceScreenVO.getDepartmentId() != null) {
            query = 1;
            String attendEmpName = empAttendanceScreenVO.getAttendEmpName();
            Integer isRelated = empAttendanceScreenVO.getIsRelated();
            String empCategory = empAttendanceScreenVO.getEmpCategory();
            Long departmentId = empAttendanceScreenVO.getDepartmentId();
            empIds = employeeMapper.findEmpIdsByParams(attendEmpName, isRelated, empCategory, departmentId);
            loanIds = loanWorkerMapper.findLoanIdsByParams(attendEmpName, isRelated, empCategory, departmentId);
        }
        List<Long> attendIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(empIds)) {
            attendIds = attendanceMapper.findEmpAttendIdByEmpIdsAndFlag(empIds, 1);
        }
        if (!CollectionUtils.isEmpty(loanIds)) {
            attendIds.addAll(attendanceMapper.findEmpAttendIdByEmpIdsAndFlag(loanIds, 2));
        }
        startPage();
        List<EmpAttendance> list = empAttendanceService.selectEmpAttendanceListByScreen(empAttendanceScreenVO, attendIds, query);
        return getDataTable(list);
    }

//    /**
//     * 按年统计查询考勤列表
//     */
//     @PreAuthorize("@ss.hasPermi('labor:attendance:countYear')")
//    @GetMapping("/countYear")
//     @ApiOperation("查询考勤列表")
//    public TableDataInfo countYear(String nameOrIdcard, String year)
//    {
//        startPage();
//        List<AttendanceCountVO>  list = empAttendanceService.countEmpsAttendanceListByParams(nameOrIdcard,year,null);
//        return getDataTable(list);
//    }

    @PreAuthorize("@ss.hasPermi('labor:attendance:count')")
    @GetMapping("/countYearMonth")
    @ApiOperation("按年月统计查询考勤列表")
    public TableDataInfo countYearMonth(String nameOrIdcard, String year, String month) {
        startPage();
        List<AttendanceCountVO> list = empAttendanceService.countEmpsAttendanceListByParams(nameOrIdcard, year, month);
        return getDataTable(list);
    }


    @GetMapping("/total/countYear")
    @ApiOperation("按年统计考勤各状态数量")
    public AjaxResult countEmpAttendanceByAttendanceYear(String year) {
        List<AttendanceYearVO> list = empAttendanceService.countEmpAttendanceByAttendanceYear(year);
        Map<String, List<AttendanceYearVO>> map = list.stream().collect(Collectors.
                groupingBy(AttendanceYearVO::getRecordYear));
        return AjaxResult.success(map);
    }


    @GetMapping("/total/count")
    @ApiOperation("考勤统计情况")
    public TableDataInfo totalCount(String keyword, String yearNum, Integer pageSize, Integer pageNum) {
        // startPage();
        //List<AttendCountVO> list = empAttendanceService.totalCountAttend(keyword, yearNum);
        return empAttendanceService.totalCountAttend(keyword, yearNum, pageSize, pageNum);
    }

    @GetMapping("/total/detail")
    @ApiOperation("个人考勤详情")
    public AjaxResult totalCountDetail(@RequestParam Long empId, @RequestParam Date time, @RequestParam Integer flag) {

        return success(empAttendanceService.totalCountDetail(empId, time, flag));
    }


    //   @PreAuthorize("@ss.hasPermi('labor:attendance:import')")
    @ApiOperation("下载考勤导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response) {

        empAttendanceService.downloadExcelTemplate(response);
    }

    @ApiOperation("导入考勤信息")
    @PreAuthorize("@ss.hasPermi('labor:attendance:import')")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file, @Param("flag") Integer flag) {
        String username = getUsername();
        Long userId = getUserId();
        return success(empAttendanceService.uploadAttendanceInfosFile(file, userId, username, flag));
    }



    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出员工考勤信息")
//    @PreAuthorize("@ss.hasPermi('labor:attendance:export')")
    @Log(title = "员工考勤", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        empAttendanceService.exportDivide(response, export);
    }

    /**
     * 导出考勤列表
     */
    //  @PreAuthorize("@ss.hasPermi('labor:attendance:export')")
    @Log(title = "考勤", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出考勤列表")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<EmpAttendance> list = empAttendanceService.findExportInfos(export);
        ExcelUtil<EmpAttendance> util = new ExcelUtil<>(EmpAttendance.class);
        Field[] declaredFields = EmpAttendance.class.getDeclaredFields();
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
        util.exportExcel(response, list, "员工考勤数据");
        ;
    }


    /**
     * 获取考勤详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:attendance:query')")
    @GetMapping(value = "/{attendId}")
    @ApiOperation("获取考勤详细信息")
    public AjaxResult getInfo(@PathVariable("attendId") Long attendId) {
        return success(empAttendanceService.selectEmpAttendanceByAttendId(attendId));
    }

    /**
     * 新增考勤
     */
    @PreAuthorize("@ss.hasPermi('labor:attendance:add')")
    @Log(title = "考勤", businessType = BusinessType.INSERT)
    @ApiOperation("新增考勤")
    @PostMapping
    public AjaxResult add(@RequestBody EmpAttendance empAttendance) {
        empAttendance.setCreateBy(getUsername());
        empAttendance.setCreateTime(DateUtils.getNowDate());
        return toAjax(empAttendanceService.insertEmpAttendance(empAttendance));
    }

    /**
     * 修改考勤
     */
    @PreAuthorize("@ss.hasPermi('labor:attendance:edit')")
    @Log(title = "考勤", businessType = BusinessType.UPDATE)
    @ApiOperation("修改考勤")
    @PutMapping
    public AjaxResult edit(@RequestBody EmpAttendance empAttendance) {
        empAttendance.setUpdateBy(getUsername());
        empAttendance.setUpdateTime(DateUtils.getNowDate());
        return toAjax(empAttendanceService.updateEmpAttendance(empAttendance));
    }

    /**
     * 删除考勤
     */
    @PreAuthorize("@ss.hasPermi('labor:attendance:remove')")
    @Log(title = "考勤", businessType = BusinessType.DELETE)
    @ApiOperation("删除考勤")
    @DeleteMapping("/{attendIds}")
    public AjaxResult remove(@PathVariable Long[] attendIds) {
        return toAjax(empAttendanceService.deleteEmpAttendanceByAttendIds(attendIds));
    }
}
