package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 考勤概览数据
 * 
 * @author syw
 * @since 2023-11-06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("考勤概览数据")
public class EmpAttendanceOverviewVO {
//    @ApiModelProperty("出勤天数")
//    private Integer attendanceNum;
//
//    @ApiModelProperty("出差天数")
//    private Integer onBusinessNum;
//
//    @ApiModelProperty("休假天数")
//    private Integer holidayNum;
//
//    @ApiModelProperty("加班天数")
//    private Integer overTimeNum;

    @ApiModelProperty("考勤信息集合")
    private Map<String, Integer> attendMap;

    private List<EmpAttendNumVO> attendNumList;

    @ApiModelProperty("连续上班最大天数")
    private Integer maxContinuousWorkingNum;
}
