package com.yuantu.labor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.BudgetMapper;
import com.yuantu.labor.domain.Budget;
import com.yuantu.labor.service.IBudgetService;

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
     * 查询所有的预算列表（无预算类别和年份）
     * 查询匹配的预算列表（预算类别和年份）
     * @param budgetTypeId
     * @param budgetYear
     * @return
     */
    @Override
    public List<Budget> selectMatchBudgetList(String budgetTypeId, Long budgetYear) {
        return budgetMapper.selectMatchBudgetList(budgetTypeId,budgetYear);
    }

    /**
     * 删除budget记录+budgetDetail
     * @param budgetId
     */
    @Override
    public void delList(Integer budgetId) {
        budgetMapper.delList(budgetId);
        budgetMapper.delDetailsList(budgetId);
    }

}
