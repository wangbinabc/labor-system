package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpTitleEnum {

    ONE("1", "助理级"),

    TWO("2", "中级"),

    THREE("3", "副高级"),

    FOUR("4", "正高级"),


    ;

    private String key;

    private String value;
}
