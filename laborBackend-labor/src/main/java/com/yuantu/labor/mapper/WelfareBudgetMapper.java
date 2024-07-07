package com.yuantu.labor.mapper;

import java.util.List;
import com.yuantu.labor.domain.WelfareBudget;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 福利保障信息执行Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
@Mapper
@Repository
public interface WelfareBudgetMapper 
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
    public int insertWelfareBudget(WelfareBudget welfareBudget);

    /**
     * 修改福利保障信息执行
     * 
     * @param welfareBudget 福利保障信息执行
     * @return 结果
     */
    public int updateWelfareBudget(WelfareBudget welfareBudget);

    /**
     * 删除福利保障信息执行
     * 
     * @param welbudId 福利保障信息执行主键
     * @return 结果
     */
    public int deleteWelfareBudgetByWelbudId(Integer welbudId);

    /**
     * 批量删除福利保障信息执行
     * 
     * @param welbudIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWelfareBudgetByWelbudIds(Integer[] welbudIds);

    public int checkWelfareBudgetUnique(WelfareBudget welfareBudget);

    public int deleteWelfareBudgetByMonth(String welbudYearMonth);

    public WelfareBudget countWelfareActual();

}
