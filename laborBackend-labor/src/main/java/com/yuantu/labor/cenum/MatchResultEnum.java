package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum MatchResultEnum {

    NO(0, "未匹配"),

    YES(1, "已匹配"),


    ;

    private Integer key;

    private String value;
}
