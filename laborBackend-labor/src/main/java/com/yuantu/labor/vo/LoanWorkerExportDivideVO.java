package com.yuantu.labor.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class LoanWorkerExportDivideVO {


    @ApiModelProperty("表格拆分字段名")
    @NotNull
    private String fieldName;

    @ApiModelProperty("借工ids")
    private List<Long> loanIds;

    @ApiModelProperty("excel名称")
    private String excelName;


}
