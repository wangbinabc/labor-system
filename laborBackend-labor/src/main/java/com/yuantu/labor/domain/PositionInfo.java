package com.yuantu.labor.domain;

import lombok.Data;

@Data
public class PositionInfo {
    /**
     *
     */
    private Long id;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 岗位类别
     */
    private String positionCategory;

    /**
     * 岗级
     */
    private String positionLevel;

    /**
     * 岗位排序 （监理员类 1 监理是类 2 总监类 3）
     */
    private Integer positionSort;

    /**
     * 薪级下限
     */
    private String salaryLow;

    /**
     * 薪级上限
     */
    private String salaryHigh;

    /**
     * 备注
     */
    private String remark;
}
