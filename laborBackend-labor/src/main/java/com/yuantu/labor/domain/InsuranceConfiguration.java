package com.yuantu.labor.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 五险配置对象 insurance_configuration
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
public class InsuranceConfiguration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer insId;

    /** 缴费基数 */
    @Excel(name = "缴费基数")
    private BigDecimal paymentBase;

    /** 企业缴费比例 */
    @Excel(name = "企业缴费比例")
    private BigDecimal enterpriseContributionRatio;

    /** 个人缴费比例 */
    @Excel(name = "个人缴费比例")
    private BigDecimal individualPaymentRatio;

    /** 类型 1养老保险 2医疗保险 3失业保险 4工商保险 5生育保险 */
    @Excel(name = "类型 1养老保险 2医疗保险 3失业保险 4工商保险 5生育保险")
    private Integer type;

    /** $column.columnComment */
   // @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer disabled;

    public void setInsId(Integer insId) 
    {
        this.insId = insId;
    }

    public Integer getInsId() 
    {
        return insId;
    }
    public void setPaymentBase(BigDecimal paymentBase) 
    {
        this.paymentBase = paymentBase;
    }

    public BigDecimal getPaymentBase() 
    {
        return paymentBase;
    }
    public void setEnterpriseContributionRatio(BigDecimal enterpriseContributionRatio) 
    {
        this.enterpriseContributionRatio = enterpriseContributionRatio;
    }

    public BigDecimal getEnterpriseContributionRatio() 
    {
        return enterpriseContributionRatio;
    }
    public void setIndividualPaymentRatio(BigDecimal individualPaymentRatio) 
    {
        this.individualPaymentRatio = individualPaymentRatio;
    }

    public BigDecimal getIndividualPaymentRatio() 
    {
        return individualPaymentRatio;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setDisabled(Integer disabled) 
    {
        this.disabled = disabled;
    }

    public Integer getDisabled() 
    {
        return disabled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("insId", getInsId())
            .append("paymentBase", getPaymentBase())
            .append("enterpriseContributionRatio", getEnterpriseContributionRatio())
            .append("individualPaymentRatio", getIndividualPaymentRatio())
            .append("type", getType())
            .append("disabled", getDisabled())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
