package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.CredentialsAddVO;
import com.yuantu.labor.vo.IdCheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class CredentialsExcelImportVerifyHandler implements IExcelVerifyHandler<CredentialsAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(CredentialsAddVO credentialsSimpleVO) {
        StringJoiner joiner = new StringJoiner(",");

        if (credentialsSimpleVO.getCredEmpName()==null){
            joiner.add("员工名不能为空");
        }
       //     Employee existEmployee = employeeMapper.findInfoByHistoryEmpIdCard(credentialsSimpleVO.getCredEmpIdcard());
        Employee existEmployee = employeeMapper.findEmpInfoByEmpName(credentialsSimpleVO.getCredEmpName());
            if (existEmployee == null) {
                joiner.add("员工姓名为:"+credentialsSimpleVO.getCredEmpName()+"的该员工不存在");
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
