package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.TrainExamLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 题库Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
@Mapper
@Repository
public interface TrainExamLibraryMapper 
{

    /**
     * 查询题库列表
     * 
     * @param trainExamLibrary 题库
     * @return 题库集合
     */
    public List<TrainExamLibrary> selectTrainExamLibraryList(TrainExamLibrary trainExamLibrary);


}
