package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EmpExpertQueryVO extends BaseEntity {

    private String empName;

    private Integer[] expertIds;

    @ApiModelProperty("年龄最小")
    private Integer empAgeMin;
    @ApiModelProperty("年龄最大")
    private Integer empAgeMax;
    @ApiModelProperty("性别")
    private String empGender;
    @ApiModelProperty("部门")
    private Integer deptId;
    @ApiModelProperty("专家级别")
    private String expertLevel;
    @ApiModelProperty("专家人才称号")
    private String expertTitle;
    @ApiModelProperty("授予时间开始")
    private Date expertGrantTimeStart;
    @ApiModelProperty("授予时间结束")
    private Date expertGrantTimeEnd;
    @ApiModelProperty("解聘时间开始")
    private Date expertDismissTimeStart;
    @ApiModelProperty("解聘时间结束")
    private Date expertDismissTimeEnd;
    @ApiModelProperty("聘用期")
    private String expertPeriod;
    @ApiModelProperty("修改时间开始")
    private Date expertUpdateTimeStart;
    @ApiModelProperty("修改时间结束")
    private Date expertUpdateTimeEnd;
    @ApiModelProperty("1 查询已删除人员的有关数据")
    private Integer isRelated;


}
