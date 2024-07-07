package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.vo.TrainProjectCheckVO;
import com.yuantu.labor.vo.TrainProjectImportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class TrainProjectExcelImportVerifyHandler implements IExcelVerifyHandler<TrainProjectImportVO> {

    private final ThreadLocal<List<TrainProjectCheckVO>> threadLocal = new ThreadLocal<>();

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployingUnitsMapper employingUnitsMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(TrainProjectImportVO projectImport) {
        StringJoiner joiner = new StringJoiner(",");
        //验证部门是否格式正确，及在数据库里是否存在
        String empUnitDeptName = projectImport.getProjectDeptName();
        String regex = "^(.)+-(.)+$";
        if (!empUnitDeptName.matches(regex)) {
            //joiner.add("单位名称-部门名称单元格数据不符合规范");
        }else{
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

        //验证Excel重复行
        List<TrainProjectCheckVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            if (e.getTrainProjectName().equals(projectImport.getProjectName())) {
                int lineNumber = e.getRowNum() + 1;
                joiner.add("培训项目名称与第" + lineNumber + "行重复");
            }
        });

        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new TrainProjectCheckVO(projectImport.getProjectName(), projectImport.getRowNum()));
        threadLocal.set(threadLocalVal);

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<TrainProjectCheckVO>> getThreadLocal() {
        return threadLocal;
    }
}
