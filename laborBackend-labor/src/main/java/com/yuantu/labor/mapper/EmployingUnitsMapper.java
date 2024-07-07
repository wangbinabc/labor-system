package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.EmployingUnits;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用工单位Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Mapper
@Repository
public interface EmployingUnitsMapper {
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


    /**
     * 查询用工单位列表和所属部门
     *
     * @param employingUnits 用工单位
     * @return 用工单位集合
     */
    public List<EmployingUnits> selectEmployingUnitAndDeptsList(EmployingUnits employingUnits);

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
     * 删除用工单位
     *
     * @param unitId 用工单位主键
     * @return 结果
     */
    public int deleteEmployingUnitsByUnitId(Long unitId);

    /**
     * 批量删除用工单位
     *
     * @param unitIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmployingUnitsByUnitIds(Long[] unitIds);

    /**
     * 查询所有用工单位信息
     *
     * @return 结果
     */
    List<EmployingUnits> findAllInfo();

    /**
     * 根据名称查询所有用工单位信息
     *
     * @return 结果
     */
    EmployingUnits findUnitInfoByName(@Param("name") String empEmployingUnits);

    /**
     * 根据名称查询所有用工单位信息
     *
     * @return 结果
     */
    List<EmployingUnits> findUnitInfoByNames(@Param("names") List<String> names);

    /**
     * 根据单位id查询用工单位信息
     *
     * @return 结果
     */
    List<EmployingUnits> findUnitInfoByUnitIds(@Param("unitIds") List<Long> unitIds);
}
