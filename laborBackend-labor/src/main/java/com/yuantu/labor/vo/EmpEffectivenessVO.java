package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class EmpEffectivenessVO {
    /**
     * 员工Id
     */
    @ApiModelProperty("员工Id")
    private Long empId;


    private Long deptId;

    private String deptName;
    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdcard;

    /**
     * 身份证
     */
    @ApiModelProperty("效能结果")
    private Double result;

    @ApiModelProperty("年月")
    @JsonFormat(pattern = "yyyy-MM")
    private String searchYearMonth
            = new SimpleDateFormat("yyyy-MM").format(new Date());
}
