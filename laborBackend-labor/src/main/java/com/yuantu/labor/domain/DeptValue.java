package com.yuantu.labor.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DeptValue {

    /**
     *
     */
    private Long id;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 年份
     */
    private String valueYear;

    /**
     * 总产值(万元)
     */
    private String sumValue;


    private String laborNum;

    private String avgValue;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private Boolean disabled;


}
