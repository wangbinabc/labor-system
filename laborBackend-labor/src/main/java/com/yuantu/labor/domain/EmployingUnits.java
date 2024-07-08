package com.yuantu.labor.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 用工单位对象 employing_units
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Data
public class EmployingUnits extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用工单位ID */
    @Excel(name = "用工单位ID")
    private Long unitId;

    /** 用工单位名称 */
    @Excel(name = "用工单位名称")
    private String unitName;

    /** 公司类别，0：用工单位，1人力资源公司 */
    @Excel(name = "公司类别，0：用工单位，1人力资源公司")
    private Long unitType;


    /** 部门树形 */
    List<Department> departments;

    /** 部门树形 */
    Map<String, List<Department>> departmentTrees;



    private Boolean disabled;

}
