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
public class EmpAttendanceStatusInfoVO {


    @ApiModelProperty("x轴-时间")
    private String xAxi;

    @ApiModelProperty("考勤信息集合")
    private Map<String, Long> attendMap;


}
