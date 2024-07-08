package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpTypeEnum {

    ONE("1", "特级员工"),

    TWO("2", "普通员工"),


    ;

    private String key;

    private String value;
}
