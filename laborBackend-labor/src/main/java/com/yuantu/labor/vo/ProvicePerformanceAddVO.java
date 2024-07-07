package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ProvicePerformanceAddVO implements IExcelDataModel, IExcelModel
{
    private static final long serialVersionUID = 1L;


    /** 所在单位 */
  //  @NotBlank(message = "不能为空")
//    @Excel(name = "省级公司")
//    private String  provinceUnitName;

    /** 所在部门 */
    @NotBlank(message = "不能为空")
    @Excel(name = "省公司部门")
    private String  ppProvinceDeptName;

    /** 所在单位 */
  //  @NotBlank(message = "不能为空")
  //  @Excel(name = "本单位(公司)")
  //  private String  unitName;

    /** 所在部门 */
    @NotBlank(message = "不能为空")
    @Excel(name = "本单位(公司)-公司牵头部门")
    private String  ppDeptName;

    /** 绩效年份 */
    @NotBlank(message = "不能为空")
    @Excel(name = "年份")
    private String ppYear;

    /** 绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度 */
    @NotBlank(message = "不能为空")
    @Excel(name = "绩效周期")
    private String ppCycle;

    @NotNull(message = "不能为空")
    @Excel(name = "目标排名")
    private Long ppTargetRanking;

    /** 季度排名 */
    @NotNull(message = "不能为空")
    @Excel(name = "季度排名")
    private Long ppQuarterlyRanking;

    /** 季度计划得分 */
    @NotNull(message = "不能为空")
    @Excel(name = "季度计划得分")
    private Long ppQuarterlyPlanScore;

    /** 季度实际得分 */
    @NotNull(message = "不能为空")
    @Excel(name = "季度实际得分")
    private Long ppQuarterlyActualScore;

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
