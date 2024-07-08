package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.vo.UnitVO;

/**
 * 用工单位Service接口
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
public interface IEmployingUnitsService 
{
    /**
     * 查询用工单位
     * 
     * @param unitId 用工单位主键
     * @return 用工单位
     */
    public EmployingUnits selectEmployingUnitsByUnitId(Long unitId);

    /**
     * 查询用工单位列表
     * 
     * @param employingUnits 用工单位
     * @return 用工单位集合
     */
    public List<EmployingUnits> selectEmployingUnitsList(EmployingUnits employingUnits);

    List<EmployingUnits> selectEmployingUnitAndDeptsList(EmployingUnits employingUnits);

    /**
     * 新增用工单位
     * 
     * @param employingUnits 用工单位
     * @return 结果
     */
    public int insertEmployingUnits(EmployingUnits employingUnits);

    /**
     * 修改用工单位
     * 
     * @param employingUnits 用工单位
     * @return 结果
     */
    public int updateEmployingUnits(EmployingUnits employingUnits);

    /**
     * 批量删除用工单位
     * 
     * @param unitIds 需要删除的用工单位主键集合
     * @return 结果
     */
    public int deleteEmployingUnitsByUnitIds(Long[] unitIds);

    /**
     * 删除用工单位信息
     * 
     * @param unitId 用工单位主键
     * @return 结果
     */
    public int deleteEmployingUnitsByUnitId(Long unitId);

    /**
     *查询用工单位及部门有关信息
     *
     * @return 结果
     */
    List<UnitVO> searchUnitDepartmentInfo();

}
