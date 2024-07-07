package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrainNatureEnum {
    ONE("1", "技术"),

    TWO("2", "管理"),

    THREE("3", "技能"),

    ;

    private String key;

    private String value;

}
