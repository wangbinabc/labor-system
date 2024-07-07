package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.mapper.EmpAttendanceMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.LoanWorkerMapper;
import com.yuantu.labor.vo.AttendanceAddVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.labor.vo.LaborDateVO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
@Component
public class AttendanceExcelImportVerifyHandler implements IExcelVerifyHandler<AttendanceAddVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

    private Integer flag;

    private List<LaborDateVO> laborDateInfos;

    private List<String> existEmpNames;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private LoanWorkerMapper loanWorkerMapper;
    @Autowired
    EmpAttendanceMapper empAttendanceMapper;


    private static String date_regex = "^(?:19|20)\\d\\d\\/(?:0?[1-9]|1[0-2])\\/(?:0?[1-9]|[12][0-9]|3[01])$";

    @Override
    public ExcelVerifyHandlerResult verifyHandler(AttendanceAddVO attendanceAddVO) {
        StringJoiner joiner = new StringJoiner(",");

        String attendEmpName = attendanceAddVO.getAttendEmpName();
        if (StringUtils.isNotEmpty(attendEmpName)) {
            if (flag == 1) {
                //  Employee existEmployee = employeeMapper.findEmpInfoByEmpName(attendanceAddVO.getAttendEmpName());

                if (!existEmpNames.contains(attendEmpName)) {
                    joiner.add("姓名为:" + attendanceAddVO.getAttendEmpName() + "在人员数据中不存在");
                }
            } else {
                //LoanWorker loanWorker = loanWorkerMapper.findInfoByName(attendanceAddVO.getAttendEmpName());
                if (!existEmpNames.contains(attendEmpName)) {
                    joiner.add("姓名为:" + attendanceAddVO.getAttendEmpName() + "在借工数据中不存在");
                }
            }
        }

        String attendRecordDate = attendanceAddVO.getAttendRecordDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        if (StringUtils.isNotEmpty(attendRecordDate)) {
            if (attendRecordDate.matches(date_regex)) {
//                Date parse = null;
//                try {
//                    parse = new SimpleDateFormat("yyyy/MM/dd").parse(attendRecordDate);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                EmpAttendance search = new EmpAttendance();
//                search.setAttendEmpName(attendanceAddVO.getAttendEmpName());
//                search.setAttendRecordDate(parse);
//                search.setFlag(flag);

                //   List<EmpAttendance> list = empAttendanceMapper.selectEmpAttendanceList(search);
                try {
                    Date formateDate = dateFormat.parse(attendRecordDate);
                    attendRecordDate = dateFormat.format(formateDate);
                    String finalAttendRecordDate = attendRecordDate;
                    List<LaborDateVO> list = laborDateInfos.stream().filter(s -> {
                        String name = s.getName();
                        String recordDate = s.getRecordDate();
                        return name.equals(attendanceAddVO.getAttendEmpName()) && recordDate.equals(finalAttendRecordDate);
                    }).collect(Collectors.toList());
                    if (list.size() != 0) {
                        joiner.add(attendanceAddVO.getAttendEmpName() + "本日已有考勤记录");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                joiner.add("考勤日期单元格格式错误");
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
