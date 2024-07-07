package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpSalaryEnum {

    ONE("1", "薪级A"),

    TWO("2", "薪级B"),

    THREE("3", "薪级C"),


    ;

    private String key;

    private String value;
}
