package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptValueVO {

    /**
     *
     */
    private Long deptValueId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String valueYear;

    /**
     * 总产值(万元)
     */
    @ApiModelProperty("总产值(万元)")
    private String sumValue;

    @ApiModelProperty("用工人数测算")
    private String laborNum;

    @ApiModelProperty("人均产值(万元)")
    private String avgValue;


}
