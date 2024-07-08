package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmpPerformConditionVO {

    @ApiModelProperty("A绩效次数")
    private Integer aNUm;
    @ApiModelProperty("B绩效次数")
    private Integer bNum;
    @ApiModelProperty("C绩效次数")
    private Integer cNum;
    @ApiModelProperty("D绩效次数")
    private Integer dNum;
    @ApiModelProperty("A/B绩效比率")
    private String ratio;
    @ApiModelProperty("排名前百分比")
    private String rank;
    @ApiModelProperty("排名描述")
    private String rankDesc;

}
