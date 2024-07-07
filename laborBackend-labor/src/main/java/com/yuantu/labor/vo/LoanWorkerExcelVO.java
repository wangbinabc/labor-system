package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@HeadRowHeight(15)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
@ColumnWidth(20)
public class LoanWorkerExcelVO {
    @ExcelProperty("序号")
    private Integer rowId;
    /**
     * 单位
     */
    @ExcelProperty("单位")
    private String loanApplyUnitName;
    /**
     * 员工姓名
     */
    @ExcelProperty("姓名")
    private String loanEmpName;

    /**
     * 身份证
     */
    @ExcelProperty("身份证号")
    private String loanEmpIdcard;

    /**
     * 用工部门
     */
    @ExcelProperty("用工部门")
    private String loanApplyUnitDeptName;

    /**
     * 科室
     */
    @ExcelProperty("处/室")
    private String loanApplyOffice;

    /**
     * 性别
     */
    @ExcelProperty("性别")
    private String loanEmpGender;

    /**
     * 年龄
     */
    @ExcelProperty("年龄")
    private Integer loanEmpAge;


    /**
     * 学历
     */
    @ExcelProperty("学历")
    private String loanEmpEducation;

    /**
     * 专业
     */
    @ExcelProperty("专业")
    private String loanEmpSpeciality;

    /**
     * 职称
     */
    @ExcelProperty("职称")
    private String loanEmpTitle;

    /**
     * 资格证书
     */
    @ExcelProperty("执业资格证书")
    private String loanEmpCredentials;

    /**
     * 新的岗位
     */
    @ExcelProperty("拟从事岗位")
    private String loanNewPost;

    /**
     * 拟安排区域
     */
    @ExcelProperty("拟派驻现场(片区)")
    private String loanArrageArea;

    /**
     * 借调入场时间
     */
    @ExcelProperty("拟入场时间（年月日）")
    @DateTimeFormat("yyyy-MM-dd")
    private Date loanBeginTime;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String loanRemark;
}
