package com.yuantu.labor.vo;

import lombok.Data;

@Data
public class AttendanceYearVO {
    /** 统计的年份 */
    private String recordYear;

    private String attendStatus;

    private Long num;
}
