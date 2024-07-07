package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 用于查询借工情况列表
 */
@Data
public class LoanWorkerSimpleVO implements IExcelDataModel, IExcelModel {
    /**
     * 单位
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "单位")
    private String loanApplyUnitName;
    /**
     * 员工姓名
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "姓名")
    private String loanEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String loanEmpIdcard;

    /**
     * 用工部门
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "用工部门")
    private String loanApplyUnitDeptName;

    /**
     * 科室
     */
    @Excel(name="处/室")
    private String loanApplyOffice;


    /**
     * 性别
     */
    @Excel(name = "性别")
    private String loanEmpGender;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private Integer loanEmpAge;
    /**
     * 学历
     */
    @Excel(name = "学历")
    private String loanEmpEducation;

    /**
     * 专业
     */
    @Excel(name = "专业")
    private String loanEmpSpeciality;

    /**
     * 职称
     */
    @Excel(name = "职称")
    private String loanEmpTitle;

    /**
     * 资格证书
     */
    @Excel(name = "执业资格证书")
    private String loanEmpCredentials;

    /**
     * 拟安排区域
     */
    @Excel(name = "拟派驻现场(片区)")
    private String loanArrageArea;

    /**
     * 新的岗位
     */
    @Excel(name = "拟从事岗位")
    private String loanNewPost;

    /**
     * 借调入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拟入场时间（年月日）")
    private Date loanBeginTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String loanRemark;


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
