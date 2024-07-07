package com.yuantu.labor.vo;

import lombok.Data;

/**
 * 用于装载查询出来的员工名称和身份证
 */
@Data
public class EmpNameCardVO {
    private Long empId;
    private String empName;
    private String empIdcard;
}
