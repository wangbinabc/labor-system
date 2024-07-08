package com.yuantu.labor.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 员工绩效对象 emp_performance
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class EmpPerformance extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long perfId;

    /**
     * 员工id
     */
    // @Excel(name = "员工id")
    private Long perfEmpId;

    /**
     * 员工名称
     */
    @Excel(name = "姓名")
    private String perfEmpName;

    /**
     * 员工身份证
     */
    @Excel(name = "身份证号")
    private String perfEmpIdcard;

    /**
     * 部门ID
     */
    //@Excel(name = "部门ID")
    private Long perfDeptId;

    /**
     * 部门名称
     */
    @Excel(name = "部门")
    private String perfDeptName;

    /**
     * 绩效年份
     */
    @Excel(name = "年份")
    private String perfYear;

    /**
     * 绩效周期  1 第一季度 2 第二季度 3 第三季度 4 第四季度  0 本年度
     */
    @Excel(name = "周期", readConverterExp = "0=年度,1=第一季度,2=第二季度 ,3=第三季度,4=第四季度")
    private String perfCycle;

    /**
     * 绩效值，A优秀，B良好，C及格，D差
     */
    @Excel(name = "绩效等级")
    private String perfLevelValue;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String perfRemark;

    /**
     * 逻辑删除(0=正常，1=删除）
     */
    //   @Excel(name = "逻辑删除(0=正常，1=删除）")
    private Integer disabled;

    private Boolean isChange = true;

    private Integer isRelated;

    private Integer flag;
}
