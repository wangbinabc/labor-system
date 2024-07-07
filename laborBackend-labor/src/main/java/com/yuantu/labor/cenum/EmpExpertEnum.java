package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpExpertEnum {

    ONE("1", "一级"),

    TWO("2", "二级"),

    THREE("3", "三级"),

    FOUR("4", "四级"),

    FIVE("5", "五级"),


    ;

    private String key;

    private String value;
}
