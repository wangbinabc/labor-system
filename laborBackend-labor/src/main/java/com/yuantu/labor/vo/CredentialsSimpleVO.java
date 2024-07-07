package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;


import com.yuantu.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;


/**
 * 资格证书对象 emp_credentials
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class CredentialsSimpleVO  implements IExcelDataModel, IExcelModel
{
    private static final long serialVersionUID = 1L;


    /** 证书名称 */
    @NotBlank(message = "不能为空")
    @Excel(name = "证书名称")
    private String credName;


    /** 员工姓名 */
    @NotBlank(message = "不能为空")
    @Excel(name = "员工姓名")
    private String credEmpName;

    /** 身份证 */
   // @NotBlank(message = "不能为空")
    @Excel(name = "身份证")
    private String credEmpIdcard;


    /** 证书类型 */
    @NotBlank(message = "不能为空")
    @Excel(name = "证书类型")
    private String credType;

    /** 证书注册时间 */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证书注册时间")
    private Date credRegistTime;

    /** 资格证获取时间 */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "资格证获取时间")
    private Date credObtainTime;

    /** 有效期 */
    @NotBlank(message = "不能为空")
    @Excel(name = "有效期")
    private String credPeriod;

    /** 注册证到期时间 */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "注册证到期时间")
    private Date credExpTime;


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
