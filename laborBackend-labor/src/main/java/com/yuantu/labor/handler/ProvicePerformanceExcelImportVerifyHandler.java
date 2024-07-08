package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.vo.DeptPerformanceAddVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.ProvicePerformanceAddVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ProvicePerformanceExcelImportVerifyHandler implements IExcelVerifyHandler<ProvicePerformanceAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();



    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployingUnitsMapper employingUnitsMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ProvicePerformanceAddVO provicePerformanceAddVO) {
        StringJoiner joiner = new StringJoiner(",");

        String regex = "^(.)+-(.)+$";
        String empUnitDeptName = provicePerformanceAddVO.getPpDeptName();
        if (empUnitDeptName == null) {
            joiner.add("本单位(公司)-公司牵头部门单元格数据不能为空");
        } else {
            if (!empUnitDeptName.matches(regex)) {
                joiner.add("本单位(公司)-公司牵头部门单元格数据不符合规范");
            } else {
                String[] split = empUnitDeptName.split("-");
                String empDept = split[1];
                Department department = departmentMapper.findDepartmentInfoByName(empDept);

                if (department == null) {
                    joiner.add("系统不存在该部门名称");
                }

                String empUnit = split[0];
                EmployingUnits unit = employingUnitsMapper.findUnitInfoByName(empUnit);

                if (unit == null) {
                    joiner.add("系统不存在该单位名称");
                }

                Department existDept = departmentMapper.findDepartmentInfoByUnitAndDeptName(empUnit, empDept);
                if (existDept == null) {
                    joiner.add(empUnit + "下" + empDept + "不存在");
                }
            }
        }

        String ppUnitDeptName = provicePerformanceAddVO.getPpProvinceDeptName();
        if (ppUnitDeptName == null) {
            joiner.add("省公司部门单元格数据不能为空");
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
