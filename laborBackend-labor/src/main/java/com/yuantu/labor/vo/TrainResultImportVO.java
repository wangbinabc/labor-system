package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TrainResultImportVO implements IExcelDataModel, IExcelModel {
    /** 培训成果名称 */
    @NotBlank(message = "不能为空")
    @Excel(name = "培训成果名称")
    private String resultName;


    /** 所属项目名称 */
    @NotBlank(message = "不能为空")
    @Excel(name = "所属项目名称")
    private String resultProjectName;

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
