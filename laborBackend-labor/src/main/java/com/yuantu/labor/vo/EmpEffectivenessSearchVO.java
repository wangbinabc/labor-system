package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.javassist.Loader;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class EmpEffectivenessSearchVO {
    /**
     * 员工Id
     */
    @ApiModelProperty("员工Id")
    private Long empId;

    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdcard;

    @ApiModelProperty("年月")
    @JsonFormat(pattern = "yyyy-MM")
    private String searchYearMonth
            = new SimpleDateFormat("yyyy-MM").format(new Date());


    //年份
    private String searchYear  = new SimpleDateFormat("yyyy").format(new Date());;

    //周期
    private String searchCycle  = "1";


    private Integer pageSize;

    private Integer pageNum;
}
