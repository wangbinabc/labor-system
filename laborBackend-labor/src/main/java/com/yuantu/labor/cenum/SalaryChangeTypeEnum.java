package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalaryChangeTypeEnum {
    ONE("1", "升薪"),

    TWO("2", "降薪"),



    ;

    private String key;

    private String value;
}
