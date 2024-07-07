package com.yuantu.labor.vo;


import com.yuantu.common.annotation.Excel;
import lombok.Data;

@Data
public class EmpProfitTemplateVO {


    @Excel(name = "员工姓名")
    private String empName;

    @Excel(name = "所属年月(yyyy-MM)", cellType = Excel.ColumnType.STRING)
    private String yearMonth;

    @Excel(name = "员工个人利润值", cellType = Excel.ColumnType.NUMERIC)
    private Double profitValue;


}
