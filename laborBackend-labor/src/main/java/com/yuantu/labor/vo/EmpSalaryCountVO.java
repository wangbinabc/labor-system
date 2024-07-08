package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class EmpSalaryCountVO extends BaseEntity {
    /**
     * 员工id
     */
    //@Excel(name = "员工id")
    private Long empId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String empName;

    /**
     * 身份证
     */
    @Excel(name = "身份证")
    private String empIdcard;

    /**
     * 部门ID
     */
    // @Excel(name = "部门ID")
    private Long empDeptId;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String empDeptName;

    /**
     * 用工单位
     */
    @Excel(name = "用工单位")
    private String empEmployingUnits;

    /**
     * 人员类别
     */
    @Excel(name = "人员类别", readConverterExp = "1=外包,2=全民,3=借工,4=直签")
    private String empCategory;

    /**
     * 薪酬统计(元)
     */
    @Excel(name="薪酬统计(元)")
    private String salaryTotal;

    private Boolean isChange;

}
