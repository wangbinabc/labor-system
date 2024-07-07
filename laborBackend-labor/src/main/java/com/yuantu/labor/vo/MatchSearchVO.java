package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 员工对象 employee
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Data
public class MatchSearchVO {


    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdCard;

    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    @ApiModelProperty("关键字")
    private String keyword;


}
