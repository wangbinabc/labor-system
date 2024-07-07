package com.yuantu.labor.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 员工福利对象 emp_welfare
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
@Data
public class EmpWelfare extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工福利id */
    private Long welfareId;

    /** 员工id */
   // @Excel(name = "员工id")
    private Long welfareEmpId;

    /** 员工身份证 */
    @Excel(name = "身份证号")
    private String welfareEmpIdcard;

    /** 员工名称 */
    @Excel(name = "姓名")
    private String welfareEmpName;

    /** 五险两金费 */
    @Excel(name = "五险两金(元)")
    private BigDecimal welfareInsureFee;

    /** 劳动保护费 */
    @Excel(name = "劳动保护(元)")
    private BigDecimal welfareProtectFee;

    /** 食堂经费 */
    @Excel(name = "食堂经费(元)")
    private BigDecimal welfareFoodFee;

    /** 体检费 */
    @Excel(name = "体检费(元)")
    private BigDecimal welfareCheckupFee;

    /** $column.columnComment */
  //  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer disabled;

    private Boolean isChange = true;

    //是否删除
    private Integer  isRelated;


}
