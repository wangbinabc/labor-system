package com.yuantu.labor.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 用户证件信息导入历史对象 emp_import_history
 *
 * @author ahadon
 * @date 2023-10-08
 */
@Data
public class EmpImportHistory {


    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 员工ID
     */
    @Excel(name = "员工ID")
    private Long empId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String empName;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String idCard;

    /**
     * 0 未匹配 1 匹配
     */
    @Excel(name = "0 未匹配 1 匹配")
    private Integer status;

    /**
     * 0 未删除 1 删除
     */
    @Excel(name = "0 未删除 1 删除")
    private Boolean disabled;


    private Long createBy;


    private Date createTime;


    private Long updateBy;


    private Date updateTime;

}
