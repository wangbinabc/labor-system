package com.yuantu.labor.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class EmpProfitCheckVO {


    private String name;

    private String yearMonth;

    private Integer rowNum;

    public EmpProfitCheckVO(String name, Integer rowNum, String yearMonth) {
        this.name = name;
        this.rowNum = rowNum;
        this.yearMonth = yearMonth;
    }
}
