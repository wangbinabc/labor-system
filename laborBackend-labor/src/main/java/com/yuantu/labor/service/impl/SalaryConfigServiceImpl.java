package com.yuantu.labor.service.impl;

import java.util.List;
import com.yuantu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.SalaryConfigMapper;
import com.yuantu.labor.domain.SalaryConfig;
import com.yuantu.labor.service.ISalaryConfigService;

/**
 * 酬薪构成配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Service
public class SalaryConfigServiceImpl implements ISalaryConfigService 
{
    @Autowired
    private SalaryConfigMapper salaryConfigMapper;

    /**
     * 查询酬薪构成配置
     * 
     * @param confId 酬薪构成配置主键
     * @return 酬薪构成配置
     */
    @Override
    public SalaryConfig selectSalaryConfigByConfId(Integer confId)
    {
        return salaryConfigMapper.selectSalaryConfigByConfId(confId);
    }

    /**
     * 查询酬薪构成配置列表
     * 
     * @param salaryConfig 酬薪构成配置
     * @return 酬薪构成配置
     */
    @Override
    public List<SalaryConfig> selectSalaryConfigList(SalaryConfig salaryConfig)
    {
        return salaryConfigMapper.selectSalaryConfigList(salaryConfig);
    }

    /**
     * 新增酬薪构成配置
     * 
     * @param salaryConfig 酬薪构成配置
     * @return 结果
     */
    @Override
    public int insertSalaryConfig(SalaryConfig salaryConfig)
    {
        salaryConfig.setCreateTime(DateUtils.getNowDate());
        return salaryConfigMapper.insertSalaryConfig(salaryConfig);
    }

    /**
     * 修改酬薪构成配置
     * 
     * @param salaryConfig 酬薪构成配置
     * @return 结果
     */
    @Override
    public int updateSalaryConfig(SalaryConfig salaryConfig)
    {
        salaryConfig.setUpdateTime(DateUtils.getNowDate());
        return salaryConfigMapper.updateSalaryConfig(salaryConfig);
    }

    /**
     * 批量删除酬薪构成配置
     * 
     * @param confIds 需要删除的酬薪构成配置主键
     * @return 结果
     */
    @Override
    public int deleteSalaryConfigByConfIds(Integer[] confIds)
    {
        return salaryConfigMapper.deleteSalaryConfigByConfIds(confIds);
    }

    /**
     * 删除酬薪构成配置信息
     * 
     * @param confId 酬薪构成配置主键
     * @return 结果
     */
    @Override
    public int deleteSalaryConfigByConfId(Integer confId)
    {
        return salaryConfigMapper.deleteSalaryConfigByConfId(confId);
    }

    @Override
    public int checkConfigUnique(SalaryConfig salaryConfig) {
        return salaryConfigMapper.checkConfigUnique(salaryConfig);
    }
}
