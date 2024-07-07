package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmpSalarySimpleInfoVO {


    @ApiModelProperty("员工薪资数")
    private String salaryPayableNum;
    @ApiModelProperty("员工人数")
    private Integer empNum;
    @ApiModelProperty("是否是个人 0 否 1 是")
    private Boolean isPerson;

}
