package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmpSalaryQueryVO extends BaseEntity {
    /**
     * 月份区间
     */

    private String monthInterval;
    /**
     * 月份开始
     */
    @ApiModelProperty("开始年月")
    private String beginMonth;
    /**
     * 月份结束
     */
    @ApiModelProperty("结束年月")
    private String endMonth;
    /**
     * 员工id
     */
    @ApiModelProperty("员工id")
    private Integer empId;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String empName;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证")
    private String empIdcard;
    /**
     * 查询的年
     */
    @ApiModelProperty("查询的年")
    private String year;
    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private String empDeptId;

    /**
     * 部门名称
     *
     */
    @ApiModelProperty("部门名称")
    private String empDeptName;

    /** 应发工资 */
    @ApiModelProperty("最小应发工资")
    private BigDecimal minSalaryPayableNum;

    @ApiModelProperty("最大应发工资")
    private BigDecimal maxSalaryPayableNum;

    /** 公积金个缴 */
    @ApiModelProperty("最小公积金个缴")
    private BigDecimal minSalaryPersonFund;
    @ApiModelProperty("最大公积金个缴")
    private BigDecimal maxSalaryPersonFund;

    /** 实发工资 */
    @ApiModelProperty("最小实发工资")
    private BigDecimal minSalaryNetPay;
    @ApiModelProperty("最大实发工资")
    private BigDecimal maxSalaryNetPay;

    /**
     * 岗位
     */
    @ApiModelProperty("岗位")
    private String empPosition;

    /**
     * 员工分类
     */
    @ApiModelProperty("员工分类")
    private String empCategory;

    /**
     * 岗级
     */
    @ApiModelProperty("岗级")
    private String empPositionLevel;

    /**
     * 人力资源公司
     */
    @ApiModelProperty("人力资源公司")
    private String empHrCompany;

    /**
     * 用工单位
     */
    @ApiModelProperty("用工单位")
    private String empEmployingUnits;
    /**
     * 是否薪资验证
     */
    private Integer isSalaryCheck;

    private Integer isRelated;
    /**
     * 1：人员
     * 2：部门
     * 3：公司
     */
    @ApiModelProperty("统计类型，1:人员，2:部门，3:公司")
    private Integer queryType;

}
