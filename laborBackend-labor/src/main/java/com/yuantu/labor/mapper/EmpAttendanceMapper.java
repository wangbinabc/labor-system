package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 考勤Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Mapper
@Repository
public interface EmpAttendanceMapper {
    /**
     * 查询考勤
     *
     * @param attendId 考勤主键
     * @return 考勤
     */
    public EmpAttendance selectEmpAttendanceByAttendId(Long attendId);

    /**
     * 查询考勤效能
     *
     * @return 考勤
     */
    public List<EmpEffectivenessVO> selectEmpAttendanceEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO);


    public List<DeptEffectivenessVO> selectDeptAttendanceEffectivenessList(DeptEffectivenessSearchVO deptEffectivenessSearchVO);

    /**
     * 查询考勤列表
     *
     * @param empAttendanceQueryParamsVO
     * @return 考勤集合
     */
    public List<EmpAttendance> selectEmpAttendanceListByYearMonth(EmpAttendanceQueryParamsVO empAttendanceQueryParamsVO);

    /**
     * 查询考勤列表
     *
     * @param empAttendance 考勤
     * @return 考勤集合
     */
    public List<EmpAttendance> selectEmpAttendanceList(EmpAttendance empAttendance);

    public List<EmpAttendance> selectEmpAttendanceListByScreen(@Param("empAttend") EmpAttendanceScreenVO empAttendanceScreenVO,
                                                               @Param("attendIds") List<Long> attendIds,
                                                               @Param("query") Integer query);


    public List<AttendanceYearVO> countEmpAttendanceByAttendanceYear(String year);

    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<EmpAttendance> findExportInfos(@Param("export") ExportVO export);


    /**
     * 新增考勤
     *
     * @param empAttendance 考勤
     * @return 结果
     */
    public int insertEmpAttendance(EmpAttendance empAttendance);

    /**
     * 修改考勤
     *
     * @param empAttendance 考勤
     * @return 结果
     */
    public int updateEmpAttendance(EmpAttendance empAttendance);

    /**
     * 删除考勤
     *
     * @param attendId 考勤主键
     * @return 结果
     */
    public int deleteEmpAttendanceByAttendId(Long attendId);

    /**
     * 批量删除考勤
     *
     * @param attendIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpAttendanceByAttendIds(Long[] attendIds);

    /**
     * 查询最近一年考勤信息
     *
     * @param empIds
     * @return 结果
     */
    List<EmpAttendance> findInfosDuringOneYearByEmpIds(@Param("empIds") List<Long> empIds);

    /**
     * 根据考勤记录时间段 查询员工考勤情况
     *
     * @param empId     员工id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 考勤记录列表
     */
    List<EmpAttendance> findEmpAttendanceByEmpIdAndAttendRecordDateBetween(@Param("empId") Long empId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据考勤记录时间 查询员工考勤情况
     *
     * @param empId 员工id
     * @param time  时间（年月）
     * @return 考勤记录列表
     */
    List<EmpAttendance> findEmpAttendanceByEmpIdAndAttendRecordDate(@Param("empId") Long empId, @Param("time") Date time);

    /**
     * 根据考勤记录时间段 查询员工考勤情况
     *
     * @param empId     员工id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 考勤记录列表
     */
    List<EmpAttendance> findEmpAttendanceByEmpIdAndAttendRecordDateBetweenByMonth(@Param("empId") Long empId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    EmpAttendance findCurrentInfo(@Param("attendEmpId") Long attendEmpId, @Param("now") Date now);

    List<EmpAttendance> findInfosByEmpIds(@Param("empIds") List<Long> empIds);


    List<EmpAttendance> findInfosByEmpIdAndRecordTime(@Param("empId") Long empId, @Param("time") Date time, @Param("flag") Integer flag);

    List<EmpAttendance> findInfosByEmpIdsAndRecordTime(@Param("empIds") List<Long> empIds, @Param("now") Date now);

    List<EmpAttendance> findInfosByEmpIdsAndRecordTimeForYear(@Param("attendIds") List<Long> attendIds, @Param("now") String now);

    List<Long> findEmpAttendIdByEmpIdsAndFlag(@Param("empIds") List<Long> empIds, @Param("flag") Integer flag);

    List<String> findNamesByEmpNames(@Param("empNames") List<String> existEmpNames);

    List<LaborDateVO> findLaborAttendInfos(@Param("employNameInfos") List<String> employNameInfos,
                                           @Param("recordDateInfos") List<String> recordDateInfos,
                                           @Param("flag") Integer flag);

    int batchInsertInfos(@Param("attendInfos") List<EmpAttendance> attendInfos);

}
