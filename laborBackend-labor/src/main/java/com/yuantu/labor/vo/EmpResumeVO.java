package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EmpResumeVO {
    /**
     * 员工名或者身份证
     */
    private String empName;
    /**
     * 履历类型
     */
    private String resuType;

    private Integer[] resuIds;

    @ApiModelProperty("开始时间起始")
    private Date resuBeginDateStart;
    @ApiModelProperty("开始时间结束")
    private Date resuBeginDateEnd;
    @ApiModelProperty("截止时间起始")
    private Date resuEndDateStart;
    @ApiModelProperty("截至时间结束")
    private Date resuEndDateEnd;
    @ApiModelProperty("工作单位")
    private String resuWorkUnitName;
    @ApiModelProperty("从事岗位")
    private String resuPosition;
    @ApiModelProperty("部门")
    private String resuDept;
    @ApiModelProperty("项目名称")
    private String resuProjectName;
    @ApiModelProperty("项目规模")
    private String resuProjectScale;
    @ApiModelProperty("项目类型")
    private String resuProjectType;
    @ApiModelProperty("项目职位")
    private String resuProjectJob;
    @ApiModelProperty("证明人")
    private String resuProjectCertifier;

    private Integer isRelated;



}
