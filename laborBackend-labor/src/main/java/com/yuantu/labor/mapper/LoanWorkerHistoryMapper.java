package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.LoanWorkerHistory;
import com.yuantu.labor.vo.LoanWorkerListVO;
import com.yuantu.labor.vo.LoanWorkerQueryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Tzq
 * @description 针对表【loan_worker_history】的数据库操作Mapper
 * @createDate 2024-03-05 14:31:52
 * @Entity generator.domain.LoanWorkerHistory
 */
@Repository
public interface LoanWorkerHistoryMapper {

    int deleteByPrimaryKey(Long historyId);

    int insert(LoanWorkerHistory record);

    int insertSelective(LoanWorkerHistory record);

    LoanWorkerHistory selectByPrimaryKey(Long historyId);

    int updateByPrimaryKeySelective(LoanWorkerHistory record);

    int updateByPrimaryKey(LoanWorkerHistory record);

    List<LoanWorkerListVO> selectLoanWorkersListByWhere(@Param("query") LoanWorkerQueryVO queryVO);

    int deleteLoanWorkerByHistoryIds(@Param("list") List<Long> historyIds);

    void removeCurrentMonthHistory(@Param("now") String now);

    void batchInsertHistoryInfos(@Param("list") List<LoanWorkerHistory> loanWorkerHistories);

    String findLatestHistoryYearMonth();


}
