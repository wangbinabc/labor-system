package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.TrainResult;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 培训成果Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
@Mapper
@Repository
public interface TrainResultMapper 
{

    /**
     * 查询培训成果列表
     * 
     * @param trainResult 培训成果
     * @return 培训成果集合
     */
    public List<TrainResult> selectTrainResultList(TrainResult trainResult);



    /**
     * 各部门培训成果数量统计
     * @return
     */
    public List<PieChartVO> countByDept();



}
