package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.labor.domain.TrainMaterialDir;

/**
 * 材料目录Service接口
 *
 * @author ruoyi
 * @date 2023-09-18
 */
public interface ITrainMaterialDirService {
    /**
     * 查询材料目录
     *
     * @param dirId 材料目录主键
     * @return 材料目录
     */
    public TrainMaterialDir selectTrainMaterialDirByDirId(Integer dirId);

    /**
     * 查询材料目录列表
     *
     * @param trainMaterialDir 材料目录
     * @return 材料目录集合
     */
    public List<TrainMaterialDir> selectTrainMaterialDirList(TrainMaterialDir trainMaterialDir);


    /**
     * 树状层次结构列表
     *
     * @param dirs
     * @return
     */
    public List<TrainMaterialDir> buildMenuTree(List<TrainMaterialDir> dirs);
}
