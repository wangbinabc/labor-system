package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ChartDataVO {
    private String name;
    private Integer numval;
    private BigDecimal amountval;

    public ChartDataVO() {
    }
}
