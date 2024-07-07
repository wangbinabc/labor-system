package com.yuantu.labor.mapper;

import java.util.Date;
import java.util.List;

import com.yuantu.labor.domain.DeptEfficiency;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2023-11-12
 */
@Repository
public interface DeptEfficiencyMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public DeptEfficiency selectDeptEfficiencyById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param deptEfficiency 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DeptEfficiency> selectDeptEfficiencyList(DeptEfficiency deptEfficiency);

    /**
     * 新增【请填写功能名称】
     *
     * @param deptEfficiency 【请填写功能名称】
     * @return 结果
     */
    public int insertDeptEfficiency(DeptEfficiency deptEfficiency);

    /**
     * 修改【请填写功能名称】
     *
     * @param deptEfficiency 【请填写功能名称】
     * @return 结果
     */
    public int updateDeptEfficiency(DeptEfficiency deptEfficiency);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteDeptEfficiencyById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeptEfficiencyByIds(Long[] ids);

    void removeCurrentMonthInfos(@Param("now") Date now);

    void insertInfos(@Param("deptEfficiencyInfos") List<DeptEfficiency> deptEfficiencies);
}
