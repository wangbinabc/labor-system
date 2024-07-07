package com.yuantu.labor.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;
@Data
@HeadRowHeight(15)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
@ColumnWidth(20)
public class ResumeProjectExcelVO {
    /**
     * 员工姓名
     */

    @ExcelProperty("姓名")
    private String resuEmpName;


    /**
     * 开始日期
     */
    @ExcelProperty("开始时间")
    @DateTimeFormat("yyyy-MM-dd")
    private Date resuBeginDate;

    /**
     * 结束日期
     */
    @ExcelProperty("截止时间")
    @DateTimeFormat("yyyy-MM-dd")
    private Date resuEndDate;

    /**
     * 项目名称
     */
    @ExcelProperty("项目名称")
    private String resuProjectName;

    /**
     * 项目规模
     */
    @ExcelProperty("项目规模")
    private String resuProjectScale;

    /**
     * 项目类型
     */
    @ExcelProperty("项目类型")
    private String resuProjectType;

    /**
     * 项目职位
     */
    @ExcelProperty("项目职位")
    private String resuProjectJob;

    /**
     * 证明人
     */
    @ExcelProperty("证明人")
    private String resuProjectCertifier;

}
