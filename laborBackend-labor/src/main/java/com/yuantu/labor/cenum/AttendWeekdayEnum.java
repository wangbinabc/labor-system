package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : mt
 * @create 2023/10/17 11:18
 */
@Getter
@AllArgsConstructor
public enum AttendWeekdayEnum {

    ONE("0", "是"),

    TWO("1", "否"),





    ;

    private String key;

    private String value;
}
