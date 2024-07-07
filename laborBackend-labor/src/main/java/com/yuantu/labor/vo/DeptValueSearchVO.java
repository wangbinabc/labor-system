package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptValueSearchVO {

    /**
     *
     */
    private Long deptValueId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

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




}
