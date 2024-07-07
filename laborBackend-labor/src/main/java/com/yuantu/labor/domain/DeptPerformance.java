package com.yuantu.labor.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 部门绩效对象 dept_performance
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
public class DeptPerformance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**编号  */
    private Long dpId;

    /** 部门号 */
    @Excel(name = "部门号")
    private Long dpDeptId;

    /** 部门名 */
    @Excel(name = "部门")
    private String dpDeptName;

    /** 绩效年 */
    @Excel(name = "年份")
    private String dpYear;

    /** 绩效周期 */
    @Excel(name = "周期", readConverterExp = "0=年度,1=第一季度,2=第二季度 ,3=第三季度,4=第四季度")
    private String dpCycle;

    /**绩效等级  */
    @Excel(name = "绩效等级")
    private String dpLevelValue;

    /** 备注*/
    @Excel(name = "备注")
    private String dpRemark;

    /** 逻辑删除(0=正常，1=删除） */
  //  @Excel(name = "逻辑删除(0=正常，1=删除）")
    private Integer disabled;

    public void setDpId(Long dpId) 
    {
        this.dpId = dpId;
    }

    public Long getDpId() 
    {
        return dpId;
    }
    public void setDpDeptId(Long dpDeptId) 
    {
        this.dpDeptId = dpDeptId;
    }

    public Long getDpDeptId() 
    {
        return dpDeptId;
    }
    public void setDpDeptName(String dpDeptName) 
    {
        this.dpDeptName = dpDeptName;
    }

    public String getDpDeptName() 
    {
        return dpDeptName;
    }
    public void setDpYear(String dpYear) 
    {
        this.dpYear = dpYear;
    }

    public String getDpYear() 
    {
        return dpYear;
    }
    public void setDpCycle(String dpCycle) 
    {
        this.dpCycle = dpCycle;
    }

    public String getDpCycle() 
    {
        return dpCycle;
    }
    public void setDpLevelValue(String dpLevelValue) 
    {
        this.dpLevelValue = dpLevelValue;
    }

    public String getDpLevelValue() 
    {
        return dpLevelValue;
    }
    public void setDpRemark(String dpRemark) 
    {
        this.dpRemark = dpRemark;
    }

    public String getDpRemark() 
    {
        return dpRemark;
    }
    public void setDisabled(Integer disabled) 
    {
        this.disabled = disabled;
    }

    public Integer getDisabled() 
    {
        return disabled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dpId", getDpId())
            .append("dpDeptId", getDpDeptId())
            .append("dpDeptName", getDpDeptName())
            .append("dpYear", getDpYear())
            .append("dpCycle", getDpCycle())
            .append("dpLevelValue", getDpLevelValue())
            .append("dpRemark", getDpRemark())
            .append("disabled", getDisabled())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
