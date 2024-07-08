package com.yuantu.labor.service;

import com.yuantu.labor.domain.HealthRecords;
import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 职位变更Service接口
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
public interface IPostHistoryService 
{
    /**
     * 查询职位变更
     * 
     * @param phId 职位变更主键
     * @return 职位变更
     */
    public PostHistory selectPostHistoryByPhId(Long phId);

    /**
     * 查询职位变更列表
     * 
     * @param postHistory 职位变更
     * @return 职位变更集合
     */
    public List<PostHistory> selectPostHistoryList(PostHistory postHistory);


    List<PostHistory> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);
    /**
     * 通过人员信息与时间查询职位变更列表
     *
     * @param paramsVo 职位变更
     * @return 职位变更集合
     */
    public List<PostHistory> selectPostHistoryListByParamsVo(PostQueryParamsVo paramsVo);

    /**
     *  统计近年来职位变更信息,按年份降序排序
     *
     * @param years 近年
     * @return 职位变更统计集合
     */
    public Map countPostHistoryByYears(Long years);

    /**
     * 新增职位变更
     * 
     * @param postHistory 职位变更
     * @return 结果
     */
    public int insertPostHistory(PostHistory postHistory);

    /**
     * 修改职位变更
     * 
     * @param postHistory 职位变更
     * @return 结果
     */
    public int updatePostHistory(PostHistory postHistory);

    /**
     * 批量删除职位变更
     * 
     * @param phIds 需要删除的职位变更主键集合
     * @return 结果
     */
    public int deletePostHistoryByPhIds(Long[] phIds);

    /**
     * 删除职位变更信息
     * 
     * @param phId 职位变更主键
     * @return 结果
     */
    public int deletePostHistoryByPhId(Long phId);



     ImportResultVO uploadPostInfosFile(MultipartFile multipartFile, Long userId, String username);

    /**
     * 下载导入模板
     */
     void downloadExcelTemplate(HttpServletResponse response);


    public List<PostHistory>  selectPostHistoryListByScreen(PostHistoryScreenVO postHistoryScreenVO);
}
