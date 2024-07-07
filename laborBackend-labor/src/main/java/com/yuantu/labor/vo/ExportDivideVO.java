package com.yuantu.labor.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class ExportDivideVO {


    @ApiModelProperty("表格拆分字段名")
    @NotNull
    private String fieldName;

    @ApiModelProperty("ids")
    private List<Long> ids;

    @ApiModelProperty("excel名称")
    private String excelName;


}
