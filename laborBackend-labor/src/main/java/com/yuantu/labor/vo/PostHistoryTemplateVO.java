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
import java.util.Objects;

@Data
@HeadRowHeight(15)
@ColumnWidth(20)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
public class PostHistoryTemplateVO{


    /**
     * 员工名称
     */

    @ExcelProperty("员工名称")
    private String phEmpName;



    /**
     * 原来岗位
     */
    @ExcelProperty( "原来岗位")
    private String phOriginPostName;

    /**
     * 原来岗级
     */
    @ExcelProperty("原来岗级")
    private String phOriginPostLevel;

    /**
     * 新的岗位
     */

    @ExcelProperty("新的岗位")
    private String phDestinPostName;

    /**
     * 新的岗级
     */

    @ExcelProperty("新的岗级")
    private String phDestinPostLevel;


    /**
     * 调岗日期
     */

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty( "调岗日期")
    private Date phAdjustDate;

    /**
     * 调岗类型
     */

    @ExcelProperty("调岗类型")
    private String phAdjustType;

}
