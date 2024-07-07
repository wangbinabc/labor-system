package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class TrainResultExportVO {
    @ApiModelProperty("导出属性名")
    @NotNull
    private List<String> fieldNames;

    @ApiModelProperty("培训成果ids")
    private List<Integer> resultIds;
}
