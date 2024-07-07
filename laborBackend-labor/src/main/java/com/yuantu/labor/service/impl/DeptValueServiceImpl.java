package com.yuantu.labor.service.impl;

import com.yuantu.common.exception.ServiceException;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.DeptValue;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.DeptValueMapper;
import com.yuantu.labor.mapper.EmpHistoryMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.service.IDeptValueService;
import com.yuantu.labor.vo.DeptValueAddVO;
import com.yuantu.labor.vo.DeptValueSearchVO;
import com.yuantu.labor.vo.DeptValueVO;
import com.yuantu.labor.vo.EmpNumVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DeptValueServiceImpl implements IDeptValueService {


    @Autowired
    private DeptValueMapper deptValueMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpHistoryMapper empHistoryMapper;

    @Override
    public List<DeptValueVO> findDeptValueInfos(DeptValueSearchVO deptValue) {
        List<DeptValue> deptValues = deptValueMapper.findDeptValueInfos(deptValue);
        List<Long> deptIds = deptValues.stream().map(DeptValue::getDeptId).collect(Collectors.toList());
        Map<Long, Department> departmentMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            departmentMap = departmentMapper.findDeptInfosByDeptIds(deptIds).stream().
                    collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        }
        List<DeptValueVO> result = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String nowStr = dateFormat.format(now);
        for (DeptValue value : deptValues) {
            DeptValueVO deptValueInfo = new DeptValueVO();
            BeanUtils.copyProperties(value, deptValueInfo);
            deptValueInfo.setDeptValueId(value.getId());
            String deptName = departmentMap.getOrDefault(value.getDeptId(), new Department()).getDeptName();
            deptValueInfo.setDeptName(deptName);

            Map<String, List<EmpNumVO>> empNumMap = empHistoryMapper.findEmpInfosByDeptIdAndCreateYear(value.getDeptId(),
                            value.getValueYear()).stream().
                    filter(s -> s.getCreateTime() != null).map(s -> {
                        EmpNumVO empNum = new EmpNumVO();
                        empNum.setEmpId(s.getEmpId());
                        Date empCreateTime = s.getCreateTime();
                        empNum.setCreateMonth(dateFormat.format(empCreateTime));
                        return empNum;
                    }).collect(Collectors.groupingBy(EmpNumVO::getCreateMonth));
            List<EmpNumVO> empNumInfos = employeeMapper.findEmployeeInfosByDeptIdWithStatus(value.getDeptId(), value.getValueYear()).stream().map(s -> {
                EmpNumVO empNum = new EmpNumVO();
                empNum.setEmpId(s.getEmpId());
                empNum.setCreateMonth(nowStr);
                return empNum;
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(empNumInfos)) {
                empNumMap.put(empNumInfos.get(0).getCreateMonth(), empNumInfos);
            }
//
            int months = 0;
            int labors = 0;
            for (String month : empNumMap.keySet()) {
                months++;
                labors += empNumMap.get(month).size();
            }
            if (months != 0) {
                deptValueInfo.setLaborNum(String.format("%.2f", (double) labors / months));
                if (deptValueInfo.getLaborNum() != null) {
                    deptValueInfo.setAvgValue(String.format("%.2f", Double.parseDouble(deptValueInfo.getSumValue())
                            / Double.parseDouble(deptValueInfo.getLaborNum())));
                }
            }
            result.add(deptValueInfo);
        }
        return result;
    }

    @Override
    public DeptValueVO findDeptValueInfo(Long deptValueId) {
        DeptValue deptValue = deptValueMapper.selectByPrimaryKey(deptValueId);
        if (deptValue == null) {
            return new DeptValueVO();
        }
        DeptValueVO deptValueInfo = new DeptValueVO();
        deptValueInfo.setDeptValueId(deptValueId);
        deptValueInfo.setDeptId(deptValue.getDeptId());
        Department department = departmentMapper.selectDepartmentByDeptId(deptValue.getDeptId());
        if (department != null) {
            deptValueInfo.setDeptName(department.getDeptName());
        }
        deptValueInfo.setValueYear(deptValue.getValueYear());
        deptValueInfo.setSumValue(deptValue.getSumValue());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date now = new Date();
        String nowStr = dateFormat.format(now);
        Map<String, List<EmpNumVO>> empNumMap = empHistoryMapper.findEmpInfosByDeptIdAndCreateYear(deptValue.getDeptId(),
                        deptValue.getValueYear()).stream().
                filter(s -> s.getCreateTime() != null).map(s -> {
                    EmpNumVO empNum = new EmpNumVO();
                    empNum.setEmpId(s.getEmpId());
                    Date empCreateTime = s.getCreateTime();
                    empNum.setCreateMonth(dateFormat.format(empCreateTime));
                    return empNum;
                }).collect(Collectors.groupingBy(EmpNumVO::getCreateMonth));

        List<EmpNumVO> empNumInfos = employeeMapper.findEmployeeInfosByDeptIdWithStatus(deptValue.getDeptId(),
                deptValue.getValueYear()).stream().map(s -> {
            EmpNumVO empNum = new EmpNumVO();
            empNum.setEmpId(s.getEmpId());
            empNum.setCreateMonth(nowStr);
            return empNum;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(empNumInfos)) {
            empNumMap.put(empNumInfos.get(0).getCreateMonth(), empNumInfos);
        }
        int months = 0;
        int labors = 0;
        for (String month : empNumMap.keySet()) {
            months++;
            labors += empNumMap.get(month).size();
        }
        if (months != 0) {
            deptValueInfo.setLaborNum(String.format("%.2f", (double) labors / months));
            if (deptValueInfo.getLaborNum() != null) {
                deptValueInfo.setAvgValue(String.format("%.2f", Double.parseDouble(deptValueInfo.getSumValue())
                        / Double.parseDouble(deptValueInfo.getLaborNum())));
            }
        }
        return deptValueInfo;
    }

    @Override
    public Long updateDeptValue(DeptValueAddVO deptValueAdd, Long userId) {

        Date now = new Date();
        if (deptValueAdd.getDeptValueId() == null) {
            Long deptId = deptValueAdd.getDeptId();
            DeptValue existDeptValue = deptValueMapper.findInfoByDeptIdAndValueYear(deptId, deptValueAdd.getValueYear());
            if (existDeptValue != null) {
                throw new ServiceException("该部门产值信息已存在");
            }
            DeptValue deptValue = new DeptValue();
            BeanUtils.copyProperties(deptValueAdd, deptValue);

            Department department = departmentMapper.selectDepartmentByDeptId(deptId);
            if (department == null) {
                throw new ServiceException("部门信息不存在");
            }
            deptValue.setDisabled(false);
            deptValue.setCreateBy(userId);
            deptValue.setCreateTime(now);
            deptValueMapper.insertSelective(deptValue);
            return deptValue.getId();
        } else {
            DeptValue deptValue = new DeptValue();
            BeanUtils.copyProperties(deptValueAdd, deptValue);
            deptValue.setId(deptValueAdd.getDeptValueId());
            deptValue.setUpdateBy(userId);
            deptValue.setUpdateTime(now);
            deptValueMapper.updateByPrimaryKeySelective(deptValue);
            return deptValueAdd.getDeptValueId();
        }
    }

    @Override
    public Boolean removeDeptValueInfos(List<Long> deptValueIds) {

        deptValueMapper.removeDeptValueInfos(deptValueIds);
        return true;
    }
}
