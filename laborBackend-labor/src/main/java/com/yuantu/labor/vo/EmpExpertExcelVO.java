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
public class EmpExpertExcelVO {
    @ExcelProperty("人员姓名")
    private String expertEmpName;

    /**
     * 专家称号
     */
    @ExcelProperty("专家人才称号")
    private String expertTitle;

    /**
     * 专家级别
     */
    @ExcelProperty("专家级别")
    private String expertLevel;

    /**
     * 授予时间
     */
    @ExcelProperty("授予时间")
    @DateTimeFormat("yyyy-MM-dd")
    private Date expertGrantTime;

    /**
     * 解聘时间
     */
    @ExcelProperty("解聘时间")
    @DateTimeFormat("yyyy-MM-dd")
    private Date expertDismissTime;

}
