package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum ResumeProjectTypeEnum {

    ONE("1", "A类"),

    TWO("2", "B类"),

    THREE("3", "C类"),

    ;

    private String key;

    private String value;
}
