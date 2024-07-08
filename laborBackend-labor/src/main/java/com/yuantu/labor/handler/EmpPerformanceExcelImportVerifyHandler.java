package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;

import com.yuantu.labor.mapper.LoanWorkerMapper;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.PerformanceAddVO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Data
@Component
public class EmpPerformanceExcelImportVerifyHandler implements IExcelVerifyHandler<PerformanceAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

    private Integer flag;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(PerformanceAddVO performanceAddVO) {
        StringJoiner joiner = new StringJoiner(",");
//        Employee existEmployee = employeeMapper.findInfoByHistoryEmpIdCard(performanceAddVO.getPerfEmpIdcard());
//        if (existEmployee == null) {
//            joiner.add("身份证号为:"+performanceAddVO.getPerfEmpIdcard()+"的该员工不存在");
//        }

//        Department existDepartment = departmentMapper.findDepartmentInfoByUnitAndDeptName(performanceAddVO.getUnitName(),performanceAddVO.getPerfDeptName());
//        if (existDepartment == null) {
//            joiner.add(performanceAddVO.getPerfDeptName()+":此部门不存在");
//        }

        if (flag == 1) {
            Employee existEmployee = employeeMapper.findEmpInfoByEmpName(performanceAddVO.getPerfEmpName());
            if (existEmployee == null) {
                joiner.add("姓名为:" + performanceAddVO.getPerfEmpName() + "在人员数据中不存在");
            }
        } else {
            LoanWorker loanWorker = loanWorkerMapper.findInfoByName(performanceAddVO.getPerfEmpName());
            if (loanWorker == null) {
                joiner.add("姓名为:" + performanceAddVO.getPerfEmpName() + "在借工数据中不存在");
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
