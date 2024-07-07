package com.yuantu.labor.vo;

import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.domain.EmpAttendance;
import lombok.Data;

import java.util.List;

@Data
public class EmpAttendanceChangeDicVO {


    private String chineseName;

    private List<EmpAttendance> exportInfos;

}
