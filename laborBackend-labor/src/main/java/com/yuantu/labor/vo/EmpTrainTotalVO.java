package com.yuantu.labor.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmpTrainTotalVO {
    private Integer totalCount;
    private BigDecimal totalFee;
    private Integer totalProject;
    private Integer totalPeriod;
}
