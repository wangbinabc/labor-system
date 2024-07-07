package com.yuantu.labor.service;

import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.domain.EmpResume;
import com.yuantu.labor.vo.*;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public interface IEmpInfoService {


    /**
     * 查询人员基本信息，出勤情况和培训信息
     *
     * @param empId
     * @return 结果
     */
    EmpBaseInfoVO getEmpBaseInfos(Long empId);

    /**
     * 薪酬情况分析
     *
     * @param empId
     * @return 结果
     */
    EmpSalaryConditionVO getEmpSalaryInfos(Long empId);

    /**
     * 绩效情况分析
     *
     * @param empId
     * @return 结果
     */
    EmpPerformConditionVO getEmpPerformInfos(Long empId);

    /**
     * 胜任力情况分析
     *
     * @param empId
     * @return 结果
     */
    AbilityVO getAbilityInfos(Long empId);


    /**
     * 资格证书信息
     *
     * @param empId
     * @return 结果
     */
    List<EmpCredentialSimpleVO> getEmpPaperInfos(Long empId);

    /**
     * 履历信息
     *
     * @param empId
     * @return 结果
     */
    List<EmpResume> getEmpResumeInfos(Long empId);


    /**
     * 个人画像绩效
     */
    public EmpPerformGeneralVO countPersonalPerformance(EmpPerformance search);

    /**
     * 考勤概览
     *
     * @param empId 员工id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 员工考勤概览数据
     */
    EmpAttendanceOverviewVO getEmpAttendanceOverview(Long empId, Date startTime, Date endTime);

    /**
     * 考勤详情
     *
     * @param empId 员工id
     * @param time 时间（年月）
     * @return 员工考勤详情数据
     */
    List<EmpAttendanceDetailVO> getEmpAttendanceDetail(Long empId, Date time);

    /**
     * 考勤统计
     *
     * @param empId 员工id
     * @return 员工考勤统计数据
     */
    EmpAttendanceStatisticsVO getEmpAttendanceStatistics(Long empId);

}
