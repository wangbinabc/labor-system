package com.yuantu.labor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yuantu.labor.domain.Department;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.vo.UnitDepartmentVO;
import com.yuantu.labor.vo.UnitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.service.IEmployingUnitsService;
import org.springframework.util.CollectionUtils;

/**
 * 用工单位Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Service
public class EmployingUnitsServiceImpl implements IEmployingUnitsService {
    @Autowired
    private EmployingUnitsMapper employingUnitsMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 查询用工单位
     *
     * @param unitId 用工单位主键
     * @return 用工单位
     */
    @Override
    public EmployingUnits selectEmployingUnitsByUnitId(Long unitId) {
        return employingUnitsMapper.selectEmployingUnitsByUnitId(unitId);
    }

    /**
     * 查询用工单位列表
     *
     * @param employingUnits 用工单位
     * @return 用工单位
     */
    @Override
    public List<EmployingUnits> selectEmployingUnitsList(EmployingUnits employingUnits) {
        return employingUnitsMapper.selectEmployingUnitsList(employingUnits);
    }


    /**
     * 查询用工单位列表和所属部门
     *
     * @param employingUnits 用工单位
     * @return 用工单位集合
     */
    @Override
    public List<EmployingUnits> selectEmployingUnitAndDeptsList(EmployingUnits employingUnits){
        List<EmployingUnits> units=
                employingUnitsMapper.selectEmployingUnitAndDeptsList(employingUnits);
        for (EmployingUnits unit:units){
            List<Department> depts=unit.getDepartments();
            if (depts!=null){
               // Map<String,List<String>> map=depts.stream().collect(Collectors.groupingBy(Department::getDeptType,Collectors.mapping(Department::getDeptName,Collectors.toList())));
                Map<String,List<Department>> map=depts.stream().collect(Collectors.groupingBy(Department::getDeptType));
                unit.setDepartments(null);
                unit.setDepartmentTrees(map);
            }
        }

        return units;
    }

    /**
     * 新增用工单位
     *
     * @param employingUnits 用工单位
     * @return 结果
     */
    @Override
    public int insertEmployingUnits(EmployingUnits employingUnits) {
   //     employingUnits.setCreateTime(DateUtils.getNowDate());
        return employingUnitsMapper.insertEmployingUnits(employingUnits);
    }

    /**
     * 修改用工单位
     *
     * @param employingUnits 用工单位
     * @return 结果
     */
    @Override
    public int updateEmployingUnits(EmployingUnits employingUnits) {
    //    employingUnits.setUpdateTime(DateUtils.getNowDate());
        return employingUnitsMapper.updateEmployingUnits(employingUnits);
    }

    /**
     * 批量删除用工单位
     *
     * @param unitIds 需要删除的用工单位主键
     * @return 结果
     */
    @Override
    public int deleteEmployingUnitsByUnitIds(Long[] unitIds) {
        return employingUnitsMapper.deleteEmployingUnitsByUnitIds(unitIds);
    }

    /**
     * 删除用工单位信息
     *
     * @param unitId 用工单位主键
     * @return 结果
     */
    @Override
    public int deleteEmployingUnitsByUnitId(Long unitId) {
        return employingUnitsMapper.deleteEmployingUnitsByUnitId(unitId);
    }




    @Override
    public List<UnitVO> searchUnitDepartmentInfo() {

        List<EmployingUnits> units = employingUnitsMapper.findAllInfo();
        if (CollectionUtils.isEmpty(units)) {
            return new ArrayList<>();
        }
        List<Long> unitIds = units.stream().map(EmployingUnits::getUnitId).collect(Collectors.toList());
        List<Department> departments = departmentMapper.findDepartmentInfosByUnitIds(unitIds);
        Map<Long, List<Department>> departmentMap = departments.stream().collect(Collectors.groupingBy(Department::getDeptUnitId));
        List<UnitVO> result = new ArrayList<>();
        for (EmployingUnits unit : units) {
            UnitVO unitInfo = new UnitVO();
            unitInfo.setUnitId(unit.getUnitId());
            unitInfo.setUnitName(unit.getUnitName());
            List<Department> departmentList = departmentMap.getOrDefault(unit.getUnitId(), new ArrayList<>());
            List<UnitDepartmentVO> unitDepartments = new ArrayList<>();
            for (Department department : departmentList) {
                UnitDepartmentVO unitDepartment = new UnitDepartmentVO();
                unitDepartment.setDeptId(department.getDeptId());
                unitDepartment.setDeptName(department.getDeptName());
                unitDepartments.add(unitDepartment);
            }
            unitInfo.setUnitDepartments(unitDepartments);
            result.add(unitInfo);
        }
        return result;
    }
}
