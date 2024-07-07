package com.yuantu.labor.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 预算主对象 budget
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@Data
public class Budget extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    private Integer budgetId;

    /**
     * 申请公司id
     */
    @Excel(name = "申请公司id")
    private Long budgetCompanyId;

    /**
     * 申请公司名称
     */
    @Excel(name = "申请公司名称")
    private String budgetCompanyName;

    /**
     * 预算类别key
     */
    @Excel(name = "预算类别key")
    private String budgetTypeId;

    /**
     * 预算类别名称
     */
    @Excel(name = "预算类别名称")
    private String budgetTypeName;

    /**
     * 申请年
     */
    @Excel(name = "申请年")
    private Long budgetYear;

    /**
     * 预算金额
     */
    @Excel(name = "预算金额")
    private BigDecimal budgetAmount;

    /**
     * 预算实际使用金额
     */
    @Excel(name = "预算实际使用金额")
    private BigDecimal budgetActualuse;

    private Boolean disabled;

    /**
     * 执行进度
     */
    @Excel(name = "执行进度")
    private String budgetExecProgress;

    private BudgetDetail[] budgetDetails;

    private List<BudgetDetail> budgetDetailList;

}
