package com.yuantu.labor.service.impl;

import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.domain.LoanWorkerHistory;
import com.yuantu.labor.mapper.LoanWorkerHistoryMapper;
import com.yuantu.labor.service.ILoanWorkerHistoryService;
import com.yuantu.labor.vo.LoanWorkerListVO;
import com.yuantu.labor.vo.LoanWorkerQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class LoanWorkerHistoryServiceImpl implements ILoanWorkerHistoryService {


    @Autowired
    private LoanWorkerHistoryMapper loanWorkerHistoryMapper;


    @Override
    public List<LoanWorkerListVO> selectLoanWorkersListByWhere(LoanWorkerQueryVO queryVO) {

        List<LoanWorkerListVO> result = loanWorkerHistoryMapper.selectLoanWorkersListByWhere(queryVO);

        if (!CollectionUtils.isEmpty(result)) {
            for (LoanWorkerListVO vo : result) {
                if (StringUtils.isNotEmpty(vo.getLoanEmpIdcard())) {
                    vo.setLoanEmpIdcard(Base64.desensitize(vo.getLoanEmpIdcard()));
                }
            }
        }
        return result;
    }

    @Override
    public LoanWorkerHistory selectLoanWorkerByHistoryId(Long historyId) {


        return loanWorkerHistoryMapper.selectByPrimaryKey(historyId);
    }

    @Override
    public Boolean updateLoanWorkerHistory(LoanWorkerHistory loanWorker, String username) {

        Date now = new Date();
        loanWorker.setUpdateTime(now);
        loanWorker.setUpdateBy(username);
        loanWorkerHistoryMapper.updateByPrimaryKeySelective(loanWorker);
        return true;
    }

    @Override
    public Boolean deleteLoanWorkerByHistoryIds(List<Long> historyIds) {

        loanWorkerHistoryMapper.deleteLoanWorkerByHistoryIds(historyIds);
        return true;
    }
}
