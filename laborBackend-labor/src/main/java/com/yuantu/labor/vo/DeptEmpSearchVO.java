package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DeptEmpSearchVO {

    @NotNull(message = "部门id不能为空")
    private Long deptId;
    @JsonFormat(pattern = "yyyy-MM")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM")
    private Date endTime;
    @ApiModelProperty("员工状态 3 辞职 4 辞退")
    private String empStatus;

    private String empName;




}
