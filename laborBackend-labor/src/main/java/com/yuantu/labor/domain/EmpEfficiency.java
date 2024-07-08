package com.yuantu.labor.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 emp_efficiency
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
public class EmpEfficiency extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 员工id */
    @Excel(name = "员工id")
    private Long empId;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String empName;

    /** 个人利润值 */
    @Excel(name = "个人利润值")
    private String profit;

    /** 出勤率 */
    @Excel(name = "出勤率")
    private String attendRatio;

    /** 绩效指数 */
    @Excel(name = "绩效指数")
    private String performRatio;

    /** 成本指数 */
    @Excel(name = "成本指数")
    private String costRatio;

    /** 能效指数 */
    @Excel(name = "能效指数")
    private String effectRatio;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEmpId(Long empId) 
    {
        this.empId = empId;
    }

    public Long getEmpId() 
    {
        return empId;
    }
    public void setEmpName(String empName) 
    {
        this.empName = empName;
    }

    public String getEmpName() 
    {
        return empName;
    }
    public void setProfit(String profit) 
    {
        this.profit = profit;
    }

    public String getProfit() 
    {
        return profit;
    }
    public void setAttendRatio(String attendRatio) 
    {
        this.attendRatio = attendRatio;
    }

    public String getAttendRatio() 
    {
        return attendRatio;
    }
    public void setPerformRatio(String performRatio) 
    {
        this.performRatio = performRatio;
    }

    public String getPerformRatio() 
    {
        return performRatio;
    }
    public void setCostRatio(String costRatio) 
    {
        this.costRatio = costRatio;
    }

    public String getCostRatio() 
    {
        return costRatio;
    }
    public void setEffectRatio(String effectRatio) 
    {
        this.effectRatio = effectRatio;
    }

    public String getEffectRatio() 
    {
        return effectRatio;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("empId", getEmpId())
            .append("empName", getEmpName())
            .append("profit", getProfit())
            .append("attendRatio", getAttendRatio())
            .append("performRatio", getPerformRatio())
            .append("costRatio", getCostRatio())
            .append("effectRatio", getEffectRatio())
            .append("createTime", getCreateTime())
            .toString();
    }
}
