package com.yuantu.labor.service;

import com.yuantu.labor.domain.LoanWorkerHistory;
import com.yuantu.labor.vo.LoanWorkerListVO;
import com.yuantu.labor.vo.LoanWorkerQueryVO;

import java.util.List;

public interface ILoanWorkerHistoryService {



    /**
     * 根据条件查询借工历史表
     *
     * @param queryVO
     * @return
     */
    List<LoanWorkerListVO> selectLoanWorkersListByWhere(LoanWorkerQueryVO queryVO);

    /**
     * 根据historyId查询借工历史数据
     *
     * @param historyId
     * @return
     */
    LoanWorkerHistory selectLoanWorkerByHistoryId(Long historyId);

    /**
     * 根据historyId更新借工历史数据
     *
     * @param loanWorker
     * @param username
     *
     * @return
     */
    Boolean updateLoanWorkerHistory(LoanWorkerHistory loanWorker, String username);

    /**
     * 根据historyIds删除借工历史数据
     *
     * @param historyIds
     *
     * @return
     */
    Boolean deleteLoanWorkerByHistoryIds(List<Long> historyIds);
}
