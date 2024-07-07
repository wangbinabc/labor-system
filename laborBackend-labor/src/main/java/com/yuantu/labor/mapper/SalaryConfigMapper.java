package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.SalaryConfig;
import com.yuantu.labor.vo.SalaryLabelAndItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 酬薪构成配置Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Mapper
@Repository
public interface SalaryConfigMapper {
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
     * 删除酬薪构成配置
     *
     * @param confId 酬薪构成配置主键
     * @return 结果
     */
    public int deleteSalaryConfigByConfId(Integer confId);

    /**
     * 批量删除酬薪构成配置
     *
     * @param confIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSalaryConfigByConfIds(Integer[] confIds);

    public int checkConfigUnique(SalaryConfig salaryConfig);

    public List<SalaryLabelAndItemVO> selectSalaryLabelAndItem();

    /**
     * 查询所有酬薪构成配置
     *
     * @return 结果
     */
    List<SalaryConfig> findAllSalaryConfigs();

    SalaryConfig findInfoByConfItemAndTypeValue(@Param("confItem") String confItem, @Param("typeLabel") String typeLabel);
}
