package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpCategoryEnum {

    ONE("1", "外包"),

    TWO("2", "全民"),

    THREE("3", "借工"),

    FOUR("4", "直签"),



    ;

    private String key;

    private String value;
}
