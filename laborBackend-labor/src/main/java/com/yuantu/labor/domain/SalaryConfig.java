package com.yuantu.labor.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 酬薪构成配置对象 salary_config
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class SalaryConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer confId;

    /** 分类值 */
    @Excel(name = "分类值")
    private String confTypeValue;

    /** 分类标签 */
    @Excel(name = "分类标签")
    private String confTypeLabel;

    /** 薪酬构成 */
    @Excel(name = "薪酬构成")
    private String confItem;

    /** 对应工资比例 */
    @Excel(name = "对应工资比例")
    private BigDecimal confRatio;
}
