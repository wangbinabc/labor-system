package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpExpert;
import com.yuantu.labor.vo.EmpExpertQueryVO;
import com.yuantu.labor.vo.ExpertExportListVO;
import com.yuantu.labor.vo.ExpertListVO;
import com.yuantu.labor.vo.PieChartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Mapper
@Repository
public interface EmpExpertMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param expertId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public EmpExpert selectEmpExpertByExpertId(Integer expertId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param empExpert 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EmpExpert> selectEmpExpertList(EmpExpert empExpert);

    /**
     * 新增【请填写功能名称】
     *
     * @param empExpert 【请填写功能名称】
     * @return 结果
     */
    public int insertEmpExpert(EmpExpert empExpert);

    /**
     * 修改【请填写功能名称】
     *
     * @param empExpert 【请填写功能名称】
     * @return 结果
     */
    public int updateEmpExpert(EmpExpert empExpert);

    /**
     * 删除【请填写功能名称】
     *
     * @param expertId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteEmpExpertByExpertId(Integer expertId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param expertIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpExpertByExpertIds(Integer[] expertIds);

    /**
     * 根据员工名称或身份证查询专家信息
     *
     * @param empName
     * @return
     */
    public List<ExpertListVO> selectEmpExpertListByWhere(EmpExpertQueryVO queryVO);

    /**
     * 统计每个部分专家的人数
     *
     * @return
     */
    public List<PieChartVO> countByDept();

    /**
     * 统计各个称号专家的人数
     *
     * @return
     */
    public List<PieChartVO> countByTitle();

    /**
     * 批量插入专家信息
     *
     * @param empExperts
     * @return
     */
    void batchInsertEmpExperts(@Param("empExports") List<EmpExpert> empExperts);

    /**
     * 查询有关专家信息
     *
     * @param empIds
     * @return
     */
    List<ExpertExportListVO> findExpertInfoByEmpIds(@Param("empIds") List<Long> empIds);
}
