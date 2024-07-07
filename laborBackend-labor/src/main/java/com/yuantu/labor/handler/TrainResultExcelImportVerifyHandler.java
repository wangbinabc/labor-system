package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.mapper.TrainProjectMapper;
import com.yuantu.labor.vo.TrainResultCheckVO;
import com.yuantu.labor.vo.TrainResultImportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class TrainResultExcelImportVerifyHandler implements IExcelVerifyHandler<TrainResultImportVO> {
    @Autowired
    private TrainProjectMapper trainProjectMapper;
    private final ThreadLocal<List<TrainResultCheckVO>> threadLocal = new ThreadLocal<>();

    @Override
    public ExcelVerifyHandlerResult verifyHandler(TrainResultImportVO trainResultImport) {
        StringJoiner joiner = new StringJoiner(",");
        //验证项目是否存在
        if(trainResultImport.getResultProjectName()!=null && !"".equals(trainResultImport.getResultProjectName().trim())){
            List<TrainProject> trainList = trainProjectMapper.selectTrainProjectByProjectName(trainResultImport.getResultProjectName());
            if(trainList==null || trainList.isEmpty()){
                joiner.add("该培训项目不存在");
            }
        }
        //验证Excel重复行
        List<TrainResultCheckVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            if (e.getResultName().equals(trainResultImport.getResultName())) {
                int lineNumber = e.getRowNum() + 1;
                joiner.add("培训成果与第" + lineNumber + "行重复");
            }
        });

        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new TrainResultCheckVO(trainResultImport.getResultName(),trainResultImport.getRowNum()));
        threadLocal.set(threadLocalVal);

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return  new ExcelVerifyHandlerResult(true);
    }
    public ThreadLocal<List<TrainResultCheckVO>> getThreadLocal() {
        return threadLocal;
    }
}
