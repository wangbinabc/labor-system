package com.yuantu.labor.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.excel.EasyExcel;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.*;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.AttendanceExcelImportVerifyHandler;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEmpAttendanceService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 考勤Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Service
public class EmpAttendanceServiceImpl implements IEmpAttendanceService {
    @Autowired
    private EmpAttendanceMapper empAttendanceMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private AttendCountMapper attendCountMapper;


    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    /**
     * 查询考勤
     *
     * @param attendId 考勤主键
     * @return 考勤
     */
    @Override
    public EmpAttendance selectEmpAttendanceByAttendId(Long attendId) {
        return empAttendanceMapper.selectEmpAttendanceByAttendId(attendId);
    }

    @Override
    public List<EmpEffectivenessVO> selectEmpAttendanceEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO) {
        return empAttendanceMapper.selectEmpAttendanceEffectivenessList(empEffectivenessSearchVO);
    }

    /**
     * 查询考勤列表
     *
     * @param empAttendance 考勤
     * @return 考勤
     */
    @Override
    public List<EmpAttendanceListVO> selectEmpAttendanceList(EmpAttendance empAttendance) {
        List<EmpAttendance> empAttendanceList = empAttendanceMapper.
                selectEmpAttendanceList(empAttendance);
        List<EmpAttendanceListVO> result = new ArrayList<>();
        for (EmpAttendance attendance : empAttendanceList) {
            EmpAttendanceListVO empAttendanceInfo = new EmpAttendanceListVO();
            BeanUtils.copyProperties(attendance, empAttendanceInfo);
            result.add(empAttendanceInfo);
        }
        List<Long> empIds = result.stream().map(EmpAttendanceListVO::getAttendEmpId).distinct().collect(Collectors.toList());
        Map<Long, Employee> employeeMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(empIds)) {
            employeeMap = employeeMapper.selectEmployeeInfosByIds(empIds).stream().
                    collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
        }
        for (EmpAttendanceListVO empAttendanceInfo : result) {
            Long empId = empAttendanceInfo.getAttendEmpId();
            Employee employee = employeeMap.getOrDefault(empId, new Employee());
            //   Employee employee = employeeMapper.selectEmployeeByEmpId(empId);
            empAttendanceInfo.setDepartmentId(employee.getEmpDeptId());
            empAttendanceInfo.setDepartmentName(employee.getEmpDeptName());
            empAttendanceInfo.setEmpCategory(employee.getEmpCategory());
        }
        return result;
    }


    public List<EmpAttendance> selectEmpAttendanceListByScreen(EmpAttendanceScreenVO empAttendanceScreenVO,
                                                               List<Long> attendIds, Integer query) {
        List<EmpAttendance> attendances = empAttendanceMapper.selectEmpAttendanceListByScreen(empAttendanceScreenVO, attendIds, query);
        List<EmpAttendance> empAttends = attendances.stream().filter(s -> s.getFlag() == 1).collect(Collectors.toList());
        List<EmpAttendance> loanAttends = attendances.stream().filter(s -> s.getFlag() == 2).collect(Collectors.toList());
        List<Long> empIds = empAttends.stream().map(EmpAttendance::getAttendEmpId).collect(Collectors.toList());
        List<Long> loanIds = loanAttends.stream().map(EmpAttendance::getAttendEmpId).collect(Collectors.toList());
        Map<Long, Employee> empMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(empIds)) {
            empMap = employeeMapper.findEmpInfosByIdsWithoutDisabled(empIds).stream().collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
        }
        Map<Long, LoanWorkerExportListVO> loanWorkMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(loanIds)) {
            loanWorkMap = loanWorkerMapper.findExportInfosLoanEmpIds(loanIds).stream().
                    collect(Collectors.toMap(LoanWorkerExportListVO::getLoanId, Function.identity(), (v1, v2) -> v2));
        }
        Map<Long, Department> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        for (EmpAttendance attendance : attendances) {
            if (attendance.getFlag() == 1) {
                Employee employee = empMap.get(attendance.getAttendEmpId());
                attendance.setDepartmentId(employee.getEmpDeptId() + "");
                if (employee.getEmpDeptId() != null) {
                    Department dept = deptMap.getOrDefault(employee.getEmpDeptId(), new Department());
                    attendance.setDepartmentName(dept.getDeptName());
                }
            }
            if (attendance.getFlag() == 2) {
                LoanWorkerExportListVO loanWorker = loanWorkMap.get(attendance.getAttendEmpId());
                attendance.setDepartmentId(loanWorker.getLoanApplyDeptId() + "");
                attendance.setEmpCategory(EmpCategoryEnum.THREE.getKey());
                if (loanWorker.getLoanApplyDeptId() != null) {
                    Department dept = deptMap.getOrDefault(loanWorker.getLoanApplyDeptId(), new Department());
                    attendance.setDepartmentName(dept.getDeptName());
                }
            }
        }
        return attendances;
    }

    @Override
    public List<AttendanceYearVO> countEmpAttendanceByAttendanceYear(String year) {

        return empAttendanceMapper.countEmpAttendanceByAttendanceYear(year);
    }

    /**
     * 按年月和（身份证或姓名）统计考勤
     *
     * @return 考勤
     */
    @Override
    public List<AttendanceCountVO> countEmpsAttendanceListByParams(String nameOrIdcard, String year, String month) {

        List<AttendanceCountVO> counts = new ArrayList<>();
        List<EmpAttendance> attendances = empAttendanceMapper.selectEmpAttendanceListByYearMonth(new EmpAttendanceQueryParamsVO(nameOrIdcard, year, month));

        List<SysDictData> dictDatas = sysDictDataMapper.selectDictDataByType("attend_status");
        String attendanceData = dictDatas.get(0).getDictValue();
        String businessTripData = dictDatas.get(1).getDictValue();
        String vacationData = dictDatas.get(2).getDictValue();
        String overtimeData = dictDatas.get(3).getDictValue();

        //按日期排序
        Collections.sort(attendances, Comparator.comparing(EmpAttendance::getAttendRecordDate));
        //用于保存统计结果
        Map<AttendanceCountVO, Map<String, LinkedList>> saveMap = new HashMap<>();

        for (EmpAttendance attendance : attendances) {
            AttendanceCountVO vo = new AttendanceCountVO();
            vo.setAttendEmpIdcard(attendance.getAttendEmpIdcard());
            String status = attendance.getAttendStatus();


            if (!saveMap.containsKey(vo)) {
                //插入数据
                vo.setAttendEmpId(attendance.getAttendEmpId());
                vo.setAttendEmpName(attendance.getAttendEmpName());
                vo.setWorkingYear(year);
                vo.setWorkingMonth("-1");

                //创建统计详细map
                Map<String, LinkedList> map = new HashMap<>();

                //将考勤时间list和状态放入统计详细map
                LinkedList<Date> dates = new LinkedList<>();
                dates.add(attendance.getAttendRecordDate());

                map.put(status, dates);


                //统计连续上班天数
                LinkedList<Integer> linkedList = new LinkedList<>();
                int count = vacationData.equals(status) ? 0 : 1;//休假
                linkedList.add(count);
                map.put("continuous", linkedList);

                //将第一次的数据全部保存
                saveMap.put(vo, map);
            } else {
                Map<String, LinkedList> map = saveMap.get(vo);

                //状态key已存在,且不是3(休假)
                if (!vacationData.equals(status)) {
                    if (map.containsKey(status)) {
                        //将日期放入对应的list
                        map.get(status).add(attendance.getAttendRecordDate());
                    } else {
                        LinkedList<Date> datess = new LinkedList<Date>();
                        datess.add(attendance.getAttendRecordDate());
                        map.put(status, datess);
                    }

                    //将continuous最后一个值取出+1放回去
                    int count = (Integer) (map.get("continuous").getLast()) + 1;
                    map.get("continuous").removeLast();
                    map.get("continuous").add(count);

                } else {
                    if (map.containsKey(status)) {
                        map.get(status).add(attendance.getAttendRecordDate());
                    } else {
                        LinkedList<Date> datess = new LinkedList<Date>();
                        datess.add(attendance.getAttendRecordDate());
                        map.put(status, datess);
                    }


                    int count = 0;//休假
                    map.get("continuous").add(count);
                }
            }
        }
        //循环完毕,统计值
        for (AttendanceCountVO vo : saveMap.keySet()) {
            Map<String, LinkedList> map = saveMap.get(vo);

            vo.setMaximumConsecutiveWorkingDays(Collections.max((LinkedList<Integer>) map.get("continuous")));
            Integer attendanceDays = map.get(attendanceData) == null ? 0 : map.get(attendanceData).size();
            Integer businessTripDays = map.get(businessTripData) == null ? 0 : map.get(businessTripData).size();
            Integer overtimeDays = map.get(overtimeData) == null ? 0 : map.get(overtimeData).size();
            Integer vacationDays = map.get(vacationData) == null ? 0 : map.get(vacationData).size();
            vo.setBusinessTripDays(businessTripDays);
            vo.setAttendanceDays(attendanceDays);
            vo.setWorkingDays(attendanceDays + businessTripDays + overtimeDays);
            vo.setOvertimeDays(overtimeDays);
            vo.setVacationDays(vacationDays);

            map.remove("continuous");
            vo.setDetail(map);
            counts.add(vo);
        }
        return counts;
    }

    /**
     * 获取年月
     *
     * @return 考勤
     */
    private static Map<String, String> getYearMonth(Date date) {
        Map<String, String> map = new HashMap<>();
        map.put("year", new SimpleDateFormat("yyyy").format(date).toString());
        map.put("month", new SimpleDateFormat("MM").format(date).toString());
        map.put("yearAndmonth", new SimpleDateFormat("yyyy-MM").format(date).toString());
        return map;
    }


    public List<EmpAttendance> isChange(List<EmpAttendance> list) {
        for (EmpAttendance empAttendance : list) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empAttendance.getAttendEmpId());
            if (e == null) {
                empAttendance.setIsChange(false);
            }
        }

        return list;
    }

    @Override
    public List<EmpAttendance> findExportInfos(ExportVO export) {
        List<EmpAttendance> list = empAttendanceMapper.findExportInfos(export);
        Map<Long, String> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Department::getDeptName));
        for (EmpAttendance empAttendance : list) {
            Long empId = empAttendance.getAttendEmpId();
            if (empAttendance.getFlag() == 1) {
                Employee employee = employeeMapper.selectEmployeeByEmpId(empId);
                if (employee != null) {
                    empAttendance.setEmpCategory(employee.getEmpCategory());
                    if (employee.getEmpDeptId() != null) {
                        empAttendance.setDepartmentName(deptMap.getOrDefault(employee.getEmpDeptId(), ""));
                    }
                }
            } else {
                empAttendance.setEmpCategory(null);
                LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(empId);
                if (loanWorker != null) {
                    if (loanWorker.getLoanApplyDeptId() != null) {
                        empAttendance.setDepartmentName(deptMap.getOrDefault(loanWorker.getLoanApplyDeptId(), ""));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<EmpAttendance> exportInfos = findExportInfos(export);


        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }
        //-------------------导出不脱敏----------------------
//        List<Employee> employeeList = employeeMapper.findAllEmployees();
//        Map<Long, Employee> empMap = employeeList.stream().collect(Collectors.
//                toMap(Employee::getEmpId, Function.identity()));
//
//        for (EmpAttendance ea : exportInfos) {
//            ea.setAttendEmpIdcard((empMap.get(ea.getAttendEmpId()) != null && empMap.get(ea.getAttendEmpId()).getEmpIdcard() != null ? empMap.get(ea.getAttendEmpId()).getEmpIdcard() : "card_is_Null"));
//        }
        //----------------------------------------------------
        EmpAttendanceChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<EmpAttendance>> groupedData = exportInfos.stream()
                .collect(Collectors.groupingBy(emp -> {
                    try {
                        // 使用反射获取指定属性的值
                        Object value = emp.getClass().getMethod("get" +
                                groupByProperty.substring(0, 1).toUpperCase() +
                                groupByProperty.substring(1)).invoke(emp);
                        return value.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 处理获取属性值异常
                        return null;
                    }
                }));

        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        String fieldName = exportDivide.getFieldName();
        for (String key : groupedData.keySet()) {
            List<EmpAttendance> exportList = groupedData.get(key);

            for (EmpAttendance exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = EmpAttendance.class.getDeclaredField(fieldName);
                        ReflectionUtils.makeAccessible(field);
                        Object value = field.get(exportInfo);
                        for (SysDictData dictDataInfo : dictDataInfos) {
                            if (value.toString().equals(dictDataInfo.getDictLabel())) {
                                field.set(exportInfo, dictDataInfo.getDictValue());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        File baseDir = new File("员工考勤信息");
        for (String key : groupedData.keySet()) {
            String excelName = exportDivide.getExcelName();
            if (excelName.contains("#")) {
                excelName = excelName.replace("#", key);
            }
            File file = org.apache.commons.io.FileUtils.getFile(baseDir + File.separator + key + File.separator + excelName + ".xlsx");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<EmpAttendance> empAttendanceList = groupedData.get(key);
            //----------脱敏---------
            for (EmpAttendance empAttendance : empAttendanceList) {
                if (!StringUtils.isEmpty(empAttendance.getAttendEmpIdcard()) && !empAttendance.getAttendEmpIdcard().equals("card_is_Null")) {
                    empAttendance.setAttendEmpIdcard(Base64.desensitize(empAttendance.getAttendEmpIdcard()));
                }
            }
            ExcelUtil<EmpAttendance> util = new ExcelUtil<EmpAttendance>(EmpAttendance.class);
            util.init(empAttendanceList, "员工考勤信息", excelName, Excel.Type.EXPORT);
            util.writeSheet();
            Workbook wb = util.getWb();
            MultipartFile multipartFile = FileUtils.workbookToCommonsMultipartFile(wb, excelName + ".xlsx");
            if (multipartFile != null) {
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileUtils.writeCompressedFileToResponse(response, baseDir);
        FileUtils.deleteFolder(baseDir);
    }

    @Autowired
    private SysDictDataMapper dictDataMapper;

    private EmpAttendanceChangeDicVO changeDicValue(List<EmpAttendance> exportInfos, String fieldName) {
        Field[] declaredFields = EmpAttendance.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (EmpAttendance exportInfo : exportInfos) {
                try {
                    Field field = EmpAttendance.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpAttendanceChangeDicVO empChangeDic = new EmpAttendanceChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        EmpAttendanceChangeDicVO empChangeDic = new EmpAttendanceChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }


    /**
     * 新增考勤
     *
     * @param empAttendance 考勤
     * @return 结果
     */
    @Override
    public int insertEmpAttendance(EmpAttendance empAttendance) {
//        if (empAttendance.getAttendEmpId() != null) {
//            Date now = new Date();
//            //  EmpAttendance existAttendance = empAttendanceMapper.findCurrentInfo(empAttendance.getAttendEmpId(), now);
//            Employee e = employeeMapper.selectEmployeeByEmpId(empAttendance.getAttendEmpId());
//            empAttendance.setAttendEmpIdcard(e.getEmpIdcard());
//            empAttendance.setAttendEmpName(e.getEmpName());
//        }
//        else if (empAttendance.getAttendEmpIdcard() != null){
//            Employee e = employeeMapper.findInfoByEmpIdCard(empAttendance.getAttendEmpIdcard());
//            empAttendance.setAttendEmpId(e.getEmpId());
//        }
//        else {
//            Employee e = employeeMapper.
//                    findEmpInfoByEmpName(empAttendance.getAttendEmpName());
//            empAttendance.setAttendEmpId(e.getEmpId());
//            empAttendance.setAttendEmpIdcard(e.getEmpIdcard());
//        }
        if (empAttendance.getFlag() == 1) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empAttendance.getAttendEmpId());
            empAttendance.setAttendEmpIdcard(e.getEmpIdcard());
            empAttendance.setAttendEmpName(e.getEmpName());
        }
        if (empAttendance.getFlag() == 2) {
            LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(empAttendance.getAttendEmpId());
            empAttendance.setAttendEmpIdcard(loanWorker.getLoanEmpIdcard());
            empAttendance.setAttendEmpName(loanWorker.getLoanEmpName());
        }
        EmpAttendance search = new EmpAttendance();
        search.setAttendEmpId(empAttendance.getAttendEmpId());
        search.setAttendRecordDate(empAttendance.getAttendRecordDate());
        search.setFlag(empAttendance.getFlag());
        List<EmpAttendance> list = empAttendanceMapper.selectEmpAttendanceList(search);
        if (list.size() != 0) {
            throw new ServiceException("本日已有考勤记录");
        }
        return empAttendanceMapper.insertEmpAttendance(empAttendance);
    }

    /**
     * 修改考勤
     *
     * @param empAttendance 考勤
     * @return 结果
     */
    @Override
    public int updateEmpAttendance(EmpAttendance empAttendance) {
        //   empAttendance.setUpdateTime(DateUtils.getNowDate());
        if (empAttendance.getFlag() == 1) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empAttendance.getAttendEmpId());
            empAttendance.setAttendEmpIdcard(e.getEmpIdcard());
            empAttendance.setAttendEmpName(e.getEmpName());
        }
        if (empAttendance.getFlag() == 2) {
            LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(empAttendance.getAttendEmpId());
            empAttendance.setAttendEmpIdcard(loanWorker.getLoanEmpIdcard());
            empAttendance.setAttendEmpName(loanWorker.getLoanEmpName());
        }
        return empAttendanceMapper.updateEmpAttendance(empAttendance);
    }

    /**
     * 批量删除考勤
     *
     * @param attendIds 需要删除的考勤主键
     * @return 结果
     */
    @Override
    public int deleteEmpAttendanceByAttendIds(Long[] attendIds) {
        return empAttendanceMapper.deleteEmpAttendanceByAttendIds(attendIds);
    }

    /**
     * 删除考勤信息
     *
     * @param attendId 考勤主键
     * @return 结果
     */
    @Override
    public int deleteEmpAttendanceByAttendId(Long attendId) {
        return empAttendanceMapper.deleteEmpAttendanceByAttendId(attendId);
    }

    public void downloadExcelTemplate(HttpServletResponse response) {
//        InputStream inputStream = null;
//        try {
//            ClassPathResource classPathResource = new ClassPathResource("static/attend.xlsx");
//            inputStream = classPathResource.getInputStream();
//            ExcelUtil.downLoadExcel("post", response, WorkbookFactory.create(inputStream));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        OutputStream outputStream = null;

        try {

            List<String> statusDic = dictDataMapper.selectDictDataByType("attend_status").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

            List<String> workdayDic = dictDataMapper.selectDictDataByType("is_workday").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());


            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(3, statusDic);
            downDropMap.put(2, workdayDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap, 1);
            //   HorizontalCellStyleStrategy cellStyleStrategy = setMyCellStyle();
            List<AttendanceTemplateVO> list = new ArrayList<>();

            String fileName = URLEncoder.encode("考勤模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, AttendanceTemplateVO.class).
                    registerWriteHandler(downWriteHandler).sheet("考勤信息").doWrite(list);
//            EasyExcel.write(response.getOutputStream(), AttendanceTemplateVO.class).registerWriteHandler(new TemplateCellWriteHandler(headHeight))
//                    .registerWriteHandler(cellStyleStrategy).
//                    registerWriteHandler(downWriteHandler).sheet("考勤导入模板").doWrite(empList);
        } catch (IOException e) {
            throw new ServiceException("下载考勤模板失败");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();//刷新流：通道中数据全部输出
                    outputStream.close();//关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private FileService fileService;


    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;


    @Override
    public ImportResultVO uploadAttendanceInfosFile(MultipartFile multipartFile, Long userId, String username, Integer flag) {


        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.ELEVEN.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

//        List<MultipartFile> multipartFiles;
//        try {
//            //分割大文件
//            multipartFiles = splitLargeExcel(multipartFile, 6000);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<Future<ImportResultVO>> futures = new ArrayList<>();
//        List<ImportResultVO> importResults = new ArrayList<>();
//        for (final MultipartFile file : multipartFiles) {
//            // 将导入逻辑包装为Callable
//            Callable<ImportResultVO> importTask = () -> getImportResult(file, username, flag, now);
//            // 提交任务到线程池
//            Future<ImportResultVO> future = taskExecutor.submit(importTask);
//            futures.add(future);
//        }
//        // 等待所有任务完成
//        for (Future<ImportResultVO> future : futures) {
//            try {
//                ImportResultVO importResult = future.get();
//                importResults.add(importResult);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println("开始导入：" + new Date());
        ImportResultVO importResult = getImportResult(multipartFile, username, flag, now);
        System.out.println("结束导入：" + new Date());
        Long fileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_ATTENDANCE_INFO.getKey(), "attend", multipartFile, username).getFileId();
        fileImportRecord.setSuccessCount(importResult.getSuccessCount());
        fileImportRecord.setFailureCount(importResult.getErrorCount());
        fileImportRecord.setTotalCount(importResult.getTotalCount());
        fileImportRecord.setOriginFileId(fileId);
        fileImportRecord.setFailFileId(importResult.getFailFileId());
        fileImportRecord.setImportStatus(FileImportStatusEnum.FINISHED.getKey());
        fileImportRecordMapper.updateFileImportRecord(fileImportRecord);
        return importResult;
    }


    @Value("${ruoyi.profile}")
    private String basePath;

    private ErrorSimpleVO getErrorImportSimpleInfo(List<ImportResultVO> importResults, String userName) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");
        String nowStr = dateFormat.format(new Date());
        Boolean isExist = false;
        for (ImportResultVO importResult : importResults) {
            String failFileUrl = importResult.getFailFileUrl();
            if (failFileUrl != null) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            return null;
        }
        List<Workbook> failWorkbooks = new ArrayList<>();
        for (ImportResultVO importResult : importResults) {
            String failFileUrl = importResult.getFailFileUrl();
            if (StringUtils.isNotEmpty(failFileUrl)) {
                try {
                    InputStream inputStream = Files.newInputStream(Paths.get(basePath + failFileUrl));
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    failWorkbooks.add(workbook);
                    File file = new File(basePath + failFileUrl);
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return mergeWorkbooksAndUpload(failWorkbooks, "error_import.xlsx", nowStr, userName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ErrorSimpleVO mergeWorkbooksAndUpload(List<Workbook> failWorkbooks, String mergedFileName, String nowStr, String userName) throws IOException {
        Workbook mergedWorkbook = new XSSFWorkbook(); // 新建一个目标工作簿，默认为.xlsx格式
        for (Workbook sourceWorkbook : failWorkbooks) {
            for (int i = 0; i < sourceWorkbook.getNumberOfSheets(); i++) {
                Sheet sourceSheet = sourceWorkbook.getSheetAt(i);
                if (sourceSheet != null) {
                    Sheet destinationSheet = mergedWorkbook.createSheet(sourceSheet.getSheetName()); // 创建新的工作表
                    // 复制样式、行和单元格数据
                    copySheetContent(sourceSheet, destinationSheet);
                }
            }
            //释放资源
            sourceWorkbook.close();
        }

        MultipartFile file = FileUtils.workbookToCommonsMultipartFile(mergedWorkbook, mergedFileName + nowStr + ".xlsx");
        FileVO attendanceFile = new FileVO();
        if (file != null) {
            attendanceFile = fileService.upLoadEmpFile(FileTypeEnum.EMP_ATTENDANCE_FAIL_INFO.getKey(), "attend", file, userName);
        }
        ErrorSimpleVO errorSimple = new ErrorSimpleVO();
        errorSimple.setFailId(attendanceFile.getFileId());
        errorSimple.setFailUrl(attendanceFile.getFileUrl());
        // 关闭最终合并的工作簿
        mergedWorkbook.close();
        return errorSimple;
    }


    private void copySheetContent(Sheet sourceSheet, Sheet destinationSheet) {
        for (Row sourceRow : sourceSheet) {
            Row newRow = destinationSheet.createRow(sourceRow.getRowNum());
            for (Cell sourceCell : sourceRow) {
                Cell destCell = newRow.createCell(sourceCell.getColumnIndex());
                copyCellContent(sourceCell, destCell);
            }
        }
    }

    private void copyCellContent(Cell sourceCell, Cell destCell) {
        destCell.setCellType(sourceCell.getCellType());

        switch (sourceCell.getCellType()) {
            case STRING:
                destCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                destCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                destCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            // 其他类型的单元格也做类似处理...
            default:
                break;
        }

        // 复制样式（如果有需要的话）
        if (sourceCell.getCellStyle() != null) {
            destCell.setCellStyle(cloneCellStyle(sourceCell.getCellStyle(), destCell.getSheet().getWorkbook()));
        }
    }

    // 深度克隆单元格样式
    private CellStyle cloneCellStyle(CellStyle style, Workbook workbook) {
        CellStyle clonedStyle = workbook.createCellStyle();
        clonedStyle.cloneStyleFrom(style);
        return clonedStyle;
    }

    private static String date_regex = "^(?:19|20)\\d\\d\\/(?:0?[1-9]|1[0-2])\\/(?:0?[1-9]|[12][0-9]|3[01])$";

    private ImportResultVO getImportResult(MultipartFile multipartFile, String username, Integer flag, Date now) {
        XSSFWorkbook originalWorkbook = null;
        InputStream inputStream = null;
        List<LaborDateVO> laborDateInfos = new ArrayList<>();
        List<ErrorForm> excelErrorForms = new ArrayList<>();
        try {
            inputStream = multipartFile.getInputStream();
            originalWorkbook = new XSSFWorkbook(inputStream);
            Sheet sheet = originalWorkbook.getSheetAt(0);
            //获取第一行表头内容
            Row headerRow = sheet.getRow(0);
            Cell firstCell = headerRow.getCell(0);
            Cell secondCell = headerRow.getCell(1);
            if (!("员工姓名".equals(firstCell.getStringCellValue()) && "考勤日期".equals(secondCell.getStringCellValue()))) {
                throw new RuntimeException("Excel模板不正确");
            }
            Row firstDataRow = sheet.getRow(1);
            if (firstDataRow == null) {
                throw new RuntimeException("导入excel数据为空");
            }
            int index = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            while (sheet.getRow(index) != null) {
                Row dataRow = sheet.getRow(index);
                Cell nameCell = dataRow.getCell(0);
                Cell dateCell = dataRow.getCell(1);
                Cell workDayCell = dataRow.getCell(2);
                Cell attendTypeCell = dataRow.getCell(3);
                Cell attendDetailCell = dataRow.getCell(4);
                ErrorForm errorForm = new ErrorForm();
                StringJoiner joiner = new StringJoiner(",");
                if (nameCell == null) {
                    joiner.add("员工姓名不能为空");
                }
                if (dateCell == null) {
                    joiner.add("考勤日期不能为空");
                } else {
                    String dateStr = dateCell.getStringCellValue();
                    if (!dateStr.matches(date_regex)) {
                        joiner.add("考勤日期格式错误");
                    }
                }
                if (workDayCell == null) {
                    joiner.add("工作日判定不能为空");
                }
                if (attendTypeCell == null) {
                    joiner.add("考勤类型不能为空");
                }

                String errorStr = joiner.toString();
                if (StringUtils.isNotEmpty(errorStr)) {
                    errorForm.setId(index);
                    errorForm.setReason(errorStr);
                    excelErrorForms.add(errorForm);
                } else {
                    LaborDateVO laborDate = new LaborDateVO();
                    laborDate.setName(nameCell.getStringCellValue());
                    String dateStr = dateCell.getStringCellValue();
                    Date date = dateFormat.parse(dateStr);
                    laborDate.setRecordDate(dateFormat.format(date));
                    laborDate.setIsWorkDay(workDayCell.getStringCellValue());
                    laborDate.setAttendType(attendTypeCell.getStringCellValue());
                    if (attendDetailCell != null) {
                        laborDate.setAttendDetail(attendDetailCell.getStringCellValue());
                    }
                    laborDate.setOrderNum(index);
                    laborDateInfos.add(laborDate);
                }
                index++;
            }
            laborDateInfos = laborDateInfos.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (originalWorkbook != null) {
                try {
                    originalWorkbook.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("开始查询excel表的有关数据：" + new Date());
        List<String> employNames = laborDateInfos.stream().map(LaborDateVO::getName).collect(Collectors.toList());
        List<String> existEmpNames = new ArrayList<>();
        if (!CollectionUtils.isEmpty(employNames)) {
            if (flag == 1) {
                existEmpNames = employeeMapper.findNamesByEmpNames(employNames);
            }
            if (flag == 2) {
                existEmpNames = loanWorkerMapper.findNamesByEmpNames(employNames);
            }
        }
        List<LaborDateVO> existLaborDateInfos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(laborDateInfos)) {
            List<String> employNameList = laborDateInfos.stream().map(LaborDateVO::getName).collect(Collectors.toList());
            List<String> recordDateInfos = laborDateInfos.stream().map(LaborDateVO::getRecordDate).collect(Collectors.toList());
            existLaborDateInfos = attendanceMapper.findLaborAttendInfos(employNameList, recordDateInfos, flag);
        }
        System.out.println("结束查询excel表的有关数据：" + new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");
        String nowStr = dateFormat.format(now);
        System.out.println("开始验证excel表的有关数据：" + new Date());
        AttendExcelVerifyVO attendExcelVerify = verifyAttendExcelDate(laborDateInfos, existEmpNames, existLaborDateInfos, flag);
        System.out.println("结束验证excel表的有关数据：" + new Date());
        List<ErrorForm> errorFormList = attendExcelVerify.getErrorFormList();
        excelErrorForms.addAll(errorFormList);
        excelErrorForms = excelErrorForms.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ErrorForm::getId))),
                        ArrayList::new
                ));
        int errorCount = excelErrorForms.size();
        List<LaborDateVO> laborDateList = attendExcelVerify.getLaborDateList();
        int successCount = laborDateList.size();
        if (!CollectionUtils.isEmpty(laborDateList)) {
            List<AttendanceAddVO> attendanceAddInfos = new ArrayList<>();
            for (LaborDateVO laborDate : laborDateList) {
                AttendanceAddVO attendanceAdd = new AttendanceAddVO();
                attendanceAdd.setAttendEmpName(laborDate.getName());
                attendanceAdd.setAttendRecordDate(laborDate.getRecordDate());
                attendanceAdd.setAttendIsweekday(laborDate.getIsWorkDay());
                attendanceAdd.setAttendStatus(laborDate.getAttendType());
                attendanceAdd.setAttendStatusDetail(laborDate.getAttendDetail());
                attendanceAddInfos.add(attendanceAdd);
            }
            batchInsertPostInfos(attendanceAddInfos, flag);
        }

        // ExcelImportResult<AttendanceAddVO> excelDate = getExcelDate(multipartFile, AttendanceAddVO.class, flag, existEmpNames, laborDateInfos);
//        System.out.println("结束验证excel表的有关数据：" + new Date());
//        List<ErrorForm> failReport = excelDate.getFailList().stream()
//                .map(entity -> {
//                    int line = entity.getRowNum();
//                    return new ErrorForm(line, entity.getErrorMsg());
//                }).collect(Collectors.toList());
//        System.out.println("开始插入excel表的有关数据：" + new Date());
//        if (!CollectionUtils.isEmpty(excelDate.getList())) {
//            batchInsertPostInfos(excelDate.getList(), flag);
//        }
//        System.out.println("结束插入excel表的有关数据：" + new Date());
//        int successCount = CollectionUtils.isEmpty(excelDate.getList()) ? 0 : excelDate.getList().size();
//        int errorCount = CollectionUtils.isEmpty(failReport) ? 0 : failReport.size();

        Long failFileId = null;
        String failFileUrl = null;
        if (!CollectionUtils.isEmpty(excelErrorForms)) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("错误信息", "Sheet1", ExcelType.XSSF),
                    ErrorForm.class, excelErrorForms);
            MultipartFile file = FileUtils.workbookToCommonsMultipartFile(workbook, multipartFile.getOriginalFilename() + "错误信息" + nowStr + ".xlsx");
            if (file != null) {
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_ATTENDANCE_FAIL_INFO.getKey(), "attend", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
        }

        ImportResultVO importResult = new ImportResultVO();
        importResult.setTotalCount(successCount + errorCount);
        importResult.setSuccessCount(successCount);
        importResult.setErrorCount(errorCount);
        importResult.setFailFileId(failFileId);
        importResult.setFailFileUrl(failFileUrl);
        return importResult;
    }

    private AttendExcelVerifyVO verifyAttendExcelDate(List<LaborDateVO> laborDateInfos,
                                                      List<String> existEmpNames, List<LaborDateVO> existLaborDateInfos,
                                                      Integer flag) {
        AttendExcelVerifyVO attendExcelVerify = new AttendExcelVerifyVO();
        List<ErrorForm> errorForms = new ArrayList<>();
        for (LaborDateVO laborDateInfo : laborDateInfos) {
            String name = laborDateInfo.getName();
            Integer orderNum = laborDateInfo.getOrderNum();
            ErrorForm errorForm = new ErrorForm();
            boolean nameExist = existEmpNames.contains(name);
            boolean attendExist = existLaborDateInfos.contains(laborDateInfo);
            if (!nameExist || attendExist) {
                errorForm.setId(orderNum);
                if (!nameExist) {
                    if (flag == 1) {
                        errorForm.setReason("姓名:" + name + "在人员数据中不存在");
                    }
                    if (flag == 2) {
                        errorForm.setReason("姓名:" + name + "在借工数据中不存在");
                    }
                }
                if (attendExist) {
                    errorForm.setReason("姓名:" + name + "本日已有考勤记录");
                }
                errorForms.add(errorForm);
            }
        }
        laborDateInfos = laborDateInfos.stream().filter(s -> {
            Integer orderNum = s.getOrderNum();
            return !errorForms.stream().anyMatch(e -> e.getId().equals(orderNum));
        }).collect(Collectors.toList());
        attendExcelVerify.setLaborDateList(laborDateInfos);
        attendExcelVerify.setErrorFormList(errorForms);
        return attendExcelVerify;
    }


    @Autowired
    private EmpAttendanceMapper attendanceMapper;

    @Override
    public TableDataInfo totalCountAttend(String keyword, String yearNum, Integer pageSize, Integer pageNum) {
//        List<AttendCount> attendCounts = attendCountMapper.findInfoByReferenceParams(keyword, yearNum);
//        List<Long> empIds = attendCounts.stream().map(AttendCount::getEmpId).collect(Collectors.toList());
//        Map<Long, Employee> empMap = new HashMap<>(16);
//        if (!CollectionUtils.isEmpty(empIds)) {
//            empMap = employeeMapper.selectEmployeeInfosByIds(empIds).stream().collect(Collectors.
//                    toMap(Employee::getEmpId, Function.identity()));
//        }
//        List<AttendCountVO> attendCountList = new ArrayList<>();
//        for (AttendCount attendCount : attendCounts) {
//            AttendCountVO attendCountInfo = new AttendCountVO();
//            BeanUtils.copyProperties(attendCount, attendCountInfo);
//            attendCountInfo.setYear(attendCount.getCurrentYear());
//            Employee employee = empMap.getOrDefault(attendCount.getEmpId(), new Employee());
//            attendCountInfo.setEmpName(employee.getEmpName());
//            attendCountList.add(attendCountInfo);
//        }
        try {
            TableDataInfo tableDataInfo = new TableDataInfo();
            List<Employee> allEmployees = employeeMapper.findAllEmployees();
            List<LoanWorker> currentInfos = loanWorkerMapper.findCurrentInfos();
            if (CollectionUtils.isEmpty(allEmployees) && CollectionUtils.isEmpty(currentInfos)) {
                tableDataInfo.setCode(200);
                tableDataInfo.setMsg("查询成功");
                tableDataInfo.setTotal(0);
                tableDataInfo.setRows(new ArrayList<>());
            }
            List<Long> attendIds = new ArrayList<>();
            if (keyword != null) {
                List<Long> empIds = employeeMapper.findEmpIdsByParams(keyword, null, null, null);
                List<Long> loanIds = loanWorkerMapper.findLoanIdsByParams(keyword, null, null, null);
                if (!CollectionUtils.isEmpty(empIds)) {
                    attendIds = attendanceMapper.findEmpAttendIdByEmpIdsAndFlag(empIds, 1);
                    allEmployees = allEmployees.stream().filter(s -> empIds.contains(s.getEmpId())).collect(Collectors.toList());
                } else {
                    allEmployees = new ArrayList<>();
                }
                if (!CollectionUtils.isEmpty(loanIds)) {
                    attendIds.addAll(attendanceMapper.findEmpAttendIdByEmpIdsAndFlag(loanIds, 2));
                    currentInfos = currentInfos.stream().filter(s -> loanIds.contains(s.getLoanId())).collect(Collectors.toList());
                } else {
                    currentInfos = new ArrayList<>();
                }
            }
            List<AttendCount> attendInfos = getAttendInfos(yearNum, attendIds, allEmployees, currentInfos);
//            List<Long> empIds = null;
//            if (keyword != null) {
//                empIds = employeeMapper.findInfoByEmpIdCardOrName(keyword).stream().map(Employee::getEmpId).
//                        collect(Collectors.toList());
//            }
//            if (empIds != null && !empIds.isEmpty()) {
//                List<Long> finalEmpIds = empIds;
//                attendInfos = attendInfos.stream().filter(s -> finalEmpIds.contains(s.getEmpId())).
//                        collect(Collectors.toList());
//            }
            List<AttendCountVO> attendCountInfos = getPageResult(attendInfos, pageNum, pageSize);
            tableDataInfo.setCode(200);
            tableDataInfo.setMsg("查询成功");
            tableDataInfo.setTotal(attendInfos.size());
            tableDataInfo.setRows(attendCountInfos);
            return tableDataInfo;
        } catch (Exception e) {
            throw new ServiceException("查询失败");
        }
    }

    private List<AttendCountVO> getPageResult(List<AttendCount> result, Integer pageNum, Integer pageSize) {

        int beginIndex = 0;
        int endIndex = 0;
        beginIndex = pageSize * (pageNum - 1);
        endIndex = Math.min(pageNum * pageSize, result.size());
        if (beginIndex >= result.size()) {
            //超出范围则无数据
            result = new ArrayList<>();
        } else {
            endIndex = Math.min(result.size(), endIndex);
            result = result.subList(beginIndex, endIndex);
        }
        List<AttendCountVO> pageResult = new ArrayList<>();
        for (AttendCount attendCount : result) {
            AttendCountVO attendCountInfo = new AttendCountVO();
            attendCountInfo.setEmpId(attendCount.getEmpId());
            attendCountInfo.setEmpName(attendCount.getEmpName());
            attendCountInfo.setYear(attendCount.getCurrentYear());
            attendCountInfo.setFlag(attendCount.getFlag());
            //    attendCountInfo.setTotalDays(attendCount.getTotalDays());
            attendCountInfo.setLinkDays(attendCount.getLinkDays());
//            attendCountInfo.setBusinessDays(attendCount.getBusinessDays());
//            attendCountInfo.setHolidayDays(attendCount.getHolidayDays());
//            attendCountInfo.setOverTimeDays(attendCount.getOverTimeDays());
            Map<String, Integer> attendMap = attendCount.getAttendanceCount();
            attendCountInfo.setAttendanceCount(attendMap);
            List<EmpAttendNumVO> attendNumList = new ArrayList<>();
            for (String key : attendMap.keySet()) {
                EmpAttendNumVO empAttendNum = new EmpAttendNumVO();
                empAttendNum.setAttendStatus(key);
                empAttendNum.setNum(attendMap.get(key));
                attendNumList.add(empAttendNum);
            }
            attendNumList = attendNumList.stream().sorted().
                    collect(Collectors.toList());
            attendCountInfo.setAttendNumList(attendNumList);
            pageResult.add(attendCountInfo);
        }
        pageResult = pageResult.stream().sorted(Comparator.comparing(AttendCountVO::getYear, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return pageResult;
    }

    private List<AttendCount> getAttendInfos(String yearNum, List<Long> attendIds, List<Employee> allEmployees, List<LoanWorker> loanWorkers) {

        List<EmpAttendance> empAttendanceList = attendanceMapper.findInfosByEmpIdsAndRecordTimeForYear(attendIds, yearNum);
        Map<Integer, List<EmpAttendance>> attendMap = empAttendanceList.stream().
                collect(Collectors.groupingBy(EmpAttendance::getFlag));
        List<SysDictData> attendStatuses = dictDataMapper.selectDictDataByType("attend_status");
        List<AttendCount> attendCounts = new ArrayList<>();
        List<EmpAttendance> empAttends = attendMap.getOrDefault(1, new ArrayList<>());
        List<EmpAttendance> loanAttends = attendMap.getOrDefault(2, new ArrayList<>());
        if (!CollectionUtils.isEmpty(empAttends)) {
            Map<Long, List<EmpAttendance>> empAttendMap = empAttends.stream().collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
            for (Employee employee : allEmployees) {
                Long empId = employee.getEmpId();
                List<EmpAttendance> attendances = empAttendMap.get(empId);
                if (!CollectionUtils.isEmpty(attendances)) {
                    Map<String, List<EmpAttendance>> attendYearMap = attendances.stream().
                            filter(s -> s.getAttendRecordDate() != null).collect(Collectors.groupingBy(s -> {
                                Date attendRecordDate = s.getAttendRecordDate();
                                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                                return yearFormat.format(attendRecordDate);
                            }));
                    for (String year : attendYearMap.keySet()) {
                        AttendCount attendCount = new AttendCount();
                        attendCount.setEmpId(empId);
                        attendCount.setEmpName(employee.getEmpName());
                        attendCount.setCurrentYear(year);
                        attendCount.setFlag(1);
                        List<EmpAttendance> empAttendances = attendYearMap.get(year);
                        // 先根据记录日期排序，再根据id排序
                        empAttendances.sort(Comparator.comparing(EmpAttendance::getAttendRecordDate).
                                thenComparing(EmpAttendance::getAttendId));

                        int continuousDays = 0;
                        String continuousWorkingNum = "continuous";
                        Map<String, Integer> attendanceCount = new HashMap<>();
//                    attendanceCount.put(AttendStatusEnum.ONE.getKey(), 0);
//                    attendanceCount.put(AttendStatusEnum.TWO.getKey(), 0);
//                    attendanceCount.put(AttendStatusEnum.THREE.getKey(), 0);
//                    attendanceCount.put(AttendStatusEnum.FOUR.getKey(), 0);

                        for (SysDictData attendStatus : attendStatuses) {
                            attendanceCount.put(attendStatus.getDictValue(), 0);
                        }

                        attendanceCount.put(continuousWorkingNum, 0);

                        for (int i = 0; i < empAttendances.size(); i++) {
                            EmpAttendance empAttendance = empAttendances.get(i);
                            // 计算考勤状态个数
                            if (!ObjectUtils.isEmpty(empAttendance.getAttendStatus()) && attendanceCount.containsKey(empAttendance.getAttendStatus())) {
                                int count = attendanceCount.get(empAttendance.getAttendStatus());
                                attendanceCount.put(empAttendance.getAttendStatus(), count + 1);
                            }
                            // 计算连续上班最大天数
                            boolean verifyCount = (i == 0 && AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()))
                                    || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()) && AttendStatusEnum.ONE.getKey().equals(empAttendances.get(i - 1).getAttendStatus()))
                                    || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()));
                            if (verifyCount) {
                                continuousDays++;
                            } else {
                                // 判断当前连续上班天数是否为最大值，并更新连续上班天数
                                Integer maxContinuousDays = attendanceCount.get(continuousWorkingNum);
                                if (continuousDays > maxContinuousDays) {
                                    attendanceCount.put(continuousWorkingNum, continuousDays);
                                }
                                // 重置连续上班天数
                                continuousDays = 0;
                            }
                        }
                        // 判断最后一次计数是否为最大值
                        int lastCount = attendanceCount.get(continuousWorkingNum);
                        if (continuousDays > lastCount) {
                            attendanceCount.put(continuousWorkingNum, continuousDays);
                        }

                        attendCount.setLinkDays(attendanceCount.get(continuousWorkingNum));
//                    attendCount.setTotalDays(attendanceCount.get(AttendStatusEnum.ONE.getKey()));
//                    attendCount.setBusinessDays(attendanceCount.get(AttendStatusEnum.TWO.getKey()));
//                    attendCount.setOverTimeDays(attendanceCount.get(AttendStatusEnum.FOUR.getKey()));
//                    attendCount.setHolidayDays(attendanceCount.get(AttendStatusEnum.THREE.getKey()));
                        Map<String, Integer> attendanceMap = new HashMap<>();
                        for (String key : attendanceCount.keySet()) {
                            if (!continuousWorkingNum.equals(key)) {
                                attendanceMap.put(key, attendanceCount.get(key));
                            }
                        }
                        attendCount.setAttendanceCount(attendanceMap);
                        attendCounts.add(attendCount);
                    }
                } else {
                    AttendCount attendCount = new AttendCount();
                    attendCount.setEmpId(empId);
                    attendCount.setEmpName(employee.getEmpName());
                    attendCount.setCurrentYear("");
                    // attendCount.setTotalDays(0);
                    attendCount.setLinkDays(0);
                    attendCount.setFlag(1);
//                attendCount.setBusinessDays(0);
//                attendCount.setOverTimeDays(0);
//                attendCount.setHolidayDays(0);
                    Map<String, Integer> attendanceCount = new HashMap<>();
                    for (SysDictData attendStatus : attendStatuses) {
                        attendanceCount.put(attendStatus.getDictValue(), 0);
                    }
                    attendCount.setAttendanceCount(attendanceCount);
                    attendCounts.add(attendCount);
                }

            }
        } else {
            for (Employee employee : allEmployees) {
                AttendCount attendCount = new AttendCount();
                attendCount.setEmpId(employee.getEmpId());
                attendCount.setEmpName(employee.getEmpName());
                attendCount.setCurrentYear("");
                attendCount.setLinkDays(0);
                attendCount.setFlag(1);
                Map<String, Integer> attendanceCount = new HashMap<>();
                for (SysDictData attendStatus : attendStatuses) {
                    attendanceCount.put(attendStatus.getDictValue(), 0);
                }
                attendCount.setAttendanceCount(attendanceCount);
                attendCounts.add(attendCount);
            }
        }
        if (!CollectionUtils.isEmpty(loanAttends)) {
            Map<Long, List<EmpAttendance>> loanAttendMap = loanAttends.stream().collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
            for (LoanWorker loanWorker : loanWorkers) {
                Long loanId = loanWorker.getLoanId();
                List<EmpAttendance> attendances = loanAttendMap.get(loanId);
                if (!CollectionUtils.isEmpty(attendances)) {
                    Map<String, List<EmpAttendance>> attendYearMap = attendances.stream().
                            filter(s -> s.getAttendRecordDate() != null).collect(Collectors.groupingBy(s -> {
                                Date attendRecordDate = s.getAttendRecordDate();
                                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                                return yearFormat.format(attendRecordDate);
                            }));
                    for (String year : attendYearMap.keySet()) {
                        AttendCount attendCount = new AttendCount();
                        attendCount.setEmpId(loanId);
                        attendCount.setEmpName(loanWorker.getLoanEmpName());
                        attendCount.setCurrentYear(year);
                        attendCount.setFlag(2);
                        List<EmpAttendance> empAttendances = attendYearMap.get(year);
                        // 先根据记录日期排序，再根据id排序
                        empAttendances.sort(Comparator.comparing(EmpAttendance::getAttendRecordDate).
                                thenComparing(EmpAttendance::getAttendId));

                        int continuousDays = 0;
                        String continuousWorkingNum = "continuous";
                        Map<String, Integer> attendanceCount = new HashMap<>();
//                    attendanceCount.put(AttendStatusEnum.ONE.getKey(), 0);
//                    attendanceCount.put(AttendStatusEnum.TWO.getKey(), 0);
//                    attendanceCount.put(AttendStatusEnum.THREE.getKey(), 0);
//                    attendanceCount.put(AttendStatusEnum.FOUR.getKey(), 0);

                        for (SysDictData attendStatus : attendStatuses) {
                            attendanceCount.put(attendStatus.getDictValue(), 0);
                        }

                        attendanceCount.put(continuousWorkingNum, 0);

                        for (int i = 0; i < empAttendances.size(); i++) {
                            EmpAttendance empAttendance = empAttendances.get(i);
                            // 计算考勤状态个数
                            if (!ObjectUtils.isEmpty(empAttendance.getAttendStatus()) && attendanceCount.containsKey(empAttendance.getAttendStatus())) {
                                int count = attendanceCount.get(empAttendance.getAttendStatus());
                                attendanceCount.put(empAttendance.getAttendStatus(), count + 1);
                            }
                            // 计算连续上班最大天数
                            boolean verifyCount = (i == 0 && AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()))
                                    || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()) && AttendStatusEnum.ONE.getKey().equals(empAttendances.get(i - 1).getAttendStatus()))
                                    || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()));
                            if (verifyCount) {
                                continuousDays++;
                            } else {
                                // 判断当前连续上班天数是否为最大值，并更新连续上班天数
                                Integer maxContinuousDays = attendanceCount.get(continuousWorkingNum);
                                if (continuousDays > maxContinuousDays) {
                                    attendanceCount.put(continuousWorkingNum, continuousDays);
                                }
                                // 重置连续上班天数
                                continuousDays = 0;
                            }
                        }
                        // 判断最后一次计数是否为最大值
                        int lastCount = attendanceCount.get(continuousWorkingNum);
                        if (continuousDays > lastCount) {
                            attendanceCount.put(continuousWorkingNum, continuousDays);
                        }

                        attendCount.setLinkDays(attendanceCount.get(continuousWorkingNum));
//                    attendCount.setTotalDays(attendanceCount.get(AttendStatusEnum.ONE.getKey()));
//                    attendCount.setBusinessDays(attendanceCount.get(AttendStatusEnum.TWO.getKey()));
//                    attendCount.setOverTimeDays(attendanceCount.get(AttendStatusEnum.FOUR.getKey()));
//                    attendCount.setHolidayDays(attendanceCount.get(AttendStatusEnum.THREE.getKey()));
                        Map<String, Integer> attendanceMap = new HashMap<>();
                        for (String key : attendanceCount.keySet()) {
                            if (!continuousWorkingNum.equals(key)) {
                                attendanceMap.put(key, attendanceCount.get(key));
                            }
                        }
                        attendCount.setAttendanceCount(attendanceMap);
                        attendCounts.add(attendCount);
                    }
                } else {
                    AttendCount attendCount = new AttendCount();
                    attendCount.setEmpId(loanId);
                    attendCount.setEmpName(loanWorker.getLoanEmpName());
                    attendCount.setCurrentYear("");
                    attendCount.setLinkDays(0);
                    attendCount.setFlag(2);
                    Map<String, Integer> attendanceCount = new HashMap<>();
                    for (SysDictData attendStatus : attendStatuses) {
                        attendanceCount.put(attendStatus.getDictValue(), 0);
                    }
                    attendCount.setAttendanceCount(attendanceCount);
                    attendCounts.add(attendCount);
                }
            }
        } else {
            for (LoanWorker loanWorker : loanWorkers) {
                AttendCount attendCount = new AttendCount();
                attendCount.setEmpId(loanWorker.getLoanId());
                attendCount.setEmpName(loanWorker.getLoanEmpName());
                attendCount.setCurrentYear("");
                attendCount.setLinkDays(0);
                attendCount.setFlag(2);
                Map<String, Integer> attendanceCount = new HashMap<>();
                for (SysDictData attendStatus : attendStatuses) {
                    attendanceCount.put(attendStatus.getDictValue(), 0);
                }
                attendCount.setAttendanceCount(attendanceCount);
                attendCounts.add(attendCount);
            }
        }
        return attendCounts;
    }


    @Override
    public EmpAttendanceGenVO totalCountDetail(Long empId, Date time, Integer flag) {

        EmpAttendanceGenVO empAttendanceGen = new EmpAttendanceGenVO();
        if (flag == 1) {
            Employee employee = employeeMapper.selectEmployeeByEmpId(empId);
            empAttendanceGen.setEmpId(empId);
            if (employee != null) {
                empAttendanceGen.setEmpName(employee.getEmpName());
            } else {
                throw new ServiceException("人员数据中不存在此员工");
            }
        } else {
            LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(empId);
            empAttendanceGen.setEmpId(empId);
            if (loanWorker != null) {
                empAttendanceGen.setEmpName(loanWorker.getLoanEmpName());
            } else {
                throw new ServiceException("借工数据中不存在此员工");
            }
        }
        List<EmpAttendance> empAttendances = empAttendanceMapper.findInfosByEmpIdAndRecordTime(empId, time, flag);
        List<SysDictData> attendStatuses = dictDataMapper.selectDictDataByType("attend_status");
        if (!CollectionUtils.isEmpty(empAttendances)) {
            // 先根据记录日期排序，再根据id排序
            empAttendances.sort(Comparator.comparing(EmpAttendance::getAttendRecordDate).thenComparing(EmpAttendance::getAttendId));

            //List<String> workingStatus = Arrays.asList(AttendStatusEnum.ONE.getKey(), AttendStatusEnum.TWO.getKey(), AttendStatusEnum.FOUR.getKey());
            int continuousDays = 0;
            String continuousWorkingNum = "continuous";
            Map<String, Integer> attendanceCount = new HashMap<>(16);
//            attendanceCount.put(AttendStatusEnum.ONE.getKey(), 0);
//            attendanceCount.put(AttendStatusEnum.TWO.getKey(), 0);
//            attendanceCount.put(AttendStatusEnum.THREE.getKey(), 0);
//            attendanceCount.put(AttendStatusEnum.FOUR.getKey(), 0);
            attendanceCount.put(continuousWorkingNum, 0);

            for (int i = 0; i < empAttendances.size(); i++) {
                EmpAttendance empAttendance = empAttendances.get(i);
                String attendStatus = empAttendance.getAttendStatus();
                for (SysDictData attendStatusDicInfo : attendStatuses) {
                    if (attendStatusDicInfo.getDictValue().equals(attendStatus)) {
                        attendanceCount.merge(attendStatusDicInfo.getDictValue(), 1, Integer::sum);
                    }
                }

                // 计算考勤状态个数
//                if (!ObjectUtils.isEmpty(empAttendance.getAttendStatus()) && attendanceCount.containsKey(empAttendance.getAttendStatus())) {
//                    int count = attendanceCount.get(empAttendance.getAttendStatus());
//                    attendanceCount.put(empAttendance.getAttendStatus(), count + 1);
//                }
                // 计算连续上班最大天数
                boolean verifyCount = (i == 0 && AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()))
                        || AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus())
                        && AttendStatusEnum.ONE.getKey().equals(empAttendances.get(i - 1).getAttendStatus())
                        || (AttendStatusEnum.ONE.getKey().equals(empAttendance.getAttendStatus()));
                if (verifyCount) {
                    continuousDays++;
                } else {
                    // 判断当前连续上班天数是否为最大值，并更新连续上班天数
                    Integer maxContinuousDays = attendanceCount.get(continuousWorkingNum);
                    if (continuousDays > maxContinuousDays) {
                        attendanceCount.put(continuousWorkingNum, continuousDays);
                    }
                    // 重置连续上班天数
                    continuousDays = 0;
                }
            }
            // 判断最后一次计数是否为最大值
            int lastCount = attendanceCount.get(continuousWorkingNum);
            if (continuousDays > lastCount) {
                attendanceCount.put(continuousWorkingNum, continuousDays);
            }
//            new EmpAttendanceOverviewVO(attendanceCount.get(AttendStatusEnum.ONE.getKey()),
//                    attendanceCount.get(AttendStatusEnum.TWO.getKey()), attendanceCount.get(AttendStatusEnum.THREE.getKey()),
//                    attendanceCount.get(AttendStatusEnum.FOUR.getKey()), attendanceCount.get(continuousWorkingNum));

            empAttendanceGen.setMaxContinuousWorkingNum(attendanceCount.get(continuousWorkingNum));
            Map<String, Integer> attendMap = new HashMap<>(32);
            for (String key : attendanceCount.keySet()) {
                if (!continuousWorkingNum.equals(key)) {
                    attendMap.put(key, attendanceCount.get(key));
                }
            }
            for (SysDictData attendStatus : attendStatuses) {
                if (!attendMap.containsKey(attendStatus.getDictValue())) {
                    attendMap.put(attendStatus.getDictValue(), 0);
                }
            }
            empAttendanceGen.setAttendMap(attendMap);
            List<EmpAttendNumVO> attendNumList = new ArrayList<>();
            for (String key : attendMap.keySet()) {
                EmpAttendNumVO empAttendNum = new EmpAttendNumVO();
                empAttendNum.setAttendStatus(key);
                empAttendNum.setNum(attendMap.get(key));
                attendNumList.add(empAttendNum);
            }
            attendNumList = attendNumList.stream().sorted().collect(Collectors.toList());
            empAttendanceGen.setAttendNumList(attendNumList);
            List<EmpAttendanceDetailVO> detailList = empAttendances.stream().collect(Collectors.groupingBy(EmpAttendance::getAttendRecordDate))
                    .entrySet().stream().map(data -> new EmpAttendanceDetailVO(data.getKey(),
                            data.getValue().stream().sorted(
                                    Comparator.comparing(EmpAttendance::getAttendId)
                            ).map(EmpAttendance::getAttendStatus).collect(Collectors.toList()))
                    ).sorted(Comparator.comparing(EmpAttendanceDetailVO::getTime)).collect(Collectors.toList());
            empAttendanceGen.setDetailList(detailList);

        } else {
            Map<String, Integer> attendMap = new HashMap<>(32);
            List<EmpAttendNumVO> attendNumList = new ArrayList<>();
            for (SysDictData attendStatus : attendStatuses) {
                attendMap.put(attendStatus.getDictValue(), 0);
                EmpAttendNumVO empAttendNum = new EmpAttendNumVO();
                empAttendNum.setAttendStatus(attendStatus.getDictValue());
                empAttendNum.setNum(0);
                attendNumList.add(empAttendNum);
            }
            attendNumList = attendNumList.stream().sorted().
                    collect(Collectors.toList());
            empAttendanceGen.setAttendNumList(attendNumList);
            empAttendanceGen.setAttendMap(attendMap);
            empAttendanceGen.setMaxContinuousWorkingNum(0);
            empAttendanceGen.setDetailList(new ArrayList<>());
        }
        return empAttendanceGen;
    }

    @Override
    public void uploadAttendanceInfosFileForSplit(HttpServletResponse response, MultipartFile file) {


        System.out.println("原始文件大小为:" + file.getSize());
        List<MultipartFile> result = new ArrayList<>();
        try {
            result = splitLargeExcel(file, 10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<MultipartFile> splitLargeExcel(MultipartFile originalFile, int rowsPerFile) throws IOException {

        // 加载Excel工作簿
        try (InputStream inputStream = originalFile.getInputStream()) {
            Workbook originalWorkbook = new XSSFWorkbook(inputStream);
            Sheet sheet = originalWorkbook.getSheetAt(0); // 获取第一个工作表，根据需要遍历所有工作表

            //获取第一行表头内容
            Row headerRow = sheet.getRow(0);
            Cell firstCell = headerRow.getCell(0);
            Cell secondCell = headerRow.getCell(1);
            if (!("序号".equals(firstCell.getStringCellValue()) && "员工姓名".equals(secondCell.getStringCellValue()))) {
                throw new RuntimeException("Excel模板不正确");
            }
            Row firstDataRow = sheet.getRow(1);
            if (firstDataRow == null) {
                throw new RuntimeException("导入excel数据为空");
            }
            List<MultipartFile> splitFiles = new ArrayList<>();
            int startRow = 1;
            while (true) {
                // 创建一个新的SXSSFWorkbook用于写入小文件
                SXSSFWorkbook smallWorkbook = new SXSSFWorkbook(10000);
                // 复制指定行范围的数据到新的工作簿
                Sheet newSheet = smallWorkbook.createSheet(sheet.getSheetName());

                Row firstRow = newSheet.createRow(0);
                copyRow(headerRow, firstRow);

                for (int i = startRow; i <= Math.min(startRow + rowsPerFile, sheet.getLastRowNum() + 1); i++) {
                    Row sourceRow = sheet.getRow(i);
                    if (sourceRow != null) {
                        Row newRow = newSheet.createRow(i - startRow + 1);
                        copyRow(sourceRow, newRow);
                    } else {
                        break;
                    }
                }

                MultipartFile splitFile = FileUtils.workbookToCommonsMultipartFile(smallWorkbook, originalFile.getName() + "-" + startRow + ".xlsx");
                smallWorkbook.dispose(); // 清理不需要的资源
                splitFiles.add(splitFile);
                startRow += rowsPerFile;
                // 检查是否已经处理完所有行
                if (startRow > sheet.getLastRowNum() + 1) {
                    break;
                }
            }
            return splitFiles;
        }
    }

    // 辅助方法：复制一行数据
    private void copyRow(Row sourceRow, Row targetRow) {
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = targetRow.createCell(i);
                copyCell(sourceCell, targetCell);
            }
        }
    }

    // 辅助方法：复制一个单元格数据
    private void copyCell(Cell sourceCell, Cell targetCell) {
        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            // 其他类型的单元格处理...
        }
    }


    private void batchInsertPostInfos(List<AttendanceAddVO> attendanceAddVOS, Integer flag) {

        List<String> empNames = attendanceAddVOS.stream().map(AttendanceAddVO::getAttendEmpName).collect(Collectors.toList());
        Map<String, EmpSimpleVO> empSimpleMap = new HashMap<>();
        if (flag == 1) {
            List<Employee> employees = employeeMapper.findInfoEmpNames(empNames);
            List<EmpSimpleVO> empSimpleInfos = new ArrayList<>();
            for (Employee employee : employees) {
                EmpSimpleVO empSimple = new EmpSimpleVO();
                empSimple.setEmpName(employee.getEmpName());
                empSimple.setEmpId(employee.getEmpId());
                empSimple.setEmpIdcard(employee.getEmpIdcard());
                empSimpleInfos.add(empSimple);
            }
            empSimpleMap = empSimpleInfos.stream().collect(Collectors.toMap(EmpSimpleVO::getEmpName, empSimpleVO -> empSimpleVO, (o, n) -> o));
        } else {
            List<LoanWorker> loanWorkers = loanWorkerMapper.findInfoByNames(empNames);
            List<EmpSimpleVO> empSimpleInfos = new ArrayList<>();
            for (LoanWorker loanWorker : loanWorkers) {
                EmpSimpleVO empSimple = new EmpSimpleVO();
                empSimple.setEmpName(loanWorker.getLoanEmpName());
                empSimple.setEmpId(loanWorker.getLoanId());
                empSimple.setEmpIdcard(loanWorker.getLoanEmpIdcard());
                empSimpleInfos.add(empSimple);
            }
            empSimpleMap = empSimpleInfos.stream().collect(Collectors.toMap(EmpSimpleVO::getEmpName, empSimpleVO -> empSimpleVO, (o, n) -> o));
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<EmpAttendance> attendInfos = new ArrayList<>();
        List<SysDictData> attendStatuses = dictDataMapper.selectDictDataByType("attend_status");
        List<SysDictData> is_workdays = dictDataMapper.selectDictDataByType("is_workday");
        for (AttendanceAddVO attendanceAddVO : attendanceAddVOS) {
            EmpAttendance empAttendance = new EmpAttendance();
            BeanUtils.copyProperties(attendanceAddVO, empAttendance);
            String attendRecordDate = attendanceAddVO.getAttendRecordDate();
            try {
                Date parse = dateFormat.parse(attendRecordDate);
                empAttendance.setAttendRecordDate(parse);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            empAttendance.setFlag(flag);
            EmpSimpleVO empSimple = empSimpleMap.getOrDefault(attendanceAddVO.getAttendEmpName(), new EmpSimpleVO());
            empAttendance.setAttendEmpId(empSimple.getEmpId());
            empAttendance.setAttendEmpName(empSimple.getEmpName());
            empAttendance.setAttendEmpIdcard(empSimple.getEmpIdcard());
            for (SysDictData attendStatus : attendStatuses) {
                if (attendStatus.getDictLabel().equals(attendanceAddVO.getAttendStatus())) {
                    empAttendance.setAttendStatus(attendStatus.getDictValue());
                }
            }


//            for (AttendStatusEnum value : AttendStatusEnum.values()) {
//                if (value.getValue().equals(attendanceAddVO.getAttendStatus())) {
//                    empAttendance.setAttendStatus(value.getKey());
//                }
//            }
            for (SysDictData is_workday : is_workdays) {
                if (is_workday.getDictLabel().equals(attendanceAddVO.getAttendIsweekday())) {
                    empAttendance.setAttendIsweekday(is_workday.getDictValue());
                }
            }
//            for (AttendWeekdayEnum value : AttendWeekdayEnum.values()) {
//                if (value.getValue().equals(attendanceAddVO.getAttendIsweekday())) {
//                    empAttendance.setAttendIsweekday(value.getKey());
//                }
//            }
            empAttendance.setCreateTime(DateUtils.getNowDate());
            attendInfos.add(empAttendance);
            // insertEmpAttendance(empAttendance);
        }
        //批量插入
        attendanceMapper.batchInsertInfos(attendInfos);

    }


    @Autowired
    private AttendanceExcelImportVerifyHandler attendanceExcelImportVerifyHandler;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<AttendanceAddVO> getExcelDate(MultipartFile file, Class<AttendanceAddVO> postImportClass,
                                                            Integer flag, List<String> existEmpNames,
                                                            List<LaborDateVO> laborDateInfos) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            attendanceExcelImportVerifyHandler.setFlag(flag);
            attendanceExcelImportVerifyHandler.setExistEmpNames(existEmpNames);
            attendanceExcelImportVerifyHandler.setLaborDateInfos(laborDateInfos);
            params.setVerifyHandler(attendanceExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), postImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = attendanceExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
