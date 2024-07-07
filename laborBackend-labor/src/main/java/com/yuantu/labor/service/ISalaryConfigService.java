package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.SalaryConfig;

/**
 * 酬薪构成配置Service接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
public interface ISalaryConfigService 
{
    /**
     * 查询酬薪构成配置
     * 
     * @param confId 酬薪构成配置主键
     * @return 酬薪构成配置
     */
    public SalaryConfig selectSalaryConfigByConfId(Integer confId);

    /**
     * 查询酬薪构成配置列表
     * 
     * @param salaryConfig 酬薪构成配置
     * @return 酬薪构成配置集合
     */
    public List<SalaryConfig> selectSalaryConfigList(SalaryConfig salaryConfig);

    /**
     * 新增酬薪构成配置
     * 
     * @param salaryConfig 酬薪构成配置
     * @return 结果
     */
    public int insertSalaryConfig(SalaryConfig salaryConfig);

    /**
     * 修改酬薪构成配置
     * 
     * @param salaryConfig 酬薪构成配置
     * @return 结果
     */
    public int updateSalaryConfig(SalaryConfig salaryConfig);

    /**
     * 批量删除酬薪构成配置
     * 
     * @param confIds 需要删除的酬薪构成配置主键集合
     * @return 结果
     */
    public int deleteSalaryConfigByConfIds(Integer[] confIds);

    /**
     * 删除酬薪构成配置信息
     * 
     * @param confId 酬薪构成配置主键
     * @return 结果
     */
    public int deleteSalaryConfigByConfId(Integer confId);

    public int checkConfigUnique(SalaryConfig salaryConfig);
}
