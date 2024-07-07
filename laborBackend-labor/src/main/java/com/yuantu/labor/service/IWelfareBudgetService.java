package com.yuantu.labor.service;

import java.util.List;
import com.yuantu.labor.domain.WelfareBudget;

/**
 * 福利保障信息执行Service接口
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
public interface IWelfareBudgetService 
{
    /**
     * 查询福利保障信息执行
     * 
     * @param welbudId 福利保障信息执行主键
     * @return 福利保障信息执行
     */
    public WelfareBudget selectWelfareBudgetByWelbudId(Integer welbudId);

    /**
     * 查询福利保障信息执行列表
     * 
     * @param welfareBudget 福利保障信息执行
     * @return 福利保障信息执行集合
     */
    public List<WelfareBudget> selectWelfareBudgetList(WelfareBudget welfareBudget);

    /**
     * 新增福利保障信息执行
     * 
     * @param welfareBudget 福利保障信息执行
     * @return 结果
     */
    public int insertWelfareBudget(WelfareBudget welfareBudget,String username);

    /**
     * 修改福利保障信息执行
     * 
     * @param welfareBudget 福利保障信息执行
     * @return 结果
     */
    public int updateWelfareBudget(WelfareBudget welfareBudget,String username);

    /**
     * 批量删除福利保障信息执行
     * 
     * @param welbudIds 需要删除的福利保障信息执行主键集合
     * @return 结果
     */
    public int deleteWelfareBudgetByWelbudIds(Integer[] welbudIds);

    /**
     * 删除福利保障信息执行信息
     * 
     * @param welbudId 福利保障信息执行主键
     * @return 结果
     */
    public int deleteWelfareBudgetByWelbudId(Integer welbudId);

    public int checkWelfareBudgetUnique(WelfareBudget welfareBudget);

    public int deleteWelfareBudgetByMonth(String welbudYearMonth);
    public WelfareBudget getWelfareBudgetByMonth(WelfareBudget welfareBudget);


}
