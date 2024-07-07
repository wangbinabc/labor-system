package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

import com.yuantu.labor.vo.EmpProfitCheckVO;
import com.yuantu.labor.vo.EmpProfitImportVO;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class EmpProfitExcelImportVerifyHandler implements IExcelVerifyHandler<EmpProfitImportVO> {

    private final ThreadLocal<List<EmpProfitCheckVO>> threadLocal = new ThreadLocal<>();


    @Override
    public ExcelVerifyHandlerResult verifyHandler(EmpProfitImportVO empProfitImport) {

        StringJoiner joiner = new StringJoiner(",");

        String yearMonth = empProfitImport.getYearMonth();
        String regex = "\\d{4}-\\d{2}";
        if (yearMonth == null) {
            joiner.add("所属年月为空");
        } else {
            if (!yearMonth.matches(regex)) {
                joiner.add("所属年月格式错误");
            }
        }


        List<EmpProfitCheckVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }


        threadLocalVal.forEach(e -> {
            int lineNumber = e.getRowNum() + 1;
            if (e.getYearMonth() != null && e.getYearMonth().equals(empProfitImport.getYearMonth())) {
                joiner.add("所属年月与第" + lineNumber + "行重复");
            }
        });


        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new EmpProfitCheckVO(empProfitImport.getEmpName(), empProfitImport.getRowNum(), empProfitImport.getYearMonth()));
        threadLocal.set(threadLocalVal);


        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<EmpProfitCheckVO>> getThreadLocal() {
        return threadLocal;
    }
}
