package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class SalaryHisExportDivideVO {
    @ApiModelProperty("表格拆分字段名")
    @NotNull
    private String fieldName;

    @ApiModelProperty("薪资历史ids")
    private List<Integer> hisIds;

    @ApiModelProperty("excel名称")
    private String excelName;
}
