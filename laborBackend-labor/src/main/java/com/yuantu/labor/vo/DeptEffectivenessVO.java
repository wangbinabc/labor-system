package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Data
public class DeptEffectivenessVO {
    /**
     * 部门Id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    /**
     * 部门名
     */
    @ApiModelProperty("部门名")
    private String deptName;


    /**
     * 效能结果
     */
    @ApiModelProperty("效能结果")
    private Double result;

    @ApiModelProperty("年月")
    @JsonFormat(pattern = "yyyy-MM")
    private String searchYearMonth
            = new SimpleDateFormat("yyyy-MM").format(new Date());

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptEffectivenessVO that = (DeptEffectivenessVO) o;
        return Objects.equals(deptName, that.deptName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptName);
    }
}
