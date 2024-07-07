package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.ResumeProjectImportVO;
import com.yuantu.labor.vo.ResumeUnitImportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ResumeUnitExcelImportVerifyHandler implements IExcelVerifyHandler<ResumeUnitImportVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();


    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(ResumeUnitImportVO resume) {

        StringJoiner joiner = new StringJoiner(",");

//        List<IdCheckVO> threadLocalVal = threadLocal.get();
//        if (threadLocalVal == null) {
//            threadLocalVal = new ArrayList<>();
//        }
//
//        threadLocalVal.forEach(e -> {
//            if (e.getIdCard().equals(loanWorkerSimple.getLoanEmpIdcard())) {
//                int lineNumber = e.getRowNum() + 1;
//                joiner.add("身份证号与第" + lineNumber + "行重复");
//            }
//
//        });
//
//        // 添加本行数据对象到ThreadLocal中
//        threadLocalVal.add(new IdCheckVO(loanWorkerSimple.getLoanEmpIdcard(), loanWorkerSimple.getRowNum()));
//        threadLocal.set(threadLocalVal);

        //String empIdcard = resume.getResuEmpIdcard();
        String empName = resume.getResuEmpName();
        if (empName != null) {
            //Employee existEmployee = employeeMapper.findInfoByHistoryEmpIdCard(empIdcard);
            Employee existEmployee = employeeMapper.findInfoByHistoryEmpName(empName);
            if (existEmployee == null) {
                joiner.add("系统中不存在此员工");
            }
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
