package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 部门绩效对象 dept_performance
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class DeptPerformanceScreenVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**编号  */
    @ApiModelProperty(value = "编号")
    private Long dpId;

    /** 部门号 */
    @ApiModelProperty(value = "部门号")
    @Excel(name = "部门号")
    private Long dpDeptId;

    /** 部门名 */
    @ApiModelProperty(value = "部门名")
    @Excel(name = "部门")
    private String dpDeptName;

    /** 绩效年 */
    @ApiModelProperty(value = "绩效年")
    @Excel(name = "年份")
    private String dpYear;

    /** 绩效周期 */
    @ApiModelProperty(value = "绩效周期")
    @Excel(name = "周期", readConverterExp = "0=年度,1=第一季度,2=第二季度 ,3=第三季度,4=第四季度")
    private String dpCycle;

    /**绩效等级  */
    @ApiModelProperty(value = "绩效等级")
    @Excel(name = "绩效等级")
    private String dpLevelValue;

    /**备注*/
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String dpRemark;

    /**
     * 修改更新时间区间
     */
    @ApiModelProperty(value = "修改更新时间区间起始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeStart;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改更新时间区间结束")
    private Date updateTimeEnd;


}
