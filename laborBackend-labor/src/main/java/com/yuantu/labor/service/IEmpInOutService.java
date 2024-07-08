package com.yuantu.labor.service;

import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.labor.vo.*;

import java.util.Date;
import java.util.List;

public interface IEmpInOutService {


    /**
     * 各部门进出人员统计
     *
     * @param startTime
     * @param endTime
     * @return 结果
     */
    List<DeptFlowInfoVO> getDeptFlowInfos(Date startTime, Date endTime);

    /**
     * 各部门新进、离职、到龄员工占比
     *
     * @param type      1新进 2离职 3到龄
     * @param startTime
     * @param endTime
     * @return 结果
     */
    List<DeptNumVO> getDeptFlowTypeInfos(Date startTime, Date endTime, String type);


    /**
     * 各部门新进、离职、到龄员工占比
     *
     * @param type      1新进 2离职 3到龄
     * @param timeRange 1 按月 2 按年
     * @param deptId
     * @return 结果
     */
    List<TimeRangeVO> getDeptFlowChangeInfos(Long deptId, String type, String timeRange);

    /**
     * 查询离职员工绩效信息
     * @param pageNum
     * @param pageSize
     * @param deptEmpSearch
     * @return 结果
     */
    TableDataInfo getQuitEmpPerformInfos(Integer pageNum, Integer pageSize, DeptEmpSearchVO deptEmpSearch);
}
