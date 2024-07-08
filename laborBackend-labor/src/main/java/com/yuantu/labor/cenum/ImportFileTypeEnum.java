package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum ImportFileTypeEnum {

    ONE("1", "人员信息"),

    TWO("2", "借工基本信息"),

    THREE("3", "职业资格管理"),

    FOUR("4", "社会履历"),

    FIVE("5", "项目履历"),

    SIX("6", "部门绩效"),

    SEVEN("7", "培训项目"),

    EIGHT("8", "培训记录"),

    NINE("9", "培训成果"),

    TEN("10", "专家信息"),

    ELEVEN("11", "考勤信息"),

    TWELVE("12", "员工绩效信息"),

    THIRTEEN("13", "省公司专业考核"),

    FOURTEEN("14", "员工福利信息"),

    FIFTEEN("15", "员工健康档案信息"),

    SIXTEEN("16", "薪酬变动"),

    SEVENTEEN("17", "员工薪资"),

    EIGHTEEN("18", "员工利润"),

    NINETEEN("19", "人员历史导入"),


    ;

    private String key;

    private String value;
}
