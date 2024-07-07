package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmpWelfareMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.EmpWelfareAddVO;
import com.yuantu.labor.vo.HealthRecordsAddVO;
import com.yuantu.labor.vo.IdCheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class HealthRecordsExcelImportVerifyHandler implements IExcelVerifyHandler<HealthRecordsAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

    @Autowired
    private EmployeeMapper employeeMapper;




    @Override
    public ExcelVerifyHandlerResult verifyHandler(HealthRecordsAddVO healthRecordsAddVO) {
        StringJoiner joiner = new StringJoiner(",");
//        Employee existEmployee = employeeMapper.findInfoByHistoryEmpIdCard(healthRecordsAddVO.getHealthEmpIdcard());
//        if (existEmployee == null) {
//            joiner.add("身份证号为:"+healthRecordsAddVO.getHealthEmpIdcard()+"的员工不存在");
//        }

        Employee existEmployee = employeeMapper.findEmpInfoByEmpName(healthRecordsAddVO.getHealthEmpName());
        if (existEmployee == null) {
            joiner.add("员工姓名为:"+healthRecordsAddVO.getHealthEmpName()+"的该员工不存在");
        }

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }

        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<IdCheckVO>> getThreadLocal() {
        return threadLocal;
    }
}
