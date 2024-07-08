package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpStatusEnum {

    ON_POSITION("1", "在职"),

    NEW_POSITION("2", "新入职"),

    RESIGN("3", "辞职"),

    FIRE("4", "辞退"),

    ALMOST("5", "即将到龄"),

    EXPIRE("6", "到龄"),

    RE_EMPLOY("7", "返聘"),

    ;

    private String key;

    private String value;
}
