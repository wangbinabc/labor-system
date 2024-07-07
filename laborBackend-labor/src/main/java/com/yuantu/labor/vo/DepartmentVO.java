package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 部门对象 department
 * 
 * @author ruoyi
 * @date 2023-09-06
 */
@Data
public class DepartmentVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门id */
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 部门类型 */
    @Excel(name = "部门类型")

    private String deptType;

    /** 所属用工单位名称 */
    @Excel(name = "所属用工单位名称")
    private String deptUnitName;

    /** 用工单位ID */
    @Excel(name = "用工单位ID")
    private Long deptUnitId;
}
