package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum EmpDocTypeEnum {

    ONE("1", "个人照片"),

    TWO("2", "身份证照片"),

    THREE("3", "最高职称证书照片"),

    FOUR("4", "学历照片"),

    FIVE("5", "培训材料"),

    SIX("6", "资格证书"),

    SEVEN("7", "培训成果"),

    EIGHT("8", "题库"),

    NINE("11", "员工健康档案"),

    THIRTY_FOUR("34", "员工档案"),

    THIRTY_FIVE("35", "员工其他"),

    ;

    private String key;

    private String value;
}
