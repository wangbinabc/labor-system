package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeInfoVO {
    //部门ID
    private Integer empDeptId;
    /**学历**/
    private Integer empEducation;
    /**性别**/
    private String empGender;
    /**年龄区间**/
    private String empAgeInterval;
    /**职称**/
    private String empTitle;

    /**最小年龄**/
    private Integer minAge;
    /**最大年龄**/
    private Integer maxAge;

    private List<String> empStatusList;



}
