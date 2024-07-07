package com.yuantu.labor.service.impl;

import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.labor.cenum.EmpStatusEnum;
import com.yuantu.labor.cenum.PerfCycleEnum;
import com.yuantu.labor.domain.EmpEfficiency;
import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmpHistoryMapper;
import com.yuantu.labor.mapper.EmpPerformanceMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.service.IEmpInOutService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmpInOutServiceImpl implements IEmpInOutService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpHistoryMapper empHistoryMapper;

    @Autowired
    private EmpPerformanceMapper empPerformanceMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Override
    public List<DeptFlowInfoVO> getDeptFlowInfos(Date startTime, Date endTime) {

        if (startTime == null || endTime == null) {
            throw new ServiceException("开始时间或结束时间不能为空");
        }
        if (startTime.after(endTime)) {
            throw new ServiceException("开始时间不能大于结束时间");
        }

        Date now = new Date();
        if (startTime.after(now)) {
            return new ArrayList<>();
        }
        if (endTime.after(now)) {
            endTime = now;
        }
        List<DeptFlowInfoVO> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String startTimeStr = dateFormat.format(startTime);
        String endTimeStr = dateFormat.format(endTime);
        List<SysDictData> empStatusDicDataInfos = dictDataMapper.selectDictDataByType("emp_status");
        String nowStr = dateFormat.format(now);
        if (startTimeStr.equals(endTimeStr)) {
            if (nowStr.equals(endTimeStr)) {
                List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(startTimeStr);
                Map<String, List<Employee>> empStatusMap = currentEmpInfos.stream().filter(s -> s.getEmpStatus() != null).
                        filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.groupingBy(Employee::getEmpStatus));
                for (String empStatus : empStatusMap.keySet()) {
                    List<Employee> employees = empStatusMap.get(empStatus);
                    for (SysDictData empStatusDicDataInfo : empStatusDicDataInfos) {
                        if(empStatusDicDataInfo.getDictValue().equals(empStatus)){
                            empStatus = empStatusDicDataInfo.getDictLabel();
                        }
                    }
                    DeptFlowInfoVO deptFlowInfo = new DeptFlowInfoVO();
                    deptFlowInfo.setType(empStatus);
                    Map<Long, List<Employee>> empDeptMap = employees.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(Employee::getEmpDeptId));
                    List<DeptNumVO> deptNumInfos = new ArrayList<>();
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<Employee> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new Employee()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        deptNumInfos.add(deptNum);
                    }
                    deptFlowInfo.setDeptNumInfos(deptNumInfos);
                    result.add(deptFlowInfo);
                }
            } else {
                List<EmpHistory> empHistories = empHistoryMapper.findEmpInfosByCreateTime(startTime);
                Map<String, List<EmpHistory>> empStatusMap = empHistories.stream().filter(s -> s.getEmpStatus() != null).
                        filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.groupingBy(EmpHistory::getEmpStatus));
                for (String empStatus : empStatusMap.keySet()) {
                    List<EmpHistory> employees = empStatusMap.get(empStatus);
                    for (SysDictData empStatusDicDataInfo : empStatusDicDataInfos) {
                        if(empStatusDicDataInfo.getDictValue().equals(empStatus)){
                            empStatus = empStatusDicDataInfo.getDictLabel();
                        }
                    }
                    DeptFlowInfoVO deptFlowInfo = new DeptFlowInfoVO();
                    deptFlowInfo.setType(empStatus);
                    Map<Long, List<EmpHistory>> empDeptMap = employees.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    List<DeptNumVO> deptNumInfos = new ArrayList<>();
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        deptNumInfos.add(deptNum);
                    }
                    deptFlowInfo.setDeptNumInfos(deptNumInfos);
                    result.add(deptFlowInfo);
                }
            }
        } else {
            if (nowStr.equals(endTimeStr)) {
                List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(endTimeStr).stream().
                        filter(s -> s.getEmpStatus() != null).
                        filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());

                Calendar calendar = Calendar.getInstance(); // 创建日历对象
                calendar.setTime(endTime); // 将结束日期设置到日历对象中
                calendar.add(Calendar.MONTH, -1); // 减去一个月份
                Date previousMonthDate = calendar.getTime(); // 获取上一个月的日期
                List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, previousMonthDate).stream().
                        filter(s -> s.getEmpId() != null).
                        filter(s -> s.getEmpStatus() != null).
                        filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                        .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                List<EmpSimpleVO> empSimpleList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(currentEmpInfos)) {
                    for (Employee currentEmpInfo : currentEmpInfos) {
                        EmpSimpleVO empSimple = new EmpSimpleVO();
                        BeanUtils.copyProperties(currentEmpInfo, empSimple);
                        empSimpleList.add(empSimple);
                    }
                }
                if (!CollectionUtils.isEmpty(empHistoryInfos)) {
                    if (CollectionUtils.isEmpty(empSimpleList)) {
                        for (EmpHistory empHistoryInfo : empHistoryInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(empHistoryInfo, empSimple);
                            empSimpleList.add(empSimple);
                        }
                    } else {
                        for (EmpHistory empHistoryInfo : empHistoryInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(empHistoryInfo, empSimple);
                            if (empSimple.getEmpId() != null) {
                                if (!empSimpleList.contains(empSimple)) {
                                    empSimpleList.add(empSimple);
                                }
                            }
                        }
                    }
                }
                Map<String, List<EmpSimpleVO>> empSimpleMap = empSimpleList.stream().filter(s -> s.getEmpStatus() != null).
                        collect(Collectors.groupingBy(EmpSimpleVO::getEmpStatus));
                for (String type : empSimpleMap.keySet()) {
                    List<EmpSimpleVO> empSimpleInfos = empSimpleMap.get(type);
                    for (SysDictData empStatusDicDataInfo : empStatusDicDataInfos) {
                        if(empStatusDicDataInfo.getDictValue().equals(type)){
                            type = empStatusDicDataInfo.getDictLabel();
                        }
                    }
                    DeptFlowInfoVO deptFlowInfo = new DeptFlowInfoVO();
                    deptFlowInfo.setType(type);
                    List<DeptNumVO> deptNumList = new ArrayList<>();
                    Map<Long, List<EmpSimpleVO>> deptMap = empSimpleInfos.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpSimpleVO::getEmpDeptId));
                    for (Long deptId : deptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpSimpleVO> empSimples = deptMap.get(deptId);
                        String empDeptName = empSimples.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpSimpleVO()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empSimples.size());
                        deptNumList.add(deptNum);
                    }
                    deptFlowInfo.setDeptNumInfos(deptNumList);
                    result.add(deptFlowInfo);
                }
            } else {
                List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, endTime).stream().
                        filter(s -> s.getEmpId() != null).
                        filter(s -> s.getEmpStatus() != null).
                        filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                ;
                empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                        .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                Map<String, List<EmpHistory>> empStatusMap = empHistoryInfos.stream().filter(s -> s.getEmpStatus() != null).
                        collect(Collectors.groupingBy(EmpHistory::getEmpStatus));
                for (String empStatus : empStatusMap.keySet()) {
                    List<EmpHistory> employees = empStatusMap.get(empStatus);
                    for (SysDictData empStatusDicDataInfo : empStatusDicDataInfos) {
                        if(empStatusDicDataInfo.getDictValue().equals(empStatus)){
                            empStatus = empStatusDicDataInfo.getDictLabel();
                        }
                    }
                    DeptFlowInfoVO deptFlowInfo = new DeptFlowInfoVO();
                    deptFlowInfo.setType(empStatus);
                    Map<Long, List<EmpHistory>> empDeptMap = employees.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    List<DeptNumVO> deptNumInfos = new ArrayList<>();
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        deptNumInfos.add(deptNum);
                    }
                    deptFlowInfo.setDeptNumInfos(deptNumInfos);
                    result.add(deptFlowInfo);
                }
            }
        }
        return result;
    }

    /**
     * 各部门新进、离职、到龄员工占比
     *
     * @param type      1新进 2离职 3到龄
     * @param startTime
     * @param endTime
     * @return 结果
     */
    @Override
    public List<DeptNumVO> getDeptFlowTypeInfos(Date startTime, Date endTime, String type) {

        if (startTime == null || endTime == null) {
            throw new ServiceException("开始时间或结束时间不能为空");
        }
        if (startTime.after(endTime)) {
            throw new ServiceException("开始时间不能大于结束时间");
        }

        Date now = new Date();
        if (startTime.after(now)) {
            return new ArrayList<>();
        }
        if (endTime.after(now)) {
            endTime = now;
        }
        List<DeptNumVO> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String startTimeStr = dateFormat.format(startTime);
        String endTimeStr = dateFormat.format(endTime);

        String nowStr = dateFormat.format(now);
        if ("1".equals(type)) {
            if (startTimeStr.equals(endTimeStr)) {
                if (nowStr.equals(endTimeStr)) {
                    List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(startTimeStr).stream().filter(s -> s.getEmpStatus() != null).
                            filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Map<Long, List<Employee>> empDeptMap = currentEmpInfos.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(Employee::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        deptNum.setDeptId(deptId);
                        List<Employee> employees = empDeptMap.get(deptId);
                        String empDeptName = employees.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new Employee()).getEmpDeptName();
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(employees.size());
                        result.add(deptNum);
                    }
                } else {
                    List<EmpHistory> empHistories = empHistoryMapper.findEmpInfosByCreateTime(startTime).stream().filter(s -> s.getEmpStatus() != null).
                            filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Map<Long, List<EmpHistory>> empDeptMap = empHistories.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        result.add(deptNum);
                    }
                }
            } else {
                if (nowStr.equals(endTimeStr)) {
                    List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(endTimeStr).stream().filter(s -> s.getEmpStatus() != null).
                            filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                            ).collect(Collectors.toList());
                    Calendar calendar = Calendar.getInstance(); // 创建日历对象
                    calendar.setTime(endTime); // 将结束日期设置到日历对象中
                    calendar.add(Calendar.MONTH, -1); // 减去一个月份
                    Date previousMonthDate = calendar.getTime(); // 获取上一个月的日期
                    List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, previousMonthDate).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                            ).collect(Collectors.toList());
                    empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                            .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                    List<EmpSimpleVO> empSimpleList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(currentEmpInfos)) {
                        for (Employee currentEmpInfo : currentEmpInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(currentEmpInfo, empSimple);
                            empSimpleList.add(empSimple);
                        }
                    }
                    if (!CollectionUtils.isEmpty(empHistoryInfos)) {
                        if (CollectionUtils.isEmpty(empSimpleList)) {
                            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                                EmpSimpleVO empSimple = new EmpSimpleVO();
                                BeanUtils.copyProperties(empHistoryInfo, empSimple);
                                empSimpleList.add(empSimple);
                            }
                        } else {
                            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                                EmpSimpleVO empSimple = new EmpSimpleVO();
                                BeanUtils.copyProperties(empHistoryInfo, empSimple);
                                if (empSimple.getEmpId() != null) {
                                    if (!empSimpleList.contains(empSimple)) {
                                        empSimpleList.add(empSimple);
                                    }
                                }
                            }
                        }
                    }
                    empSimpleList = empSimpleList.stream().distinct().collect(Collectors.toList());
                    Map<Long, List<EmpSimpleVO>> deptMap = empSimpleList.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpSimpleVO::getEmpDeptId));
                    for (Long deptId : deptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpSimpleVO> empSimples = deptMap.get(deptId);
                        String empDeptName = empSimples.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpSimpleVO()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empSimples.size());
                        result.add(deptNum);
                    }

                } else {
                    List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, endTime).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())
                            ).collect(Collectors.toList());
                    empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                            .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                    Map<Long, List<EmpHistory>> empDeptMap = empHistoryInfos.stream().filter(s -> s.getEmpDeptId() != null).collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        result.add(deptNum);
                    }
                }
            }
        }
        if ("2".equals(type)) {
            if (startTimeStr.equals(endTimeStr)) {
                if (nowStr.equals(endTimeStr)) {
                    List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(startTimeStr).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus()) ||
                                    EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Map<Long, List<Employee>> empDeptMap = currentEmpInfos.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(Employee::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        deptNum.setDeptId(deptId);
                        List<Employee> employees = empDeptMap.get(deptId);
                        String empDeptName = employees.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new Employee()).getEmpDeptName();
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(employees.size());
                        result.add(deptNum);
                    }
                } else {
                    List<EmpHistory> empHistories = empHistoryMapper.findEmpInfosByCreateTime(startTime).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus()) ||
                                    EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Map<Long, List<EmpHistory>> empDeptMap = empHistories.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        result.add(deptNum);
                    }
                }
            } else {
                if (nowStr.equals(endTimeStr)) {
                    List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(endTimeStr).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                    || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Calendar calendar = Calendar.getInstance(); // 创建日历对象
                    calendar.setTime(endTime); // 将结束日期设置到日历对象中
                    calendar.add(Calendar.MONTH, -1); // 减去一个月份
                    Date previousMonthDate = calendar.getTime(); // 获取上一个月的日期
                    List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, previousMonthDate).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                    || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                            .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                    List<EmpSimpleVO> empSimpleList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(currentEmpInfos)) {
                        for (Employee currentEmpInfo : currentEmpInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(currentEmpInfo, empSimple);
                            empSimpleList.add(empSimple);
                        }
                    }
                    if (!CollectionUtils.isEmpty(empHistoryInfos)) {
                        if (CollectionUtils.isEmpty(empSimpleList)) {
                            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                                EmpSimpleVO empSimple = new EmpSimpleVO();
                                BeanUtils.copyProperties(empHistoryInfo, empSimple);
                                empSimpleList.add(empSimple);
                            }
                        } else {
                            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                                EmpSimpleVO empSimple = new EmpSimpleVO();
                                BeanUtils.copyProperties(empHistoryInfo, empSimple);
                                if (empSimple.getEmpId() != null) {
                                    if (!empSimpleList.contains(empSimple)) {
                                        empSimpleList.add(empSimple);
                                    }
                                }
                            }
                        }
                    }
                    empSimpleList = empSimpleList.stream().distinct().collect(Collectors.toList());
                    Map<Long, List<EmpSimpleVO>> deptMap = empSimpleList.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpSimpleVO::getEmpDeptId));
                    for (Long deptId : deptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpSimpleVO> empSimples = deptMap.get(deptId);
                        String empDeptName = empSimples.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpSimpleVO()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empSimples.size());
                        result.add(deptNum);
                    }

                } else {
                    List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, endTime).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                    || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                            .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                    Map<Long, List<EmpHistory>> empDeptMap = empHistoryInfos.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        result.add(deptNum);
                    }
                }
            }
        }
        if ("3".equals(type)) {
            if (startTimeStr.equals(endTimeStr)) {
                if (nowStr.equals(endTimeStr)) {
                    List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(startTimeStr).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Map<Long, List<Employee>> empDeptMap = currentEmpInfos.stream().filter(s -> s.getEmpDeptId() != null).collect(Collectors.groupingBy(Employee::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        deptNum.setDeptId(deptId);
                        List<Employee> employees = empDeptMap.get(deptId);
                        String empDeptName = employees.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new Employee()).getEmpDeptName();
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(employees.size());
                        result.add(deptNum);
                    }
                } else {
                    List<EmpHistory> empHistories = empHistoryMapper.findEmpInfosByCreateTime(startTime).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
                    Map<Long, List<EmpHistory>> empDeptMap = empHistories.stream().filter(s -> s.getEmpDeptId() != null).collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        result.add(deptNum);
                    }
                }
            } else {
                if (nowStr.equals(endTimeStr)) {
                    List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(endTimeStr).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())
                            ).collect(Collectors.toList());
                    Calendar calendar = Calendar.getInstance(); // 创建日历对象
                    calendar.setTime(endTime); // 将结束日期设置到日历对象中
                    calendar.add(Calendar.MONTH, -1); // 减去一个月份
                    Date previousMonthDate = calendar.getTime(); // 获取上一个月的日期
                    List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, previousMonthDate).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())
                            ).collect(Collectors.toList());
                    empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                            .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                    List<EmpSimpleVO> empSimpleList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(currentEmpInfos)) {
                        for (Employee currentEmpInfo : currentEmpInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(currentEmpInfo, empSimple);
                            empSimpleList.add(empSimple);
                        }
                    }
                    if (!CollectionUtils.isEmpty(empHistoryInfos)) {
                        if (CollectionUtils.isEmpty(empSimpleList)) {
                            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                                EmpSimpleVO empSimple = new EmpSimpleVO();
                                BeanUtils.copyProperties(empHistoryInfo, empSimple);
                                empSimpleList.add(empSimple);
                            }
                        } else {
                            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                                EmpSimpleVO empSimple = new EmpSimpleVO();
                                BeanUtils.copyProperties(empHistoryInfo, empSimple);
                                if (empSimple.getEmpId() != null) {
                                    if (!empSimpleList.contains(empSimple)) {
                                        empSimpleList.add(empSimple);
                                    }
                                }
                            }
                        }
                    }
                    empSimpleList = empSimpleList.stream().distinct().collect(Collectors.toList());
                    Map<Long, List<EmpSimpleVO>> deptMap = empSimpleList.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpSimpleVO::getEmpDeptId));
                    for (Long deptId : deptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpSimpleVO> empSimples = deptMap.get(deptId);
                        String empDeptName = empSimples.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpSimpleVO()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empSimples.size());
                        result.add(deptNum);
                    }

                } else {
                    List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, endTime).stream().
                            filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())
                            ).collect(Collectors.toList());
                    empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                            .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                    Map<Long, List<EmpHistory>> empDeptMap = empHistoryInfos.stream().filter(s -> s.getEmpDeptId() != null).
                            collect(Collectors.groupingBy(EmpHistory::getEmpDeptId));
                    for (Long deptId : empDeptMap.keySet()) {
                        DeptNumVO deptNum = new DeptNumVO();
                        List<EmpHistory> empInfos = empDeptMap.get(deptId);
                        String empDeptName = empInfos.stream().filter(s -> s.getEmpDeptName() != null && !Objects.equals(s.getEmpDeptName(), ""))
                                .findFirst().orElse(new EmpHistory()).getEmpDeptName();
                        deptNum.setDeptId(deptId);
                        deptNum.setDeptName(empDeptName);
                        deptNum.setDeptNum(empInfos.size());
                        result.add(deptNum);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<TimeRangeVO> getDeptFlowChangeInfos(Long deptId, String type, String timeRange) {

        List<TimeRangeVO> result = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance(); // 创建日历对象
        calendar.setTime(now); // 将结束日期设置到日历对象中
        calendar.add(Calendar.MONTH, -1); // 减去一个月份
        Date previousMonthDate = calendar.getTime(); // 获取上一个月的日期
        String nowStr = dateFormat.format(now);
        List<EmpSimpleVO> empSimpleInfos = new ArrayList<>();
        List<Employee> employees = employeeMapper.findEmpInfosByCreateTime(nowStr);
        for (Employee employee : employees) {
            EmpSimpleVO empSimple = new EmpSimpleVO();
            BeanUtils.copyProperties(employee, empSimple);
            empSimple.setCreateTime(employee.getEmpCreateTime());
            empSimpleInfos.add(empSimple);
        }
        EmpHistory empHistory = empHistoryMapper.findEmpInfoWithMinCrateTime();
        if (empHistory != null) {
            Date minCreateTime = empHistory.getCreateTime();
            List<EmpHistory> empHistoryList = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(minCreateTime, previousMonthDate);
            for (EmpHistory history : empHistoryList) {
                EmpSimpleVO empSimple = new EmpSimpleVO();
                BeanUtils.copyProperties(history, empSimple);
                empSimple.setCreateTime(history.getCreateTime());
                if (empSimple.getEmpId() != null) {
                    if (!empSimpleInfos.contains(empSimple)) {
                        empSimpleInfos.add(empSimple);
                    }
                }
            }
        }
        empSimpleInfos = empSimpleInfos.stream().distinct().collect(Collectors.toList());
        if (deptId != null) {
            empSimpleInfos = empSimpleInfos.stream().filter(s -> deptId.equals(s.getEmpDeptId())).collect(Collectors.toList());
        }
        if ("1".equals(type)) {
            empSimpleInfos = empSimpleInfos.stream().filter(s -> EmpStatusEnum.NEW_POSITION.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
            result = getTimeRangeInfos(timeRange, dateFormat, empSimpleInfos);
        }
        if ("2".equals(type)) {
            empSimpleInfos = empSimpleInfos.stream().filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                    || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
            result = getTimeRangeInfos(timeRange, dateFormat, empSimpleInfos);
        }
        if ("3".equals(type)) {
            empSimpleInfos = empSimpleInfos.stream().filter(s -> EmpStatusEnum.EXPIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList());
            result = getTimeRangeInfos(timeRange, dateFormat, empSimpleInfos);
        }
        return result;
    }

    @Override
    public TableDataInfo getQuitEmpPerformInfos(Integer pageNum, Integer pageSize, DeptEmpSearchVO deptEmpSearch) {

        Date startTime = deptEmpSearch.getStartTime();
        Date endTime = deptEmpSearch.getEndTime();
        if (startTime == null || endTime == null) {
            throw new ServiceException("开始时间或结束时间不能为空");
        }
        if (startTime.after(endTime)) {
            throw new ServiceException("开始时间不能大于结束时间");
        }

        Date now = new Date();
        if (startTime.after(now)) {
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setTotal(0);
            tableDataInfo.setCode(200);
            tableDataInfo.setMsg("查询成功");
            tableDataInfo.setRows(new ArrayList<>());
            return tableDataInfo;
        }
        if (endTime.after(now)) {
            endTime = now;
        }
        List<DeptEmpPerformVO> result = new ArrayList<>();
        List<EmpSimpleVO> empSimpleList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFormatWithDay = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeStr = dateFormat.format(startTime);
        String endTimeStr = dateFormat.format(endTime);
        String nowStr = dateFormat.format(now);

        String empStatus = deptEmpSearch.getEmpStatus();
        Long deptId = deptEmpSearch.getDeptId();
        String empName = deptEmpSearch.getEmpName();

        if (startTimeStr.equals(endTimeStr)) {
            if (nowStr.equals(endTimeStr)) {
                List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(startTimeStr).stream().
                        filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).filter(s -> deptId.equals(s.getEmpDeptId())).collect(Collectors.toList());
                if (empStatus != null) {
                    currentEmpInfos = currentEmpInfos.stream().filter(s -> s.getEmpStatus().equals(empStatus)).collect(Collectors.toList());
                }
                for (Employee empInfo : currentEmpInfos) {
                    EmpSimpleVO empSimple = new EmpSimpleVO();
                    empSimple.setEmpId(empInfo.getEmpId());
                    empSimple.setEmpDeptId(empInfo.getEmpDeptId());
                    empSimple.setEmpName(empInfo.getEmpName());
                    Date quitTime = empInfo.getEmpStatusUpdateTime() != null ? empInfo.getEmpStatusUpdateTime() : empInfo.getEmpCreateTime();
                    empSimple.setCreateTimeStr(dateFormatWithDay.format(quitTime));
                    empSimpleList.add(empSimple);
                }
            } else {
                List<EmpHistory> empHistories = empHistoryMapper.findEmpInfosByCreateTime(startTime).stream().filter(s -> s.getEmpId() != null).
                        filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).filter(s -> deptId.equals(s.getEmpDeptId())).collect(Collectors.toList());
                if (empStatus != null) {
                    empHistories = empHistories.stream().filter(s -> s.getEmpStatus().equals(empStatus)).collect(Collectors.toList());
                }
                for (EmpHistory empHistory : empHistories) {
                    EmpSimpleVO empSimple = new EmpSimpleVO();
                    empSimple.setEmpId(empHistory.getEmpId());
                    empSimple.setEmpDeptId(empHistory.getEmpDeptId());
                    empSimple.setEmpName(empHistory.getEmpName());
                    empSimple.setCreateTimeStr(dateFormatWithDay.format(empHistory.getCreateTime()));
                    empSimpleList.add(empSimple);
                }
            }
        } else {
            if (nowStr.equals(endTimeStr)) {
                List<Employee> currentEmpInfos = employeeMapper.findEmpInfosByCreateTime(endTimeStr).stream().
                        filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).filter(s -> deptId.equals(s.getEmpDeptId())).collect(Collectors.toList());
                Calendar calendar = Calendar.getInstance(); // 创建日历对象
                calendar.setTime(endTime); // 将结束日期设置到日历对象中
                calendar.add(Calendar.MONTH, -1); // 减去一个月份
                Date previousMonthDate = calendar.getTime(); // 获取上一个月的日期
                List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, previousMonthDate).stream().
                        filter(s -> s.getEmpId() != null).
                        filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).filter(s -> deptId.equals(s.getEmpDeptId())
                        ).collect(Collectors.toList());
                empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                        .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                if (empStatus != null) {
                    currentEmpInfos = currentEmpInfos.stream().filter(s -> empStatus.equals(s.getEmpStatus())).collect(Collectors.toList());
                    empHistoryInfos = empHistoryInfos.stream().filter(s -> empStatus.equals(s.getEmpStatus())).collect(Collectors.toList());
                }
                if (!CollectionUtils.isEmpty(currentEmpInfos)) {
                    for (Employee currentEmpInfo : currentEmpInfos) {
                        EmpSimpleVO empSimple = new EmpSimpleVO();
                        BeanUtils.copyProperties(currentEmpInfo, empSimple);
                        Date quitTime = currentEmpInfo.getEmpStatusUpdateTime() != null ?
                                currentEmpInfo.getEmpStatusUpdateTime() : currentEmpInfo.getEmpCreateTime();
                        empSimple.setCreateTimeStr(dateFormatWithDay.format(quitTime));
                        empSimpleList.add(empSimple);
                    }
                }
                if (!CollectionUtils.isEmpty(empHistoryInfos)) {
                    if (CollectionUtils.isEmpty(empSimpleList)) {
                        for (EmpHistory empHistoryInfo : empHistoryInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(empHistoryInfo, empSimple);
                            empSimple.setCreateTimeStr(dateFormatWithDay.format(empHistoryInfo.getCreateTime()));
                            empSimpleList.add(empSimple);
                        }
                    } else {
                        for (EmpHistory empHistoryInfo : empHistoryInfos) {
                            EmpSimpleVO empSimple = new EmpSimpleVO();
                            BeanUtils.copyProperties(empHistoryInfo, empSimple);
                            empSimple.setCreateTimeStr(dateFormatWithDay.format(empHistoryInfo.getCreateTime()));
                            if (empSimple.getEmpId() != null) {
                                if (!empSimpleList.contains(empSimple)) {
                                    empSimpleList.add(empSimple);
                                }
                            }
                        }
                    }
                }
                empSimpleList = empSimpleList.stream().distinct().collect(Collectors.toList());
            } else {
                List<EmpHistory> empHistoryInfos = empHistoryMapper.findEmpInfosByTimeRangeForCreateTime(startTime, endTime).stream().filter(s -> s.getEmpId() != null).
                        filter(s -> s.getEmpStatus() != null).filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                                || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).filter(s -> deptId.equals(s.getEmpDeptId())).collect(Collectors.toList());
                empHistoryInfos = new ArrayList<>(empHistoryInfos.stream()
                        .collect(HashSet<EmpHistory>::new, Set::add, Set::addAll));
                if (empStatus != null) {
                    empHistoryInfos = empHistoryInfos.stream().filter(s -> empStatus.equals(s.getEmpStatus())).collect(Collectors.toList());
                }
                for (EmpHistory empHistoryInfo : empHistoryInfos) {
                    EmpSimpleVO empSimple = new EmpSimpleVO();
                    BeanUtils.copyProperties(empHistoryInfo, empSimple);
                    empSimple.setCreateTimeStr(dateFormatWithDay.format(empHistoryInfo.getCreateTime()));
                    if (empSimple.getEmpId() != null) {
                        if (!empSimpleList.contains(empSimple)) {
                            empSimpleList.add(empSimple);
                        }
                    }
                }
            }
        }
        if (empName != null) {
            empSimpleList = empSimpleList.stream().filter(s -> s.getEmpName().contains(empName)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(empSimpleList)) {
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setTotal(0);
            tableDataInfo.setCode(200);
            tableDataInfo.setMsg("查询成功");
            tableDataInfo.setRows(new ArrayList<>());
            return tableDataInfo;
        }
        empSimpleList = empSimpleList.stream().distinct().collect(Collectors.toList());
        List<Long> empIds = empSimpleList.stream().map(EmpSimpleVO::getEmpId).collect(Collectors.toList());
        List<EmpPerformance> empPerformances = empPerformanceMapper.findInfosByEmpIds(empIds);
        Set<String> yearSet = new HashSet<>(8);
        for (EmpPerformance empPerformance : empPerformances) {
            String perfYear = empPerformance.getPerfYear();
            yearSet.add(perfYear);
        }
        Map<Long, List<EmpPerformance>> performMap = empPerformances.stream().collect(Collectors.groupingBy(EmpPerformance::getPerfEmpId));
        long orderNum = 1L;
        for (EmpSimpleVO empSimple : empSimpleList) {
            DeptEmpPerformVO deptEmpPerform = new DeptEmpPerformVO();
            deptEmpPerform.setOrderNum(orderNum++);
            deptEmpPerform.setEmpName(empSimple.getEmpName());
            deptEmpPerform.setQuitDate(empSimple.getCreateTimeStr());
            Map<String, String> oneQuarterMap = new HashMap<>(8);
            Map<String, String> twoQuarterMap = new HashMap<>(8);
            Map<String, String> threeQuarterMap = new HashMap<>(8);
            Map<String, String> fourQuarterMap = new HashMap<>(8);
            Map<String, String> annualMap = new HashMap<>(8);
            List<EmpPerformance> performances = performMap.getOrDefault(empSimple.getEmpId(), Collections.emptyList());
            for (EmpPerformance empPerformance : performances) {
                String perfCycle = empPerformance.getPerfCycle();
                if (PerfCycleEnum.ONE.getKey().equals(perfCycle)) {
                    oneQuarterMap.put(empPerformance.getPerfYear(), empPerformance.getPerfLevelValue());
                }
                if (PerfCycleEnum.TWO.getKey().equals(perfCycle)) {
                    twoQuarterMap.put(empPerformance.getPerfYear(), empPerformance.getPerfLevelValue());
                }
                if (PerfCycleEnum.THREE.getKey().equals(perfCycle)) {
                    threeQuarterMap.put(empPerformance.getPerfYear(), empPerformance.getPerfLevelValue());
                }
                if (PerfCycleEnum.FOUR.getKey().equals(perfCycle)) {
                    fourQuarterMap.put(empPerformance.getPerfYear(), empPerformance.getPerfLevelValue());
                }
                if (PerfCycleEnum.ZERO.getKey().equals(perfCycle)) {
                    annualMap.put(empPerformance.getPerfYear(), empPerformance.getPerfLevelValue());
                }
            }
            for (String performYear : yearSet) {
                if (!oneQuarterMap.containsKey(performYear)) {
                    oneQuarterMap.put(performYear, null);
                }
                if (!twoQuarterMap.containsKey(performYear)) {
                    twoQuarterMap.put(performYear, null);
                }
                if (!threeQuarterMap.containsKey(performYear)) {
                    threeQuarterMap.put(performYear, null);
                }
                if (!fourQuarterMap.containsKey(performYear)) {
                    fourQuarterMap.put(performYear, null);
                }
                if (!annualMap.containsKey(performYear)) {
                    annualMap.put(performYear, null);
                }
            }
            deptEmpPerform.setOneQuarterMap(oneQuarterMap);
            deptEmpPerform.setTwoQuarterMap(twoQuarterMap);
            deptEmpPerform.setThreeQuarterMap(threeQuarterMap);
            deptEmpPerform.setFourQuarterMap(fourQuarterMap);
            deptEmpPerform.setAnnualMap(annualMap);
            result.add(deptEmpPerform);
        }
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setTotal(result.size());
        tableDataInfo.setRows(getPageResult(result, pageSize, pageNum));
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        return tableDataInfo;
    }


    private List<DeptEmpPerformVO> getPageResult(List<DeptEmpPerformVO> result, Integer pageSize, Integer pageNum) {

        int beginIndex = 0;
        int endIndex = 0;
        beginIndex = pageSize * (pageNum - 1);
        endIndex = Math.min(pageNum * pageSize, result.size());
        if (beginIndex >= result.size()) {
            //超出范围则无数据
            result = new ArrayList<>();
        } else {
            endIndex = Math.min(result.size(), endIndex);
            result = result.subList(beginIndex, endIndex);
        }
        return result;
    }

    private List<TimeRangeVO> getTimeRangeInfos(String timeRange, SimpleDateFormat dateFormat, List<EmpSimpleVO> empSimpleInfos) {
        List<TimeRangeVO> result = new ArrayList<>();
        if ("1".equals(timeRange)) {
            for (EmpSimpleVO empSimpleInfo : empSimpleInfos) {
                Date createTime = empSimpleInfo.getCreateTime();
                String createTimeStr = dateFormat.format(createTime);
                empSimpleInfo.setCreateTimeStr(createTimeStr);
            }
            Map<String, List<EmpSimpleVO>> empSimpleMap = empSimpleInfos.stream().filter(s -> s.getCreateTimeStr() != null).
                    collect(Collectors.groupingBy(EmpSimpleVO::getCreateTimeStr));
            for (String month : empSimpleMap.keySet()) {
                TimeRangeVO timeRangeInfo = new TimeRangeVO();
                timeRangeInfo.setTimeRange(month);
                timeRangeInfo.setNum(empSimpleMap.get(month).size());
                result.add(timeRangeInfo);
            }
        }
        if ("2".equals(timeRange)) {
            dateFormat = new SimpleDateFormat("yyyy");
            for (EmpSimpleVO empSimpleInfo : empSimpleInfos) {
                Date createTime = empSimpleInfo.getCreateTime();
                String creatStr = dateFormat.format(createTime);
                empSimpleInfo.setCreateTimeStr(creatStr);
            }
            Map<String, List<EmpSimpleVO>> empSimpleMap = empSimpleInfos.stream().filter(s -> s.getCreateTimeStr() != null).
                    collect(Collectors.groupingBy(EmpSimpleVO::getCreateTimeStr));
            for (String year : empSimpleMap.keySet()) {
                TimeRangeVO timeRangeInfo = new TimeRangeVO();
                timeRangeInfo.setTimeRange(year);
                timeRangeInfo.setNum(empSimpleMap.get(year).size());
                result.add(timeRangeInfo);
            }
        }
        return result;
    }


}