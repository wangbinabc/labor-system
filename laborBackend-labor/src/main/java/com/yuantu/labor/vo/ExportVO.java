package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExportVO {

    @ApiModelProperty("导出属性名")
    @NotNull
    private List<String> fieldNames;

    @ApiModelProperty("记录ids")
    private List<Long> ids;

}
