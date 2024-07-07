package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.InsuranceConfiguration;

/**
 * 五险配置Service接口
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
public interface IInsuranceConfigurationService 
{
    /**
     * 查询五险配置
     * 
     * @param insId 五险配置主键
     * @return 五险配置
     */
    public InsuranceConfiguration selectInsuranceConfigurationByInsId(Integer insId);

    /**
     * 查询五险配置列表
     * 
     * @param insuranceConfiguration 五险配置
     * @return 五险配置集合
     */
    public List<InsuranceConfiguration> selectInsuranceConfigurationList(InsuranceConfiguration insuranceConfiguration);

    /**
     * 新增五险配置
     * 
     * @param insuranceConfiguration 五险配置
     * @return 结果
     */
    public int insertInsuranceConfiguration(InsuranceConfiguration insuranceConfiguration);

    /**
     * 修改五险配置
     * 
     * @param insuranceConfiguration 五险配置
     * @return 结果
     */
    public int updateInsuranceConfiguration(InsuranceConfiguration insuranceConfiguration);

    /**
     * 批量删除五险配置
     * 
     * @param insIds 需要删除的五险配置主键集合
     * @return 结果
     */
    public int deleteInsuranceConfigurationByInsIds(Integer[] insIds);

    /**
     * 删除五险配置信息
     * 
     * @param insId 五险配置主键
     * @return 结果
     */
    public int deleteInsuranceConfigurationByInsId(Integer insId);
}
