package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class DeptPerformanceAddVO implements IExcelDataModel, IExcelModel
{
    private static final long serialVersionUID = 1L;


    /** 所在单位 */
    @NotBlank(message = "不能为空")
    @Excel(name = "单位(公司)")
    private String  unitName;

    /** 所在部门 */
    @NotBlank(message = "不能为空")
    @Excel(name = "部门")
    private String  dpDeptName;

    /** 绩效年份 */
    @NotBlank(message = "不能为空")
    @Excel(name = "年份")
    private String dpYear;

    /** 绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度 */
    @NotBlank(message = "不能为空")
    @Excel(name = "绩效周期")
    private String dpCycle;

    /** 绩效值，A优秀，B良好，C及格，D差 */
    @NotBlank(message = "不能为空")
    @Excel(name = "绩效等级")
    private String dpLevelValue;

    /** 备注 */
    @Excel(name = "备注")
    private String dpRemark;


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
