package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IsPostChangeEnum {
    YES("0", "是"),

    NOT("1", "否"),


    ;

    private String key;

    private String value;
}
