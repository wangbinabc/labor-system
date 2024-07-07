package com.yuantu.quartz.task;


import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FamilyRelations;
import com.yuantu.labor.domain.FamilyRelationsHistory;
import com.yuantu.labor.mapper.EmpHistoryMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FamilyRelationsHistoryMapper;
import com.yuantu.labor.mapper.FamilyRelationsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("employeeRecordTask")
public class EmployeeRecordTask {


    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpHistoryMapper empHistoryMapper;

    @Autowired
    private FamilyRelationsMapper familyRelationsMapper;

    @Autowired
    private FamilyRelationsHistoryMapper familyRelationsHistoryMapper;

    public void saveEmployeeRecord() {

        List<Employee> employees = employeeMapper.findCurrentInfos();
        if (CollectionUtils.isEmpty(employees)) {
            return;
        }
        List<EmpHistory> empHistories = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date now = new Date();
        String dateStr = dateFormat.format(now);
        for (Employee employee : employees) {
            EmpHistory empHistory = new EmpHistory();
            empHistory.setHistoryYearMoth(dateStr);
            BeanUtils.copyProperties(employee, empHistory);
            empHistory.setBirthDate(employee.getBirthDate());
            empHistory.setEmpAge(employee.getEmpAge());
            empHistory.setEmpExpireTime(employee.getEmpExpireTime());
            empHistory.setCreateTime(now);
            empHistory.setDisabled(0);
            empHistories.add(empHistory);
        }
        empHistoryMapper.removeCurrentMonthHistory(now);
        empHistoryMapper.batchInsertHistoryInfos(empHistories);
        Map<Long, EmpHistory> empHistoryMap = empHistories.stream().collect(Collectors.toMap(EmpHistory::getEmpId, Function.identity()));
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
                familyRelationsHistory.setHistoryYearMonth(dateStr);
                familyRelationsHistories.add(familyRelationsHistory);
            }
            familyRelationsHistoryMapper.batchInsertFamilyRelationsHistory(familyRelationsHistories);
        }
        System.out.println("任务执行结束");
    }


}
