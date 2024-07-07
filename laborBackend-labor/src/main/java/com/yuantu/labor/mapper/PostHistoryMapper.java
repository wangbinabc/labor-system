package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.HealthRecords;
import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.PostChangesCountVO;
import com.yuantu.labor.vo.PostHistoryScreenVO;
import com.yuantu.labor.vo.PostQueryParamsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 职位变更Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Mapper
@Repository
public interface PostHistoryMapper
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

    /**
     * 通过人员信息与时间查询职位变更列表
     *
     * @param paramsVo 职位变更
     * @return 职位变更集合
     */
    public List<PostHistory> selectPostHistoryListByParamsVo(PostQueryParamsVo paramsVo);

    public List<PostHistory>  selectPostHistoryListByScreen(PostHistoryScreenVO postHistoryScreenVO);

    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<PostHistory> findExportInfos(@Param("export") ExportVO export);

    /**
     *  统计近年来职位变更信息,按年份降序排序
     *
     * @param years 近年
     * @return 职位变更统计集合
    */
    public List<PostChangesCountVO> countPostHistoryByYears(Long years);

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
     * 删除职位变更
     * 
     * @param phId 职位变更主键
     * @return 结果
     */
    public int deletePostHistoryByPhId(Long phId);

    /**
     * 批量删除职位变更
     * 
     * @param phIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePostHistoryByPhIds(Long[] phIds);

    /**
     * 删除当日职位变更
     * @param empId
     * @return 结果
     */
    int removeCurrentDayInfo(Long empId);

}
