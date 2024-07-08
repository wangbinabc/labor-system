package com.yuantu.labor.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@HeadRowHeight(15)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
@ColumnWidth(20)
public class TrainProjectExcelVO {
    /** 培训项目名称 */

    @ExcelProperty("培训项目名称")
    private String projectName;

    /** 主要培训内容/课程 */
    @ExcelProperty("主要培训内容/课程")
    private String projectContent;

    /** 年度 */
    @ExcelProperty("年度")
    private String projectYear;

    /** 责任部门名称 */
    @ExcelProperty("单位名称-部门名称")
    private String projectDeptName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @ExcelProperty("培训性质")
    private String projectNature;

    /** 培训方式(1=内培, 2=外培) */
    @ExcelProperty("培训方式")
    private String projectMethod;

    /** 项目分类(1=一类，2=二类，3=三类) */
    @ExcelProperty("项目分类")
    private String projectClassify;

    /** 是否完成 */
    @ExcelProperty("是否完成")
    private String projectIsfinish;

}
