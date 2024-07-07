package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : mt
 * @create 2023/10/17 11:18
 */
@Getter
@AllArgsConstructor
public enum CredentialsTypeEnum {

    ONE("1", "A类"),

    TWO("2", "B类"),

    THREE("3", "C类"),

   // FOUR("4", "D类"),



    ;

    private String key;

    private String value;
}
