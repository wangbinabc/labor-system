package com.yuantu.labor.service.impl;

import java.util.List;

import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.SecurityUtils;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.service.IDepartmentService;
import com.yuantu.labor.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 部门Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-06
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService
{
    @Autowired
    private DepartmentMapper departmentMapper;



    /**
     * 查询部门,单位名
     * 
     * @param deptId 部门主键
     * @return 部门
     */
    @Override
    public DepartmentVO selectDepartmentVOByDeptId(Long deptId)
    {
        return departmentMapper.selectDepartmentVOByDeptId(deptId);
    }

    /**
     * 查询部门列表,单位名
     * 
     * @param department 部门
     * @return 部门
     */
    @Override
    public List<DepartmentVO> selectDepartmentVOList(Department department)
    {



        return departmentMapper.selectDepartmentVOList(department);
    }

    /**
     * 查询部门
     *
     * @param deptId 部门主键
     * @return 部门
     */
    @Override
    public Department selectDepartmentByDeptId(Long deptId) {
        return departmentMapper.selectDepartmentByDeptId(deptId);
    }

    /**
     * 查询部门列表
     *
     * @param department 部门
     * @return 部门
     */
    @Override
    public List<Department> selectDepartmentList(Department department) {
        return departmentMapper.selectDepartmentList(department);
    }

    /**
     * 新增部门
     * 
     * @param department 部门
     * @return 结果
     */
    @Override
    public int insertDepartment(Department department)
    {
     //   department.setCreateTime(DateUtils.getNowDate());
        return departmentMapper.insertDepartment(department);
    }

    /**
     * 修改部门
     * 
     * @param department 部门
     * @return 结果
     */
    @Override
    public int updateDepartment(Department department)
    {

        return departmentMapper.updateDepartment(department);
    }

    /**
     * 批量删除部门
     * 
     * @param deptIds 需要删除的部门主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentByDeptIds(Long[] deptIds)
    {
        return departmentMapper.deleteDepartmentByDeptIds(deptIds);
    }

    /**
     * 删除部门信息
     * 
     * @param deptId 部门主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentByDeptId(Long deptId)
    {
        return departmentMapper.deleteDepartmentByDeptId(deptId);
    }

    @Override
    public List<Department> selectDepartmentListByDept(Department department) {
        return departmentMapper.selectDepartmentListByDept(department);
    }
}
