package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpPoliticalEnum {

    ONE("1", "中共党员"),

    TWO("2", "中共预备党员"),

    THREE("3", "共青团员"),

    FOUR("4", "民革党员"),

    FIVE("5", "民盟盟员"),

    SIX("6", "无党派人士"),

    SEVEN("7", "群众"),

    ;

    private String key;

    private String value;
}
