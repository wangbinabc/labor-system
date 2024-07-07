package com.yuantu.labor.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DeptFlowInfo {


    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 员工id
     */
    private Long empId;

    /**
     * 创建时间
     */
    private Date createTime;
}
