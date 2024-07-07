package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : mt
 * @create 2023/10/17 11:18
 */
@Getter
@AllArgsConstructor
public enum AttendStatusEnum {

    ONE("1", "出勤"),

    TWO("2", "出差"),

    THREE("3", "休假"),

    FOUR("4", "加班"),



    ;

    private String key;

    private String value;
}
