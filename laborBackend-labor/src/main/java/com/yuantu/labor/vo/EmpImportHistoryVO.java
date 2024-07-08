package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户证件信息导入历史对象 emp_import_history
 *
 * @author ahadon
 * @date 2023-10-08
 */
@Data
public class EmpImportHistoryVO {


    /**
     * $column.columnComment
     */
    @ApiModelProperty("匹配id")
    private Long id;

    /**
     * 员工ID
     */
    @Excel(name = "员工ID")
    @ApiModelProperty("员工ID")
    private Long empId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    @ApiModelProperty("员工姓名")
    private String empName;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    @ApiModelProperty("身份证号")
    private String idCard;

    /**
     * 0 未匹配 1 匹配
     */
    @Excel(name = "匹配状态")
    @ApiModelProperty("匹配状态 0 未匹配 1 匹配")
    private Integer status;

    /**
     * 0 未删除 1 删除
     */
    private Boolean disabled;

//    @ApiModelProperty("创建人")
//    private String createBy;
//
//    @ApiModelProperty("创建时间")
//    private Date createTime;
//
//    @ApiModelProperty("修改人")
//    private String updateBy;
//
//    @ApiModelProperty("修改时间")
//    private Date updateTime;

}
