package com.yuantu.quartz.task;


import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.cenum.EmpStatusEnum;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.system.mapper.SysConfigMapper;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("employeeChangeStatusTask")
public class EmployeeChangeStatusTask {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;


    public void changeEmployeeStatus() {

        List<Employee> employees = employeeMapper.findAllEmployees();
        if (CollectionUtils.isEmpty(employees)) {
            return;
        }
        String manRetireAge = configMapper.checkConfigKeyUnique("labor.man.retireAge").getConfigValue();
        String womanRetireAge = configMapper.checkConfigKeyUnique("labor.woman.retireAge").getConfigValue();
        for (Employee employee : employees) {
            String empIdcard = employee.getEmpIdcard();
            if (StringUtils.isNotEmpty(empIdcard)) {
                Date expireTime = DateUtils.calculateExpireTime(empIdcard, employee.getEmpGender(), manRetireAge, womanRetireAge);
                employee.setEmpExpireTime(expireTime);
                employeeMapper.updateExpireTimeByEmpId(expireTime, employee.getEmpId());
                String empPosition = employee.getEmpPosition();
                List<String> empPositionDinInfos = dictDataMapper.selectDictDataByType("emp_position").stream().
                        filter(s -> {
                            String dictLabel = s.getDictLabel();
                            return dictLabel.contains("造价") || dictLabel.contains("监理") || dictLabel.contains("安全质量监督")
                                    || dictLabel.contains("监造") || dictLabel.contains("设计") || dictLabel.contains("招标");
                        }).map(SysDictData::getDictValue).collect(Collectors.toList());

                if (empPositionDinInfos.contains(empPosition)) {
                    Date hireLimit = DateUtils.calculateExpireTime(empIdcard, employee.getEmpGender(), "65", "63");
                    employee.setHireLimitDate(hireLimit);
                } else {
                    employee.setHireLimitDate(expireTime);
                }
                employeeMapper.updateHireLimitDateByEmpId(employee.getHireLimitDate(), employee.getEmpId());
            }
        }

        // 获取当前日期
        Date now = new Date();
        // 创建一个Calendar对象
        Calendar calendar = Calendar.getInstance();
        // 设置Calendar对象的日期为当前日期
        calendar.setTime(now);
        // 将日期减去一个月
        calendar.add(Calendar.MONTH, -1);
        // 获取上一个月的日期
        Date lastMonthDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        List<Long> newRecruitIds = employees.stream().filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())).filter(s -> {
            Date createTime = s.getEmpCreateTime();
            String lastMonthDateStr = dateFormat.format(lastMonthDate);
            return lastMonthDateStr.compareTo(dateFormat.format(createTime)) >= 0;
        }).map(Employee::getEmpId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(newRecruitIds)) {
            employeeMapper.changeEmpStatusByEmpIdsAndEmpStatus(newRecruitIds, EmpStatusEnum.ON_POSITION.getKey(), now);
        }

        List<Long> soonExpiredIds = new ArrayList<>();
        List<Long> expiredIds = new ArrayList<>();
        List<Employee> onPositionEmpInfos = employees.stream().filter(s -> EmpStatusEnum.ON_POSITION.getKey().
                equals(s.getEmpStatus())).collect(Collectors.toList());
        String retireDays = configMapper.checkConfigKeyUnique("labor.employee.retireDays").getConfigValue();
        for (Employee onPositionEmpInfo : onPositionEmpInfos) {
            Date empExpireTime = onPositionEmpInfo.getEmpExpireTime();
            if (empExpireTime != null) {
                long intervalNum = empExpireTime.getTime() - now.getTime();
                long intervalDays = DateUtils.calculateDays(now, empExpireTime);
                if (intervalNum <= 0) {
                    expiredIds.add(onPositionEmpInfo.getEmpId());
                } else {
                    if (intervalDays <= Long.parseLong(retireDays)) {
                        soonExpiredIds.add(onPositionEmpInfo.getEmpId());
                    }
                }
            }
        }
        List<Employee> almostEmpInfos = employees.stream().filter(s -> EmpStatusEnum.ALMOST.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
        for (Employee almostEmpInfo : almostEmpInfos) {
            Date empExpireTime = almostEmpInfo.getEmpExpireTime();
            if (empExpireTime != null) {
                long intervalNum = empExpireTime.getTime() - now.getTime();
                if (intervalNum <= 0) {
                    expiredIds.add(almostEmpInfo.getEmpId());
                }
            }
        }
        if (!CollectionUtils.isEmpty(soonExpiredIds)) {
            employeeMapper.changeEmpStatusByEmpIdsAndEmpStatus(soonExpiredIds, EmpStatusEnum.ALMOST.getKey(), now);
        }
        if (!CollectionUtils.isEmpty(expiredIds)) {
            employeeMapper.changeEmpStatusByEmpIdsAndEmpStatus(expiredIds, EmpStatusEnum.EXPIRE.getKey(), now);
        }

        List<Employee> offEmployees = employees.stream().
                filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus()) || EmpStatusEnum.FIRE.getKey().
                        equals(s.getEmpStatus())).filter(s -> {
                    String lastMonthStr = dateFormat.format(lastMonthDate);
                    Date statusUpdateTime = s.getEmpStatusUpdateTime();
                    if (statusUpdateTime == null) {
                        statusUpdateTime = now;
                    }
                    String statusTimeStr = dateFormat.format(statusUpdateTime);
                    return lastMonthStr.equals(statusTimeStr) || lastMonthStr.compareTo(statusTimeStr) > 0;
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(offEmployees)) {
            List<Long> empIds = offEmployees.stream().
                    map(Employee::getEmpId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(empIds)) {
                employeeMapper.changeEmpInvisibleByEmpIdsAndInvisible(empIds, true);
            }
        }
        System.out.println("任务执行结束");
    }


}
