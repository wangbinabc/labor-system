package com.yuantu.labor.service.impl;

import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.cenum.AttendStatusEnum;
import com.yuantu.labor.cenum.PerfCycleEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEmpInfoService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EmpInfoServiceImpl implements IEmpInfoService {


    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpAttendanceMapper empAttendanceMapper;

    @Autowired
    private EmpTrainMapper empTrainMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployingUnitsMapper unitsMapper;

    @Autowired
    private EmpPerformanceMapper empPerformanceMapper;

    @Autowired
    private EmpSalaryMapper empSalaryMapper;

    @Autowired
    private SalaryHistoryMapper salaryHistoryMapper;

    @Autowired
    private EmpCredentialsMapper empCredentialsMapper;

    @Autowired
    private EmpDocumentMapper empDocumentMapper;

    @Autowired
    private EmpResumeMapper empResumeMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;


    @Override
    public EmpBaseInfoVO getEmpBaseInfos(Long empId) {

        EmpBaseInfoVO empBaseInfo = new EmpBaseInfoVO();
        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (existEmployee == null) {
            throw new ServiceException("此员工不存在");
        }
        BeanUtils.copyProperties(existEmployee, empBaseInfo);
        List<EmpAttendance> empAttendances = empAttendanceMapper.findInfosDuringOneYearByEmpIds(Collections.singletonList(empId));
        int attendNum = empAttendances.stream().filter(s -> AttendStatusEnum.ONE.getKey().equals(s.getAttendStatus()))
                .collect(Collectors.toList()).size();
        int holidayNum = empAttendances.stream().filter(s -> AttendStatusEnum.THREE.getKey().equals(s.getAttendStatus()))
                .collect(Collectors.toList()).size();
        int overTimeNum = empAttendances.stream().filter(s -> AttendStatusEnum.FOUR.getKey().equals(s.getAttendStatus()))
                .collect(Collectors.toList()).size();
        int onBusinessNum = empAttendances.stream().filter(s -> AttendStatusEnum.TWO.getKey().equals(s.getAttendStatus()))
                .collect(Collectors.toList()).size();
        Long empDeptId = existEmployee.getEmpDeptId();
        List<Long> empIds = new ArrayList<>();
        List<EmpAttendance> empAttendancesForUnit = new ArrayList<>();
        List<EmpTrain> empTrainsUnit = new ArrayList<>();
        if (empDeptId != null) {
            Long deptUnitId = departmentMapper.selectDepartmentByDeptId(empDeptId).getDeptUnitId();

            EmployingUnits units = unitsMapper.selectEmployingUnitsByUnitId(deptUnitId);
            empBaseInfo.setEmpUnitId(deptUnitId);
            empBaseInfo.setEmpUnitName(units.getUnitName());
            List<Long> deptIds = departmentMapper.findDepartmentInfosByUnitIds(Collections.singletonList(deptUnitId)).
                    stream().map(Department::getDeptId).collect(Collectors.toList());
            empIds = employeeMapper.findEmpInfosByEmpDeptIds(deptIds).stream().map(Employee::getEmpId).
                    distinct().collect(Collectors.toList());
            empAttendancesForUnit = empAttendanceMapper.findInfosDuringOneYearByEmpIds(empIds);
            empTrainsUnit = empTrainMapper.findInfosByEmpIds(empIds);
        }

        int workDays = countWorkingDaysOfPreviousYear();
        String attendRatio = String.format("%.1f", (double) attendNum / workDays * 100) + "%";
        Map<Long, List<EmpAttendance>> overTimeMap = empAttendancesForUnit.stream().filter(s ->
                AttendStatusEnum.FOUR.getKey().equals(s.getAttendStatus())).collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
        List<Integer> overTimeNumList = new ArrayList<>();
        for (Long attendEmpId : overTimeMap.keySet()) {
            overTimeNumList.add(overTimeMap.get(attendEmpId).size());
        }
        overTimeNumList = overTimeNumList.stream().sorted(Comparator.comparing(Integer::intValue,
                Comparator.reverseOrder())).distinct().collect(Collectors.toList());
        int overTimeRank = 0;
        if (overTimeNum != 0) {
            for (Integer overTimeDays : overTimeNumList) {
                if (overTimeNum <= overTimeDays) {
                    overTimeRank++;
                }
            }
        }

        int totalOverTimeNum = overTimeNumList.stream().mapToInt(Integer::intValue).sum();
        int avgOverTimeNum = empIds.size() == 0 ? 0 : totalOverTimeNum / empIds.size();
        String compareOverTime = overTimeNum - avgOverTimeNum >= 0 ? "多" + (overTimeNum - avgOverTimeNum) + "天" : "少" + (avgOverTimeNum - overTimeNum) + "天";

        Map<Long, List<EmpAttendance>> onBusinessMap = empAttendancesForUnit.stream().filter(s ->
                AttendStatusEnum.TWO.getKey().equals(s.getAttendStatus())).collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
        List<Integer> onBusinessNumList = new ArrayList<>();
        for (Long attendEmpId : onBusinessMap.keySet()) {
            onBusinessNumList.add(onBusinessMap.get(attendEmpId).size());
        }
        onBusinessNumList = onBusinessNumList.stream().sorted(Comparator.comparing(Integer::intValue, Comparator.reverseOrder())).distinct().collect(Collectors.toList());
        int onBusinessRank = 0;
        if (onBusinessNum != 0) {
            for (Integer onBusinessDays : onBusinessNumList) {
                if (onBusinessNum <= onBusinessDays) {
                    onBusinessRank++;
                }
            }
        }

        int totalOnBusinessNum = onBusinessNumList.stream().mapToInt(Integer::intValue).sum();
        int avgOnBusinessNum = empIds.size() == 0 ? 0 : totalOnBusinessNum / empIds.size();
        String compareOnBusiness = onBusinessNum - avgOnBusinessNum >= 0 ?
                "多" + (onBusinessNum - avgOnBusinessNum) + "天" : "少" + (avgOnBusinessNum - onBusinessNum) + "天";
        empBaseInfo.setAttendanceNum(attendNum);
        empBaseInfo.setAttendanceRatio(attendRatio);
        empBaseInfo.setHolidayNum(holidayNum);
        empBaseInfo.setOverTimeNum(overTimeNum);
        empBaseInfo.setOverTimeRank(overTimeRank == 0 ? null : overTimeRank);
        empBaseInfo.setAvgOverTimeNum(avgOverTimeNum);
        empBaseInfo.setCompareOverTime(compareOverTime);
        empBaseInfo.setOnBusinessNum(onBusinessNum);
        empBaseInfo.setOnBusinessRank(onBusinessRank == 0 ? null : onBusinessRank);
        empBaseInfo.setAvgOnBusinessNum(avgOnBusinessNum);
        empBaseInfo.setCompareOnBusiness(compareOnBusiness);

        List<EmpTrain> empTrains = empTrainMapper.findInfosByEmpIds(Collections.singletonList(empId));
        int trainCourseNum = empTrains.size();

        int trainCourseNumUnit = empTrainsUnit.size();
        int avgTrainCourseNum = empIds.size() == 0 ? 0 : trainCourseNumUnit / empIds.size();
        String compareTrainCourse = trainCourseNum - avgTrainCourseNum >= 0 ?
                "多" + (trainCourseNum - avgTrainCourseNum) + "个" : "少" + (avgTrainCourseNum - trainCourseNum) + "个";
        empBaseInfo.setTrainCourseNum(trainCourseNum);
        empBaseInfo.setAvgTrainCourseNum(avgTrainCourseNum);
        empBaseInfo.setCompareTrainCourse(compareTrainCourse);
        int trainHourNum = !CollectionUtils.isEmpty(empTrains) ? empTrains.stream().filter(s -> s.getTrainPeriod() != null).mapToInt(EmpTrain::getTrainPeriod).sum() : 0;
        System.out.println(!CollectionUtils.isEmpty(empTrainsUnit));
        int trainHourNumUnit = !CollectionUtils.isEmpty(empTrainsUnit) ? empTrainsUnit.stream().filter(s -> s.getTrainPeriod() != null).mapToInt(EmpTrain::getTrainPeriod).sum() : 0;
        int avgTrainHourNum = empIds.size() == 0 ? 0 : trainHourNumUnit / empIds.size();
        String compareTrainHour = trainHourNum - avgTrainHourNum >= 0 ? "多" + (trainHourNum - avgTrainHourNum) + "小时"
                : "少" + (avgTrainHourNum - trainHourNum) + "小时";
        empBaseInfo.setTrainHourNum(trainHourNum);
        empBaseInfo.setAvgTrainHourNum(avgTrainHourNum);
        empBaseInfo.setCompareTrainHour(compareTrainHour);
        return empBaseInfo;
    }


    public int countWorkingDaysOfPreviousYear() {
        int workingDays = 0;
        LocalDate endDate = LocalDate.now(); // 设置结束日期为当前日期
        LocalDate startDate = endDate.minusYears(1); // 设置开始日期为当前日期的前一年

        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            startDate = startDate.plusDays(1); // 日期加1
        }

        return workingDays;
    }


    @Override
    public EmpSalaryConditionVO getEmpSalaryInfos(Long empId) {

        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (existEmployee == null) {
            throw new ServiceException("此员工不存在");
        }
        EmpSalaryConditionVO empSalaryCondition = new EmpSalaryConditionVO();

        Long empDeptId = existEmployee.getEmpDeptId();
        Map<Long, List<EmpSalary>> empSalaryMap = new HashMap<>(16);
        if (empDeptId != null) {
            Long deptUnitId = departmentMapper.selectDepartmentByDeptId(empDeptId).getDeptUnitId();
            List<Long> deptIds = departmentMapper.findDepartmentInfosByUnitIds(Collections.singletonList(deptUnitId)).
                    stream().map(Department::getDeptId).collect(Collectors.toList());
            List<Long> empIds = employeeMapper.findEmpInfosByEmpDeptIds(deptIds).stream().map(Employee::getEmpId).collect(Collectors.toList());

            empSalaryMap = empSalaryMapper.findInfosByEmpIds(empIds).stream().
                    collect(Collectors.groupingBy(EmpSalary::getSalaryEmpId));
        }

        List<EmpSalary> empSalaries = empSalaryMapper.findInfosByEmpIds(Collections.singletonList(empId));
        empSalaries = empSalaries.stream().sorted(Comparator.comparing(EmpSalary::getSalaryYearMonth)).collect(Collectors.toList());


        List<String> salaryInfosUnit = new ArrayList<>();
        for (Long salaryEmpId : empSalaryMap.keySet()) {
            List<EmpSalary> salaries = empSalaryMap.get(salaryEmpId).stream().
                    sorted(Comparator.comparing(EmpSalary::getSalaryYearMonth)).collect(Collectors.toList());
            String salaryPayableNum = salaries.get(salaries.size() - 1).getSalaryPayableNum();
            salaryInfosUnit.add(salaryPayableNum);
        }
        Set<String> hashSet = new HashSet<>(salaryInfosUnit);
        List<String> salaryInfoUnitForRank = new ArrayList<>(hashSet);
        Collections.sort(salaryInfoUnitForRank, Collections.reverseOrder());
        Date now = new Date();
        Date empHireDate = existEmployee.getEmpHiredate();
        if (empHireDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            empSalaryCondition.setEmpHireDate(dateFormat.format(empHireDate));
            int i = calculateYearDifference(empHireDate, now);
            if (i == 0) {
                empSalaryCondition.setWorkYears("不满一年");
            } else {
                empSalaryCondition.setWorkYears(i + "年");
            }
        }
        List<SalaryHistory> salaryHistories = salaryHistoryMapper.selectInfosByEmpId(empId).stream().
                sorted(Comparator.comparing(SalaryHistory::getHisYearMonth)).collect(Collectors.toList());
        String currentSalary = null;
        if (!CollectionUtils.isEmpty(empSalaries)) {
            String beginSalary = empSalaries.get(0).getSalaryPayableNum();
            currentSalary = empSalaries.get(empSalaries.size() - 1).getSalaryPayableNum();
            empSalaryCondition.setBeginSalaryNum(beginSalary + "元");
            empSalaryCondition.setEndSalaryNum(currentSalary + "元");
            String salaryIncreaseRatio = String.format("%.1f",
                    (Double.parseDouble(currentSalary) - Double.parseDouble(beginSalary)) / Double.parseDouble(beginSalary) * 100) + "%";
            empSalaryCondition.setSalaryIncreaseRatio(salaryIncreaseRatio);
            int salaryRank = 0;
            for (String salary : salaryInfoUnitForRank) {
                if (currentSalary.compareTo(salary) <= 0) {
                    salaryRank++;
                }
            }
            String salaryRankRatio = String.format("%.1f", (double) salaryRank / salaryInfoUnitForRank.size() * 100);
            empSalaryCondition.setSalaryRankRatio(salaryRankRatio);
            int sameSalaryNum = 0;
            for (String salary : salaryInfosUnit) {
                if (salary.equals(currentSalary)) {
                    sameSalaryNum++;
                }
            }
            empSalaryCondition.setSalaryEmpNum(sameSalaryNum);
        }
        if (!CollectionUtils.isEmpty(salaryHistories)) {
            String hisPreviousLevel = salaryHistories.get(0).getHisPreviousLevel();
            String hisNextLevel = salaryHistories.get(salaryHistories.size() - 1).getHisNextLevel();
            empSalaryCondition.setSalaryLevelBegin(hisPreviousLevel + "级");
            empSalaryCondition.setSalaryLevelEnd(hisNextLevel + "级");
            empSalaryCondition.setSalaryChangeNum(salaryHistories.size());
        } else {
            empSalaryCondition.setSalaryLevelBegin(existEmployee.getEmpSalaryLevel() + "级");
            empSalaryCondition.setSalaryLevelEnd(existEmployee.getEmpSalaryLevel() + "级");
        }
        Collections.sort(salaryInfosUnit);
        Map<String, List<String>> salaryMap = salaryInfosUnit.stream().collect(Collectors.groupingBy(String::toString));

        List<EmpSalarySimpleInfoVO> empSalarySimpleInfos = new ArrayList<>();
        for (String salary : salaryMap.keySet()) {
            EmpSalarySimpleInfoVO empSalarySimpleInfo = new EmpSalarySimpleInfoVO();
            empSalarySimpleInfo.setSalaryPayableNum(salary);
            empSalarySimpleInfo.setEmpNum(salaryMap.get(salary).size());
            if (salary.equals(currentSalary)) {
                empSalarySimpleInfo.setIsPerson(true);
            } else {
                empSalarySimpleInfo.setIsPerson(false);
            }
            empSalarySimpleInfos.add(empSalarySimpleInfo);
        }
        empSalarySimpleInfos = empSalarySimpleInfos.stream().sorted(Comparator.comparing(EmpSalarySimpleInfoVO::getSalaryPayableNum)).
                collect(Collectors.toList());
        empSalaryCondition.setEmpSalarySimpleInfos(empSalarySimpleInfos);
        return empSalaryCondition;
    }

    @Override
    public EmpPerformConditionVO getEmpPerformInfos(Long empId) {

        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (existEmployee == null) {
            throw new ServiceException("此员工不存在");
        }
        Date empHireDate = existEmployee.getEmpHiredate();
        Date now = new Date();
        int i = 0;
        if (empHireDate != null) {
            i = calculateYearDifference(empHireDate, now);
        }

        List<Employee> allEmployees = employeeMapper.findAllEmployees();
        int finalI = i;
        List<Long> empIds = allEmployees.stream().filter(s -> s.getEmpHiredate() != null).filter(s -> {
            Date hireDate = s.getEmpHiredate();
            int year = calculateYearDifference(hireDate, now);
            return year == finalI;
        }).map(Employee::getEmpId).collect(Collectors.toList());
        EmpPerformConditionVO empPerformCondition = new EmpPerformConditionVO();
        List<EmpPerformance> empPerformances = empPerformanceMapper.findInfosByEmpIds(Collections.singletonList(empId));
        Map<String, List<EmpPerformance>> empPerformanceMap = empPerformances.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfLevelValue));
        for (String type : empPerformanceMap.keySet()) {
            if ("A".equals(type)) {
                empPerformCondition.setANUm(empPerformanceMap.get(type).size());
            }
            if ("B".equals(type)) {
                empPerformCondition.setBNum(empPerformanceMap.get(type).size());
            }
            if ("C".equals(type)) {
                empPerformCondition.setCNum(empPerformanceMap.get(type).size());
            }
            if ("D".equals(type)) {
                empPerformCondition.setDNum(empPerformanceMap.get(type).size());
            }
        }
        empPerformCondition.setANUm(empPerformCondition.getANUm() == null ? 0 : empPerformCondition.getANUm());
        empPerformCondition.setBNum(empPerformCondition.getBNum() == null ? 0 : empPerformCondition.getBNum());
        empPerformCondition.setCNum(empPerformCondition.getCNum() == null ? 0 : empPerformCondition.getCNum());
        empPerformCondition.setDNum(empPerformCondition.getDNum() == null ? 0 : empPerformCondition.getDNum());
        String ratio = "0.0";
        int performSum = empPerformCondition.getANUm() + empPerformCondition.getBNum() + empPerformCondition.getCNum()
                + empPerformCondition.getDNum();
        if (performSum != 0) {
            ratio = String.format("%.1f", (double) (empPerformCondition.getANUm() + empPerformCondition.getBNum()) / performSum);
        }
        empPerformCondition.setRatio(ratio);
        List<String> ratios = new ArrayList<>();
        Map<Long, List<EmpPerformance>> empPerformMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(empIds)) {
            List<EmpPerformance> empPerformancesUnit = empPerformanceMapper.findInfosByEmpIds(empIds);
            empPerformMap = empPerformancesUnit.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfEmpId));
        }

        for (Long perEmpId : empPerformMap.keySet()) {
            List<EmpPerformance> performances = empPerformMap.get(perEmpId);
            Map<String, List<EmpPerformance>> performanceMap = performances.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfLevelValue));
            int aNUm = 0;
            int bNum = 0;
            int performNumSum = 0;
            for (String type : performanceMap.keySet()) {
                if ("A".equals(type)) {
                    aNUm = performanceMap.get(type).size();
                }
                if ("B".equals(type)) {
                    bNum = performanceMap.get(type).size();
                }
                performNumSum = performSum + 1;
            }
            if (performNumSum != 0) {
                ratios.add(String.format("%.1f", (double) (aNUm + bNum) / performNumSum));
            }
        }
        Set<String> hashSet = new HashSet<>(ratios);
        ratios = new ArrayList<>(hashSet);
        Collections.sort(ratios, Collections.reverseOrder());
        int rank = 0;
        if (!"0.0".equals(ratio) && !CollectionUtils.isEmpty(ratios)) {
            for (String ratioNum : ratios) {
                if (ratio.compareTo(ratioNum) <= 0) {
                    rank++;
                }
            }
            empPerformCondition.setRank(String.format("%.1f", (double) rank / ratios.size() * 100));
        }
        if (empPerformCondition.getRank() != null) {
            Double num = Double.valueOf(empPerformCondition.getRank());
            if (num <= 15.0) {
                empPerformCondition.setRankDesc("优异");
            } else if (15.0 < num && num <= 45.0) {
                empPerformCondition.setRankDesc("良好");
            } else if (45.0 < num && num <= 85.0) {
                empPerformCondition.setRankDesc("及格");
            } else {
                empPerformCondition.setRankDesc("不及格");
            }
        }
        return empPerformCondition;
    }


    @Override
    public AbilityVO getAbilityInfos(Long empId) {

        AbilityVO ability = new AbilityVO();
        List<Employee> employees = employeeMapper.findAllEmployees();
        if (CollectionUtils.isEmpty(employees)) {
            ability.setAbilityRatio(0);
            ability.setEducationRatio(0);
            ability.setAvgEducationRatio(0);
            ability.setPaperNum(0);
            ability.setAvgPaperNum(0);
            ability.setWorkYears(0);
            ability.setAvgWorkYears(0);
            ability.setPerformRatio(0);
            ability.setAvgPerformRatio(0);
            ability.setOnWorkRatio(0);
            ability.setAvgOnWorkRatio(0);
            ability.setTrainRatio(0);
            ability.setAvgTrainRatio(0);
            return ability;
        }
        List<Long> empIds = employees.stream().map(Employee::getEmpId).collect(Collectors.toList());
        int empNums = employees.size();
        Employee employee = employeeMapper.selectEmployeeByEmpId(empId);
        if (employee == null) {
            ability.setAbilityRatio(0);
            ability.setEducationRatio(0);
            ability.setAvgEducationRatio(0);
            ability.setPaperNum(0);
            ability.setAvgPaperNum(0);
            ability.setWorkYears(0);
            ability.setAvgWorkYears(0);
            ability.setPerformRatio(0);
            ability.setAvgPerformRatio(0);
            ability.setOnWorkRatio(0);
            ability.setAvgOnWorkRatio(0);
            ability.setTrainRatio(0);
            ability.setAvgTrainRatio(0);
            return ability;
        }
        if (employee.getEmpEducation() != null) {
            String empEducation = employee.getEmpEducation();
            ability.setEducationRatio(10 + Integer.parseInt(empEducation));
        } else {
            ability.setEducationRatio(10);
        }
        String regex = "\\d+";
        int sum = employees.stream().filter(s -> s.getEmpEducation() != null).filter(s -> {
            String empEducation = s.getEmpEducation();
            return empEducation.matches(regex);
        }).mapToInt(s -> Integer.parseInt(s.getEmpEducation())).sum();
        if (sum != 0) {
            int avgEducation = (sum + 10 * empNums) / empNums;
            ability.setAvgEducationRatio(avgEducation);
        } else {
            ability.setAvgEducationRatio(10);
        }

        List<CredentialsDetailsVO> empCredentials = empCredentialsMapper.findInfosByEmpIds(Collections.singletonList(empId));
        if (!CollectionUtils.isEmpty(empCredentials)) {
            if (empCredentials.size() > 10) {
                ability.setPaperNum(20);
            } else {
                ability.setPaperNum(empCredentials.size() + 10);
            }
        } else {
            ability.setPaperNum(10);
        }
        List<CredentialsDetailsVO> allCredentials = empCredentialsMapper.findInfosByEmpIds(empIds);
        if (!CollectionUtils.isEmpty(allCredentials)) {
            int avgPaperNum = (allCredentials.size() + empNums * 10) / empNums;
            ability.setAvgPaperNum(avgPaperNum);
        } else {
            ability.setAvgPerformRatio(10);
        }

        Date attendTime = employee.getAttendTime();
        Date now = new Date();
        if (attendTime != null) {
            long workYears = DateUtils.calculateDays(attendTime, now) / 365;
            if (workYears > 0) {
                if (workYears > 10) {
                    ability.setWorkYears(20);
                } else {
                    ability.setWorkYears(10 + (int) workYears);
                }
            } else {
                ability.setWorkYears(10);
            }
        } else {
            ability.setWorkYears(10);
        }
        List<Date> allAttendTime = employees.stream().filter(s -> s.getAttendTime() != null).map(Employee::getAttendTime).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(allAttendTime) && allAttendTime.size() != 0) {
            long allWorkYears = 0;
            for (Date allTime : allAttendTime) {
                allWorkYears = DateUtils.calculateDays(allTime, now) / 365 + allWorkYears;
            }
            int avgWorkYears = (int) (allWorkYears / allAttendTime.size() + 10 * allAttendTime.size()) / allAttendTime.size();
            ability.setAvgWorkYears(avgWorkYears);
        } else {
            ability.setAvgWorkYears(10);
        }

        List<EmpPerformance> empPerformances = empPerformanceMapper.findInfosDuringOneYearByEmpIds(Collections.singletonList(empId));
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
            ability.setPerformRatio(10 + score);
        } else {
            ability.setPerformRatio(10);
        }
        List<EmpPerformance> allPerformances = empPerformanceMapper.findInfosDuringOneYearByEmpIds(empIds);
        if (!CollectionUtils.isEmpty(allPerformances)) {
            int score = 0;
            for (EmpPerformance allPerformance : allPerformances) {
                if ("A".equals(allPerformance.getPerfLevelValue())) {
                    score = score + 10;
                }
                if ("B".equals(allPerformance.getPerfLevelValue())) {
                    score = score + 7;
                }
                if ("C".equals(allPerformance.getPerfLevelValue())) {
                    score = score + 4;
                }
                if ("D".equals(allPerformance.getPerfLevelValue())) {
                    score = score + 1;
                }
            }
            ability.setAvgPerformRatio((score + 10 * empNums) / empNums);
        } else {
            ability.setAvgPerformRatio(10);
        }

        List<EmpAttendance> empAttendances = empAttendanceMapper.findInfosDuringOneYearByEmpIds(Collections.singletonList(empId));
        if (!CollectionUtils.isEmpty(empAttendances)) {
            List<EmpAttendance> onWorks = empAttendances.stream().filter(s -> AttendStatusEnum.ONE.getKey().
                    equals(s.getAttendStatus())).collect(Collectors.toList());
            int onWorkRatio = onWorks.size() / empAttendances.size() * 10 + 10;
            ability.setOnWorkRatio(onWorkRatio + 10);
        } else {
            ability.setOnWorkRatio(10);
        }
        List<EmpAttendance> allAttendances = empAttendanceMapper.findInfosDuringOneYearByEmpIds(empIds);
        if (!CollectionUtils.isEmpty(allAttendances)) {
            List<EmpAttendance> allOnWorks = allAttendances.stream().filter(s -> AttendStatusEnum.ONE.getKey().
                    equals(s.getAttendStatus())).collect(Collectors.toList());
            int avgOnWorkRatio = (allOnWorks.size() / allAttendances.size() * 10 + 10 * allOnWorks.size()) / allOnWorks.size();
            ability.setAvgOnWorkRatio(avgOnWorkRatio);
        } else {
            ability.setAvgOnWorkRatio(10);
        }
        List<EmpTrain> empTrains = empTrainMapper.findInfosDuringOneYearByEmpIds(Collections.singletonList(empId));
        if (!CollectionUtils.isEmpty(empTrains)) {
            if (empTrains.size() > 10) {
                ability.setTrainRatio(20);
            } else {
                int trainRatio = 10 + empTrains.size();
                ability.setTrainRatio(trainRatio);
            }
        } else {
            ability.setTrainRatio(10);
        }
        List<EmpTrain> allTrains = empTrainMapper.findInfosDuringOneYearByEmpIds(empIds);
        if (!CollectionUtils.isEmpty(allTrains) && allTrains.size() > 0) {
            int avgTrain = (allTrains.size() + 10 * allTrains.size()) / allTrains.size();
            ability.setAvgTrainRatio(avgTrain);
        } else {
            ability.setAvgTrainRatio(10);
        }
        ability.setAbilityRatio((ability.getEducationRatio() + ability.getPaperNum() + ability.getWorkYears() + ability.getPerformRatio()
                + ability.getOnWorkRatio() + ability.getTrainRatio()) / 6);
        return ability;
    }

    @Override
    public List<EmpCredentialSimpleVO> getEmpPaperInfos(Long empId) {

        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (existEmployee == null) {
            throw new ServiceException("该员工不存在");
        }
        List<EmpCredentialSimpleVO> result = new ArrayList<>();
        List<CredentialsDetailsVO> paperInfos = empCredentialsMapper.findInfosByEmpIds(Collections.singletonList(empId));
        for (CredentialsDetailsVO cdo : paperInfos) {
            Long credAualificationAnnex = cdo.getCredAualificationAnnex();
            EmpDocument credAualificationAnnexDoc = empDocumentMapper.selectEmpDocumentByDocId(credAualificationAnnex);

            cdo.setCredAualificationDocument(credAualificationAnnexDoc);

            List<Long> credRegistAnnexs = cdo.getCredRegistAnnex();
            for (Long credRegistAnnex : credRegistAnnexs) {
                EmpDocument credRegistAnnexDoc = empDocumentMapper.selectEmpDocumentByDocId(credRegistAnnex);
                cdo.getCredRegistAnnexDocuments().add(credRegistAnnexDoc);
            }
        }
        for (CredentialsDetailsVO paperInfo : paperInfos) {
            EmpCredentialSimpleVO empCredentialSimple = new EmpCredentialSimpleVO();
            BeanUtils.copyProperties(paperInfo, empCredentialSimple);
            List<String> paperPaths = paperInfo.getCredRegistAnnexDocuments().stream().map(EmpDocument::getDocAnnexPath).
                    collect(Collectors.toList());
            List<FileVO> fileInfos = new ArrayList<>();
            for (EmpDocument document : paperInfo.getCredRegistAnnexDocuments()) {
                FileVO file = new FileVO();
                file.setFileId(document.getDocId());
                file.setFileName(document.getDocName());
                file.setFileUrl(document.getDocAnnexPath());
                fileInfos.add(file);
            }
            empCredentialSimple.setFileInfos(fileInfos);
            empCredentialSimple.setPaperPath(paperPaths);
            result.add(empCredentialSimple);
        }
        return result;
    }

    @Override
    public List<EmpResume> getEmpResumeInfos(Long empId) {

        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (existEmployee == null) {
            throw new ServiceException("该员工不存在");
        }
        List<EmpResume> empResumes = empResumeMapper.findInfosByEmpIds(Collections.singletonList(empId));
        List<Long> deptIds = empResumes.stream().filter(s -> s.getResuDept() != null).filter(s -> s.getResuDept().matches("\\d+")).
                map(s -> Long.parseLong(s.getResuDept())).collect(Collectors.toList());
        Map<Long, Department> deptMap = new HashMap<>(16);
        Map<Long, EmployingUnits> unitsMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            List<Department> deptInfos = departmentMapper.findDeptInfosByDeptIds(deptIds);
            deptMap = deptInfos.stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
            List<Long> unitIds = deptInfos.stream().map(Department::getDeptUnitId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(unitIds)) {
                unitsMap = unitsMapper.findUnitInfoByUnitIds(unitIds).stream().
                        collect(Collectors.toMap(EmployingUnits::getUnitId, Function.identity()));
            }
        }
        for (EmpResume empResume : empResumes) {
            String resuDept = empResume.getResuDept();
            if (!StringUtils.isEmpty(resuDept) && resuDept.matches("\\d+")) {
                Long deptId = Long.parseLong(resuDept);
                Department deptInfo = deptMap.getOrDefault(deptId, new Department());
                empResume.setResuDeptName(deptInfo.getDeptName());
                if (deptInfo.getDeptUnitId() != null) {
                    EmployingUnits unitInfo = unitsMap.getOrDefault(deptInfo.getDeptUnitId(), new EmployingUnits());
                    empResume.setResuWorkUnitName(unitInfo.getUnitName());
                }
            }
        }
        return empResumes;
    }


    public int calculateYearDifference(Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = startLocalDate.until(endLocalDate);
        return period.getYears();
    }


    /**
     * 根据员工ID,身份证 统计查询员工个人历年绩效列表
     *
     * @param search 员工绩效
     * @return 绩效统计集合
     */
    @Override
    public EmpPerformGeneralVO countPersonalPerformance(EmpPerformance search) {

        EmpPerformGeneralVO empPerformGeneral = new EmpPerformGeneralVO();
        List<EmpPerformYearInfoVO> performYearInfos = new ArrayList<>();
        List<EmpPerformSimpleVO> performSimpleInfos = new ArrayList<>();
        search.setFlag(1);
        List<EmpPerformance> empPerformances = empPerformanceMapper.selectEmpPerformanceList(search);
        if (CollectionUtils.isEmpty(empPerformances)) {
            return empPerformGeneral;
        }
        Map<String, List<EmpPerformance>> yearMap = empPerformances.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfYear));

        for (String year : yearMap.keySet()) {
            EmpPerformYearInfoVO empPerformYearInfo = new EmpPerformYearInfoVO();
            empPerformYearInfo.setYear(year);
            List<EmpPerformance> performances = yearMap.get(year);
            Map<String, List<EmpPerformance>> cycleMap = performances.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfCycle));
            List<EmpPerformSeasonVO> seasonInfos = new ArrayList<>();
            for (String cycle : cycleMap.keySet()) {
                for (PerfCycleEnum value : PerfCycleEnum.values()) {
                    if (value.getKey().equals(cycle)) {
                        EmpPerformSeasonVO empPerformSeason = new EmpPerformSeasonVO();
                        empPerformSeason.setSeasonName(value.getValue());
                        String evaluation = cycleMap.get(cycle).get(0).getPerfLevelValue();
                        empPerformSeason.setEvaluation(evaluation);
                        seasonInfos.add(empPerformSeason);
                    }
                }
            }
            List<String> existSeasonNames = seasonInfos.stream().map(EmpPerformSeasonVO::getSeasonName).collect(Collectors.toList());
            for (PerfCycleEnum value : PerfCycleEnum.values()) {
                if (!existSeasonNames.contains(value.getValue())) {
                    EmpPerformSeasonVO empPerformSeason = new EmpPerformSeasonVO();
                    empPerformSeason.setSeasonName(value.getValue());
                    empPerformSeason.setEvaluation("");
                    seasonInfos.add(empPerformSeason);
                }
            }
            empPerformYearInfo.setSeasonInfos(seasonInfos);
            performYearInfos.add(empPerformYearInfo);
        }

        Map<String, List<EmpPerformance>> levelValueMap = empPerformances.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfLevelValue));
        for (String levelValue : levelValueMap.keySet()) {
            EmpPerformSimpleVO empPerformSimple = new EmpPerformSimpleVO();
            empPerformSimple.setPerformType("绩效" + levelValue);
            empPerformSimple.setNum(levelValueMap.get(levelValue).size());
            performSimpleInfos.add(empPerformSimple);
        }
        empPerformGeneral.setYearInfos(performYearInfos);
        empPerformGeneral.setPerformSimpleInfos(performSimpleInfos);

//        TreeMap<String, TreeMap<String, String>> res = new TreeMap<>();
//        TreeMap<String, String> levelValuesCount = new TreeMap<>();
//
//        for (EmpPerformance empPerformance : empPerformances) {
//            TreeMap<String, String> yearCount = null;
//            if (!res.containsKey(empPerformance.getPerfYear())) {
//                yearCount = new TreeMap<>();
//                res.put(empPerformance.getPerfYear(), yearCount);
//            } else {
//                yearCount = res.get(empPerformance.getPerfYear());
//            }
//            yearCount.put(empPerformance.getPerfCycle(), empPerformance.getPerfLevelValue());
//            if (!levelValuesCount.containsKey(empPerformance.getPerfLevelValue())) {
//                levelValuesCount.put(empPerformance.getPerfLevelValue(), "1");
//            } else {
//                levelValuesCount.put(empPerformance.getPerfLevelValue(),
//                        "" + (Integer.parseInt(levelValuesCount.get(empPerformance.getPerfLevelValue())) + 1));
//            }
//        }
//
//        if (res.size() == 0) {
//            TreeMap map = new TreeMap();
//            map.put("performanceTableInfo", null);
//            map.put("salaryCondition", null);
//            return map;
//        }
//        Integer nowYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
//        Integer firstYear = Integer.valueOf(res.firstKey()) < nowYear - 6 ? Integer.valueOf(res.firstKey()) : nowYear - 6;
//
//        for (Integer year = firstYear; year <= nowYear; year++) {
//
//            if (!res.containsKey(year.toString())) {
//                res.put(year.toString(), new TreeMap<>());
//            }
//            for (Integer cycle = 0; cycle <= 4; cycle++) {
//                if (!res.get(year.toString()).containsKey(cycle.toString())) {
//                    res.get(year.toString()).put(cycle.toString(), null);
//                }
//            }
//        }
//        TreeMap map = new TreeMap();
//        map.put("performanceTableInfo", res);
//
//        map.put("salaryCondition", levelValuesCount);


        return empPerformGeneral;
    }


    @Override
    public EmpAttendanceOverviewVO getEmpAttendanceOverview(Long empId, Date startTime, Date endTime) {
        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (ObjectUtils.isEmpty(existEmployee)) {
            throw new ServiceException("此员工不存在");
        }
        List<SysDictData> attendStatusDicInfos = sysDictDataMapper.selectDictDataByType("attend_status");
        List<EmpAttendance> empAttendances = empAttendanceMapper.findEmpAttendanceByEmpIdAndAttendRecordDateBetween(empId, startTime, endTime);
        if (CollectionUtils.isEmpty(empAttendances)) {
            Map<String, Integer> attendanceCount = new HashMap<>(32);
            List<EmpAttendNumVO> attendNumList = new ArrayList<>();
            for (SysDictData attendStatusDicInfo : attendStatusDicInfos) {
                attendanceCount.put(attendStatusDicInfo.getDictValue(), 0);
                EmpAttendNumVO empAttendNum = new EmpAttendNumVO(attendStatusDicInfo.getDictValue(), 0);
                attendNumList.add(empAttendNum);
            }
            attendNumList = attendNumList.stream().sorted().
                    collect(Collectors.toList());
            return new EmpAttendanceOverviewVO(attendanceCount, attendNumList, 0);
        }
        // 先根据记录日期排序，再根据id排序
        empAttendances.sort(Comparator.comparing(EmpAttendance::getAttendRecordDate).thenComparing(EmpAttendance::getAttendId));

        //   List<String> workingStatus = Arrays.asList(AttendStatusEnum.ONE.getKey(), AttendStatusEnum.TWO.getKey(), AttendStatusEnum.FOUR.getKey());

        int continuousDays = 0;
        String continuousWorkingNum = "continuous";
        Map<String, Integer> attendanceCount = new HashMap<>(32);
//        attendanceCount.put(AttendStatusEnum.ONE.getKey(), 0);
//        attendanceCount.put(AttendStatusEnum.TWO.getKey(), 0);
//        attendanceCount.put(AttendStatusEnum.THREE.getKey(), 0);
//        attendanceCount.put(AttendStatusEnum.FOUR.getKey(), 0);
        attendanceCount.put(continuousWorkingNum, 0);

        for (int i = 0; i < empAttendances.size(); i++) {
            EmpAttendance empAttendance = empAttendances.get(i);
            String attendStatus = empAttendance.getAttendStatus();
            for (SysDictData attendStatusDicInfo : attendStatusDicInfos) {
                if (attendStatusDicInfo.getDictValue().equals(attendStatus)) {
                    attendanceCount.merge(attendStatusDicInfo.getDictValue(), 1, Integer::sum);
                }
            }

            // 计算考勤状态个数
//            if (!ObjectUtils.isEmpty(empAttendance.getAttendStatus()) && attendanceCount.containsKey(empAttendance.getAttendStatus())) {
//                int count = attendanceCount.get(empAttendance.getAttendStatus());
//                attendanceCount.put(empAttendance.getAttendStatus(), count + 1);
//            }
            // 计算连续上班最大天数
            boolean verifyCount = (i == 0 && AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()))
                    || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()) && AttendStatusEnum.ONE.getKey().equals(empAttendances.get(i - 1).getAttendStatus()))
                    || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()));
            if (verifyCount) {
                continuousDays++;
            } else {
                // 判断当前连续上班天数是否为最大值，并更新连续上班天数
                Integer maxContinuousDays = attendanceCount.get(continuousWorkingNum);
                if (continuousDays > maxContinuousDays) {
                    attendanceCount.put(continuousWorkingNum, continuousDays);
                }
                // 重置连续上班天数
                continuousDays = 0;
            }
        }
        // 判断最后一次计数是否为最大值
        int lastCount = attendanceCount.get(continuousWorkingNum);
        if (continuousDays > lastCount) {
            attendanceCount.put(continuousWorkingNum, continuousDays);
        }
        for (SysDictData attendStatusDicInfo : attendStatusDicInfos) {
            if (!attendanceCount.containsKey(attendStatusDicInfo.getDictValue())) {
                attendanceCount.put(attendStatusDicInfo.getDictValue(), 0);
            }
        }
        Map<String, Integer> attendMap = new HashMap<>(32);
        for (String key : attendanceCount.keySet()) {
            if (!continuousWorkingNum.equals(key)) {
                attendMap.put(key, attendanceCount.get(key));
            }
        }
        for (SysDictData attendStatus : attendStatusDicInfos) {
            if (!attendMap.containsKey(attendStatus.getDictValue())) {
                attendMap.put(attendStatus.getDictValue(), 0);
            }
        }
        List<EmpAttendNumVO> attendNumList = new ArrayList<>();
        for (String key : attendMap.keySet()) {
            EmpAttendNumVO empAttendNum = new EmpAttendNumVO();
            empAttendNum.setAttendStatus(key);
            empAttendNum.setNum(attendMap.get(key));
            attendNumList.add(empAttendNum);
        }
        attendNumList = attendNumList.stream().sorted().
                collect(Collectors.toList());
//        return new EmpAttendanceOverviewVO(attendanceCount.get(AttendStatusEnum.ONE.getKey()),
//                attendanceCount.get(AttendStatusEnum.TWO.getKey()), attendanceCount.get(AttendStatusEnum.THREE.getKey()),
//                attendanceCount.get(AttendStatusEnum.FOUR.getKey()), attendanceCount.get(continuousWorkingNum));
        EmpAttendanceOverviewVO empAttendanceOverview = new EmpAttendanceOverviewVO();
        empAttendanceOverview.setAttendMap(attendMap);
        empAttendanceOverview.setAttendNumList(attendNumList);
        empAttendanceOverview.setMaxContinuousWorkingNum(attendanceCount.getOrDefault(continuousWorkingNum, 0));
        return empAttendanceOverview;
    }

    @Override
    public List<EmpAttendanceDetailVO> getEmpAttendanceDetail(Long empId, Date time) {
        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (ObjectUtils.isEmpty(existEmployee)) {
            throw new ServiceException("此员工不存在");
        }

        List<EmpAttendance> empAttendances = empAttendanceMapper.findEmpAttendanceByEmpIdAndAttendRecordDate(empId, time);
        if (CollectionUtils.isEmpty(empAttendances)) {
            return Collections.emptyList();
        }

        return empAttendances.stream().collect(Collectors.groupingBy(EmpAttendance::getAttendRecordDate))
                .entrySet().stream().map(data -> new EmpAttendanceDetailVO(data.getKey(),
                        data.getValue().stream().sorted(
                                Comparator.comparing(EmpAttendance::getAttendId)
                        ).map(EmpAttendance::getAttendStatus).collect(Collectors.toList()))
                ).sorted(Comparator.comparing(EmpAttendanceDetailVO::getTime)).collect(Collectors.toList());
    }

    @Override
    public EmpAttendanceStatisticsVO getEmpAttendanceStatistics(Long empId) {
        Employee existEmployee = employeeMapper.selectEmployeeByEmpId(empId);
        if (ObjectUtils.isEmpty(existEmployee)) {
            throw new ServiceException("此员工不存在");
        }

        Date endTime = new Date();
        Date startTime = getTimeByMonth(endTime, -5);

        String formatStr = "yyyy.MM";
        List<String> xAxis = getMonthBetween(startTime, endTime, formatStr);
        EmpAttendanceStatisticsVO res = new EmpAttendanceStatisticsVO();
        res.setXAxis(xAxis);
//        List<Integer> attendanceAxis = Arrays.asList(0, 0, 0, 0, 0, 0);
//        List<Integer> onBusinessAxis = Arrays.asList(0, 0, 0, 0, 0, 0);
//        List<Integer> overTimeAxis = Arrays.asList(0, 0, 0, 0, 0, 0);

        List<SysDictData> attendStatusDicInfos = sysDictDataMapper.selectDictDataByType("attend_status");
        attendStatusDicInfos = attendStatusDicInfos.stream().sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getDictValue()))).collect(Collectors.toList());
        List<EmpAttendance> empAttendances = empAttendanceMapper.findEmpAttendanceByEmpIdAndAttendRecordDateBetweenByMonth(empId, startTime, endTime);
        for (EmpAttendance empAttendance : empAttendances) {
            for (SysDictData attendStatusDicInfo : attendStatusDicInfos) {
                if (attendStatusDicInfo.getDictValue().equals(empAttendance.getAttendStatus())) {
                    empAttendance.setAttendStatus(attendStatusDicInfo.getDictLabel());
                }
            }
        }

        List<EmpAttendanceStatusInfoVO> attendanceStatusInfos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(empAttendances)) {
            Map<String, Map<String, Long>> attendanceMap = empAttendances.stream().collect(
                    Collectors.groupingBy(entry -> new SimpleDateFormat(formatStr).format(entry.getAttendRecordDate()),
                            Collectors.groupingBy(EmpAttendance::getAttendStatus, Collectors.counting())
                    ));
            for (int i = 0; i < xAxis.size(); i++) {
                if (attendanceMap.containsKey(xAxis.get(i))) {
                    EmpAttendanceStatusInfoVO empAttendanceStatusInfo = new EmpAttendanceStatusInfoVO();
                    Map<String, Long> countMap = attendanceMap.get(xAxis.get(i));
//                    attendanceAxis.set(i, countMap.getOrDefault(AttendStatusEnum.ONE.getKey(), 0L).intValue());
//                    onBusinessAxis.set(i, countMap.getOrDefault(AttendStatusEnum.TWO.getKey(), 0L).intValue());
//                    overTimeAxis.set(i, countMap.getOrDefault(AttendStatusEnum.FOUR.getKey(), 0L).intValue());
                    empAttendanceStatusInfo.setXAxi(xAxis.get(i));
                    for (SysDictData attendStatusDicInfo : attendStatusDicInfos) {
                        if (!countMap.containsKey(attendStatusDicInfo.getDictLabel())) {
                            countMap.put(attendStatusDicInfo.getDictLabel(), 0L);
                        }
                    }
                    empAttendanceStatusInfo.setAttendMap(countMap);
                    attendanceStatusInfos.add(empAttendanceStatusInfo);
                }
            }
        }
        res.setAttendanceStatusInfos(attendanceStatusInfos);
//        res.setAttendanceAxis(attendanceAxis);
//        res.setOnBusinessAxis(onBusinessAxis);
//        res.setOverTimeAxis(overTimeAxis);
        return res;
    }

    /**
     * 获取时间前六个月的时间
     */
    public static Date getTimeByMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 获取两个时间节点之间的月份列表
     **/
    public static List<String> getMonthBetween(Date minDate, Date maxDate, String formatStr) {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(minDate);
        max.setTime(maxDate);

        while (!min.after(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }

}
