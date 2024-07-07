package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AbilityVO {

    @ApiModelProperty("胜任力指数")
    private Integer abilityRatio;
    @ApiModelProperty("学历")
    private Integer educationRatio;
    @ApiModelProperty("平均学历")
    private Integer avgEducationRatio;
    @ApiModelProperty("证书数量")
    private Integer paperNum;
    @ApiModelProperty("平均证书数量")
    private Integer avgPaperNum;
    @ApiModelProperty("工作经验")
    private Integer workYears;
    @ApiModelProperty("平均工作经验")
    private Integer avgWorkYears;
    @ApiModelProperty("绩效分数")
    private Integer performRatio;
    @ApiModelProperty("平均绩效分数")
    private Integer avgPerformRatio;
    @ApiModelProperty("出勤分数")
    private Integer onWorkRatio;
    @ApiModelProperty("平均出勤分数")
    private Integer avgOnWorkRatio;
    @ApiModelProperty("培训分数")
    private Integer trainRatio;
    @ApiModelProperty("平均培训分数")
    private Integer avgTrainRatio;

}
