package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class DeptEffectivenessSearchVO {
    /**
     * 部门Id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    /**
     * 部门名
     */
    @ApiModelProperty("部门名")
    private String deptName;


    @ApiModelProperty("年月")
    @JsonFormat(pattern = "yyyy-MM")
    private String searchYearMonth
            = new SimpleDateFormat("yyyy-MM").format(new Date());

    private Integer pageSize;

    private Integer pageNum;

}
