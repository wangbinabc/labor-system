package com.yuantu.labor.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 员工酬薪从（详细科目）对象 emp_salary_item
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class EmpSalaryItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer itemId;

    /** 员工工资主表id */
    @Excel(name = "员工工资主表id")
    private Integer itemSalaryId;

    /** 酬薪配置表id */
    @Excel(name = "酬薪配置表id")
    private Integer itemConfId;

    /** 酬薪分类值 */
    @Excel(name = "酬薪分类值")
    private String itemConfTypeValue;

    /** 酬薪分类标签 */
    @Excel(name = "酬薪分类标签")
    private String itemConfTypeLabel;

    /** 薪酬类目 */
    @Excel(name = "薪酬类目")
    private String itemConfItem;

    /** 酬薪类目金额 */
    @Excel(name = "酬薪类目金额")
    private BigDecimal itemValue;

    /** 对应工资比例 */
    private BigDecimal confRatio;
}
