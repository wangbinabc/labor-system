package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 借工Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Mapper
@Repository
public interface LoanWorkerMapper {
    /**
     * 查询借工
     *
     * @param loanId 借工主键
     * @return 借工
     */
    public LoanWorker selectLoanWorkerByLoanId(@Param("loanId") Long loanId);

    /**
     * 查询借工列表
     *
     * @param loanWorker 借工
     * @return 借工集合
     */
    public List<LoanWorker> selectLoanWorkerList(LoanWorker loanWorker);

    /**
     * 新增借工
     *
     * @param loanWorker 借工
     * @return 结果
     */
    public int insertLoanWorker(LoanWorker loanWorker);

    /**
     * 修改借工
     *
     * @param loanWorker 借工
     * @return 结果
     */
    public int updateLoanWorker(LoanWorker loanWorker);

    /**
     * 删除借工
     *
     * @param loanId 借工主键
     * @return 结果
     */
    public int deleteLoanWorkerByLoanId(Integer loanId);

    /**
     * 批量删除借工
     *
     * @param loanIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLoanWorkerByLoanIds(Integer[] loanIds);

    /**
     * 根据条件查询借工记录表
     *
     * @param queryVO
     * @return LoadWorkerListVO
     */
    public List<LoanWorkerListVO> selectLoanWorkersListByWhere(@Param("query") LoanWorkerQueryVO queryVO);

    /**
     * 统计每个部分借工的人数
     *
     * @return
     */
    public List<PieChartVO> countByDept();

    /**
     * 统计每个单位借工的人数
     *
     * @return
     */
    public List<PieChartVO> countByUnit();

    /**
     * 批量插入借工数据
     * @param loanWorkers
     * @return
     */
    void batchInsertInfos(@Param("loanWorkers") List<LoanWorker> loanWorkers);

    /**
     * 查询借工数据
     * @param empIds
     * @return
     */
    List<LoanWorkerExportListVO> findExportInfosLoanEmpIds(@Param("loanIds")  List<Long> loanIds);

    /**
     * 根据员工身份证号查找借工信息
     * @param idList
     * @return
     */
    List<LoanWorker> findInfosByLoanIdCards(@Param("idList") List<String> idList);

    LoanWorker findInfoByName(@Param("name") String empName);

    List<LoanWorker> findCurrentInfos();

    List<Long> findLoanIdsByParams(@Param("loanEmpName") String loanEmpName, @Param("isRelated") Integer isRelated,
                                   @Param("empCategory") String empCategory,
                                   @Param("deptId") Long deptId);

    List<LoanWorker> findInfoByNames(@Param("names") List<String> empNames);

    List<String> findNamesByEmpNames(@Param("empNames") List<String> empNames);
}
