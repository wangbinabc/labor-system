package com.yuantu.labor.vo;

import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用于借工页装载查询条件
 */
@Data
public class LoanWorkerQueryVO {
    private Integer[] loanIds;
    private String yearMonth;
    private String empName;
    /**
     * 借工部门
     */
    private String loanApplyDeptName;
    /**
     * 借工单位
     */
    private String loanApplyUnitName;
    @ApiModelProperty("借工单位id")
    private Integer loanApplyUnitId;
    @ApiModelProperty("借工部门id")
    private Integer loanApplyDeptId;

    @ApiModelProperty("性别")
    private String loanEmpGender;
    @ApiModelProperty("学历")
    private String loanEmpEducation;
    @ApiModelProperty("专业")
    private String loanEmpSpeciality;
    @ApiModelProperty("职称")
    private String loanEmpTitle;
    @ApiModelProperty("执业资格证书")
    private String loanEmpCredentials;
    @ApiModelProperty("年龄开始")
    private Integer loanEmpAgeBegin;
    @ApiModelProperty("年龄结束")
    private Integer loanEmpAgeEnd;

    @ApiModelProperty("处/室")
    private String loanApplyOffice;

    @ApiModelProperty("拟安排区域")
    private String loanArrageArea;
    @ApiModelProperty("拟从事岗位")
    private String loanNewPost;
    @ApiModelProperty("拟入场时间开始")
    private Date loanBeginTimeStart;
    @ApiModelProperty("拟入场时间结束")
    private Date loanBeginTimeEnd;
    @ApiModelProperty("借调更新时间开始")
    private Date loanUpdateDateStart;
    @ApiModelProperty("借调更新时间结束")
    private Date loanUpdateDateEnd;

    private Integer isRelated;

}
