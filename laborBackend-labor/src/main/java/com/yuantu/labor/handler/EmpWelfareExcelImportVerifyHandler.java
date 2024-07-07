package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmpWelfareMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.EmpWelfareAddVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.PerformanceAddVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class EmpWelfareExcelImportVerifyHandler implements IExcelVerifyHandler<EmpWelfareAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpWelfareMapper empWelfareMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(EmpWelfareAddVO empWelfareAddVO) {
        StringJoiner joiner = new StringJoiner(",");
//        if(empWelfareAddVO.getWelfareEmpIdcard() == null || empWelfareAddVO.getWelfareEmpIdcard().equals("")){
//            joiner.add("身份证号为空");
//        }else {
//            Employee existEmployee = employeeMapper.findInfoByHistoryEmpIdCard(empWelfareAddVO.getWelfareEmpIdcard());
//            if (existEmployee == null) {
//                joiner.add("身份证号为:"+empWelfareAddVO.getWelfareEmpIdcard()+"的该员工不存在");
//            }
//
//            EmpWelfare search = new EmpWelfare();
//            search.setWelfareEmpIdcard(empWelfareAddVO.getWelfareEmpIdcard());
//            List<EmpWelfare>  empWelfares = empWelfareMapper.selectEmpWelfareList(search);
//            if (empWelfares!=null && empWelfares.size() !=0){
//                joiner.add("身份证号为:"+empWelfareAddVO.getWelfareEmpIdcard()+"的该员工已有福利记录");
//            }
//        }
        Employee existEmployee = employeeMapper.findEmpInfoByEmpName(empWelfareAddVO.getWelfareEmpName());
        if (existEmployee == null) {
            joiner.add("员工姓名为:"+empWelfareAddVO.getWelfareEmpName()+"的该员工不存在");
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
