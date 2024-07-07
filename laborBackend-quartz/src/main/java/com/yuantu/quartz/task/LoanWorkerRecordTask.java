package com.yuantu.quartz.task;


import com.yuantu.labor.domain.*;
import com.yuantu.labor.mapper.EmpHistoryMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.LoanWorkerHistoryMapper;
import com.yuantu.labor.mapper.LoanWorkerMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("loanWorkerRecordTask")
public class LoanWorkerRecordTask {


    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    @Autowired
    private LoanWorkerHistoryMapper loanWorkerHistoryMapper;



    public void saveEmployeeRecord() {

        List<LoanWorker> loanWorkers = loanWorkerMapper.findCurrentInfos();
        if (CollectionUtils.isEmpty(loanWorkers)) {
            return;
        }
        List<LoanWorkerHistory> loanWorkerHistories = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date now = new Date();
        String dateStr = dateFormat.format(now);
        for (LoanWorker loanWorker : loanWorkers) {
            LoanWorkerHistory loanWorkerHistory = new LoanWorkerHistory();
            loanWorkerHistory.setHistoryYearMonth(dateStr);
            BeanUtils.copyProperties(loanWorker, loanWorkerHistory);
            loanWorkerHistory.setCreateTime(now);
            loanWorkerHistory.setDisabled(false);
            loanWorkerHistories.add(loanWorkerHistory);
        }
        loanWorkerHistoryMapper.removeCurrentMonthHistory(dateStr);
        loanWorkerHistoryMapper.batchInsertHistoryInfos(loanWorkerHistories);
        System.out.println("任务执行结束");
    }


}
