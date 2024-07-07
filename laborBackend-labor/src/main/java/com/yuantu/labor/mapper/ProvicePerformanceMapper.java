package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.domain.ProvicePerformance;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.ProvicePerformanceScreenVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 省公司考核Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Mapper
@Repository
public interface ProvicePerformanceMapper 
{
    /**
     * 查询省公司考核
     * 
     * @param ppId 省公司考核主键
     * @return 省公司考核
     */
     ProvicePerformance selectProvicePerformanceByPpId(Long ppId);


    List<ProvicePerformance> selectProvicePerformanceListByScreen(ProvicePerformanceScreenVO provicePerformanceScreenVO);
    /**
     * 查询省公司考核列表
     * 
     * @param provicePerformance 省公司考核
     * @return 省公司考核集合
     */
     List<ProvicePerformance> selectProvicePerformanceList(ProvicePerformance provicePerformance);


    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<ProvicePerformance> findExportInfos(@Param("export") ExportVO export);
    /**
     * 新增省公司考核
     * 
     * @param provicePerformance 省公司考核
     * @return 结果
     */
     int insertProvicePerformance(ProvicePerformance provicePerformance);

    /**
     * 修改省公司考核
     * 
     * @param provicePerformance 省公司考核
     * @return 结果
     */
     int updateProvicePerformance(ProvicePerformance provicePerformance);

    /**
     * 删除省公司考核
     * 
     * @param ppId 省公司考核主键
     * @return 结果
     */
     int deleteProvicePerformanceByPpId(Long ppId);

    /**
     * 批量删除省公司考核
     * 
     * @param ppIds 需要删除的数据主键集合
     * @return 结果
     */
     int deleteProvicePerformanceByPpIds(Long[] ppIds);
}
