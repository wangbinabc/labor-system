package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SalaryHisImportVO implements IExcelDataModel, IExcelModel {
    /** 人员姓名 */
    @NotBlank(message = "不能为空")
    @Excel(name = "员工姓名")
    private String hisEmpName;

    /** 年月 */
    @NotBlank(message = "不能为空")
    @Excel(name = "变动年月(yyyy-MM)")
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "格式不正确,如2018-01")
    private String hisYearMonth;

    /** 变动前薪级 */
    @NotBlank(message = "不能为空")
    @Excel(name = "变动前薪级")
    private String hisPreviousLevel;

    /** 变动后薪级 */
    @NotBlank(message = "不能为空")
    @Excel(name = "变动后薪级")
    private String hisNextLevel;

    /** 变动类别 */
    @NotBlank(message = "不能为空")
    @Excel(name = "变动类别")
    private String hisChangeType;

    /** 是否岗位变动引起的 */
    @NotBlank(message = "不能为空")
    @Excel(name = "是否由岗位变动引起")
    private String hisIspostChange;

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
