package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpEducationEnum {

    ONE("1", "高中"),

    TWO("2", "中专"),

    THREE("3", "大专"),

    FOUR("4", "本科"),

    FIVE("5", "硕士"),

    SIX("6", "博士"),

    ;

    private String key;

    private String value;
}
