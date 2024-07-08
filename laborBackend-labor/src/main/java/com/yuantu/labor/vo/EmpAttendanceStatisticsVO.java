package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 考勤统计数据
 *
 * @author syw
 * @since 2023-11-06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("考勤统计数据")
public class EmpAttendanceStatisticsVO {


    @ApiModelProperty("x轴-时间")
    private List<String> xAxis;

    @ApiModelProperty("考勤信息集合")
    private List<EmpAttendanceStatusInfoVO> attendanceStatusInfos;

//    @ApiModelProperty("y轴-出勤")
//    private List<Integer> attendanceAxis;
//
//    @ApiModelProperty("y轴-出差")
//    private List<Integer> onBusinessAxis;
//
//    @ApiModelProperty("y轴-加班")
//    private List<Integer> overTimeAxis;

}
