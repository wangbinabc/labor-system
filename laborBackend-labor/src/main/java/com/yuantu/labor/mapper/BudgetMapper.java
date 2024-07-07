package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.Budget;
import com.yuantu.labor.domain.BudgetDetail;
import com.yuantu.labor.vo.BudgetQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 预算主Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-07
 */
@Mapper
@Repository
public interface BudgetMapper 
{

    /**
     * 查询预算主列表
     * 
     * @param budget 预算主
     * @return 预算主集合
     */
    public List<Budget> selectBudgetList(Budget budget);



}
