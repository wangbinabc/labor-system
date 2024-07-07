package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 履历对象 emp_resume
 *
 * @author ruoyi
 * @date 2023-09-12
 */
@Data
public class ResumeProjectImportVO implements IExcelDataModel, IExcelModel {

    /**
     * 员工姓名
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "姓名")
    private String resuEmpName;

    /**
     * 身份证
     */
    //@Pattern(regexp = "\\d{17}[0-9Xx]", message = "格式不正确")
    //@NotBlank(message = "不能为空")
    //@Excel(name = "身份证号")
    //private String resuEmpIdcard;
    ;

    /**
     * 开始日期
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间")
    private Date resuBeginDate;

    /**
     * 结束日期
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "截止时间")
    private Date resuEndDate;


    /**
     * 项目名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目名称")
    private String resuProjectName;

    /**
     * 项目规模
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目规模")
    private String resuProjectScale;


    /**
     * 项目类型
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目类型")
    private String resuProjectType;

    /**
     * 项目职位
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目职位")
    private String resuProjectJob;


    /**
     * 证明人
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "证明人")
    private String resuProjectCertifier;


    /**
     * 履历内容
     */
    @Excel(name = "工作内容简述")
    private String resuContext;


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
