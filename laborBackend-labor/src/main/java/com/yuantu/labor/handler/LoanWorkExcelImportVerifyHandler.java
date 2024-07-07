package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.mapper.LoanWorkerMapper;
import com.yuantu.labor.vo.EmpImportVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.LoanWorkerSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class LoanWorkExcelImportVerifyHandler implements IExcelVerifyHandler<LoanWorkerSimpleVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

//    @Autowired
//    private PayMajorMapper payMajorMapper;
//    @Autowired
//    private PayFeeMapper feeMapper;
//    @Autowired
//    private ISysDictTypeService dictTypeService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployingUnitsMapper employingUnitsMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(LoanWorkerSimpleVO loanWorkerSimple) {
        StringJoiner joiner = new StringJoiner(",");
        //String regex = "^([\\u4E00-\\u9FA5-])+-([\\u4E00-\\u9FA5-])+$";
        String regex = "^(.)+-(.)+$";
        String loanApplyUnitDeptName = loanWorkerSimple.getLoanApplyUnitDeptName();
        if (StringUtils.isNotEmpty(loanApplyUnitDeptName)) {
            if (!loanApplyUnitDeptName.matches(regex)) {
                //joiner.add("借工单位名称-借工部门名称单元格数据不符合规范");
            } else {
                String[] loanApplySplit = loanApplyUnitDeptName.split("-");
                String applyEmpDept = loanApplySplit[1];
                Department applyDepartment = departmentMapper.findDepartmentInfoByName(applyEmpDept);
                if (applyDepartment == null) {
                    joiner.add("系统不存在该用工部门名称");
                }
                String applyEmpUnit = loanApplySplit[0];
                EmployingUnits applyUnit = employingUnitsMapper.findUnitInfoByName(applyEmpUnit);

                if (applyUnit == null) {
                    joiner.add("系统不存在该用工单位名称");
                }

                Department exitDept = departmentMapper.findDepartmentInfoByUnitAndDeptName(applyEmpUnit, applyEmpDept);
                if (exitDept == null) {
                    joiner.add(applyEmpUnit + "下" + applyEmpDept + "不存在");
                }
            }
        }
        String loanEmpName = loanWorkerSimple.getLoanEmpName();
        if(StringUtils.isNotEmpty(loanEmpName)){
            LoanWorker existLoanWork = loanWorkerMapper.findInfoByName(loanEmpName);
            if (existLoanWork != null) {
                joiner.add("借工数据中已存在该姓名");
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
