package com.yuantu.labor.vo;

import lombok.Data;

@Data
public class CountDeptPerformanceVO {

    private Long dpDeptId;
    private String dpDeptName;
    private String dpYear;

    /**
     * 季度和年度考核结果
     * */
    private String firstQuarter;
    private String secondQuarter;
    private String thirdQuarter;
    private String fourthQuarter;
    private String thisYear;

}
