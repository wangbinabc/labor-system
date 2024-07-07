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
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.EmpPerformanceExcelImportVerifyHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEmpPerformanceService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工绩效Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Service
public class EmpPerformanceServiceImpl implements IEmpPerformanceService {
    @Autowired
    private EmpPerformanceMapper empPerformanceMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 查询员工绩效
     *
     * @param perfId 员工绩效主键
     * @return 员工绩效
     */
    @Override
    public EmpPerformance selectEmpPerformanceByPerfId(Long perfId) {
        return empPerformanceMapper.selectEmpPerformanceByPerfId(perfId);
    }

    /**
     * 查询员工绩效列表
     *
     * @param empPerformance 员工绩效
     * @return 员工绩效
     */
    @Override
    public List<EmpPerformance> selectEmpPerformanceList(EmpPerformance empPerformance, List<Long> performIds, Integer query) {
        List<EmpPerformance> empPerformances = empPerformanceMapper.selectEmpPerformanceListForSearch(empPerformance, performIds, query);
        List<Long> deptIds = empPerformances.stream().map(EmpPerformance::getPerfDeptId).collect(Collectors.toList());
        Map<Long, Department> deptMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            deptMap = departmentMapper.findDeptInfosByDeptIds(deptIds).stream().collect(Collectors.
                    toMap(Department::getDeptId, Function.identity()));
        }
        for (EmpPerformance performance : empPerformances) {
            Long perfDeptId = performance.getPerfDeptId();
            if (perfDeptId != null) {
                String deptName = deptMap.getOrDefault(perfDeptId, new Department()).getDeptName();
                performance.setPerfDeptName(deptName);
            }
        }
        return empPerformances;
    }

    @Override
    public List<EmpPerformance> findExportInfos(ExportVO export) {

        return empPerformanceMapper.findExportInfos(export);
    }

    public List<EmpPerformance> selectEmpPerformanceListByScreen(EmpPerformanceScreenVO empPerformanceScreenVO,
                                                                 List<Long> performIds,
                                                                 Integer query) {
        List<EmpPerformance> empPerformances = empPerformanceMapper.selectEmpPerformanceListByScreen(empPerformanceScreenVO, performIds, query);
        Map<Long, Department> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        for (EmpPerformance empPerformance : empPerformances) {
            empPerformance.setPerfDeptName(deptMap.getOrDefault(empPerformance.getPerfDeptId(), new Department()).getDeptName());
        }
        return empPerformances;
    }

    @Override
    public List<EmpEffectivenessVO> selectEmpPerformanceEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO) {
        return empPerformanceMapper.selectEmpPerformanceEffectivenessList(empEffectivenessSearchVO);
    }

    public List<EmpPerformance> isChange(List<EmpPerformance> list) {
        for (EmpPerformance empPerformance : list) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empPerformance.getPerfEmpId());
            if (e == null) {
                empPerformance.setIsChange(false);
            }
        }

        return list;
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);


        List<EmpPerformance> exportInfos = findExportInfos(export);

        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }
        //-------------------导出不脱敏----------------------
//        List<Employee> employeeList = employeeMapper.findAllEmployees();
//        Map<Long, Employee> empMap = employeeList.stream().collect(Collectors.
//                toMap(Employee::getEmpId, Function.identity()));
//
//        for (EmpPerformance ea : exportInfos) {
//            ea.setPerfEmpIdcard(empMap.get(ea.getPerfEmpId()) != null && empMap.get(ea.getPerfEmpId()).getEmpIdcard() != null ? empMap.get(ea.getPerfEmpId()).getEmpIdcard() : "card_is_Null");
//        }
        //----------------------------------------

        EmpPerformanceChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<EmpPerformance>> groupedData = exportInfos.stream()
                .collect(Collectors.groupingBy(emp -> {
                    try {
                        // 使用反射获取指定属性的值
                        Object value = emp.getClass().getMethod("get" +
                                groupByProperty.substring(0, 1).toUpperCase() +
                                groupByProperty.substring(1)).invoke(emp);
                        return value != null ? value.toString() : "";
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 处理获取属性值异常
                        return null;
                    }
                }));

        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        String fieldName = exportDivide.getFieldName();
        for (String key : groupedData.keySet()) {
            List<EmpPerformance> exportList = groupedData.get(key);

            for (EmpPerformance exportInfo : exportList) {
                if (StringUtils.isNotEmpty(exportInfo.getPerfEmpIdcard()) && !exportInfo.getPerfEmpIdcard().equals("card_is_Null")) {
                    exportInfo.setPerfEmpIdcard(Base64.desensitize(exportInfo.getPerfEmpIdcard()));
                }
                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = EmpPerformance.class.getDeclaredField(fieldName);
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

        File baseDir = new File("员工绩效信息");
        for (String key : groupedData.keySet()) {
            String excelName = exportDivide.getExcelName();
            if (excelName.contains("#")) {
                excelName = excelName.replace("#", key);
            }
            if (key.contains(" ")) {
                key = key.replace(" ", "");
            }

            String illegalCharsRegex = "[!@#$%^&*()_+\\[\\]{}|\\\\;:'\",.<>/?]";
            if (key.matches(illegalCharsRegex)) {
                key = key.split(illegalCharsRegex)[0];
            }

//            if (key.length()>10) {
//                key = key.substring(0, 10);
//            }

            File file = org.apache.commons.io.FileUtils.getFile(baseDir + File.separator + key + File.separator + excelName + ".xlsx");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<EmpPerformance> empPerformances = groupedData.get(key);
            ExcelUtil<EmpPerformance> util = new ExcelUtil<>(EmpPerformance.class);
            util.init(empPerformances, "员工绩效信息", excelName, Excel.Type.EXPORT);
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

    private EmpPerformanceChangeDicVO changeDicValue(List<EmpPerformance> exportInfos, String fieldName) {
        Field[] declaredFields = EmpPerformance.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (EmpPerformance exportInfo : exportInfos) {
                try {
                    Field field = EmpPerformance.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpPerformanceChangeDicVO empChangeDic = new EmpPerformanceChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        EmpPerformanceChangeDicVO empChangeDic = new EmpPerformanceChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }

    /**
     * 统计查询员工绩效列表
     *
     * @param empPerformance 员工绩效
     * @return 绩效统计集合
     */
    @Override
    public List<CountEmpPerformanceVO> countEmpPerformanceList(EmpPerformance empPerformance) {
        List<CountEmpPerformanceVO> list = empPerformanceMapper.countEmpPerformanceList(empPerformance);
        List<Long> deptIds = list.stream().map(CountEmpPerformanceVO::getPerfDeptId).collect(Collectors.toList());
        Map<Long, Department> deptMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            deptMap = departmentMapper.findDeptInfosByDeptIds(deptIds).stream().collect(Collectors.
                    toMap(Department::getDeptId, Function.identity()));
        }
        for (CountEmpPerformanceVO countEmpPerformanceVO : list) {
            Map<String, List<EmpPerformance>> map = countEmpPerformanceVO.getMap();
            EmpPerformance search = new EmpPerformance();
            BeanUtils.copyProperties(countEmpPerformanceVO, search);
            if (countEmpPerformanceVO.getPerfDeptId() != null) {
                String deptName = deptMap.getOrDefault(countEmpPerformanceVO.getPerfDeptId(), new Department()).getDeptName();
                countEmpPerformanceVO.setPerfDeptName(deptName);
            }
            List<EmpPerformance> empPerformanceList = empPerformanceMapper.selectEmpPerformanceList(search);
            for (EmpPerformance ep : empPerformanceList) {
                if (!map.containsKey(ep.getPerfLevelValue())) {
                    List<EmpPerformance> mapList = new ArrayList<>();
                    mapList.add(ep);
                    map.put(ep.getPerfLevelValue(), mapList);
                } else {
                    map.get(ep.getPerfLevelValue()).add(ep);
                }
            }
        }
        return list;
    }


    /**
     * 统计查询员工绩效-详情
     *
     * @param empPerformance 员工绩效
     * @return 绩效统计集合
     */
    @Override
    public Map countEmpPerformanceDetails(EmpPerformance empPerformance) {
        Map<String, List<EmpPerformance>> details = new HashMap();
        List<EmpPerformance> list = empPerformanceMapper.selectEmpPerformanceList(empPerformance);
        Map<String, Integer> counts = new HashMap<>();

        for (EmpPerformance e : list) {
            List<EmpPerformance> resultList = null;
            if (!details.containsKey(e.getPerfLevelValue())) {
                resultList = new ArrayList<>();
                details.put(e.getPerfLevelValue(), resultList);
                counts.put(e.getPerfLevelValue(), 1);
            } else {
                resultList = (List<EmpPerformance>) details.get(e.getPerfLevelValue());
                counts.put(e.getPerfLevelValue(), counts.get(e.getPerfLevelValue()) + 1);
            }
            resultList.add(e);
        }
        int sum = 0;
        for (List l : details.values()) {
            sum += l.size();

        }
        counts.put("sum", sum);
        Map map = new HashMap();
        map.put("res", counts);
        map.put("details", details);
        return map;
    }


    /**
     * 新增员工绩效
     *
     * @param empPerformance 员工绩效
     * @return 结果
     */
    @Override
    public int insertEmpPerformance(EmpPerformance empPerformance) {

        if (empPerformance.getFlag() == 1) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empPerformance.getPerfEmpId());
            empPerformance.setPerfEmpIdcard(e.getEmpIdcard());
            empPerformance.setPerfEmpName(e.getEmpName());
            empPerformance.setPerfDeptId(e.getEmpDeptId());
            empPerformance.setPerfDeptName(e.getEmpDeptName());
        } else {
            LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(empPerformance.getPerfEmpId());
            empPerformance.setPerfEmpIdcard(loanWorker.getLoanEmpIdcard());
            empPerformance.setPerfEmpName(loanWorker.getLoanEmpName());
            empPerformance.setPerfDeptId(loanWorker.getLoanApplyDeptId());
            empPerformance.setPerfDeptName(loanWorker.getLoanApplyDeptName());
        }
        return empPerformanceMapper.insertEmpPerformance(empPerformance);
    }

    /**
     * 修改员工绩效
     *
     * @param empPerformance 员工绩效
     * @return 结果
     */
    @Override
    public int updateEmpPerformance(EmpPerformance empPerformance) {

        if (empPerformance.getFlag() == 1) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empPerformance.getPerfEmpId());
            empPerformance.setPerfEmpIdcard(e.getEmpIdcard());
            empPerformance.setPerfEmpName(e.getEmpName());
            empPerformance.setPerfDeptId(e.getEmpDeptId());
            empPerformance.setPerfDeptName(e.getEmpDeptName());
        } else {
            LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(empPerformance.getPerfEmpId());
            empPerformance.setPerfEmpIdcard(loanWorker.getLoanEmpIdcard());
            empPerformance.setPerfEmpName(loanWorker.getLoanEmpName());
            empPerformance.setPerfDeptId(loanWorker.getLoanApplyDeptId());
            empPerformance.setPerfDeptName(loanWorker.getLoanApplyDeptName());
        }
        //  empPerformance.setUpdateTime(DateUtils.getNowDate());
        return empPerformanceMapper.updateEmpPerformance(empPerformance);
    }

    /**
     * 批量删除员工绩效
     *
     * @param perfIds 需要删除的员工绩效主键
     * @return 结果
     */
    @Override
    public int deleteEmpPerformanceByPerfIds(Long[] perfIds) {
        return empPerformanceMapper.deleteEmpPerformanceByPerfIds(perfIds);
    }

    /**
     * 删除员工绩效信息
     *
     * @param perfId 员工绩效主键
     * @return 结果
     */
    @Override
    public int deleteEmpPerformanceByPerfId(Long perfId) {
        return empPerformanceMapper.deleteEmpPerformanceByPerfId(perfId);
    }


    public void downloadExcelTemplate(HttpServletResponse response) {
//        InputStream inputStream = null;
//        try {
//            ClassPathResource classPathResource = new ClassPathResource("static/empPerformance.xlsx");
//            inputStream = classPathResource.getInputStream();
//            ExcelUtil.downLoadExcel("empPerformance", response, WorkbookFactory.create(inputStream));
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

            List<String> cycleDic = dictDataMapper.selectDictDataByType("performance_cycle").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

            List<String> levelDic = dictDataMapper.selectDictDataByType("performance_level").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());
            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(2, cycleDic);
            downDropMap.put(3, levelDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
            List<PerformanceTemplateVO> list = new ArrayList<>();

            String fileName = URLEncoder.encode("绩效模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, PerformanceTemplateVO.class).
                    registerWriteHandler(downWriteHandler).sheet("绩效模板").doWrite(list);
        } catch (IOException e) {
            throw new ServiceException("下载绩效模板失败");
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

    @Override
    public ImportResultVO uploadEmpPerformanceInfosFile(MultipartFile multipartFile, Long userId, String username, Integer flag) {
        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.TWELVE.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<PerformanceAddVO> excelDate = getExcelDate(multipartFile, PerformanceAddVO.class, flag);
        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertPostInfos(excelDate.getList(), flag);
        }

        int successCount = CollectionUtils.isEmpty(excelDate.getList()) ? 0 : excelDate.getList().size();
        int errorCount = CollectionUtils.isEmpty(failReport) ? 0 : failReport.size();

        Long failFileId = null;
        String failFileUrl = null;
        fileImportRecord.setImportStatus(FileImportStatusEnum.FINISHED.getKey());
        if (errorCount != 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("错误信息", "Sheet1", ExcelType.XSSF),
                    ErrorForm.class, failReport);
            MultipartFile file = FileUtils.workbookToCommonsMultipartFile(workbook, multipartFile.getOriginalFilename() + "错误信息" + nowStr + ".xlsx");
            if (file != null) {
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_PERFORM_FAIL_INFO.getKey(), "perform", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }

        Long fileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_PERFORM_INFO.getKey(), "perform", multipartFile, username).getFileId();
        fileImportRecord.setOriginFileId(fileId);
        fileImportRecord.setSuccessCount(successCount);
        fileImportRecord.setFailureCount(errorCount);
        fileImportRecord.setTotalCount(successCount + errorCount);
        fileImportRecord.setFailFileId(failFileId);
        fileImportRecordMapper.updateFileImportRecord(fileImportRecord);

        ImportResultVO importResult = new ImportResultVO();
        importResult.setTotalCount(successCount + errorCount);
        importResult.setSuccessCount(successCount);
        importResult.setErrorCount(errorCount);
        importResult.setFailFileId(failFileId);
        importResult.setFailFileUrl(failFileUrl);
        return importResult;
    }

    private void batchInsertPostInfos(List<PerformanceAddVO> performanceAddVOS, Integer flag) {

        Map<Long, Department> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        for (PerformanceAddVO performanceAddVO : performanceAddVOS) {
            EmpPerformance empPerformance = new EmpPerformance();

            BeanUtils.copyProperties(performanceAddVO, empPerformance);
            empPerformance.setFlag(flag);
            List<SysDictData> performance_cycles = dictDataMapper.selectDictDataByType("performance_cycle");
            for (SysDictData performance_cycle : performance_cycles) {
                if (performance_cycle.getDictLabel().equals(performanceAddVO.getPerfCycle())) {
                    empPerformance.setPerfCycle(performance_cycle.getDictValue());
                }
            }

//            for (PerfCycleEnum value : PerfCycleEnum.values()) {
//                if (value.getValue().equals(performanceAddVO.getPerfCycle())) {
//                    empPerformance.setPerfCycle(value.getKey());
//                }
//            }

            //     Department department = departmentMapper.findDepartmentInfoByUnitAndDeptName(performanceAddVO.getUnitName(),performanceAddVO.getPerfDeptName());
//            if (performanceAddVO.getPerfEmpIdcard() != null) {
//                Employee e = employeeMapper.findInfoByEmpIdCard(performanceAddVO.getPerfEmpIdcard());
//                empPerformance.setPerfDeptId(e.getEmpDeptId());
//                empPerformance.setPerfDeptName(e.getEmpDeptName());
//                empPerformance.setCreateTime(DateUtils.getNowDate());
//                empPerformance.setPerfEmpName(e.getEmpName());
//                insertEmpPerformance(empPerformance);
//            } else {
//                Employee e = employeeMapper.findEmpInfoByEmpName(performanceAddVO.getPerfEmpName());
//                empPerformance.setPerfDeptId(e.getEmpDeptId());
//                empPerformance.setPerfEmpIdcard(e.getEmpIdcard());
//                empPerformance.setPerfDeptName(e.getEmpDeptName());
//                empPerformance.setCreateTime(DateUtils.getNowDate());
//                empPerformance.setPerfEmpIdcard(e.getEmpIdcard());
//                insertEmpPerformance(empPerformance);
//            }
            if (flag == 1) {
                Employee employee = employeeMapper.findEmpInfoByEmpName(performanceAddVO.getPerfEmpName());
                if (employee != null) {
                    empPerformance.setPerfDeptId(employee.getEmpDeptId());
                    if (employee.getEmpDeptId() != null) {
                        empPerformance.setPerfDeptName(deptMap.getOrDefault(employee.getEmpDeptId(), new Department()).getDeptName());
                    }
                    empPerformance.setPerfEmpId(employee.getEmpId());
                    empPerformance.setCreateTime(DateUtils.getNowDate());
                    empPerformance.setPerfEmpName(employee.getEmpName());
                }
            }
            if (flag == 2) {
                LoanWorker loanWorker = loanWorkerMapper.findInfoByName(performanceAddVO.getPerfEmpName());
                if (loanWorker != null) {
                    empPerformance.setPerfDeptId(loanWorker.getLoanApplyDeptId());
                    if (loanWorker.getLoanApplyDeptId() != null) {
                        empPerformance.setPerfDeptName(deptMap.getOrDefault(loanWorker.getLoanApplyDeptId(), new Department()).getDeptName());
                    }
                    empPerformance.setPerfEmpId(loanWorker.getLoanId());
                    empPerformance.setCreateTime(DateUtils.getNowDate());
                    empPerformance.setPerfEmpName(loanWorker.getLoanEmpName());
                }
            }
            insertEmpPerformance(empPerformance);
        }

    }


    @Autowired
    private EmpPerformanceExcelImportVerifyHandler empPerformanceExcelImportVerifyHandler;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<PerformanceAddVO> getExcelDate(MultipartFile file, Class<PerformanceAddVO> postImportClass,
                                                             Integer flag) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            empPerformanceExcelImportVerifyHandler.setFlag(flag);
            params.setVerifyHandler(empPerformanceExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), postImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = empPerformanceExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
