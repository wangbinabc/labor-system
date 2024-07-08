package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import com.yuantu.labor.domain.EmpSalaryItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Data
public class EmpSalaryResultVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer salaryId;


    /**
     * 员工ID
     */
    // @Excel(name = "员工ID")
    private Long salaryEmpId;


    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String salaryEmpName;


    /**
     * 身份证
     */
    @Excel(name = "身份证")
    private String salaryEmpIdcard;

    /**
     * 年月
     */
    @Excel(name = "年月")
    private String salaryYearMonth;

    /**
     * 应发工资
     */
    @Excel(name = "应发工资")
    private BigDecimal salaryPayableNum;

    /**
     * 公积金个缴
     */
    @Excel(name = "公积金个缴")
    private BigDecimal salaryPersonFund;

    /**
     * 实发工资
     */
    @Excel(name = "实发工资")
    private BigDecimal salaryNetPay;


    // @Excel(name = "部门ID")
    private Long empDeptId;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String empDeptName;


    /**
     * 人力资源公司
     */
    @Excel(name = "人力资源公司")
    private String empHrCompany;

    /**
     * 岗级
     */
    @Excel(name = "岗级", readConverterExp = "1=一级,2=二级,3=三级,4=四级")
    private String empPositionLevel;

    /**
     * 岗位
     */
    @Excel(name = "岗位")
    private String empPosition;

    /**
     * 备注
     */
    //@Excel(name = "备注")
    private String empRemark;

    @ApiModelProperty("有关员工数据 已删除 false 未时除 true")
    private Boolean isChange;

    private List<EmpSalaryItem> salaryItemList;

}

