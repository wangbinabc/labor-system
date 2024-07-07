package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IsFinishEnum {
    MALE("1", "是"),

    FEMALE("0", "否"),


    ;

    private String key;

    private String value;
}
