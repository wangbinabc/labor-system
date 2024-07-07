package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@HeadRowHeight(15)
@HeadFontStyle(color = 80,fontName = "黑体", fontHeightInPoints=10)
@ColumnWidth(20)
public class EmpTrainExcelVO {
    /** 姓名 */
    @ExcelProperty("人员姓名")
    private String trainEmpName;

    /** 培训项目名称 */
    @ExcelProperty("培训项目")
    private String trainProjectName;

    /** 学时 */
    @ExcelProperty("学时")
    private Integer trainPeriod;

    /** 培训费用(元) */
    @ExcelProperty("培训费用(元)")
    private BigDecimal trainFee;

    /** 开始时间 */
    @ExcelProperty(value = "开始时间")
    @DateTimeFormat("yyyy-MM-dd")
    private Date trainBeginTime;

    /** 结束时间 */
    @ExcelProperty(value = "结束时间")
    @DateTimeFormat("yyyy-MM-dd")
    private Date trainEndTime;

    /** 培训地点/方式  */
    @ExcelProperty("培训地点/方式")
    private String trainAddrMethod;

    /** 考核方式（考评、考试） */
    @ExcelProperty("考核方式")
    private String trainExamMethod;

    /** 所属公司 */
    @ExcelProperty("所属公司")
    private String trainWorkUnitsName;

    /** 劳务公司 */
    @ExcelProperty("劳务公司")
    private String trainHrCompanyName;

}
