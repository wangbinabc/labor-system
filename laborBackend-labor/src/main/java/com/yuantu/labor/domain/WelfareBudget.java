package com.yuantu.labor.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 福利保障信息执行对象 welfare_budget
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
@Data
public class WelfareBudget extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer welbudId;

    /** 年月 */
    @Excel(name = "年月")
    private String welbudYearMonth;

    /** 五险二金预算费用 */
    @Excel(name = "五险二金预算费用")
    private BigDecimal welbudInsureBudget;

    /** 五险二金实际消耗费用 */
    @Excel(name = "五险二金实际消耗费用")
    private BigDecimal welbudInsureActual;

    /** 劳动保护预算费用 */
    @Excel(name = "劳动保护预算费用")
    private BigDecimal welbudLaborprotectBudget;

    /** 劳动保护实际消耗费用 */
    @Excel(name = "劳动保护实际消耗费用")
    private BigDecimal welbudLaborprotectActual;

    /** 食堂经费预算费用 */
    @Excel(name = "食堂经费预算费用")
    private BigDecimal welbudCanteenBudget;

    /** 食堂经费实际消耗费用 */
    @Excel(name = "食堂经费实际消耗费用")
    private BigDecimal welbudCanteenActual;

    /** 体检费预算费用 */
    @Excel(name = "体检费预算费用")
    private BigDecimal welbudPhysicalexamBudget;

    /** 体检费实际消耗费用 */
    @Excel(name = "体检费实际消耗费用")
    private BigDecimal welbudPhysicalexamActual;


}
