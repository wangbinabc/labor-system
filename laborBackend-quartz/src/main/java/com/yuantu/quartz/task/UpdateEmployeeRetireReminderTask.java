package com.yuantu.quartz.task;


import com.yuantu.common.utils.DateUtils;
import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmpHistoryMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component("updateEmployeeRetireTask")
public class UpdateEmployeeRetireReminderTask {


    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpHistoryMapper empHistoryMapper;


    public void updateEmpRetireReminder() {

        List<Employee> employees = employeeMapper.findAllEmployees();
        if (CollectionUtils.isEmpty(employees)) {
            return;
        }
        Date now = new Date();
        for (Employee employee : employees) {
            Date empExpireTime = employee.getEmpExpireTime();
            if (empExpireTime != null) {
                long days = DateUtils.calculateDays(now, empExpireTime);
                employee.setRetireReminder(days + "");
                employeeMapper.updateRetireReminder(employee.getEmpId(), employee.getRetireReminder());
            }
        }
    }
}
