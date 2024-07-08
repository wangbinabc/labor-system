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
 * 省公司考核对象 provice_performance
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class ProvicePerformanceScreenVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ppId;

    /** 本公司部门ID */
    @ApiModelProperty("本公司部门ID")
  //  @Excel(name = "本公司部门ID")
    private Long ppDeptId;

    /** 公司牵头部门*/
    @ApiModelProperty("公司牵头部门")
    @Excel(name = "公司牵头部门")
    private String ppDeptName;

    /** 省级公司部门ID */
    @ApiModelProperty("省级公司部门ID")
 //   @Excel(name = "省级公司部门ID")
    private Long ppProvinceDeptId;

    /** 省公司部门 */
    @ApiModelProperty("省公司部门")
    @Excel(name = "省公司部门")
    private String ppProvinceDeptName;

    /** 年份 */
    @ApiModelProperty("年份")
    @Excel(name = "年份")
    private String ppYear;

    /** 周期 */
    @ApiModelProperty("周期")
    @Excel(name = "周期",readConverterExp = "0=年度,1=第一季度,2=第二季度 ,3=第三季度,4=第四季度")
    private String ppCycle;

    /** 目标排名 */
    @ApiModelProperty("目标排名")
    @Excel(name = "目标排名")
    private Long ppTargetRanking;

    /** 季度排名区间 */

    @Excel(name = "季度排名")
    @ApiModelProperty("季度排名区间起始")
    private Long ppQuarterlyRankingStart;

    @ApiModelProperty("季度排名区间结束")
    private Long ppQuarterlyRankingEnd;

    /** 季度计划得分 */
    @ApiModelProperty("季度计划得分")
    @Excel(name = "季度计划得分")
   // private Long ppQuarterlyPlanScoreStart;
    private Long ppQuarterlyPlanScore;

    /** 季度实际得分 */
    @Excel(name = "季度实际得分")
    @ApiModelProperty("季度实际得分起始")
    private Long ppQuarterlyActualScoreStart;

    @ApiModelProperty("季度实际得分结束")
    private Long ppQuarterlyActualScoreEnd;


    /**
     * 修改更新时间区间
     */
    @ApiModelProperty("修改更新时间区间起始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeStart;

    @ApiModelProperty("修改更新时间区间结束")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeEnd;
}
