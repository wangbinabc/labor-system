package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.EmpTrain;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.mapper.EmpTrainMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.TrainProjectMapper;
import com.yuantu.labor.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class EmpTrainExcelImportVerifyHandler implements IExcelVerifyHandler<EmpTrainImportVO> {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmpTrainMapper empTrainMapper;

    @Autowired
    private TrainProjectMapper trainProjectMapper;
    private final ThreadLocal<List<EmpTrainCheckVO>> threadLocal = new ThreadLocal<>();
    @Override
    public ExcelVerifyHandlerResult verifyHandler(EmpTrainImportVO empTrainImport) {
        StringJoiner joiner = new StringJoiner(",");

        // //验证员工姓名是否存在
        Employee existEmployee = employeeMapper.findInfoByHistoryEmpName(empTrainImport.getTrainEmpName());

        if (existEmployee == null) {
            joiner.add("该员工不存在");
        }

        //验证项目是否存在
        List<TrainProject> trainList = trainProjectMapper.selectTrainProjectByProjectName(empTrainImport.getTrainProjectName());
        if(trainList==null || trainList.isEmpty()){
            joiner.add("该培训项目不存在");
        }

        //验证Excel重复行
        List<EmpTrainCheckVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            if (e.getTrainProjectName().equals(empTrainImport.getTrainProjectName()) && e.getTrainEmpName().equals(empTrainImport.getTrainEmpName()) && e.getTrainBeginTime().equals(empTrainImport.getTrainBeginTime())) {
                int lineNumber = e.getRowNum() + 1;
                joiner.add("培训人员，项目，开始时间与第" + lineNumber + "行重复");
            }
        });

        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new EmpTrainCheckVO(empTrainImport.getTrainEmpName(), empTrainImport.getTrainProjectName() ,empTrainImport.getTrainBeginTime(),empTrainImport.getRowNum()));
        threadLocal.set(threadLocalVal);

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return  new ExcelVerifyHandlerResult(true);
    }
    public ThreadLocal<List<EmpTrainCheckVO>> getThreadLocal() {
        return threadLocal;
    }
}
