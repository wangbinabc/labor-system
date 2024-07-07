package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.vo.EmployeeNameVO;
import com.yuantu.labor.vo.TrainEmployeeNameVO;
import com.yuantu.labor.vo.TrainProjectCheckVO;
import com.yuantu.labor.vo.TrainProjectImportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class TrainProjectRecordExcelImportVerifyHandler implements IExcelVerifyHandler<EmployeeNameVO> {

    private final ThreadLocal<List<EmployeeNameVO>> threadLocal = new ThreadLocal<>();

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(EmployeeNameVO empName) {

        StringJoiner joiner = new StringJoiner(",");

        String name = empName.getName();
        if (name == null) {
            joiner.add("姓名不能为空");
        }

        Employee existEmployee = employeeMapper.findEmpInfoByEmpName(empName.getName());
        if (existEmployee == null) {
            joiner.add("员工不存在");
        }

        //验证Excel重复行
        List<EmployeeNameVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            if (e.getName().equals(empName.getName())) {
                int lineNumber = e.getRowNum() + 1;
                joiner.add("姓名与第" + lineNumber + "行重复");
            }
        });

        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new EmployeeNameVO(empName.getName(), empName.getRowNum()));
        threadLocal.set(threadLocalVal);

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<EmployeeNameVO>> getThreadLocal() {
        return threadLocal;
    }
}
