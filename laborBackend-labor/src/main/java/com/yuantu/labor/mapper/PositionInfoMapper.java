package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.PositionInfo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Tzq
 * @description 针对表【position_info】的数据库操作Mapper
 * @createDate 2023-11-04 18:35:12
 * @Entity generator.domain.PositionInfo
 */
@Repository
public interface PositionInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PositionInfo record);

    int insertSelective(PositionInfo record);

    PositionInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PositionInfo record);

    int updateByPrimaryKey(PositionInfo record);

    PositionInfo findInfoByReferenceParams(@Param("positionName") String empPosition,
                                           @Param("positionLevel") String empPositionLevel,
                                           @Param("positionCategory") String empPositionCategory);
}
