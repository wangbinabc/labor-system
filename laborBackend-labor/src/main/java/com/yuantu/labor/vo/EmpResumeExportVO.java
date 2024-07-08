package com.yuantu.labor.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EmpResumeExportVO {

    @ApiModelProperty("导出属性名")
    @NotNull
    private List<String> fieldNames;

    @ApiModelProperty("员工ids")
    private List<Long> empIds;

    @ApiModelProperty("履历类型 1.社会，2.项目，3.本单位")
    private String type;


}
