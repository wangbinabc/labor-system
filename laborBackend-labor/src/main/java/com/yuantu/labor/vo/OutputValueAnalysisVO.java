package com.yuantu.labor.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OutputValueAnalysisVO {
    /**
     * 总人数
     */
    private Integer totalNum = 0;
    /**
     * 总产值
     */
    private BigDecimal totalOutputValue = new BigDecimal(0);
    /**
     * 总成本
     */
    private BigDecimal totalCost = new BigDecimal(0);
    /**
     * 总利润
     */
    private BigDecimal totalProfit = new BigDecimal(0);
    /**
     * 成本利润率
     */
    private BigDecimal costProfitRate = new BigDecimal(0);
    /**
     * 平均产值
     */
    private BigDecimal avgOutputValue = new BigDecimal(0);
    /**
     * 平均成本
     */
    private BigDecimal avgCost = new BigDecimal(0);
    /**
     * 平均利润
     */
    private BigDecimal avgProfit = new BigDecimal(0);
}
