package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.TrainMaterialDir;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 材料目录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
@Mapper
@Repository
public interface TrainMaterialDirMapper 
{
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


    public List<TrainMaterialDir> selectDirAndParentById(Integer dirId);
}
