package com.yuantu.labor.mapper;

import java.util.Date;
import java.util.List;

import com.yuantu.labor.domain.EmpSalary;
import com.yuantu.labor.domain.EmpSalaryItem;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工酬薪主Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Mapper
@Repository
public interface EmpSalaryMapper {


    /**
     * 查询员工酬薪主列表
     *
     * @param empSalary 员工酬薪主
     * @return 员工酬薪主集合
     */
    public List<EmpSalary> selectEmpSalaryList(EmpSalary empSalary);

    public List<EmpSalaryItem> selectEmpSalaryItemListBySalaryId(Integer salaryId);

    /**
     * 查询年度实付工资
     *
     * @param empSalary
     * @return
     */
    String sumLastYearNetPay(EmpSalary empSalary);

    /**
     * 统计一年来每个月的工资
     *
     * @param queryVO
     * @return
     */
    List<SalaryChartVO> countPastYearNetPay(EmpSalaryQueryVO queryVO);

    List<EmpSalary> findInfosByEmpIdsAndCurrentTime(@Param("empIds") List<Long> empIds, @Param("now") Date now);

    List<EmpSalary> findInfosByEmpIds(@Param("empIds") List<Long> singletonList);
}
