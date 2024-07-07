package com.yuantu.labor.service;

import com.yuantu.labor.domain.Department;
import com.yuantu.labor.vo.DepartmentVO;

import java.util.List;


/**
 * 部门Service接口
 * 
 * @author ruoyi
 * @date 2023-09-06
 */
public interface IDepartmentService 
{
    /**
     * 查询部门(单位名)
     * 
     * @param deptId 部门主键
     * @return 部门
     */
    public DepartmentVO selectDepartmentVOByDeptId(Long deptId);

    /**
     * 查询部门列表(单位名)
     * 
     * @param department 部门
     * @return 部门集合
     */
    public List<DepartmentVO> selectDepartmentVOList(Department department);


    /**
     * 查询部门
     *
     * @param deptId 部门主键
     * @return 部门
     */
    public Department selectDepartmentByDeptId(Long deptId);

    /**
     * 查询部门列表
     *
     * @param department 部门
     * @return 部门集合
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
     * 批量删除部门
     * 
     * @param deptIds 需要删除的部门主键集合
     * @return 结果
     */
    public int deleteDepartmentByDeptIds(Long[] deptIds);

    /**
     * 删除部门信息
     * 
     * @param deptId 部门主键
     * @return 结果
     */
    public int deleteDepartmentByDeptId(Long deptId);

    public List<Department> selectDepartmentListByDept(Department department);
}
