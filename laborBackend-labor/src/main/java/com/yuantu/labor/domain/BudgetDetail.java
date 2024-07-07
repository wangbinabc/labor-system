package com.yuantu.labor.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 预算从对象 budget_detail
 * 
 * @author ruoyi
 * @date 2023-10-07
 */
@Data
public class BudgetDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer detailId;

    /** 预算id */
    @Excel(name = "预算id")
    private Integer detailBudgetId;

    /** 月份 */
    @Excel(name = "月份")
    private Integer detailMonth;

    /** 预算金额 */
    @Excel(name = "预算金额")
    private BigDecimal detailBudgetAmount;

    /** 实际使用 */
    @Excel(name = "实际使用")
    private BigDecimal detailActualAmount;

}
