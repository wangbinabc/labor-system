package com.yuantu.labor.vo;

import lombok.Data;

@Data
public class EmpAttendanceQueryParamsVO {
   private String nameOrIdcard;
    private  String year;
    private String month;

    public EmpAttendanceQueryParamsVO(String nameOrIdcard, String year, String month) {
        this.nameOrIdcard = nameOrIdcard;
        this.year = year;
        this.month = month;
    }
}
