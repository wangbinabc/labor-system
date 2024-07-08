package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@HeadRowHeight(15)
@ColumnWidth(20)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
public class CredentialsTemplateVO
{
    private static final long serialVersionUID = 1L;


    /** 证书名称 */
    @ExcelProperty(value =  "证书名称")
    private String credName;


    /** 员工姓名 */
    @ExcelProperty(value =  "员工姓名")
    private String credEmpName;

    /** 身份证 */
  //  @NotBlank(message = "不能为空")
//    @Excel(name = "身份证")
//    private String credEmpIdcard;


    /** 证书类型 */
    @ExcelProperty(value =  "证书类型")
    private String credType;

    /** 证书注册时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value =  "证书注册时间")
    private Date credRegistTime;

    /** 资格证获取时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value =  "资格证获取时间")
    private Date credObtainTime;

    /** 有效期 */
    @ExcelProperty(value =  "有效期")
    private String credPeriod;

    /** 注册证到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value =  "注册证到期时间")
    private Date credExpTime;

}
