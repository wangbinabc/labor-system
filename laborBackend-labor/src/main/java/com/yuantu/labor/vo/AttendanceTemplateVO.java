package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.utils.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
@HeadRowHeight(15)
@ColumnWidth(20)
@HeadFontStyle(color = 80, fontName = "黑体", fontHeightInPoints = 10)
public class AttendanceTemplateVO {
    private static final long serialVersionUID = 1L;


    public static final String bigTitle = "填写须知： \n" +
            "1.请勿修改当前模板结构\n" +
            "2.红色字段必填，黑色字段按照实际情况选填\n" +
            "3.日期格式统一按照“2023/9/1”格式填写\n";

//    /**
//     * 每个模板的首行高度， 换行数目+2 乘以15
//     */
//    public static int getHeadHeight() {
//        return (StringUtils.getRowCounts(bigTitle) + 2) * 15;
//    }

//    @ExcelProperty(value = "序号")
//    private String orderNum;


    /**
     * 员工姓名
     */
    @ExcelProperty(value = "员工姓名")
    // @HeadFontStyle(color = 10)
    private String attendEmpName;

    /** 身份证 */
    //   @NotBlank(message = "不能为空")
    //   @Excel(name = "身份证")
    //   private String attendEmpIdcard;


    /**
     * 考勤日期
     */
    @ExcelProperty(value = "考勤日期")
    // @HeadFontStyle(color = 10)
    @ContentStyle(dataFormat = 49)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date attendRecordDate;


    /**
     * 是否工作日：0：是，1：否
     */
    @ExcelProperty(value = "工作日判定")
    // @HeadFontStyle(color = 10)
    private String attendIsweekday;

    @ExcelProperty(value = "考勤类型")
    //  @HeadFontStyle(color = 10)
    private String attendStatus;

    @ExcelProperty(value = "考勤明细")
    //  @HeadFontStyle(color = 8)
    private String attendStatusDetail;

//    @Excel(name = "修改时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date updateTime;


}
