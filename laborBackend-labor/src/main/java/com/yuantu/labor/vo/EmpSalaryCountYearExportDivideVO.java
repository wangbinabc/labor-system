package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EmpSalaryCountYearExportDivideVO {
    @ApiModelProperty("表格拆分字段名")
    @NotNull
    private String fieldName;

    @ApiModelProperty("excel名称")
    private String excelName;

    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty("姓名或身份证号")
    private String empName;
}
