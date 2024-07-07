package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum ResumeTypeEnum {

    ONE("1", "社会"),

    TWO("2", "项目"),

    THREE("3", "本单位"),

    ;

    private String key;

    private String value;
}
