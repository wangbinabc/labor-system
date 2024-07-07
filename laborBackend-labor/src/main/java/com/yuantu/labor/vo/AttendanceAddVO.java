package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 资格证书对象 emp_credentials
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class AttendanceAddVO implements IExcelDataModel, IExcelModel {
    private static final long serialVersionUID = 1L;

    private static final String regex = "^(?:19|20)\\d\\d\\/(?:0?[1-9]|1[0-2])\\/(?:0?[1-9]|[12][0-9]|3[01])$";

//    @NotNull(message = "不能为空")
//    @Excel(name = "序号")
//    private Long orderNum;

    /**
     * 员工姓名
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "员工姓名")
    private String attendEmpName;


    /**
     * 考勤日期
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "考勤日期")
   // @Pattern(regexp = regex, message = "日期格式不正确")
    private String attendRecordDate;


    /**
     * 是否工作日：0：是，1：否
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "工作日判定")
    private String attendIsweekday;

    @NotBlank(message = "不能为空")
    @Excel(name = "考勤类型")
    private String attendStatus;

    @Excel(name = "考勤明细")
    private String attendStatusDetail;



    /**
     * 行号
     */
    private int rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
