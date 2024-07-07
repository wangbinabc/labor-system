package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.labor.domain.TrainMaterials;

/**
 * 培训项目材料Service接口
 *
 * @author ruoyi
 * @date 2023-09-18
 */
public interface ITrainMaterialsService {


    /**
     * 查询培训项目材料列表
     *
     * @param trainMaterials 培训项目材料
     * @return 培训项目材料集合
     */
    public List<TrainMaterials> selectTrainMaterialsList(TrainMaterials trainMaterials);


}