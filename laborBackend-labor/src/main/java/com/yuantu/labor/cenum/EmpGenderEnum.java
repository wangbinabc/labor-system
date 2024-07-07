package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpGenderEnum {

    MALE("0", "男"),

    FEMALE("1", "女"),


    ;

    private String key;

    private String value;
}
