package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.EmpPerformance;
import lombok.Data;

import java.util.List;

@Data
public class EmpPerformanceChangeDicVO {


    private String chineseName;

    private List<EmpPerformance> exportInfos;

}
