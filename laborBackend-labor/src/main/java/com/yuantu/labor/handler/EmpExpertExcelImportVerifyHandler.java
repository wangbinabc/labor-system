package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.EmpExpertImportVO;
import com.yuantu.labor.vo.EmpResumeImportVO;
import com.yuantu.labor.vo.IdCheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Component
public class EmpExpertExcelImportVerifyHandler implements IExcelVerifyHandler<EmpExpertImportVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();


    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(EmpExpertImportVO expertImport) {

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
        Date expertGrantTime = expertImport.getExpertGrantTime();
        Date expertDismissTime = expertImport.getExpertDismissTime();
        Date now = new Date();
        if (expertGrantTime != null && expertGrantTime.after(now)) {
            joiner.add("授予时间大于当前时间");
        }

        if (expertDismissTime != null) {
            if (expertGrantTime.after(expertDismissTime)) {
                joiner.add("授予时间大于解聘时间");
            }
        }

        //String empIdcard = expertImport.getExpertEmpIdcard();
        String empName = expertImport.getExpertEmpName();
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
