package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpPostLevelEnum {

    LEVEL_A("1", "一级"),

    LEVEL_B("2", "二级"),

    LEVEL_C("3", "三级"),

    LEVEL_D("4", "财务辅助"),

    LEVEL_E("5", "财务辅助组长"),

    LEVEL_F("6", "专业管理辅助组长"),


    ;

    private String key;

    private String value;
}
