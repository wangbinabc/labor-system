package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.SalaryHistory;
import com.yuantu.labor.vo.ChartDataVO;
import com.yuantu.labor.vo.PieChartVO;
import com.yuantu.labor.vo.SalaryHisQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 薪级变动Mapper接口
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@Mapper
@Repository
public interface SalaryHistoryMapper {

    /**
     * 查询薪级变动列表
     *
     * @param vo 薪级变动
     * @return 薪级变动集合
     */
    public List<SalaryHistory> selectSalaryHistoryList(SalaryHisQueryVO vo);

    /**
     * 新增薪级变动
     *
     * @param salaryHistory 薪级变动
     * @return 结果
     */
    public int insertSalaryHistory(SalaryHistory salaryHistory);



    /**
     * 根据员工id查询薪资变动信息
     *
     * @param empId
     * @return 结果
     */
    List<SalaryHistory> selectInfosByEmpId(@Param("empId") Long empId);


    /**
     * 根据员工id和年月删除有关薪资变动信息
     *
     * @param empId
     * @return 结果
     */
    void removeInfosByYearmonthAndEmpId(@Param("yearMonth") String yearMonth, @Param("empId") Long empId);
}
