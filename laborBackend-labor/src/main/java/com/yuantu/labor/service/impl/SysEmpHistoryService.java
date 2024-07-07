package com.yuantu.labor.service.impl;

import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SysEmpHistoryService {


    @Autowired
    private EmployeeMapper employeeMapper;


    @Autowired
    private EmpHistoryMapper empHistoryMapper;

    @Autowired
    private FamilyRelationsMapper familyRelationsMapper;

    @Autowired
    private FamilyRelationsHistoryMapper familyRelationsHistoryMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;


    @Autowired
    private LoanWorkerHistoryMapper loanWorkerHistoryMapper;

    @PostConstruct
    public void init() {
        List<Employee> employees = employeeMapper.findCurrentInfos();
        List<LoanWorker> loanWorkers = loanWorkerMapper.findCurrentInfos();
        if (CollectionUtils.isEmpty(employees) && CollectionUtils.isEmpty(loanWorkers)) {
            return;
        }
        //查询快照表，找出最近的快照记录
        Date now = new Date();
        String latestYearMonthForEmp = empHistoryMapper.findLatestHistoryYearMonth();
        String latestYearMonthForLoanWorker = loanWorkerHistoryMapper.findLatestHistoryYearMonth();
        if (StringUtils.isNotEmpty(latestYearMonthForEmp)) {
            LocalDate currDate = LocalDate.now();
            YearMonth yearMonth = YearMonth.from(currDate.withDayOfMonth(1));
            LocalDate givenDateForEmp = LocalDate.parse(latestYearMonthForEmp + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算两个日期之间的月数差
            long monthsDiffForEmp = ChronoUnit.MONTHS.between(currDate.withDayOfMonth(1), givenDateForEmp.withDayOfMonth(1));
            for (int i = 0; i < Math.abs(monthsDiffForEmp); i++) {
                if (monthsDiffForEmp > 0) {
                    yearMonth = yearMonth.plusMonths(1);
                } else {
                    yearMonth = yearMonth.minusMonths(1);
                }
                // 排除比较的月份
                if (!yearMonth.equals(YearMonth.from(givenDateForEmp.withDayOfMonth(1)))) {
                    List<EmpHistory> empHistories = new ArrayList<>();
                    for (Employee employee : employees) {
                        EmpHistory empHistory = new EmpHistory();
                        empHistory.setHistoryYearMoth(yearMonth.toString());
                        BeanUtils.copyProperties(employee, empHistory);
                        empHistory.setBirthDate(employee.getBirthDate());
                        empHistory.setEmpAge(employee.getEmpAge());
                        empHistory.setEmpExpireTime(employee.getEmpExpireTime());
                        empHistory.setCreateTime(now);
                        empHistory.setDisabled(0);
                        empHistories.add(empHistory);
                    }
                    empHistoryMapper.removeEmpHistoryListByYearMonth(yearMonth.toString());
                    empHistoryMapper.batchInsertHistoryInfos(empHistories);
                    Map<Long, EmpHistory> empHistoryMap = empHistories.stream().collect(Collectors.toMap(EmpHistory::getEmpId,
                            Function.identity(), (s1, s2) -> s2));
                    List<Long> empIds = employees.stream().map(Employee::getEmpId).collect(Collectors.toList());
                    List<FamilyRelations> familyRelations = familyRelationsMapper.selectFamilyInfosByEmpIds(empIds);
                    List<FamilyRelationsHistory> familyRelationsHistories = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(familyRelations)) {
                        for (FamilyRelations familyRelation : familyRelations) {
                            FamilyRelationsHistory familyRelationsHistory = new FamilyRelationsHistory();
                            BeanUtils.copyProperties(familyRelation, familyRelationsHistory);
                            Long famEmpId = familyRelation.getFamEmpId();
                            EmpHistory empHistory = empHistoryMap.getOrDefault(famEmpId, new EmpHistory());
                            familyRelationsHistory.setFamEmpId(empHistory.getHistoryId());
                            familyRelationsHistory.setHistoryYearMonth(yearMonth.toString());
                            familyRelationsHistories.add(familyRelationsHistory);
                        }
                        familyRelationsHistoryMapper.batchInsertFamilyRelationsHistory(familyRelationsHistories);
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(latestYearMonthForLoanWorker)) {
            LocalDate currDate = LocalDate.now();
            YearMonth yearMonth = YearMonth.from(currDate.withDayOfMonth(1));
            LocalDate givenDateForLoan = LocalDate.parse(latestYearMonthForLoanWorker + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算两个日期之间的月数差
            long monthsDiffForLoan = ChronoUnit.MONTHS.between(currDate.withDayOfMonth(1), givenDateForLoan.withDayOfMonth(1));
            for (int i = 0; i < Math.abs(monthsDiffForLoan); i++) {
                if (monthsDiffForLoan > 0) {
                    yearMonth = yearMonth.plusMonths(1);
                } else {
                    yearMonth = yearMonth.minusMonths(1);
                }
                // 排除比较的月份
                if (!yearMonth.equals(YearMonth.from(givenDateForLoan.withDayOfMonth(1)))) {
                    List<LoanWorkerHistory> loanWorkerHistories = new ArrayList<>();
                    for (LoanWorker loanWorker : loanWorkers) {
                        LoanWorkerHistory loanWorkerHistory = new LoanWorkerHistory();
                        loanWorkerHistory.setHistoryYearMonth(yearMonth.toString());
                        BeanUtils.copyProperties(loanWorker, loanWorkerHistory);
                        loanWorkerHistory.setCreateTime(now);
                        loanWorkerHistory.setDisabled(false);
                        loanWorkerHistories.add(loanWorkerHistory);
                    }
                    loanWorkerHistoryMapper.removeCurrentMonthHistory(yearMonth.toString());
                    loanWorkerHistoryMapper.batchInsertHistoryInfos(loanWorkerHistories);
                }
            }
        }
        System.out.println("初始化数据完成");
    }


//    public static void main(String[] args) {
//        LocalDate givenDate = LocalDate.parse("2024-05-01", DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDate currDate = LocalDate.now();
//        long monthsDiff = ChronoUnit.MONTHS.between(currDate.withDayOfMonth(1), givenDate.withDayOfMonth(1));
//        YearMonth yearMonth = YearMonth.from(currDate.withDayOfMonth(1));
//        for (int i = 0; i < Math.abs(monthsDiff); i++) {
//            if (monthsDiff > 0) {
//                yearMonth = yearMonth.plusMonths(1);
//            } else {
//                yearMonth = yearMonth.minusMonths(1);
//            }
//            // 排除比较的月份
//            if (!yearMonth.equals(YearMonth.from(givenDate.withDayOfMonth(1)))) {
//                System.out.println(yearMonth.toString());
//            }
//        }
//    }
}
