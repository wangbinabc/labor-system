package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectClassifyEnum {
    ONE("1", "一类"),

    TWO("2", "二类"),

    THREE("3", "三类"),

    ;

    private String key;

    private String value;
}
