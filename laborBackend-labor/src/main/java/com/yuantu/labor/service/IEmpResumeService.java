package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpResume;
import com.yuantu.labor.vo.EmpResumeExportDivideVO;
import com.yuantu.labor.vo.EmpResumeExportVO;
import com.yuantu.labor.vo.EmpResumeVO;
import com.yuantu.labor.vo.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 履历Service接口
 *
 * @author ruoyi
 * @date 2023-09-12
 */
public interface IEmpResumeService {
    /**
     * 查询履历
     *
     * @param resuId 履历主键
     * @return 履历
     */
    public EmpResume selectEmpResumeByResuId(Integer resuId);

    /**
     * 查询履历列表
     *
     * @param empResume 履历
     * @return 履历集合
     */
    public List<EmpResume> selectEmpResumeList(EmpResume empResume);

    /**
     * 新增履历
     *
     * @param empResume 履历
     * @return 结果
     */
    public int insertEmpResume(EmpResume empResume);

    /**
     * 修改履历
     *
     * @param empResume 履历
     * @return 结果
     */
    public int updateEmpResume(EmpResume empResume);

    /**
     * 批量删除履历
     *
     * @param resuIds 需要删除的履历主键集合
     * @return 结果
     */
    public int deleteEmpResumeByResuIds(Integer[] resuIds);

    /**
     * 删除履历信息
     *
     * @param resuId 履历主键
     * @return 结果
     */
    public int deleteEmpResumeByResuId(Integer resuId);

    /**
     * 模糊查询指定类型的履历信息
     *
     * @param resumeVO
     * @return
     */
    public List<EmpResume> selectEmpResumeListByWhere(EmpResumeVO resumeVO);

    /**
     * 下载履历模板
     *
     * @param response
     * @return
     */
    void downloadTemplate(HttpServletResponse response, String type);

    /**
     * 导入履历数据
     *
     * @param loginUser
     * @param file
     * @return
     */
    ImportResultVO importEmpResumeData(MultipartFile file, LoginUser loginUser, String type);

    /**
     * 直接导出履历数据
     *
     * @param empResumeExport
     * @return
     */
    List<EmpResume> selectEmpResumeExportInfos(EmpResumeExportVO empResumeExport);

    /**
     * 表格拆分导出履历数据
     *
     * @param empResumeExportDivide
     * @return
     */
    void exportDivide(HttpServletResponse response, EmpResumeExportDivideVO empResumeExportDivide);

    /**
     * 下载履历模板
     * @param response
     * @param type
     */
    public void downloadResumeExcel(HttpServletResponse response, String type);
}
