package com.yuantu.labor.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmpSalaryItemCheckVO {
    private BigDecimal wages;
    private BigDecimal ratio;
    private String itemConfItem;
    private Boolean isError;
    private BigDecimal total;
    private String confLabel;



    public EmpSalaryItemCheckVO(String itemConfItem,BigDecimal wages, BigDecimal ratio,BigDecimal total,Boolean isError,String confLabel) {

        this.wages = wages;
        this.ratio = ratio;
        this.itemConfItem = itemConfItem;
        this.isError = isError;
        this.total = total;
        this.confLabel = confLabel;
    }
}
