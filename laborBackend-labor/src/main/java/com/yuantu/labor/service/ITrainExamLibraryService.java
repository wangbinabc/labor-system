package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.TrainExamLibrary;

/**
 * 题库Service接口
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
public interface ITrainExamLibraryService 
{


    /**
     * 查询题库列表
     * 
     * @param trainExamLibrary 题库
     * @return 题库集合
     */
    public List<TrainExamLibrary> selectTrainExamLibraryList(TrainExamLibrary trainExamLibrary);


}
