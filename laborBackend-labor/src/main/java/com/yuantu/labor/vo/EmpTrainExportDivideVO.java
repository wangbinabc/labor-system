package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class EmpTrainExportDivideVO {
    @ApiModelProperty("表格拆分字段名")
    @NotNull
    private String fieldName;

    @ApiModelProperty("员工培训ids")
    private List<Integer> trainIds;

    @ApiModelProperty("excel名称")
    private String excelName;
}
