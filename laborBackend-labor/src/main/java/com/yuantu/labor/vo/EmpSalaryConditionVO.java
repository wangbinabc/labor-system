package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmpSalaryConditionVO {

    @ApiModelProperty("员工入职日期")
    private String empHireDate;
    @ApiModelProperty("员工工作年限")
    private String workYears;
    @ApiModelProperty("薪酬变动次数")
    private Integer salaryChangeNum;
    @ApiModelProperty("入职薪酬数")
    private String beginSalaryNum;
    @ApiModelProperty("当期薪酬数")
    private String endSalaryNum;
    @ApiModelProperty("入职薪级")
    private String salaryLevelBegin;
    @ApiModelProperty("当前薪级")
    private String salaryLevelEnd;
    @ApiModelProperty("薪级涨达幅")
    private String salaryIncreaseRatio;
    @ApiModelProperty("薪资排名率")
    private String salaryRankRatio;
    @ApiModelProperty("同一薪酬人数")
    private Integer salaryEmpNum;
    @ApiModelProperty("薪酬情况")
    private List<EmpSalarySimpleInfoVO> empSalarySimpleInfos;


}
