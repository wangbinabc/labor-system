package com.yuantu.labor.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class EmpProfitImportVO implements IExcelDataModel, IExcelModel {


    @Excel(name = "员工姓名")
    @NotBlank(message = "不能为空")
    private String empName;

    @Excel(name = "所属年月(yyyy-MM)")
    private String yearMonth;

    @Excel(name = "员工个人利润值")
    @NotNull(message = "不能为空")
    private Double profitValue;


    /**
     * 行号
     */
    private int rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
