package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 部门绩效Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Mapper
@Repository
public interface DeptPerformanceMapper
{
    /**
     * 查询部门绩效
     * 
     * @param dpId 部门绩效主键
     * @return 部门绩效
     */
    public DeptPerformance selectDeptPerformanceByDpId(Long dpId);

    /**
     * 查询部门绩效列表
     * 
     * @param deptPerformance 部门绩效
     * @return 部门绩效集合
     */
    public List<DeptPerformance> selectDeptPerformanceList(DeptPerformance deptPerformance);


    public List<DeptPerformance> selectDeptPerformanceListByCycle(DeptPerformance deptPerformance);

    /**
     * 查询部门绩效效能列表
     * @return 部门绩效集合
     */
    public  List<DeptEffectivenessVO> selectDeptPerformanceEffectivenessList(DeptEffectivenessSearchVO deptEffectivenessSearchVO);

    public List<DeptPerformance> selectDeptPerformanceListByScreen(DeptPerformanceScreenVO deptPerformanceScreenVO);
    /**
     * 条件查询部门绩效列表
     *
     * @param deptPerformanceParamsVO 部门绩效查询条件
     * @return 部门绩效集合
     */
    public List<DeptPerformance> selectDeptPerformanceListByParamsVO(DeptPerformanceParamsVO deptPerformanceParamsVO);


    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<DeptPerformance> findExportInfos(@Param("export") ExportVO export);


    /**
     * 查询部门年度和季度绩效列表
     *
     * @param deptPerformance 部门绩效
     * @return 部门绩效VO集合
     */
    public List<CountDeptPerformanceVO> countDeptPerformances(DeptPerformance deptPerformance);

    /**
     * 新增部门绩效
     * 
     * @param deptPerformance 部门绩效
     * @return 结果
     */
    public int insertDeptPerformance(DeptPerformance deptPerformance);

    /**
     * 修改部门绩效
     * 
     * @param deptPerformance 部门绩效
     * @return 结果
     */
    public int updateDeptPerformance(DeptPerformance deptPerformance);

    /**
     * 删除部门绩效
     * 
     * @param dpId 部门绩效主键
     * @return 结果
     */
    public int deleteDeptPerformanceByDpId(Long dpId);

    /**
     * 批量删除部门绩效
     * 
     * @param dpIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeptPerformanceByDpIds(Long[] dpIds);
}
