package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 部门绩效Service接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
public interface IDeptPerformanceService 
{
    /**
     * 查询部门绩效
     * 
     * @param dpId 部门绩效主键
     * @return 部门绩效
     */
    public DeptPerformance selectDeptPerformanceByDpId(Long dpId);

    List<DeptPerformance> selectDeptPerformanceListByCycle(DeptPerformance deptPerformance);

    public List<DeptPerformance> selectDeptPerformanceListByScreen(DeptPerformanceScreenVO deptPerformanceScreenVO);

    /**
     * 查询部门绩效列表
     * 
     * @param deptPerformance 部门绩效
     * @return 部门绩效集合
     */
    public List<DeptPerformance> selectDeptPerformanceList(DeptPerformance deptPerformance);

    List<DeptPerformance> findExportInfos(ExportVO export);


    void exportDivide(HttpServletResponse response, ExportDivideVO export);
    /**
     * 条件查询部门绩效列表
     *
     * @param deptPerformanceParamsVO 部门绩效查询条件
     * @return 部门绩效集合
     */
    public List<DeptPerformance> selectDeptPerformanceListByParamsVO(DeptPerformanceParamsVO deptPerformanceParamsVO);



    /**
     * 查询部门年度和季度绩效列表
     *
     * @param deptPerformance 部门绩效
     * @return 部门绩效集合
     */
    public List<CountDeptPerformanceVO> countDeptPerformances(DeptPerformance  deptPerformance);

    /**
     * 新增部门绩效
     * 
     * @param deptPerformance 部门绩效
     * @return 结果
     */
    public int insertDeptPerformance(DeptPerformance  deptPerformance);

    /**
     * 修改部门绩效
     * 
     * @param deptPerformance 部门绩效
     * @return 结果
     */
    public int updateDeptPerformance(DeptPerformance deptPerformance);

    /**
     * 批量删除部门绩效
     * 
     * @param dpIds 需要删除的部门绩效主键集合
     * @return 结果
     */
    public int deleteDeptPerformanceByDpIds(Long[] dpIds);

    /**
     * 删除部门绩效信息
     * 
     * @param dpId 部门绩效主键
     * @return 结果
     */
    public int deleteDeptPerformanceByDpId(Long dpId);

    void downloadExcelTemplate(HttpServletResponse response);

    ImportResultVO uploadDeptPerformanceInfosFile(MultipartFile multipartFile, Long userId, String username);

}
