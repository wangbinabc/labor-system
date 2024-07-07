package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author syw
 * @since 2023-11-06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("考勤详情数据")
public class EmpAttendanceDetailVO {
    @ApiModelProperty("时间")
    private Date time;

    @ApiModelProperty("考勤状态集合")
    private List<String> attendStatus;
}
