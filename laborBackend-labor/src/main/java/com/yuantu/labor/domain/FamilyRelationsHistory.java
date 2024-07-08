package com.yuantu.labor.domain;

import lombok.Data;

@Data
public class FamilyRelationsHistory {


    /**
     * 主键id
     */
    private Long historyId;


    /**
     * 保存月份
     */
    private String historyYearMonth;

    /**
     * 家庭关系id
     */
    private Long famId;

    /**
     * 称谓
     */
    private String famAppellation;

    /**
     * 姓名
     */
    private String famName;

    /**
     * 年龄
     */
    private Long famAge;

    /**
     * 工作单位和岗位
     */
    private String famUnitPost;

    /**
     * 员工ID
     */
    private Long famEmpId;

    /**
     * 员工名称
     */
    private String famEmpName;


    private Integer famEmpSort;


    private String famContactPhone;


}
