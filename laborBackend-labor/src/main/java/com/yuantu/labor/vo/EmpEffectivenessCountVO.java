package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class EmpEffectivenessCountVO {
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


    @ApiModelProperty("员工个人利润")
    private Double profit = 0.0;

    @ApiModelProperty("员工个人效能值")
    private Double empProfitValue = 0.0;

    //出勤率
    private Double attendance = 0.0;

    //绩效指数
    private Double performance = 0.0;

    //成本指数
    private Double cost = 0.0;

    //效能指数
    private Double efficiency = 0.0;

    @ApiModelProperty("年月")
    @JsonFormat(pattern = "yyyy-MM")
    private String searchYearMonth
            = new SimpleDateFormat("yyyy-MM").format(new Date());
}
