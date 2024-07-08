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
import javax.validation.constraints.NotNull;


@Data
@HeadRowHeight(30)
@ColumnWidth(20)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
public class ProvicePerformanceTemplateVO
{
    private static final long serialVersionUID = 1L;




    /** 所在部门 */
    @ExcelProperty("省公司部门")
    private String  ppProvinceDeptName;



    /** 所在部门 */
    @ExcelProperty( "本单位(公司)-公司牵头部门")
    private String  ppDeptName;

    /** 绩效年份 */
    @ExcelProperty("年份")
    private String ppYear;

    /** 绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度 */
    @ExcelProperty( "绩效周期")
    private String ppCycle;


    @ExcelProperty("目标排名")
    private Long ppTargetRanking;

    /** 季度排名 */

    @ExcelProperty("季度排名")
    private Long ppQuarterlyRanking;

    /** 季度计划得分 */

    @ExcelProperty( "季度计划得分")
    private Long ppQuarterlyPlanScore;

    /** 季度实际得分 */

    @ExcelProperty( "季度实际得分")
    private Long ppQuarterlyActualScore;




}
