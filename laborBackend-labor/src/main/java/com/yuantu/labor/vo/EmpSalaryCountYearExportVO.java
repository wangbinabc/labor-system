package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EmpSalaryCountYearExportVO {
    @ApiModelProperty("导出属性名")
    @NotNull
    private List<String> fieldNames;

    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty("姓名或身份证号")
    private String empName;

}
