package com.yuantu.labor.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 省公司考核对象 provice_performance
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
public class ProvicePerformance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ppId;

    /** 本公司部门ID */
  //  @Excel(name = "本公司部门ID")
    private Long ppDeptId;

    /** 本公司部门名称 */
    @Excel(name = "公司牵头部门")
    private String ppDeptName;

    /** 省级公司部门ID */
 //   @Excel(name = "省级公司部门ID")
    private Long ppProvinceDeptId;

    /** 省级公司部门名称 */
    @Excel(name = "省公司部门")
    private String ppProvinceDeptName;

    /** 年份 */
    @Excel(name = "年份")
    private String ppYear;

    /** 周期 */
    @Excel(name = "周期",readConverterExp = "0=年度,1=第一季度,2=第二季度 ,3=第三季度,4=第四季度")
    private String ppCycle;

    /** 目标排名 */
    @Excel(name = "目标排名")
    private Long ppTargetRanking;

    /** 季度排名 */
    @Excel(name = "季度排名")
    private Long ppQuarterlyRanking;

    /** 季度计划得分 */
    @Excel(name = "季度计划得分")
    private Long ppQuarterlyPlanScore;

    /** 季度实际得分 */
    @Excel(name = "季度实际得分")
    private Long ppQuarterlyActualScore;

    /** 逻辑删除(0=正常，1=删除） */
   // @Excel(name = "逻辑删除(0=正常，1=删除）")
    private Integer disabled;

    public void setPpId(Long ppId) 
    {
        this.ppId = ppId;
    }

    public Long getPpId() 
    {
        return ppId;
    }
    public void setPpDeptId(Long ppDeptId) 
    {
        this.ppDeptId = ppDeptId;
    }

    public Long getPpDeptId() 
    {
        return ppDeptId;
    }
    public void setPpDeptName(String ppDeptName) 
    {
        this.ppDeptName = ppDeptName;
    }

    public String getPpDeptName() 
    {
        return ppDeptName;
    }
    public void setPpProvinceDeptId(Long ppProvinceDeptId) 
    {
        this.ppProvinceDeptId = ppProvinceDeptId;
    }

    public Long getPpProvinceDeptId() 
    {
        return ppProvinceDeptId;
    }
    public void setPpProvinceDeptName(String ppProvinceDeptName) 
    {
        this.ppProvinceDeptName = ppProvinceDeptName;
    }

    public String getPpProvinceDeptName() 
    {
        return ppProvinceDeptName;
    }
    public void setPpYear(String ppYear)
    {
        this.ppYear = ppYear;
    }

    public String getPpYear()
    {
        return ppYear;
    }
    public void setPpCycle(String ppCycle) 
    {
        this.ppCycle = ppCycle;
    }

    public String getPpCycle() 
    {
        return ppCycle;
    }
    public void setPpTargetRanking(Long ppTargetRanking) 
    {
        this.ppTargetRanking = ppTargetRanking;
    }

    public Long getPpTargetRanking() 
    {
        return ppTargetRanking;
    }
    public void setPpQuarterlyRanking(Long ppQuarterlyRanking) 
    {
        this.ppQuarterlyRanking = ppQuarterlyRanking;
    }

    public Long getPpQuarterlyRanking() 
    {
        return ppQuarterlyRanking;
    }
    public void setPpQuarterlyPlanScore(Long ppQuarterlyPlanScore) 
    {
        this.ppQuarterlyPlanScore = ppQuarterlyPlanScore;
    }

    public Long getPpQuarterlyPlanScore() 
    {
        return ppQuarterlyPlanScore;
    }
    public void setPpQuarterlyActualScore(Long ppQuarterlyActualScore) 
    {
        this.ppQuarterlyActualScore = ppQuarterlyActualScore;
    }

    public Long getPpQuarterlyActualScore() 
    {
        return ppQuarterlyActualScore;
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
            .append("ppId", getPpId())
            .append("ppDeptId", getPpDeptId())
            .append("ppDeptName", getPpDeptName())
            .append("ppProvinceDeptId", getPpProvinceDeptId())
            .append("ppProvinceDeptName", getPpProvinceDeptName())
            .append("ppYear", getPpYear())
            .append("ppCycle", getPpCycle())
            .append("ppTargetRanking", getPpTargetRanking())
            .append("ppQuarterlyRanking", getPpQuarterlyRanking())
            .append("ppQuarterlyPlanScore", getPpQuarterlyPlanScore())
            .append("ppQuarterlyActualScore", getPpQuarterlyActualScore())
            .append("disabled", getDisabled())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
