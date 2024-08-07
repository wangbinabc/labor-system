package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.Budget;
import com.yuantu.labor.vo.BudgetQueryVO;

/**
 * 预算主Service接口
 * 
 * @author ruoyi
 * @date 2023-10-07
 */
public interface IBudgetService 
{

    /**
     * 查询预算主列表
     * 
     * @param budget 预算主
     * @return 预算主集合
     */
    public List<Budget> selectBudgetList(Budget budget);

    /**
     * 查询匹配的预算列表（预算类别和年份）
     * @param budgetTypeId
     * @param budgetYear
     * @return
     */
    public List<Budget> selectMatchBudgetList(String budgetTypeId, Long budgetYear);

}
