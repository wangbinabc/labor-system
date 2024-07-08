package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpTrain;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 培训记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-22
 */
@Mapper
@Repository
public interface EmpTrainMapper {

    /**
     * 查询培训记录列表
     *
     * @param empTrain 培训记录
     * @return 培训记录集合
     */
    public List<EmpTrain> selectEmpTrainList(EmpTrain empTrain);


    public List<EmpTrainResultVO> selectEmpTrainByEmpId(Integer empId);

    public List<PieChartVO> countEmpTrainNatureNumByEmpId(Integer empId);

    public List<PieChartVO> countEmpTrainPeriodByEmpId(Integer empId);

    public List<PieChartVO> countRecentYearsTrainNumByEmpId(Integer empId);

    public List<PieChartVO> countRecentYearsTrainPeriodByEmpId(Integer empId);

    /**
     * 根据员工id查询培训信息
     *
     * @param empIds
     * @return 结果
     */
    List<EmpTrain> findInfosByEmpIds(@Param("empIds") List<Long> empIds);

    List<EmpTrain> findInfosDuringOneYearByEmpIds(@Param("empIds") List<Long> empIds);

}