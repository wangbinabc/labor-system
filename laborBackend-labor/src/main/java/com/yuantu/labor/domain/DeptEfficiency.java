package com.yuantu.labor.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 dept_efficiency
 *
 * @author ruoyi
 * @date 2023-11-12
 */
public class DeptEfficiency extends BaseEntity {
    private static final long serialVersionUID = 1L;



    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private Long deptId;

    private String deptName;

    /**
     * 部门利润值
     */
    @Excel(name = "部门利润值")
    private String deptProfit;

    /**
     * 考勤出勤率
     */
    @Excel(name = "考勤出勤率")
    private String deptAttendRatio;

    /**
     * 绩效指数
     */
    @Excel(name = "绩效指数")
    private String deptPerformRatio;

    /**
     * 成本指数
     */
    @Excel(name = "成本指数")
    private String deptCostRatio;

    /**
     * 效能指数
     */
    @Excel(name = "效能指数")
    private String deptEffectRatio;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptProfit(String deptProfit) {
        this.deptProfit = deptProfit;
    }

    public String getDeptProfit() {
        return deptProfit;
    }

    public void setDeptAttendRatio(String deptAttendRatio) {
        this.deptAttendRatio = deptAttendRatio;
    }

    public String getDeptAttendRatio() {
        return deptAttendRatio;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeptPerformRatio(String deptPerformRatio) {
        this.deptPerformRatio = deptPerformRatio;
    }

    public String getDeptPerformRatio() {
        return deptPerformRatio;
    }

    public void setDeptCostRatio(String deptCostRatio) {
        this.deptCostRatio = deptCostRatio;
    }

    public String getDeptCostRatio() {
        return deptCostRatio;
    }

    public void setDeptEffectRatio(String deptEffectRatio) {
        this.deptEffectRatio = deptEffectRatio;
    }

    public String getDeptEffectRatio() {
        return deptEffectRatio;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deptId", getDeptId())
                .append("deptProfit", getDeptProfit())
                .append("deptAttendRatio", getDeptAttendRatio())
                .append("deptPerformRatio", getDeptPerformRatio())
                .append("deptCostRatio", getDeptCostRatio())
                .append("deptEffectRatio", getDeptEffectRatio())
                .append("createTime", getCreateTime())
                .toString();
    }
}
