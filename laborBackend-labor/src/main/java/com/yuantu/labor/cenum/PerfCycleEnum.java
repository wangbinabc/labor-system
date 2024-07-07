package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : mt
 * @create 2023/10/17 11:18
 */
@Getter
@AllArgsConstructor
public enum PerfCycleEnum {
    ZERO("0","年度"),

    ONE("1", "第一季度"),

    TWO("2", "第二季度"),

    THREE("3", "第三季度"),

    FOUR("4", "第四季度"),



    ;

    private String key;

    private String value;
}
