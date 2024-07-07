package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TrainProjectQueryVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 培训项目名称 */
    @ApiModelProperty("培训项目名称")
    private String projectName;

    /** 主要培训内容/课程 */
    @ApiModelProperty( "主要培训内容/课程")
    private String projectContent;

    /** 年度 */
    @ApiModelProperty("年度")
    private String projectYear;

    /** 责任部门 */
    @ApiModelProperty("责任部门ID")
    private Integer projectDeptId;

    /** 责任部门名称 */
    @ApiModelProperty("责任部门")
    private String projectDeptName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @ApiModelProperty("培训性质")
    private String projectNature;

    /** 培训方式(1=内培, 2=外培) */
    @ApiModelProperty("培训方式")
    private String projectMethod;

    /** 项目分类(1=一类，2=二类，3=三类) */
    @ApiModelProperty("项目分类")
    private String projectClassify;

    /** 是否完成 */
    @ApiModelProperty("是否完成")
    private String projectIsfinish;

    /** 开始修改时间 */
    @ApiModelProperty("开始修改时间")
    private Date beginUpdateTime;

    /**
     * 结束修改时间
     */
    @ApiModelProperty("结束修改时间")
    private Date endUpdateTime;

    /**
     * 项目id集合
     */
    private Integer[] projectIds;

}
