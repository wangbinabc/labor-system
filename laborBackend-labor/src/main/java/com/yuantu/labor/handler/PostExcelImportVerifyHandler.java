package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.mapper.PostHistoryMapper;
import com.yuantu.labor.vo.EmpImportVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.PostHistorySimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class PostExcelImportVerifyHandler implements IExcelVerifyHandler<PostHistorySimpleVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PostHistoryMapper postHistoryMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(PostHistorySimpleVO postHistorySimpleVO) {
        StringJoiner joiner = new StringJoiner(",");
        Employee existEmployee = employeeMapper.findInfoByHistoryEmpIdCard(postHistorySimpleVO.getPhEmpIdcard());
//        if (existEmployee == null) {
//            joiner.add("身份证号为:"+postHistorySimpleVO.getPhEmpIdcard()+"的该员工不存在");
//        }


        PostHistory search = new PostHistory();
      //  search.setPhEmpIdcard(postHistorySimpleVO.getPhEmpIdcard());
        search.setPhEmpName(postHistorySimpleVO.getPhEmpName());
        search.setPhAdjustDate(postHistorySimpleVO.getPhAdjustDate());
        List<PostHistory> list = postHistoryMapper.selectPostHistoryList(search);
        if (list.size() !=0) {
            joiner.add("姓名为:"+postHistorySimpleVO.getPhEmpName()+"的员工在此日期("+postHistorySimpleVO.getPhAdjustDate()+")已有调岗记录");
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
