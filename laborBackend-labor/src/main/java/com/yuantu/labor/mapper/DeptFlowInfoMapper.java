package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.DeptFlowInfo;

/**
* @author Tzq
* @description 针对表【dept_flow_info】的数据库操作Mapper
* @createDate 2023-10-27 13:48:23
* @Entity generator.domain.DeptFlowInfo
*/
public interface DeptFlowInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DeptFlowInfo record);

    int insertSelective(DeptFlowInfo record);

    DeptFlowInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeptFlowInfo record);

    int updateByPrimaryKey(DeptFlowInfo record);

}
