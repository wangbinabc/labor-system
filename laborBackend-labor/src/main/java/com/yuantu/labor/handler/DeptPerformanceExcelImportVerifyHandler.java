package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.vo.DeptPerformanceAddVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.PerformanceAddVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class DeptPerformanceExcelImportVerifyHandler implements IExcelVerifyHandler<DeptPerformanceAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();


    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(DeptPerformanceAddVO deptPerformanceAddVO) {
        StringJoiner joiner = new StringJoiner(",");


        Department existDepartment = departmentMapper.findDepartmentInfoByUnitAndDeptName(deptPerformanceAddVO.getUnitName(), deptPerformanceAddVO.getDpDeptName());
        if (existDepartment == null) {
            joiner.add(deptPerformanceAddVO.getDpDeptName() + ":此部门不存在");
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
