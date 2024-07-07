package com.yuantu.labor.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.yuantu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.WelfareBudgetMapper;
import com.yuantu.labor.domain.WelfareBudget;
import com.yuantu.labor.service.IWelfareBudgetService;

import javax.xml.crypto.Data;

/**
 * 福利保障信息执行Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-06
 */
@Service
public class WelfareBudgetServiceImpl implements IWelfareBudgetService {
    @Autowired
    private WelfareBudgetMapper welfareBudgetMapper;

    /**
     * 查询福利保障信息执行
     *
     * @param welbudId 福利保障信息执行主键
     * @return 福利保障信息执行
     */
    @Override
    public WelfareBudget selectWelfareBudgetByWelbudId(Integer welbudId) {
        return welfareBudgetMapper.selectWelfareBudgetByWelbudId(welbudId);
    }

    /**
     * 查询福利保障信息执行列表
     *
     * @param welfareBudget 福利保障信息执行
     * @return 福利保障信息执行
     */
    @Override
    public List<WelfareBudget> selectWelfareBudgetList(WelfareBudget welfareBudget) {
        return welfareBudgetMapper.selectWelfareBudgetList(welfareBudget);
    }

    /**
     * 新增福利保障信息执行
     *
     * @param welfareBudget 福利保障信息执行
     * @return 结果
     */
    @Override
    public int insertWelfareBudget(WelfareBudget welfareBudget, String username) {
        welfareBudget.setCreateTime(DateUtils.getNowDate());
        welfareBudget.setCreateBy(username);
        return welfareBudgetMapper.insertWelfareBudget(welfareBudget);
    }

    /**
     * 修改福利保障信息执行
     *
     * @param welfareBudget 福利保障信息执行
     * @return 结果
     */
    @Override
    public int updateWelfareBudget(WelfareBudget welfareBudget, String username) {
        welfareBudget.setUpdateTime(DateUtils.getNowDate());
        welfareBudget.setUpdateBy(username);
        return welfareBudgetMapper.updateWelfareBudget(welfareBudget);
    }

    /**
     * 批量删除福利保障信息执行
     *
     * @param welbudIds 需要删除的福利保障信息执行主键
     * @return 结果
     */
    @Override
    public int deleteWelfareBudgetByWelbudIds(Integer[] welbudIds) {
        return welfareBudgetMapper.deleteWelfareBudgetByWelbudIds(welbudIds);
    }

    /**
     * 删除福利保障信息执行信息
     *
     * @param welbudId 福利保障信息执行主键
     * @return 结果
     */
    @Override
    public int deleteWelfareBudgetByWelbudId(Integer welbudId) {
        return welfareBudgetMapper.deleteWelfareBudgetByWelbudId(welbudId);
    }

    @Override
    public int checkWelfareBudgetUnique(WelfareBudget welfareBudget) {
        return welfareBudgetMapper.checkWelfareBudgetUnique(welfareBudget);
    }

    @Override
    public int deleteWelfareBudgetByMonth(String welbudYearMonth) {
        return welfareBudgetMapper.deleteWelfareBudgetByMonth(welbudYearMonth);
    }

    @Override
    public WelfareBudget getWelfareBudgetByMonth(WelfareBudget welfareBudget) {
        List<WelfareBudget> list = welfareBudgetMapper.selectWelfareBudgetList(welfareBudget);
        if (list != null && list.size() > 0) {
            WelfareBudget budget = list.get(0);
            WelfareBudget actual = welfareBudgetMapper.countWelfareActual();
            budget.setWelbudInsureActual(actual.getWelbudInsureActual());
            budget.setWelbudPhysicalexamActual(actual.getWelbudPhysicalexamActual());
            budget.setWelbudLaborprotectActual(actual.getWelbudLaborprotectActual());
            budget.setWelbudCanteenActual(actual.getWelbudCanteenActual());
            return budget;
        } else {
            WelfareBudget budget = new WelfareBudget();
            budget.setWelbudCanteenBudget(new BigDecimal(0));
            budget.setWelbudCanteenActual(new BigDecimal(0));
            budget.setWelbudInsureBudget(new BigDecimal(0));
            budget.setWelbudInsureActual(new BigDecimal(0));
            budget.setWelbudLaborprotectBudget(new BigDecimal(0));
            budget.setWelbudLaborprotectActual(new BigDecimal(0));
            budget.setWelbudPhysicalexamBudget(new BigDecimal(0));
            budget.setWelbudPhysicalexamActual(new BigDecimal(0));
            budget.setWelbudYearMonth(welfareBudget.getWelbudYearMonth());
            return budget;
        }

    }
}
