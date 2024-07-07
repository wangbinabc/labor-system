package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class EmpSalaryYearCountVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    //@Excel(name = "员工id")
    private Long empId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String empName;

    /**
     * 身份证
     */
    @Excel(name = "身份证")
    private String empIdcard;

    /**
     * 部门ID
     */
    // @Excel(name = "部门ID")
    private Long empDeptId;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String empDeptName;

    /**
     * 年份
     */
    @Excel(name = "年份")
    private Integer year;

    /**
     * 1月份
     */
    @Excel(name = "1月份")
    private BigDecimal month01;

    /**
     * 2月份
     */
    @Excel(name = "2月份")
    private BigDecimal month02;

    /**
     * 3月份
     */
    @Excel(name = "3月份")
    private BigDecimal month03;

    /**
     * 4月份
     */
    @Excel(name = "4月份")
    private BigDecimal month04;

    /**
     * 5月份
     */
    @Excel(name = "5月份")
    private BigDecimal month05;

    /**
     * 6月份
     */
    @Excel(name = "6月份")
    private BigDecimal month06;

    /**
     * 7月份
     */
    @Excel(name = "7月份")
    private BigDecimal month07;

    /**
     * 8月份
     */
    @Excel(name = "8月份")
    private BigDecimal month08;

    /**
     * 9月份
     */
    @Excel(name = "9月份")
    private BigDecimal month09;

    /**
     * 10月份
     */
    @Excel(name = "10月份")
    private BigDecimal month10;

    /**
     * 11月份
     */
    @Excel(name = "11月份")
    private BigDecimal month11;

    /**
     * 12月份
     */
    @Excel(name = "12月份")
    private BigDecimal month12;

    /**
     * 岗位
     */
    @Excel(name = "岗位")
    private String empPosition;

    /**
     * 岗级
     */
    @Excel(name = "岗级")
    private String empPositionLevel;
    /**
     * 人力资源公司
     */
    @Excel(name = "劳务公司")
    private String empHrCompany;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String empRemark;


}

