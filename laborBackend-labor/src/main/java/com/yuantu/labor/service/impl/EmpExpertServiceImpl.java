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
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.EmpExpert;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.EmpExpertExcelImportVerifyHandler;
import com.yuantu.labor.mapper.EmpExpertMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.service.IEmpExpertService;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Service
public class EmpExpertServiceImpl implements IEmpExpertService {


    private static final Logger log = LoggerFactory.getLogger(EmpExpertServiceImpl.class);

    @Autowired
    private EmpExpertMapper empExpertMapper;

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private EmpExpertExcelImportVerifyHandler empExpertExcelImportVerifyHandler;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;


    /**
     * 查询【请填写功能名称】
     *
     * @param expertId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public EmpExpert selectEmpExpertByExpertId(Integer expertId) {
        EmpExpert empExpert = empExpertMapper.selectEmpExpertByExpertId(expertId);
        if(empExpert!=null){
            if(StringUtils.isNotEmpty(empExpert.getExpertEmpIdcard())){
                empExpert.setExpertEmpIdcard(Base64.desensitize(empExpert.getExpertEmpIdcard()));
            }
        }
        return empExpert;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param empExpert 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EmpExpert> selectEmpExpertList(EmpExpert empExpert) {
        return empExpertMapper.selectEmpExpertList(empExpert);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param empExpert 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEmpExpert(EmpExpert empExpert, String username) {
        Date date = new Date();
        empExpert.setCreateTime(date);
        empExpert.setCreateBy(username);
        empExpert.setExpertUpdateTime(date);
        return empExpertMapper.insertEmpExpert(empExpert);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param empExpert 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEmpExpert(EmpExpert empExpert, String username) {
        Date now = new Date();
        empExpert.setExpertUpdateTime(now);
        empExpert.setUpdateTime(now);
        empExpert.setUpdateBy(username);
        return empExpertMapper.updateEmpExpert(empExpert);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param expertIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteEmpExpertByExpertIds(Integer[] expertIds) {
        return empExpertMapper.deleteEmpExpertByExpertIds(expertIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param expertId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteEmpExpertByExpertId(Integer expertId) {
        return empExpertMapper.deleteEmpExpertByExpertId(expertId);
    }

    @Override
    public List<ExpertListVO> selectEmpExpertListByWhere(EmpExpertQueryVO queryVO) {
        List<ExpertListVO> result = empExpertMapper.selectEmpExpertListByWhere(queryVO);
        List<Long> empIds = result.stream().map(ExpertListVO::getExpertEmpId).collect(Collectors.toList());
        Map<Long, Employee> employeeMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(empIds)) {
            employeeMap = employeeMapper.selectEmployeeInfosByIds(empIds).stream().collect(Collectors.
                    toMap(Employee::getEmpId, Function.identity()));
        }
        for (ExpertListVO expertList : result) {
            Employee exitEmp = employeeMap.get(expertList.getExpertEmpId());
            if (exitEmp == null) {
                expertList.setIsChange(false);
            } else {
                expertList.setIsChange(true);
            }
            if(StringUtils.isNotEmpty(expertList.getExpertEmpIdcard())) {
                expertList.setExpertEmpIdcard(Base64.desensitize(expertList.getExpertEmpIdcard()));
            }
        }
        return result;
    }

    @Override
    public List<PieChartVO> countByDept() {
        return empExpertMapper.countByDept();
    }

    @Override
    public List<PieChartVO> countByTitle() {
        return empExpertMapper.countByTitle();
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) {

        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/empExpert.xlsx");
            inputStream = classPathResource.getInputStream();
            ExcelUtil.downLoadExcel("empExpert", response, WorkbookFactory.create(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public ImportResultVO importEmpExpertData(MultipartFile multipartFile, LoginUser loginUser) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.TEN.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(loginUser.getUserId());
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<EmpExpertImportVO> excelDate = getExcelDate(multipartFile, EmpExpertImportVO.class);

        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());

        String username = loginUser.getUsername();
        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertEmpExpertInfos(loginUser.getUsername(), excelDate.getList());
        }

        int successCount = CollectionUtils.isEmpty(excelDate.getList()) ? 0 : excelDate.getList().size();
        int errorCount = CollectionUtils.isEmpty(failReport) ? 0 : failReport.size();
        int totalCount = successCount + errorCount;
        Long failFileId = null;
        String failFileUrl = null;
        fileImportRecord.setImportStatus(FileImportStatusEnum.FINISHED.getKey());
        if (errorCount != 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("错误信息", "Sheet1", ExcelType.XSSF),
                    ErrorForm.class, failReport);
            MultipartFile file = FileUtils.workbookToCommonsMultipartFile(workbook, multipartFile.getOriginalFilename() + "错误信息" + nowStr + ".xlsx");
            if (file != null) {
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_EXPERT_FAIL_INFO.getKey(), "empExpert", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_EXPERT_INFO.getKey(), "empExpert", multipartFile, username).getFileId();

        fileImportRecord.setSuccessCount(successCount);
        fileImportRecord.setFailureCount(errorCount);
        fileImportRecord.setTotalCount(totalCount);
        fileImportRecord.setOriginFileId(empInfoFileId);
        fileImportRecord.setFailFileId(failFileId);
        fileImportRecordMapper.updateFileImportRecord(fileImportRecord);

        ImportResultVO importResult = new ImportResultVO();
        importResult.setTotalCount(totalCount);
        importResult.setSuccessCount(successCount);
        importResult.setErrorCount(errorCount);
        importResult.setFailFileId(failFileId);
        importResult.setFailFileUrl(failFileUrl);
        return importResult;
    }

    @Override
    public List<ExpertExportListVO> selectExportExportInfos(EmpExpertExportVO empExpertExport) {
        List<ExpertExportListVO> list = empExpertMapper.findExpertInfoByEmpIds(empExpertExport.getEmpIds());
        for ( ExpertExportListVO vo:list){
            if(StringUtils.isNotEmpty(vo.getExpertEmpIdcard())){
                vo.setExpertEmpIdcard(Base64.desensitize(vo.getExpertEmpIdcard()));
            }
        }
        return list;
    }

    @Override
    public void exportDivide(HttpServletResponse response, EmpExpertExportDivideVO empExpertExportDivide) {

        List<ExpertExportListVO> experts = empExpertMapper.findExpertInfoByEmpIds(empExpertExportDivide.getEmpIds());
        /**
        for ( ExpertExportListVO vo:experts){
            if(vo.getExpertEmpIdcard()!=null){
                vo.setExpertEmpIdcard(Base64.desensitize(vo.getExpertEmpIdcard()));
            }
        }
         **/
        if (CollectionUtils.isEmpty(experts)) {
            return;
        }

        EmpExpertChangeDicVO empChangeDic = changeDicValue(experts, empExpertExportDivide.getFieldName());
        experts = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = empExpertExportDivide.getFieldName();
        Map<String, List<ExpertExportListVO>> groupedData = experts.stream()
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
        String fieldName = empExpertExportDivide.getFieldName();
        for (String key : groupedData.keySet()) {
            List<ExpertExportListVO> exportList = groupedData.get(key);
            for (ExpertExportListVO exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = ExpertExportListVO.class.getDeclaredField(fieldName);
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

        File baseDir = new File("专家信息");
        for (String key : groupedData.keySet()) {
            String excelName = empExpertExportDivide.getExcelName();
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
            List<ExpertExportListVO> empExpertInfos = groupedData.get(key);
            for ( ExpertExportListVO vo:empExpertInfos){
                if(StringUtils.isNotEmpty(vo.getExpertEmpIdcard())){
                    vo.setExpertEmpIdcard(Base64.desensitize(vo.getExpertEmpIdcard()));
                }
            }

            ExcelUtil<ExpertExportListVO> util = new ExcelUtil<ExpertExportListVO>(ExpertExportListVO.class);
            util.init(empExpertInfos, "专家信息", excelName, Excel.Type.EXPORT);
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

    private EmpExpertChangeDicVO changeDicValue(List<ExpertExportListVO> experts, String fieldName) {
        Field[] declaredFields = ExpertExportListVO.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (ExpertExportListVO exportInfo : experts) {
                try {
                    Field field = ExpertExportListVO.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpExpertChangeDicVO empChangeDic = new EmpExpertChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(experts);
                    return empChangeDic;
                }
            }
        }
        EmpExpertChangeDicVO empChangeDic = new EmpExpertChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(experts);
        return empChangeDic;
    }

    private void batchInsertEmpExpertInfos(String username, List<EmpExpertImportVO> empExpertImports) {
        List<EmpExpert> empExperts = new ArrayList<>();
        //List<String> empIdCards = empExpertImports.stream().map(EmpExpertImportVO::getExpertEmpIdcard).collect(Collectors.toList());
        //Map<String, Employee> employeeMap = employeeMapper.findInfoByIdCards(empIdCards).stream().
        //        collect(Collectors.toMap(Employee::getEmpIdcard, Function.identity()));
        List<String> empNameList = empExpertImports.stream().map(EmpExpertImportVO::getExpertEmpName).collect(Collectors.toList());
        Map<String, Employee> existEmpMap = employeeMapper.findInfoEmpNames(empNameList).stream().collect(Collectors.toMap(Employee::getEmpName,Function.identity()));


        Date now = new Date();
        for (EmpExpertImportVO empExpertImportInfo : empExpertImports) {
            EmpExpert empExpert = new EmpExpert();
            empExpert.setCreateBy(username);
            empExpert.setCreateTime(now);
            empExpert.setExpertUpdateTime(now);
            empExpert.setDisabled(false);
            BeanUtils.copyProperties(empExpertImportInfo, empExpert);
            Date expertDismissTime = empExpertImportInfo.getExpertDismissTime();
            if (expertDismissTime == null) {
                empExpert.setExpertPeriod("终身");
            } else {
                Date expertGrantTime = empExpert.getExpertGrantTime();
                int i = calculateYearDifference(expertGrantTime, expertDismissTime);
                if (i == 0) {
                    empExpert.setExpertPeriod("不满一年");
                } else {
                    empExpert.setExpertPeriod(i + "年");
                }
            }

            //Employee existEmployee = employeeMap.getOrDefault(empExpertImportInfo.getExpertEmpIdcard(), new Employee());
            String empName = empExpert.getExpertEmpName();
            Employee existEmployee = existEmpMap.getOrDefault(empName, new Employee());

            Long empId = existEmployee.getEmpId();
            empExpert.setExpertEmpId(empId);
            empExpert.setExpertEmpIdcard(existEmployee.getEmpIdcard());
            //empExpert.setExpertEmpName(existEmployee.getEmpName());

            List<SysDictData> dictDataList = dictDataMapper.selectDictDataByType("expert_level");
            empExpert.setExpertLevel(dictDataList.stream().filter(s->s.getDictLabel().equals(empExpert.getExpertLevel())).findFirst().get().getDictValue());
            //System.out.println("*************************empExpert"+empExpert.toString());
            empExperts.add(empExpert);
        }
        empExpertMapper.batchInsertEmpExperts(empExperts);
    }

    public int calculateYearDifference(Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = startLocalDate.until(endLocalDate);
        return period.getYears();
    }


    private ExcelImportResult<EmpExpertImportVO> getExcelDate(MultipartFile file, Class<EmpExpertImportVO> empExpertImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(empExpertExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), empExpertImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = empExpertExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
    @Override
    public void downloadExperExcel(HttpServletResponse response) {
        List<String> expertLevelDic = sysDictDataMapper.selectDictDataByType("expert_level").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());
        Map<Integer, List<String>> downDropMap = new HashMap<>(16);
        downDropMap.put(2,expertLevelDic);

        DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        OutputStream outputStream = null;

        List<EmpExpertExcelVO> list = new ArrayList<>();
        EmpExpertExcelVO vo = new EmpExpertExcelVO();
        vo.setExpertEmpName("测试数据_张三");
        vo.setExpertTitle("测试数据_地市级专家");
        if(expertLevelDic!=null) {
            vo.setExpertLevel(expertLevelDic.get(0));
        }

        vo.setExpertGrantTime(new Date(2023-1900,10,01));
        vo.setExpertDismissTime(new Date(2024-1900,10,01));
        list.add(vo);

        try {
            String fileName = URLEncoder.encode("专家人才模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, EmpExpertExcelVO.class).
                    registerWriteHandler(downWriteHandler).sheet("专家人才信息").doWrite(list);
        } catch (IOException e) {
            throw new ServiceException("下载专家人才信息模板失败");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();//刷新流：通道中数据全部输出
                    outputStream.close();//关闭流
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
