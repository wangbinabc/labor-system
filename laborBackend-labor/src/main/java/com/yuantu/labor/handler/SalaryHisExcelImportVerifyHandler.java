package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.SalaryHisCheckVO;
import com.yuantu.labor.vo.SalaryHisImportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
@Component
public class SalaryHisExcelImportVerifyHandler implements IExcelVerifyHandler<SalaryHisImportVO> {
    @Autowired
    private EmployeeMapper employeeMapper;
    private final ThreadLocal<List<SalaryHisCheckVO>> threadLocal = new ThreadLocal<>();

    @Override
    public ExcelVerifyHandlerResult verifyHandler(SalaryHisImportVO salaryHisImport) {
        StringJoiner joiner = new StringJoiner(",");

        //验证人员是否存在
        Employee existEmployee = employeeMapper.findInfoByHistoryEmpName(salaryHisImport.getHisEmpName());

        if (existEmployee == null) {
            joiner.add("该员工不存在");
        }
        //验证Excel重复行
        List<SalaryHisCheckVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            if (e.getHisEmpName().equals(salaryHisImport.getHisEmpName()) && e.getHisYearMonth().equals(salaryHisImport.getHisYearMonth())) {
                int lineNumber = e.getRowNum() + 1;
                joiner.add("姓名和变动年月与第" + lineNumber + "行重复");
            }
        });

        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new SalaryHisCheckVO(salaryHisImport.getHisEmpName(), salaryHisImport.getHisYearMonth(),salaryHisImport.getRowNum()));
        threadLocal.set(threadLocalVal);

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<SalaryHisCheckVO>> getThreadLocal() {
        return threadLocal;
    }
}
