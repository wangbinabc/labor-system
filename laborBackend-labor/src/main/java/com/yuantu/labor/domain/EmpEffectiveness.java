package com.yuantu.labor.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 人员效能对象 emp_effectiveness
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
public class EmpEffectiveness extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long effId;

    /** 员工ID */
    @Excel(name = "员工ID")
    private Long effEmpId;

    /** 员工身份证 */
    @Excel(name = "员工身份证")
    private String effEmpIdcard;

    /** 员工个人利润值 */
    @Excel(name = "员工个人利润值")
    private String effEmpProfitValue;

    /** 成本指数 */
    @Excel(name = "成本指数")
    private Long effCostIndex;

    /** 成本值 */
    @Excel(name = "成本值")
    private Long effCostValue;

//    /** 所属年份 */
//    @Excel(name = "所属年份")
//    private String effYear;
//
//    /** 所属月份 */
//    @Excel(name = "所属月份")
//    private String effMonth;

    /** 所属年月 */
    @Excel(name = "所属年月")
    private String effYearMonth;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer disabled;

    public void setEffId(Long effId) 
    {
        this.effId = effId;
    }

    public Long getEffId() 
    {
        return effId;
    }
    public void setEffEmpId(Long effEmpId) 
    {
        this.effEmpId = effEmpId;
    }

    public Long getEffEmpId() 
    {
        return effEmpId;
    }
    public void setEffEmpIdcard(String effEmpIdcard) 
    {
        this.effEmpIdcard = effEmpIdcard;
    }

    public String getEffEmpIdcard() 
    {
        return effEmpIdcard;
    }
    public void setEffEmpProfitValue(String effEmpProfitValue)
    {
        this.effEmpProfitValue = effEmpProfitValue;
    }

    public String getEffEmpProfitValue()
    {
        return effEmpProfitValue;
    }
    public void setEffCostIndex(Long effCostIndex) 
    {
        this.effCostIndex = effCostIndex;
    }

    public Long getEffCostIndex() 
    {
        return effCostIndex;
    }
    public void setEffCostValue(Long effCostValue) 
    {
        this.effCostValue = effCostValue;
    }

    public Long getEffCostValue() 
    {
        return effCostValue;
    }
//    public void setEffYear(String effYear)
//    {
//        this.effYear = effYear;
//    }
//
//    public String getEffYear()
//    {
//        return effYear;
//    }
//    public void setEffMonth(String effMonth)
//    {
//        this.effMonth = effMonth;
//    }
//
//    public String getEffMonth()
//    {
//        return effMonth;
//    }
    public void setDisabled(Integer disabled)
    {
        this.disabled = disabled;
    }

    public Integer getDisabled() 
    {
        return disabled;
    }

    public String getEffYearMonth() {
        return effYearMonth;
    }

    public void setEffYearMonth(String effYearMonth) {
        this.effYearMonth = effYearMonth;
    }
}
