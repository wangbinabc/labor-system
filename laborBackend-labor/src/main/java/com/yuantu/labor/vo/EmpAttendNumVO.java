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
public class EmpAttendNumVO implements Comparable<EmpAttendNumVO> {

    @ApiModelProperty("考勤状态")
    private String attendStatus;

    @ApiModelProperty("数量")
    private Integer num;

    @Override
    public int compareTo(EmpAttendNumVO other) {
        return Integer.parseInt(this.attendStatus) - Integer.parseInt(other.attendStatus);
    }
}
