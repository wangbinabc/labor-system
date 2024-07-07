package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeptValueAddVO {

    /**
     *
     */
    private Long deptValueId;

    /**
     * 部门id
     */
    @NotNull(message = "部门信息不能为空")
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 年份
     */
    @NotBlank(message = "年份信息不能为空")
    @ApiModelProperty("年份")
    private String valueYear;

    /**
     * 总产值(万元)
     */
    @NotBlank(message = "总产值(万元)信息不能为空")
    @ApiModelProperty("总产值(万元)")
    private String sumValue;


}
