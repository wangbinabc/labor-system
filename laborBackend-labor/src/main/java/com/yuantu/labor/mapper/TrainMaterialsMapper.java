package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.TrainMaterials;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 培训项目材料Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
@Mapper
@Repository
public interface TrainMaterialsMapper 
{


    /**
     * 查询培训项目材料列表
     * 
     * @param trainMaterials 培训项目材料
     * @return 培训项目材料集合
     */
    public List<TrainMaterials> selectTrainMaterialsList(TrainMaterials trainMaterials);


}
