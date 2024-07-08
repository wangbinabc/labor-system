package com.yuantu.labor.mapper;

import java.util.Date;
import java.util.List;

import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工绩效Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Mapper
@Repository
public interface EmpPerformanceMapper 
{
    /**
     * 查询员工绩效
     * 
     * @param perfId 员工绩效主键
     * @return 员工绩效
     */
    public EmpPerformance selectEmpPerformanceByPerfId(Long perfId);


    /**
     * 查询绩效效能
     * @return 考勤
     */
    public List<EmpEffectivenessVO> selectEmpPerformanceEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO);

    /**
     * 查询员工绩效列表
     * 
     * @param empPerformance 员工绩效
     * @return 员工绩效集合
     */
    public List<EmpPerformance> selectEmpPerformanceList(EmpPerformance empPerformance);




    List<EmpPerformance> selectEmpPerformanceListForSearch(@Param("perform") EmpPerformance empPerformance,
                                                           @Param("performIds") List<Long> performIds,
                                                           @Param("query") Integer query);


    public List<EmpPerformance> selectEmpPerformanceListByScreen(@Param("empPerform") EmpPerformanceScreenVO empPerformanceScreenVO,
                                                                 @Param("performIds") List<Long> performIds,
                                                                 @Param("query") Integer query);
    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<EmpPerformance> findExportInfos(@Param("export") ExportVO export);

    /**
     * 统计查询员工绩效列表
     *
     * @param empPerformance 员工绩效
     * @return 绩效统计VO集合
     */
    public List<CountEmpPerformanceVO>  countEmpPerformanceList(EmpPerformance empPerformance);

    /**
     * 新增员工绩效
     * 
     * @param empPerformance 员工绩效
     * @return 结果
     */
    public int insertEmpPerformance(EmpPerformance empPerformance);

    /**
     * 修改员工绩效
     * 
     * @param empPerformance 员工绩效
     * @return 结果
     */
    public int updateEmpPerformance(EmpPerformance empPerformance);

    /**
     * 删除员工绩效
     * 
     * @param perfId 员工绩效主键
     * @return 结果
     */
    public int deleteEmpPerformanceByPerfId(Long perfId);

    /**
     * 批量删除员工绩效
     * 
     * @param perfIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpPerformanceByPerfIds(Long[] perfIds);

    /**
     * 根据员工id查询员工绩效信息
     *
     * @param empIds
     * @return 结果
     */
    List<EmpPerformance> findInfosByEmpIds(@Param("empIds") List<Long> empIds);

    List<EmpPerformance> findInfosDuringOneYearByEmpIds(@Param("empIds") List<Long> empIds);

    List<EmpPerformance> findInfosByEmpIdsAndCreateTime(@Param("empIds") List<Long> empIds, @Param("now") Date now);

    List<Long> findEmpPerformanceIdByEmpIdsAndFlag(@Param("empIds") List<Long> empIds, @Param("flag") Integer flag);

}
