package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工福利对象 emp_welfare
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
@Data
public class EmpWelfareAddVO implements IExcelDataModel, IExcelModel
{
    private static final long serialVersionUID = 1L;

    /** 员工身份证 */
    @Excel(name = "员工身份证")
    private String welfareEmpIdcard;

    /** 员工名称 */
    @Excel(name = "员工名称")
    private String welfareEmpName;

    /** 五险两金费 */
    @Excel(name = "五险两金费")
    private BigDecimal welfareInsureFee;

    /** 劳动保护费 */
    @Excel(name = "劳动保护费")
    private BigDecimal welfareProtectFee;

    /** 食堂经费 */
    @Excel(name = "食堂经费")
    private BigDecimal welfareFoodFee;

    /** 体检费 */
    @Excel(name = "体检费")
    private BigDecimal welfareCheckupFee;

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
