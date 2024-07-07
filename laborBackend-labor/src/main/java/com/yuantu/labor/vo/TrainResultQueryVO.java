package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
public class TrainResultQueryVO extends BaseEntity {

    /** 培训成果名称 */
    @ApiModelProperty("培训成果名称")
    private String resultName;

    /** 所属项目id */
    @ApiModelProperty("所属项目id")
    private Integer resultProjectId;

    /** 所属项目名称 */
    @ApiModelProperty("所属项目名称")
    private String resultProjectName;

    /** 年度 */
    @ApiModelProperty("年度")
    private String projectYear;
    /**
     * 责任部门id
     */
    @ApiModelProperty("责任部门id")
    private Integer projectDeptId;

    /** 责任部门名称 */
    @ApiModelProperty("责任部门名称")
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
}
