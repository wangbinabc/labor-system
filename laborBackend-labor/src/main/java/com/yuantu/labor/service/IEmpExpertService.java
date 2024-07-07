package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpExpert;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2023-09-11
 */
public interface IEmpExpertService {
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
    public int insertEmpExpert(EmpExpert empExpert, String username);

    /**
     * 修改【请填写功能名称】
     *
     * @param empExpert 【请填写功能名称】
     * @return 结果
     */
    public int updateEmpExpert(EmpExpert empExpert, String username);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param expertIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteEmpExpertByExpertIds(Integer[] expertIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param expertId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteEmpExpertByExpertId(Integer expertId);

    /**
     * 根据员工名称或身份证查询专家信息
     *
     * @param queryV0
     * @return
     */
    public List<ExpertListVO> selectEmpExpertListByWhere(EmpExpertQueryVO queryV0);

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
     * 下载专家信息模板
     *
     * @param response
     * @return
     */
    void downloadTemplate(HttpServletResponse response);

    /**
     * 导入专家信息
     *
     * @param file
     * @param loginUser
     * @return
     */
    ImportResultVO importEmpExpertData(MultipartFile file, LoginUser loginUser);

    /**
     * 查询有关专家数据
     *
     * @param empExpertExport
     * @return
     */
    List<ExpertExportListVO> selectExportExportInfos(EmpExpertExportVO empExpertExport);

    /**
     * 拆分表格导出有关专家数据
     *
     * @param empExpertExportDivide
     * @return
     */
    void exportDivide(HttpServletResponse response, EmpExpertExportDivideVO empExpertExportDivide);

    public void downloadExperExcel(HttpServletResponse response);
}
