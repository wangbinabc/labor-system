package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.DeptValue;
import com.yuantu.labor.vo.DeptValueSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tzq
 * @description 针对表【dept_value(部门产值)】的数据库操作Mapper
 * @createDate 2023-12-04 10:30:09
 * @Entity generator.domain.DeptValue
 */
@Repository
public interface DeptValueMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DeptValue record);

    int insertSelective(DeptValue record);

    DeptValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeptValue record);

    int updateByPrimaryKey(DeptValue record);

    List<DeptValue> findDeptValueInfos(DeptValueSearchVO deptValue);

    void removeDeptValueInfos(@Param("deptValueIds") List<Long> deptValueIds);

    DeptValue findInfoByDeptIdAndValueYear(@Param("deptId") Long deptId, @Param("valueYear") String valueYear);
}
