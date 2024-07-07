package com.yuantu.labor.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class EmpExportVO {

    @ApiModelProperty("日期")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM")
    private Date date;

    @ApiModelProperty("导出属性名")
    @NotNull
    private List<String> fieldNames;

    @ApiModelProperty("员工ids")
    private List<Long> empIds;


}
