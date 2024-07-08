package com.yuantu.labor.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.labor.domain.BudgetDetail;
import com.yuantu.labor.vo.BudgetQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.BudgetMapper;
import com.yuantu.labor.domain.Budget;
import com.yuantu.labor.service.IBudgetService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预算主Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-07
 */
@Service
public class BudgetServiceImpl implements IBudgetService 
{
    @Autowired
    private BudgetMapper budgetMapper;

    /**
     * 查询预算主列表
     * 
     * @param budget 预算主
     * @return 预算主
     */
    @Override
    public List<Budget> selectBudgetList(Budget budget)
    {
        return budgetMapper.selectBudgetList(budget);
    }

    /**
     * 查询匹配的预算列表（预算类别和年份）
     * @param budgetTypeId
     * @param budgetYear
     * @return
     */
    @Override
    public List<Budget> selectMatchBudgetList(String budgetTypeId, Long budgetYear) {
        return budgetMapper.selectMatchBudgetList(budgetTypeId,budgetYear);
    }

}
