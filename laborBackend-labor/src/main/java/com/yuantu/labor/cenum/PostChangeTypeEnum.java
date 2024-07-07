package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum PostChangeTypeEnum {

    ONE("1", "晋升"),

    TWO("2", "平调"),

    THREE("3", "降级"),

    ;

    private String key;

    private String value;
}
