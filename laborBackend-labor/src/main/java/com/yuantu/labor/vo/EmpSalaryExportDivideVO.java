package com.yuantu.labor.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EmpSalaryExportDivideVO {


    @ApiModelProperty("表格拆分字段名")
    @NotNull
    private String fieldName;

    @ApiModelProperty("员工ids")
    private List<Long> empIds;

    @ApiModelProperty("excel名称")
    private String excelName;


}
