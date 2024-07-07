
package com.yuantu.web.controller.labor;

import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.service.IEmpInfoService;
import com.yuantu.labor.service.IEmpSalaryService;
import com.yuantu.labor.service.IEmpTrainService;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.service.impl.SysDictDataServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工画像信息Controller
 *
 * @author ruoyi
 * @date 2023-10-27
 */
@Api("员工画像统计管理")
@RestController
@RequestMapping("/labor/info")
public class EmpInfoController extends BaseController {

    @Autowired
    private IEmpInfoService empInfoService;

    @Autowired
    private IEmpTrainService empTrainService;
    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private SysDictDataServiceImpl sysDictDataService;

    @Autowired
    private IEmpSalaryService empSalaryService;

    @Autowired
    private DepartmentMapper departmentMapper;


    @ApiOperation("查询员工画像列表")
    //  @PreAuthorize("@ss.hasPermi('labor:employee:list')")
    @GetMapping("/list")
    public TableDataInfo listInfo(EmpSearchSimpleVO employee) {

        if (employee.getUnitId() != null) {
            List<Long> deptIds = departmentMapper.findDepartmentInfosByUnitIds(Collections.singletonList(employee.getUnitId())).
                    stream().map(Department::getDeptId).collect(Collectors.toList());
            List<Long> departmentIds = employee.getDepartmentIds();
            if (!CollectionUtils.isEmpty(departmentIds)) {
                departmentIds.addAll(deptIds);
            } else {
                departmentIds = deptIds;
            }
            employee.setDepartmentIds(departmentIds);
        }
        if (employee.getDeptId() != null) {
            List<Long> departmentIds = employee.getDepartmentIds();
            if (!CollectionUtils.isEmpty(departmentIds)) {
                departmentIds.add(employee.getDeptId());
                employee.setDepartmentIds(departmentIds);
            } else {
                List<Long> deptIds = new ArrayList<>();
                deptIds.add(employee.getDeptId());
                employee.setDepartmentIds(deptIds);
            }
        }
        startPage();
        List<EmpPicInfoVO> list = employeeService.selectEmpPicList(employee);
        return getDataTable(list);
    }

    @ApiOperation("人员基本信息，出勤情况和培训信息")
    @GetMapping("/base")
    public AjaxResult getEmpBaseInfos(@RequestParam Long empId) {
        return success(empInfoService.getEmpBaseInfos(empId));
    }


    @ApiOperation("薪酬情况分析")
    @GetMapping("/salary")
    public AjaxResult getEmpSalaryInfos(@RequestParam Long empId) {
        return success(empInfoService.getEmpSalaryInfos(empId));
    }

    @ApiOperation("绩效情况分析")
    @GetMapping("/perform/condition")
    public AjaxResult getEmpPerformInfos(@RequestParam Long empId) {
        return success(empInfoService.getEmpPerformInfos(empId));
    }


    @ApiOperation("胜任力情况分析")
    @GetMapping("/ability")
    public AjaxResult getEmpAbilityInfos(@RequestParam Long empId) {
        return success(empInfoService.getAbilityInfos(empId));
    }

    @ApiOperation("资格证书信息")
    @GetMapping("/paper")
    public AjaxResult getEmpPaperInfos(@RequestParam Long empId) {
        return success(empInfoService.getEmpPaperInfos(empId));
    }


    @ApiOperation("履历信息")
    @GetMapping("/resume")
    public AjaxResult getEmpResumeInfos(@RequestParam Long empId) {
        return success(empInfoService.getEmpResumeInfos(empId));
    }


    @ApiOperation("考勤概览")
    @GetMapping("/attendance/overview")
    public AjaxResult getEmpAttendanceOverview(@RequestParam Long empId, @RequestParam(required = false) Date startTime,
                                               @RequestParam(required = false) Date endTime) {
        return success(empInfoService.getEmpAttendanceOverview(empId, startTime, endTime));
    }

    @ApiOperation("考勤详情")
    @GetMapping("/attendance/detail")
    public AjaxResult getEmpAttendanceDetail(@RequestParam Long empId, @RequestParam Date time) {
        return success(empInfoService.getEmpAttendanceDetail(empId, time));
    }

    @ApiOperation("考勤统计")
    @GetMapping("/attendance/statistics")
    public AjaxResult getEmpAttendanceStatistics(@RequestParam Long empId) {
        return success(empInfoService.getEmpAttendanceStatistics(empId));
    }


    /**
     * employee:员工基本信息
     * empTrainData:教育培训经历
     * countEmpTrainNatureData:各类别课程个数占比统计
     * countEmpTrainNatureSum:课程总数
     * countEmpTrainPeriodData:各类别课程培训时长占比统计
     * countEmpTrainPeriodSum:培训时长总数
     * countRecentYearsTrainNumData:近5年培训课程情况
     * countRecentYearsTrainPeriodData 近5年培训时长情况
     * recommendProjectsData:培训项目推荐
     * recommendMaterialsData:培训材料推荐
     *
     * @param empId
     * @return
     */
    @ApiOperation("员工画像-培训信息")
    @PreAuthorize("@ss.hasPermi('labor:info:query')")
    @GetMapping("/train/{empId}")
    public AjaxResult getTrainInfo(@PathVariable("empId") Integer empId) {
        //人员基础信息
        Employee employee = employeeService.selectEmployeeByEmpId(Long.valueOf(empId));

        AjaxResult ajax = AjaxResult.success();
        //教育培训
        List<EmpTrainResultVO> empTrainList = empTrainService.selectEmpTrainByEmpId(empId);

        //各类别课程个数占比统计
        List<PieChartVO> countEmpTrainNatureNum = empTrainService.countEmpTrainNatureNumByEmpId(empId);
        Integer countEmpTrainNatureSum = 0;
        if (countEmpTrainNatureNum != null && countEmpTrainNatureNum.size() > 0) {
            for (PieChartVO tranNature : countEmpTrainNatureNum) {
                countEmpTrainNatureSum += tranNature.getValue();
            }
        }

        //各类别课程培训时长占比统计
        List<PieChartVO> countEmpTrainPeriod = empTrainService.countEmpTrainPeriodByEmpId(empId);
        Integer countEmpTrainPeriodSum = 0;
        if (countEmpTrainPeriod != null && countEmpTrainPeriod.size() > 0) {
            for (PieChartVO trainPeriod : countEmpTrainPeriod) {
                countEmpTrainPeriodSum += trainPeriod.getValue();
            }
        }

        //近5年培训课程情况
        List<PieChartVO> countRecentYearsTrainNum = empTrainService.countRecentYearsTrainNumByEmpId(empId);

        //近5年培训时长情况
        List<PieChartVO> countRecentYearsTrainPeriod = empTrainService.countRecentYearsTrainPeriodByEmpId(empId);

        //培训内容推荐
        //培训项目推荐
        Map<String, List> map = empTrainService.recommendTrainProject(empId);
        List<TrainProject> recommendProjects = map.get("recommendProject");
        if (recommendProjects != null && recommendProjects.size() > 3) {
            recommendProjects = recommendProjects.subList(0, 3);
        }

        List<TrainMaterials> recommendMaterials = map.get("recommendMaterial");
        if (recommendMaterials != null && recommendMaterials.size() > 3) {//判断list长度
            recommendMaterials = recommendMaterials.subList(0, 3);//取前3条数据

        }

        //培训材料推荐
        ajax.put("employee", employee);
        ajax.put("empTrainData", empTrainList);
        ajax.put("countEmpTrainNatureSum", countEmpTrainNatureSum);
        ajax.put("countEmpTrainNatureData", countEmpTrainNatureNum);
        ajax.put("countEmpTrainPeriodSum", countEmpTrainPeriodSum);
        ajax.put("countEmpTrainPeriodData", countEmpTrainPeriod);

        ajax.put("countRecentYearsTrainNumData", countRecentYearsTrainNum);
        ajax.put("countRecentYearsTrainPeriodData", countRecentYearsTrainPeriod);
        ajax.put("recommendProjectsData", recommendProjects);
        ajax.put("recommendMaterialsData", recommendMaterials);

        return ajax;

    }

    /**
     * 员工画像-薪酬信息
     * employee:员工基本信息
     * salaryLevelName:当前薪级
     * lastMonthNetPay:上月实发薪酬
     * bilateralInsurancesFund:上月双边五险一金
     * lastYearNetPay:上一年度实发薪酬
     * pastYearNetPayChart:近一年个人薪酬情况
     *
     * @param empId
     * @return
     */
    @ApiOperation("员工画像-薪酬信息")
    @PreAuthorize("@ss.hasPermi('labor:info:query')")
    @GetMapping("/salary/{empId}")
    public AjaxResult getSalaryInfo(@PathVariable("empId") Long empId) {
        AjaxResult ajax = AjaxResult.success();
        //员工信息
        Employee employee = employeeService.selectEmployeeByEmpId(empId);
        //岗级，取自员工信息
        String salaryLevelName = sysDictDataService.selectDictLabel("emp_salary_level", employee.getEmpSalaryLevel());
        //上月实发薪酬
        EmpSalary queryEmpSalary = new EmpSalary();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int lastMonth = calendar.get(Calendar.MONTH) + 1;
        String month = lastMonth >= 10 ? lastMonth + "" : ("0" + lastMonth);

        int year = calendar.get(Calendar.YEAR);
        queryEmpSalary.setSalaryYearMonth(year + "-" + month);
        queryEmpSalary.setSalaryEmpId(empId);
        List<EmpSalary> empSalaryList = empSalaryService.selectEmpSalaryList(queryEmpSalary);
        String lastMonthNetPay = "0";

        BigDecimal bilateralInsurancesFund = BigDecimal.ZERO;


        if (empSalaryList != null && empSalaryList.size() > 0) {
            EmpSalary empSalary = empSalaryList.get(0);
            lastMonthNetPay = empSalary.getSalaryNetPay();
            BigDecimal insurances = empSalaryService.sumSalaryItemBySalaryType(empSalary.getSalaryId(), "保险齐缴");
            //System.out.println("保险齐缴："+insurances.toString());
            //先算保险齐缴
            bilateralInsurancesFund = bilateralInsurancesFund.add(insurances);
            //再算保险金个缴X2
            BigDecimal bilateralFund = new BigDecimal(empSalary.getSalaryPersonFund());
            //System.out.println("公积金： "+bilateralFund.toString());

            bilateralInsurancesFund = bilateralInsurancesFund.add(bilateralFund.multiply(BigDecimal.valueOf(2)));

        }

        //上月双边五险一金


        //上一年度实发薪酬
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.YEAR, -1);
        int lastYear = calendar2.get(Calendar.YEAR);
        EmpSalary empSalary = new EmpSalary();
        empSalary.setSalaryEmpId(empId);
        empSalary.setSalaryYearMonth(lastYear + "");
        String lastYearNetPay = empSalaryService.sumLastYearNetPay(empSalary);

        //近一年个人薪酬情况
        EmpSalaryQueryVO queryVO = new EmpSalaryQueryVO();
        queryVO.setEmpId(empId.intValue());

        List<SalaryChartVO> charts = empSalaryService.countPastYearNetPay(queryVO);

        ajax.put("employee", employee);
        ajax.put("salaryLevelName", salaryLevelName);
        ajax.put("lastMonthNetPay", lastMonthNetPay);
        ajax.put("bilateralInsurancesFund", bilateralInsurancesFund);
        ajax.put("lastYearNetPay", lastYearNetPay);
        ajax.put("pastYearNetPayChart", charts);
        return ajax;
    }

    /**
     * 统计查询员工个人历年绩效列表
     */
    // @PreAuthorize("@ss.hasPermi('labor:info:list')")
    @ApiOperation("统计查询员工个人历年绩效列表")
    @GetMapping("/performance")
    public AjaxResult countPersonalPerformance(EmpPerformance empPerformance) {
        return success(empInfoService.countPersonalPerformance(empPerformance));
    }


}
