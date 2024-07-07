package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.Department;
import com.yuantu.labor.vo.DepartmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 部门Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Mapper
@Repository
public interface DepartmentMapper {
    /**
     * 查询部门
     *
     * @param deptId 部门主键
     * @return DepartmentVO
     */
    public DepartmentVO selectDepartmentVOByDeptId(Long deptId);

    /**
     * 查询部门列表
     *
     * @param department 部门
     * @return DepartmentVO集合
     */
    public List<DepartmentVO> selectDepartmentVOList(Department department);

    /**
     * 查询部门
     *
     * @param deptId 部门主键
     * @return DepartmentVO
     */
    public Department selectDepartmentByDeptId(Long deptId);

    /**
     * 查询部门列表
     *
     * @param department 部门
     * @return DepartmentVO集合
     */
    public List<Department> selectDepartmentList(Department department);

    /**
     * 新增部门
     *
     * @param department 部门
     * @return 结果
     */
    public int insertDepartment(Department department);

    /**
     * 修改部门
     *
     * @param department 部门
     * @return 结果
     */
    public int updateDepartment(Department department);

    /**
     * 删除部门
     *
     * @param deptId 部门主键
     * @return 结果
     */
    public int deleteDepartmentByDeptId(Long deptId);

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDepartmentByDeptIds(Long[] deptIds);

    public List<Department> selectDepartmentListByDept(Department department);

    /**
     * 根据单位id查询部门信息
     *
     * @param unitIds
     * @return 结果
     */
    List<Department> findDepartmentInfosByUnitIds(@Param("list") List<Long> unitIds);

    /**
     * 根据部门名称查询部门信息
     *
     * @param empDeptName
     * @return 结果
     */
    Department findDepartmentInfoByName(@Param("name") String empDeptName);

    /**
     * 根据部门名称和单位名称查询部门信息
     *
     * @param empDept
     * @param empUnit
     * @return 结果
     */
    Department findDepartmentInfoByUnitAndDeptName(@Param("empUnit") String empUnit, @Param("empDept") String empDept);


    /**
     * 根据部门名称查询部门信息
     *
     * @param deptNames
     * @return 结果
     */
    List<Department> findDepartmentInfoByNames(@Param("names") List<String> deptNames);

    List<Department> findDeptInfosByDeptIds(@Param("deptIds") List<Long> deptIds);

    List<Department> findAllDeptInfos();

}
