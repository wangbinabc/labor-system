package com.yuantu.labor.service;

import com.yuantu.labor.domain.DeptValue;
import com.yuantu.labor.vo.DeptValueAddVO;
import com.yuantu.labor.vo.DeptValueSearchVO;
import com.yuantu.labor.vo.DeptValueVO;

import java.util.List;

public interface IDeptValueService {

    /**
     * 查询部门产值列表
     *
     * @param deptValue
     * @return 部门产值列表
     */
    List<DeptValueVO> findDeptValueInfos(DeptValueSearchVO deptValue);

    /**
     * 查询部门产值列表
     *
     * @param deptValueId
     * @return 部门产值有关信息
     */
    DeptValueVO findDeptValueInfo(Long deptValueId);


    Long updateDeptValue(DeptValueAddVO deptValueAdd, Long userId);


    Boolean removeDeptValueInfos(List<Long> deptValueIds);

}
