package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import cn.afterturn.easypoi.excel.annotation.Excel;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class EmpTrainImportVO implements IExcelDataModel, IExcelModel {


    /** 培训项目名称 */
    @NotBlank(message = "不能为空")
    @Excel(name = "培训项目")
    private String trainProjectName;

    /** 学时 */
    @NotNull(message = "不能为空")
    @Excel(name = "学时")
    private Integer trainPeriod;

    /** 培训费用(元) */
    @NotNull(message = "不能为空")
    @Excel(name = "培训费用(元)")
    private BigDecimal trainFee;

    /** 培训地点/方式  */
    @NotBlank(message = "不能为空")
    @Excel(name = "培训地点/方式")
    private String trainAddrMethod;

    /** 开始时间 */
    @NotNull(message = "不能为空")
    @Excel(name = "开始时间")
    private Date trainBeginTime;

    /** 结束时间 */
    @NotNull(message = "不能为空")
    @Excel(name = "结束时间")
    private Date trainEndTime;

    /** 考核方式（考评、考试） */
    @NotBlank(message = "不能为空")
    @Excel(name = "考核方式")
    private String trainExamMethod;


    /** 姓名 */
    @NotBlank(message = "不能为空")
    @Excel(name = "人员姓名")
    private String trainEmpName;


    /** 所属公司 */
    @NotBlank(message = "不能为空")
    @Excel(name = "所属公司")
    private String trainWorkUnitsName;


    /** 劳务公司 */
    @NotBlank(message = "不能为空")
    @Excel(name = "劳务公司")
    private String trainHrCompanyName;

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
