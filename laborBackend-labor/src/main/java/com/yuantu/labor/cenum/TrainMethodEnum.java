package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrainMethodEnum {
    ONE("1", "内培"),

    TWO("2", "外培"),



    ;

    private String key;

    private String value;

}
