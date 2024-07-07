package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EmpChangeStatusVO {

    @ApiModelProperty("员工id")
    private Long empId;

    @ApiModelProperty("员工状态 1在职，2新入职，3辞职，4辞退，5即将到龄，6到龄，7返聘")
    private String empStatus;

    @ApiModelProperty("离职时间")
    private Date fireTime;

}
