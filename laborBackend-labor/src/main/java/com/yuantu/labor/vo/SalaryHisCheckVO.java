package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryHisCheckVO {
    private String hisEmpName;
    private String hisYearMonth;

    private Integer rowNum;
}
