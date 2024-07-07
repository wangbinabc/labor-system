package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import cn.afterturn.easypoi.excel.annotation.Excel;
@Data
public class TrainProjectImportVO implements IExcelDataModel, IExcelModel {

    /** 培训项目名称 */
    @Excel(name = "培训项目名称")
    @NotBlank(message = "不能为空")
    private String projectName;

    /** 主要培训内容/课程 */
    @Excel(name = "主要培训内容/课程")
    @NotBlank(message = "不能为空")
    private String projectContent;

    /** 年度 */
    @Excel(name = "年度")
    @NotBlank(message = "不能为空")
    private String projectYear;

      /** 责任部门名称 */
    @Excel(name = "单位名称-部门名称")
    @NotBlank(message = "不能为空")
    private String projectDeptName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @Excel(name = "培训性质")
    @NotBlank(message = "不能为空")
    private String projectNature;

    /** 培训方式(1=内培, 2=外培) */
    @Excel(name = "培训方式")
    @NotBlank(message = "不能为空")
    private String projectMethod;

    /** 项目分类(1=一类，2=二类，3=三类) */
    @Excel(name = "项目分类")
    @NotBlank(message = "不能为空")
    private String projectClassify;

    /** 是否完成 */
    @Excel(name = "是否完成")
    @NotBlank(message = "不能为空")
    private String projectIsfinish;

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
