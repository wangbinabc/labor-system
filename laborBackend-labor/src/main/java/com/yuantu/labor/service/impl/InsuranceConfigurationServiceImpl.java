package com.yuantu.labor.service.impl;

import java.util.List;
import com.yuantu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.InsuranceConfigurationMapper;
import com.yuantu.labor.domain.InsuranceConfiguration;
import com.yuantu.labor.service.IInsuranceConfigurationService;

/**
 * 五险配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
@Service
public class InsuranceConfigurationServiceImpl implements IInsuranceConfigurationService 
{
    @Autowired
    private InsuranceConfigurationMapper insuranceConfigurationMapper;

    /**
     * 查询五险配置
     * 
     * @param insId 五险配置主键
     * @return 五险配置
     */
    @Override
    public InsuranceConfiguration selectInsuranceConfigurationByInsId(Integer insId)
    {
        return insuranceConfigurationMapper.selectInsuranceConfigurationByInsId(insId);
    }

    /**
     * 查询五险配置列表
     * 
     * @param insuranceConfiguration 五险配置
     * @return 五险配置
     */
    @Override
    public List<InsuranceConfiguration> selectInsuranceConfigurationList(InsuranceConfiguration insuranceConfiguration)
    {
        return insuranceConfigurationMapper.selectInsuranceConfigurationList(insuranceConfiguration);
    }

    /**
     * 新增五险配置
     * 
     * @param insuranceConfiguration 五险配置
     * @return 结果
     */
    @Override
    public int insertInsuranceConfiguration(InsuranceConfiguration insuranceConfiguration)
    {
        insuranceConfiguration.setCreateTime(DateUtils.getNowDate());
        return insuranceConfigurationMapper.insertInsuranceConfiguration(insuranceConfiguration);
    }

    /**
     * 修改五险配置
     * 
     * @param insuranceConfiguration 五险配置
     * @return 结果
     */
    @Override
    public int updateInsuranceConfiguration(InsuranceConfiguration insuranceConfiguration)
    {
        insuranceConfiguration.setUpdateTime(DateUtils.getNowDate());
        return insuranceConfigurationMapper.updateInsuranceConfiguration(insuranceConfiguration);
    }

    /**
     * 批量删除五险配置
     * 
     * @param insIds 需要删除的五险配置主键
     * @return 结果
     */
    @Override
    public int deleteInsuranceConfigurationByInsIds(Integer[] insIds)
    {
        return insuranceConfigurationMapper.deleteInsuranceConfigurationByInsIds(insIds);
    }

    /**
     * 删除五险配置信息
     * 
     * @param insId 五险配置主键
     * @return 结果
     */
    @Override
    public int deleteInsuranceConfigurationByInsId(Integer insId)
    {
        return insuranceConfigurationMapper.deleteInsuranceConfigurationByInsId(insId);
    }
}
