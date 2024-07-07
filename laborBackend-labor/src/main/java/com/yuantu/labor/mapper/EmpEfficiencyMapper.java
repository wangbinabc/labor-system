package com.yuantu.labor.mapper;

import java.util.Date;
import java.util.List;
import com.yuantu.labor.domain.EmpEfficiency;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
@Repository
public interface EmpEfficiencyMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public EmpEfficiency selectEmpEfficiencyById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param empEfficiency 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EmpEfficiency> selectEmpEfficiencyList(EmpEfficiency empEfficiency);

    /**
     * 新增【请填写功能名称】
     * 
     * @param empEfficiency 【请填写功能名称】
     * @return 结果
     */
    public int insertEmpEfficiency(EmpEfficiency empEfficiency);

    /**
     * 修改【请填写功能名称】
     * 
     * @param empEfficiency 【请填写功能名称】
     * @return 结果
     */
    public int updateEmpEfficiency(EmpEfficiency empEfficiency);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteEmpEfficiencyById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpEfficiencyByIds(Long[] ids);

    void removeCurrentMonthInfos(Date now);

    void insertEmpEfficiencyInfos(@Param("empEfficiencyInfos") List<EmpEfficiency> empEfficiencyInfos);

}
