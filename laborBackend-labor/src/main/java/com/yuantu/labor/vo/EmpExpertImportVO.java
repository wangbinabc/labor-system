package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;

/**
 * 【请填写功能名称】对象 emp_expert
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Data
public class EmpExpertImportVO implements IExcelDataModel, IExcelModel {


    /**
     * 员工名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "人员姓名")
    private String expertEmpName;

    /**
     * 员工身份证
     */
    //@Pattern(regexp = "\\d{17}[0-9Xx]", message = "格式不正确")
    //@NotBlank(message = "不能为空")
   // @Excel(name = "身份证号")
    //private String expertEmpIdcard;


    /**
     * 专家级别
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "专家级别")
    private String expertLevel;

    /**
     * 专家称号
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "专家人才称号")
    private String expertTitle;

    /**
     * 授予时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "授予时间")
    @NotNull(message = "不能为空")
    private Date expertGrantTime;

    /**
     * 解聘时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解聘时间", width = 30)
    // @NotNull(message = "不能为空")
    private Date expertDismissTime;

    /**
     * 聘用期
     */
//    @Excel(name = "聘用期")
//    @NotNull(message = "不能为空")
//    private String expertPeriod;


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
