package com.yuantu.labor.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 员工绩效Service接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
public interface IEmpPerformanceService 
{
    /**
     * 查询员工绩效
     * 
     * @param perfId 员工绩效主键
     * @return 员工绩效
     */
     EmpPerformance selectEmpPerformanceByPerfId(Long perfId);


    public List<EmpPerformance> selectEmpPerformanceListByScreen(EmpPerformanceScreenVO empPerformanceScreenVO,
                                                                 List<Long> performIds, Integer query);
    /**
     * 查询员工绩效列表
     * 
     * @param empPerformance 员工绩效
     * @return 员工绩效集合
     */
     List<EmpPerformance> selectEmpPerformanceList(EmpPerformance empPerformance,  List<Long> performIds, Integer query);

    /**
     * 查询绩效效能
     * @return 考勤
     */
    public List<EmpEffectivenessVO> selectEmpPerformanceEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO);


    List<EmpPerformance> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);


    /**
     * 统计查询员工绩效列表
     *
     * @param empPerformance 员工绩效
     * @return 绩效统计集合
     */
     List<CountEmpPerformanceVO>  countEmpPerformanceList(EmpPerformance empPerformance);

    Map countEmpPerformanceDetails(EmpPerformance empPerformance);


    /**
     * 新增员工绩效
     * 
     * @param empPerformance 员工绩效
     * @return 结果
     */
     int insertEmpPerformance(EmpPerformance empPerformance);

    /**
     * 修改员工绩效
     * 
     * @param empPerformance 员工绩效
     * @return 结果
     */
     int updateEmpPerformance(EmpPerformance empPerformance);

    /**
     * 批量删除员工绩效
     * 
     * @param perfIds 需要删除的员工绩效主键集合
     * @return 结果
     */
     int deleteEmpPerformanceByPerfIds(Long[]  perfIds);

    /**
     * 删除员工绩效信息
     * 
     * @param perfId 员工绩效主键
     * @return 结果
     */
     int deleteEmpPerformanceByPerfId(Long  perfId);

    void downloadExcelTemplate(HttpServletResponse response);


    ImportResultVO uploadEmpPerformanceInfosFile(MultipartFile multipartFile, Long userId, String username, Integer flag);
}
