package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpChangeTypeEnum {
    //"变动类型 无变动0 新增1 减少2 调动3

    NOCHANGE("0", "nochanges"),

    ADD("1", "add"),

    REDUCE("2", "reduce"),

    MOBILIZE("3", "mobilize"),

    ;

    private String key;

    private String value;
}
