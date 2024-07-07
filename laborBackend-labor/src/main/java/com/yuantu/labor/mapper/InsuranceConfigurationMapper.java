package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.InsuranceConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 五险配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
@Mapper
@Repository
public interface InsuranceConfigurationMapper 
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
     * 删除五险配置
     * 
     * @param insId 五险配置主键
     * @return 结果
     */
    public int deleteInsuranceConfigurationByInsId(Integer insId);

    /**
     * 批量删除五险配置
     * 
     * @param insIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInsuranceConfigurationByInsIds(Integer[] insIds);
}
