package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@HeadRowHeight(15)
@ColumnWidth(20)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
public class PerformanceTemplateVO
{
    private static final long serialVersionUID = 1L;




    /** 员工名称 */
    @ExcelProperty("员工姓名")
    private String perfEmpName;



    /** 绩效年份 */

    @ExcelProperty( "年份")
    private String perfYear;

    /** 绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度 */

    @ExcelProperty("绩效周期")
    private String perfCycle;

    /** 绩效值，A优秀，B良好，C及格，D差 */

    @ExcelProperty( "绩效等级")
    private String perfLevelValue;

    /** 备注 */
    @ExcelProperty( "备注")
    private String perfRemark;


}
