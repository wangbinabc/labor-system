package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工对象 employee
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Data
public class MatchUpdateVO {


    @ApiModelProperty("匹配id")
    private Long id;
    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdCard;

}
