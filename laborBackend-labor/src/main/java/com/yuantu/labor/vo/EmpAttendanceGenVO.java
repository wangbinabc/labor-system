
package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author syw
 * @since 2023-11-06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("考勤详情总概数据")
public class EmpAttendanceGenVO {


    private Long empId;

    private String empName;

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
//
    @ApiModelProperty("连续上班最大天数")
    private Integer maxContinuousWorkingNum;

    @ApiModelProperty("考勤集合")
    private Map<String, Integer> attendMap;

    private List<EmpAttendNumVO> attendNumList;

    @ApiModelProperty("考勤详情数据")
    private List<EmpAttendanceDetailVO> detailList;
}
