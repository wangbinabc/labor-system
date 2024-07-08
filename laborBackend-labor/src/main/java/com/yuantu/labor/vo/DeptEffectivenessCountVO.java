package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Data
public class DeptEffectivenessCountVO {



    private Long deptId;

    private String deptName;


    //dept利润值
    private Double deptProfitValue = 0.0;

    //出勤率
    private Double attendance= 0.0;

    //绩效指数
    private Double performance= 0.0;

    //成本指数
    private Double cost= 0.0;

    //效能指数
    private Double efficiency= 0.0;

    @ApiModelProperty("年月")
    @JsonFormat(pattern = "yyyy-MM")
    private String searchYearMonth
            = new SimpleDateFormat("yyyy-MM").format(new Date());

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptEffectivenessCountVO that = (DeptEffectivenessCountVO) o;
        return Objects.equals(deptId, that.deptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptId);
    }
}
