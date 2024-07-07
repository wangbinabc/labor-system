package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.domain.ProvicePerformance;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.ImportResultVO;
import com.yuantu.labor.vo.ProvicePerformanceScreenVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 省公司考核Service接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
public interface IProvicePerformanceService 
{
    /**
     * 查询省公司考核
     * 
     * @param ppId 省公司考核主键
     * @return 省公司考核
     */
    public ProvicePerformance selectProvicePerformanceByPpId(Long  ppId);

    /**
     * 查询省公司考核列表
     * 
     * @param provicePerformance 省公司考核
     * @return 省公司考核集合
     */
    public List<ProvicePerformance> selectProvicePerformanceList(ProvicePerformance  provicePerformance);

    List<ProvicePerformance> selectProvicePerformanceListByScreen(ProvicePerformanceScreenVO provicePerformanceScreenVO);



    List<ProvicePerformance> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);
    /**
     * 新增省公司考核
     * 
     * @param provicePerformance 省公司考核
     * @return 结果
     */
    public int insertProvicePerformance(ProvicePerformance provicePerformance);

    /**
     * 修改省公司考核
     * 
     * @param provicePerformance 省公司考核
     * @return 结果
     */
    public int updateProvicePerformance(ProvicePerformance provicePerformance);

    /**
     * 批量删除省公司考核
     * 
     * @param ppIds 需要删除的省公司考核主键集合
     * @return 结果
     */
    public int deleteProvicePerformanceByPpIds(Long[] ppIds);

    /**
     * 删除省公司考核信息
     * 
     * @param ppId 省公司考核主键
     * @return 结果
     */
    public int deleteProvicePerformanceByPpId(Long ppId);

    void downloadExcelTemplate(HttpServletResponse response);

    ImportResultVO uploadProvicePerformanceInfosFile(MultipartFile multipartFile, Long userId, String username);
}
