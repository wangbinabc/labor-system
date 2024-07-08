package com.yuantu.quartz.task;

import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.cenum.AttendStatusEnum;
import com.yuantu.labor.cenum.EmpStatusEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.vo.CredentialsDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("deptEffectTask")
public class DeptEffectTask {


    @Autowired
    private DeptEfficiencyMapper deptEfficiencyMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmpEffectivenessMapper empEffectivenessMapper;

    @Autowired
    private EmpAttendanceMapper empAttendanceMapper;

    @Autowired
    private EmpPerformanceMapper empPerformanceMapper;

    @Autowired
    private EmpSalaryMapper empSalaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpCredentialsMapper empCredentialsMapper;


    public void insertDeptEffectInfos() {

        Date now = new Date();
        List<Department> departments = departmentMapper.findAllDeptInfos();
        if (CollectionUtils.isEmpty(departments)) {
            return;
        }
        List<DeptEfficiency> deptEfficiencies = new ArrayList<>();
        List<Long> deptIds = departments.stream().map(Department::getDeptId).collect(Collectors.toList());
        Map<Long, List<Employee>> empDeptMap = employeeMapper.findEmpInfosByEmpDeptIds(deptIds).stream().
                collect(Collectors.groupingBy(Employee::getEmpDeptId));
        for (Department dept : departments) {
            DeptEfficiency deptEfficiency = new DeptEfficiency();
            deptEfficiency.setDeptId(dept.getDeptId());
            deptEfficiency.setCreateTime(now);
            List<Employee> employees = empDeptMap.get(dept.getDeptId());
            if (!CollectionUtils.isEmpty(employees)) {
                int newRecruit = employees.stream().filter(s -> EmpStatusEnum.NEW_POSITION.getKey().
                        equals(s.getEmpStatus())).collect(Collectors.toList()).size();
                int resignNum = employees.stream().filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                        || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList()).size();
                double stable = (employees.size() - ((double) (newRecruit + resignNum) / 2)) / (double) employees.size();
                List<EmpEfficiency> empEfficiencyInfos = getEmpEfficiencyInfos(now, employees);
                double deptProfitSum = 0.0;
                double deptAttendRatioSum = 0.0;
                double deptPerformRatioSum = 0.0;
                double deptCostRatioSum = 0.0;
                double deptEffectRatioSum = 0.0;
                for (EmpEfficiency empEfficiencyInfo : empEfficiencyInfos) {
                    deptProfitSum = deptProfitSum + Double.parseDouble(empEfficiencyInfo.getProfit());
                    deptAttendRatioSum = deptAttendRatioSum + Double.parseDouble(empEfficiencyInfo.getAttendRatio());
                    deptPerformRatioSum = deptPerformRatioSum + Double.parseDouble(empEfficiencyInfo.getPerformRatio());
                    deptCostRatioSum = deptCostRatioSum + Double.parseDouble(empEfficiencyInfo.getCostRatio());
                    deptEffectRatioSum = deptEffectRatioSum + Double.parseDouble(empEfficiencyInfo.getEffectRatio());
                }
                deptEfficiency.setDeptProfit(String.format("%.2f", deptProfitSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptAttendRatio(String.format("%.2f", deptAttendRatioSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptPerformRatio(String.format("%.2f", deptPerformRatioSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptCostRatio(String.format("%.2f", deptCostRatioSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptEffectRatio(String.format("%.2f", deptEffectRatioSum / empEfficiencyInfos.size() * stable));
            } else {
                deptEfficiency.setDeptProfit("0");
                deptEfficiency.setDeptAttendRatio("0");
                deptEfficiency.setDeptPerformRatio("0");
                deptEfficiency.setDeptCostRatio("0");
                deptEfficiency.setDeptEffectRatio("0");
            }
            deptEfficiencies.add(deptEfficiency);
        }
        deptEfficiencyMapper.removeCurrentMonthInfos(now);
        deptEfficiencyMapper.insertInfos(deptEfficiencies);
    }

    private List<EmpEfficiency> getEmpEfficiencyInfos(Date now, List<Employee> allEmployees) {
        List<Long> empIds = allEmployees.stream().map(Employee::getEmpId).collect(Collectors.toList());
        Map<Long, List<CredentialsDetailsVO>> credentialMap = empCredentialsMapper.findInfosByEmpIds(empIds).stream().
                collect(Collectors.groupingBy(CredentialsDetailsVO::getCredEmpId));
        Map<Long, List<EmpAttendance>> empAttendMap = empAttendanceMapper.findInfosByEmpIdsAndRecordTime(empIds, now).stream().
                collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
        Map<Long, List<EmpPerformance>> empPerformanceMap = empPerformanceMapper.findInfosByEmpIdsAndCreateTime(empIds, now).stream().
                collect(Collectors.groupingBy(EmpPerformance::getPerfEmpId));
        Map<Long, List<EmpSalary>> salaryMap = empSalaryMapper.findInfosByEmpIdsAndCurrentTime(empIds, now).stream().
                collect(Collectors.groupingBy(EmpSalary::getSalaryEmpId));
        Map<Long, List<EmpEffectiveness>> empEffectiveMap = empEffectivenessMapper.findInfosByEmpIdsAndCreateTime(empIds, now).
                stream().collect(Collectors.groupingBy(EmpEffectiveness::getEffEmpId));
        List<EmpEfficiency> empEfficiencies = new ArrayList<>();
        for (Employee allEmployee : allEmployees) {
            Long empId = allEmployee.getEmpId();
            int education = 0;
            if (allEmployee.getEmpEducation() != null) {
                String empEducation = allEmployee.getEmpEducation();
                education = 10 + Integer.parseInt(empEducation);
            } else {
                education = 10;
            }
            int title = 0;
            if (allEmployee.getEmpTitle() != null) {
                String empTitle = allEmployee.getEmpTitle();
                title = 10 + Integer.parseInt(empTitle);
            } else {
                title = 10;
            }
            List<CredentialsDetailsVO> empCredentials = credentialMap.get(empId);
            int paperNum = 0;
            if (!CollectionUtils.isEmpty(empCredentials)) {
                if (empCredentials.size() > 10) {
                    paperNum = 20;
                } else {
                    paperNum = empCredentials.size() + 10;
                }
            } else {
                paperNum = 10;
            }
            Date attendTime = allEmployee.getAttendTime();
            int workExperience = 0;
            if (attendTime != null) {
                long workYears = DateUtils.calculateDays(attendTime, now) / 365;
                if (workYears > 0) {
                    if (workYears > 10) {
                        workExperience = 20;
                    } else {
                        workExperience = 10 + (int) workYears;
                    }
                }
            } else {
                workExperience = 10;
            }
            List<EmpPerformance> empPerformances = empPerformanceMap.get(empId);
            int performance = 0;
            if (!CollectionUtils.isEmpty(empPerformances)) {
                int score = 0;
                for (EmpPerformance empPerformance : empPerformances) {
                    if ("A".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 10;
                    }
                    if ("B".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 7;
                    }
                    if ("C".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 4;
                    }
                    if ("D".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 1;
                    }
                }
                performance = 10 + score;
            } else {
                performance = 10;
            }
            List<EmpAttendance> empAttendances = empAttendMap.get(empId);
            double attendRatio = 0;
            if (!CollectionUtils.isEmpty(empAttendances)) {
                int onWorkNum = empAttendances.stream().filter(s -> AttendStatusEnum.ONE.getKey().
                        equals(s.getAttendStatus())).collect(Collectors.toList()).size();
                if (empAttendances.size() != 0) {
                    attendRatio = onWorkNum / (double) empAttendances.size();
                }
            }
            List<EmpSalary> empSalaries = salaryMap.get(empId);
            double salary = 0;
            if (!CollectionUtils.isEmpty(empSalaries)) {
                for (EmpSalary empSalary : empSalaries) {
                    String salaryPayableNum = empSalary.getSalaryPayableNum();
                    if (StringUtils.isNotEmpty(salaryPayableNum)) {
                        salary = salary + Double.parseDouble(salaryPayableNum) / 10000;
                    }
                }
            }
            List<EmpEffectiveness> empEffectivenessInfos = empEffectiveMap.get(empId);
            double profit = 0;
            if (!CollectionUtils.isEmpty(empEffectivenessInfos)) {
                for (EmpEffectiveness empEffectivenessInfo : empEffectivenessInfos) {
                    String effEmpProfitValue = empEffectivenessInfo.getEffEmpProfitValue();
                    if (StringUtils.isNotEmpty(effEmpProfitValue)) {
                        profit = profit + Double.parseDouble(effEmpProfitValue);
                    }
                }
            }
            double effect = 0;
            if (salary != 0.0 && attendRatio != 0.0) {
                effect = (education + title + paperNum + workExperience) * performance / (salary + attendRatio);
            }
            EmpEfficiency efficiency = new EmpEfficiency();
            efficiency.setEmpId(empId);
            efficiency.setEmpName(allEmployee.getEmpName());
            efficiency.setProfit(profit + "");
            efficiency.setAttendRatio(String.format("%.2f", attendRatio));
            efficiency.setPerformRatio(performance + "");
            efficiency.setCostRatio(salary + "");
            efficiency.setEffectRatio(String.format("%.2f", effect));
            efficiency.setCreateTime(now);
            empEfficiencies.add(efficiency);
        }
        return empEfficiencies;
    }


}
