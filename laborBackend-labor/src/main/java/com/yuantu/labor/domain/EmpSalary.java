package com.yuantu.labor.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 员工酬薪主对象 emp_salay
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class EmpSalary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer salaryId;


    /** 姓名 */
    @Excel(name = "姓名")
    private String salaryEmpName;

    /** 身份证 */
    @Excel(name = "身份证")
    private String salaryEmpIdcard;


    /** 年月 */
    @Excel(name = "工资年月")
    private String salaryYearMonth;

    /** 应发工资 */
    @Excel(name = "应发工资")
    private String salaryPayableNum;

    /** 公积金个缴 */
    @Excel(name = "公积金个缴")
    private String salaryPersonFund;

    /** 实发工资 */
    @Excel(name = "实发工资")
    private String salaryNetPay;

    /** 员工ID */
  //  @Excel(name = "员工ID")
    private Long salaryEmpId;


    /**
     * 用于接收页面上提交的工资各个科目
     */
    private EmpSalaryItem[]  salaryItems;

    private List<EmpSalaryItem> salaryItemList;


}
