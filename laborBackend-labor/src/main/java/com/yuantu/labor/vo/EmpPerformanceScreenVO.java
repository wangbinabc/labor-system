package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 员工绩效对象 emp_performance
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class EmpPerformanceScreenVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long perfId;

    /** 员工id */
   // @Excel(name = "员工id")
    private Long perfEmpId;

    /** 员工名称 */
    @ApiModelProperty("员工名称")
    @Excel(name = "姓名")
    private String perfEmpName;

    /** 员工身份证 */
    @ApiModelProperty("员工身份证")
    @Excel(name = "身份证号")
    private String  perfEmpIdcard;

    /** 部门ID */
    @ApiModelProperty("部门ID")
    //@Excel(name = "部门ID")
    private Long perfDeptId;

    /** 部门名称 */
    @ApiModelProperty("部门名称")
    @Excel(name = "部门")
    private String perfDeptName;

    /** 绩效年份 */
    @ApiModelProperty("绩效年份")
    @Excel(name = "年份")
    private String perfYear;

    /** 绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度 */
    @ApiModelProperty("绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度")
    @Excel(name = "周期",readConverterExp = "0=年度,1=第一季度,2=第二季度 ,3=第三季度,4=第四季度")
    private String perfCycle;

    /** 绩效值，A优秀，B良好，C及格，D差 */
    @ApiModelProperty("绩效值，A优秀，B良好，C及格，D差")
    @Excel(name = "绩效等级")
    private String perfLevelValue;

    /** 备注 */
    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String perfRemark;

    /**
     * 修改更新时间区间
     */
    @ApiModelProperty("修改更新时间区间起始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeStart;

    @ApiModelProperty("修改更新时间区间结束")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeEnd;

    //是否删除
    private Integer  isRelated;


}
