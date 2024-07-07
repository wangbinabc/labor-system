package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 员工对象 employee
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Data
public class EmpSearchSimpleVO {


    private String keyword;

    private Long unitId;

    private Long deptId;

    private List<Long> departmentIds;

}
