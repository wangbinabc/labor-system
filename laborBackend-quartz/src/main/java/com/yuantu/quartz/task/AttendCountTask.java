package com.yuantu.quartz.task;

import com.yuantu.labor.cenum.AttendStatusEnum;
import com.yuantu.labor.cenum.EmpStatusEnum;
import com.yuantu.labor.domain.AttendCount;
import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.AttendCountMapper;
import com.yuantu.labor.mapper.EmpAttendanceMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component("attendCountTask")
public class AttendCountTask {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpAttendanceMapper attendanceMapper;

    @Autowired
    private AttendCountMapper attendCountMapper;


    public void insertAttendInfos() {

        List<Employee> allEmployees = employeeMapper.findAllEmployees();
        List<Long> empIds = allEmployees.stream().filter(s -> !EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus()) &&
                !EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).map(Employee::getEmpId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(empIds)) {
            return;
        }
        Map<Long, List<EmpAttendance>> attendMap = attendanceMapper.findInfosByEmpIds(empIds).stream().
                collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
        List<AttendCount> attendCounts = new ArrayList<>();

        for (Long empId : attendMap.keySet()) {
            Map<String, List<EmpAttendance>> attendYearMap = attendMap.get(empId).stream().
                    filter(s -> s.getAttendRecordDate() != null).collect(Collectors.groupingBy(s -> {
                        Date attendRecordDate = s.getAttendRecordDate();
                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                        return yearFormat.format(attendRecordDate);
                    }));
            for (String year : attendYearMap.keySet()) {
                AttendCount attendCount = new AttendCount();
                attendCount.setEmpId(empId);
                attendCount.setCurrentYear(year);
                List<EmpAttendance> empAttendances = attendYearMap.get(year);
                // 先根据记录日期排序，再根据id排序
                empAttendances.sort(Comparator.comparing(EmpAttendance::getAttendRecordDate).thenComparing(EmpAttendance::getAttendId));

                int continuousDays = 0;
                String continuousWorkingNum = "continuous";
                Map<String, Integer> attendanceCount = new HashMap<>(16);
                attendanceCount.put(AttendStatusEnum.ONE.getKey(), 0);
                attendanceCount.put(AttendStatusEnum.TWO.getKey(), 0);
                attendanceCount.put(AttendStatusEnum.THREE.getKey(), 0);
                attendanceCount.put(AttendStatusEnum.FOUR.getKey(), 0);
                attendanceCount.put(continuousWorkingNum, 0);

                for (int i = 0; i < empAttendances.size(); i++) {
                    EmpAttendance empAttendance = empAttendances.get(i);
                    // 计算考勤状态个数
                    if (!ObjectUtils.isEmpty(empAttendance.getAttendStatus()) && attendanceCount.containsKey(empAttendance.getAttendStatus())) {
                        int count = attendanceCount.get(empAttendance.getAttendStatus());
                        attendanceCount.put(empAttendance.getAttendStatus(), count + 1);
                    }
                    // 计算连续上班最大天数
                    boolean verifyCount = (i == 0 && AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()))
                            || AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()) && AttendStatusEnum.ONE.getKey().equals(empAttendances.get(i - 1).getAttendStatus())
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
             //   attendCount.setTotalDays(attendanceCount.get(AttendStatusEnum.ONE.getKey()));
                attendCount.setLinkDays(attendanceCount.get(continuousWorkingNum));
             //   attendCount.setBusinessDays(attendanceCount.get(AttendStatusEnum.TWO.getKey()));
           //     attendCount.setOverTimeDays(attendanceCount.get(AttendStatusEnum.FOUR.getKey()));
             //   attendCount.setHolidayDays(attendanceCount.get(AttendStatusEnum.THREE.getKey()));
                attendCounts.add(attendCount);
            }
        }
        if (!CollectionUtils.isEmpty(attendCounts)) {
            attendCountMapper.deleteAllInfos();
            attendCountMapper.batchInsertInfos(attendCounts);
        }

    }


}
