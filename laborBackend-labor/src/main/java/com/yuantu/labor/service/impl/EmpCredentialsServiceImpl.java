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
import com.yuantu.labor.handler.CredentialsExcelImportVerifyHandler;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.mapper.EmpCredentialsMapper;
import com.yuantu.labor.mapper.EmpDocumentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.service.IEmpCredentialsService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysConfigMapper;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * 资格证书Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Service
public class EmpCredentialsServiceImpl implements IEmpCredentialsService {
    @Autowired
    private EmpCredentialsMapper empCredentialsMapper;

    @Autowired
    private SysConfigMapper configMapper;


    @Autowired
    private EmpDocumentMapper empDocumentMapper;


//    @Autowired
//    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private FileService fileService;


    /**
     * 查询资格证书
     *
     * @param credId 资格证书主键
     * @return 资格证书
     */
    @Override
    public EmpCredentials selectEmpCredentialsByCredId(Long credId) {
        return empCredentialsMapper.selectEmpCredentialsByCredId(credId);
    }

    @Override
    public List<EmpCredentials> findExportInfos(ExportVO export) {
        List<EmpCredentials> exportInfos = empCredentialsMapper.findExportInfos(export);
        List<Long> empIds = exportInfos.stream().map(EmpCredentials::getCredEmpId).collect(Collectors.toList());
        List<Long> existEmpIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(empIds)) {
            existEmpIds = employeeMapper.findEmpInfosByIdsWithoutDisabled(empIds).stream().map(Employee::getEmpId).collect(Collectors.toList());
        }
        List<Long> finalExistEmpIds = existEmpIds;
        exportInfos = exportInfos.stream().filter(s -> finalExistEmpIds.contains(s.getCredEmpId())).collect(Collectors.toList());
        return exportInfos;
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<EmpCredentials> exportInfos = findExportInfos(export);


        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }
        //-------------------导出不脱敏----------------------
        List<Employee> employeeList = employeeMapper.findAllEmployees();
        Map<Long, Employee> empMap = employeeList.stream().collect(Collectors.
                toMap(Employee::getEmpId, Function.identity()));

        for (EmpCredentials ea : exportInfos) {
            ea.setCredEmpIdcard((empMap.get(ea.getCredEmpId()) != null && empMap.get(ea.getCredEmpId()).getEmpIdcard() != null ? empMap.get(ea.getCredEmpId()).getEmpIdcard() : "card_is_Null"));
        }
        //----------------------------------------
        EmpCredentialsChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<EmpCredentials>> groupedData = exportInfos.stream()
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
            List<EmpCredentials> exportList = groupedData.get(key);
            for (EmpCredentials exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = EmpCredentials.class.getDeclaredField(fieldName);
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

        File baseDir = new File("员工资格证书信息");
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
            List<EmpCredentials> empCredentials = groupedData.get(key);
            for (EmpCredentials empCredential : empCredentials) {
                if (!StringUtils.isEmpty(empCredential.getCredEmpIdcard()) && !empCredential.getCredEmpIdcard().equals("card_is_Null")) {
                    empCredential.setCredEmpIdcard(Base64.desensitize(empCredential.getCredEmpIdcard()));
                }
            }
            ExcelUtil<EmpCredentials> util = new ExcelUtil<>(EmpCredentials.class);
            util.init(empCredentials, "员工资格证书信息", excelName, Excel.Type.EXPORT);
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

    private EmpCredentialsChangeDicVO changeDicValue(List<EmpCredentials> exportInfos, String fieldName) {

        Field[] declaredFields = EmpCredentials.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (EmpCredentials exportInfo : exportInfos) {
                try {
                    Field field = EmpCredentials.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpCredentialsChangeDicVO empChangeDic = new EmpCredentialsChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        EmpCredentialsChangeDicVO empChangeDic = new EmpCredentialsChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }

    /**
     * 查询资格证书列表
     *
     * @param empCredentials 资格证书
     * @return 资格证书
     */
    @Override
    public List<EmpCredentials> selectEmpCredentialsList(EmpCredentials empCredentials) {
        return empCredentialsMapper.selectEmpCredentialsList(empCredentials);
    }

    /**
     * 查询资格证书详情
     *
     * @param empCredentials 资格证书
     * @return 资格证书
     */
    @Override
    public List<CredentialsDetailsVO> selectEmpCredentialsVOList(EmpCredentials empCredentials) {

        List<CredentialsDetailsVO> list = empCredentialsMapper.selectEmpCredentialsVOList(empCredentials);
        List<Long> empIds = list.stream().map(CredentialsDetailsVO::getCredEmpId).collect(Collectors.toList());
        Map<Long, Employee> employeeMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(empIds)) {
            employeeMap = employeeMapper.selectEmployeeInfosByIds(empIds).stream().collect(Collectors.
                    toMap(Employee::getEmpId, Function.identity()));
        }
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                CredentialsDetailsVO vo = list.get(i);
                Employee existEmp = employeeMap.get(vo.getCredEmpId());
                if (existEmp == null) {
                    vo.setIsChange(false);
                } else {
                    vo.setIsChange(true);
                }
            }
        }
        for (CredentialsDetailsVO cdo : list) {
            Long credAualificationAnnex = cdo.getCredAualificationAnnex();
            EmpDocument credAualificationAnnexDoc = empDocumentMapper.selectEmpDocumentByDocId(credAualificationAnnex);

            cdo.setCredAualificationDocument(credAualificationAnnexDoc);

            List<Long> credRegistAnnexs = cdo.getCredRegistAnnex();
            for (Long credRegistAnnex : credRegistAnnexs) {
                EmpDocument credRegistAnnexDoc = empDocumentMapper.selectEmpDocumentByDocId(credRegistAnnex);
                cdo.getCredRegistAnnexDocuments().add(credRegistAnnexDoc);
            }

        }
        return addReminder(list);
    }


    @Override
    public Map<String, List<CredentialsCountVO>> countEmpCredentials(CredentialsCountParamsVO credentialsCountParamsVO) {
        Integer reminderTime = Integer.valueOf(configMapper.checkConfigKeyUnique("label.empCredentials.reminderTime").getConfigValue());
        credentialsCountParamsVO.setReminderTime(reminderTime);

        List<CredentialsCountVO> byDept = empCredentialsMapper.countEmpCredentialsByDept(credentialsCountParamsVO);
        List<CredentialsCountVO> byName = empCredentialsMapper.countEmpCredentialsByCredName(credentialsCountParamsVO);

        Map<String, List<CredentialsCountVO>> map = new HashMap<>();
        map.put("byDept", byDept);
        map.put("byType", byName);


        return map;
    }


    /**
     * 新增资格证书
     *
     * @param empCredentials 资格证书
     * @return 结果
     */
    @Override
    @Transactional
    public int insertEmpCredentials(EmpCredentials empCredentials) {

        if (empCredentials.getCredEmpId() != null) {
            Employee e = employeeMapper.selectEmployeeByEmpId(empCredentials.getCredEmpId());
            empCredentials.setCredEmpIdcard(e.getEmpIdcard());
            empCredentials.setCredEmpName(e.getEmpName());

        }
//        else if (empCredentials.getCredEmpIdcard() != null){
//            Employee e = employeeMapper.findInfoByEmpIdCard(empCredentials.getCredEmpIdcard());
//            empCredentials.setCredEmpId(e.getEmpId());
//            empCredentials.setCreateTime(DateUtils.getNowDate());
//        }
        else {
            Employee e = employeeMapper.findEmpInfoByEmpName(empCredentials.getCredEmpName());
            empCredentials.setCredEmpId(e.getEmpId());
            empCredentials.setCredEmpIdcard(e.getEmpIdcard());
        }
        empCredentials.setDisabled(0);
        int line = empCredentialsMapper.insertEmpCredentials(empCredentials);
        List<Long> list = new ArrayList<>();
        list.add(empCredentials.getCredAualificationAnnex());
        list.addAll(empCredentials.getCredRegistAnnex());
        empDocumentMapper.bindEmpInfos(empCredentials.getCredId(), list);

        return line;
    }

    /**
     * 修改资格证书
     *
     * @param empCredentials 资格证书
     * @return 结果
     */
    @Override
    @Transactional
    public int updateEmpCredentials(EmpCredentials empCredentials) {
        List<Long> removePaths = new ArrayList<>();
        List<Long> updatePaths = new ArrayList<>();
        EmpCredentials oldEmpCredentials = empCredentialsMapper.selectEmpCredentialsByCredId(empCredentials.getCredId());

        //删除新的路径中不存在的路径ID
        if (oldEmpCredentials.getCredAualificationAnnex() != null
                && !oldEmpCredentials.getCredAualificationAnnex().equals(empCredentials.getCredAualificationAnnex())) {
            removePaths.add(oldEmpCredentials.getCredAualificationAnnex());
        }

        for (Long oldPath : oldEmpCredentials.getCredRegistAnnex()) {
            if (!empCredentials.getCredRegistAnnex().contains(oldPath)) {
                removePaths.add(oldPath);
            }
        }

        if (removePaths.size() != 0) {
            empDocumentMapper.removeInfoByDocIds(removePaths.toArray(new Long[removePaths.size()]));
            //绑定业务ID
            updatePaths.add(empCredentials.getCredAualificationAnnex());
            updatePaths.addAll(empCredentials.getCredRegistAnnex());
            empDocumentMapper.bindEmpInfos(empCredentials.getCredId(), updatePaths);
        }
        return empCredentialsMapper.updateEmpCredentials(empCredentials);
    }

    /**
     * 批量删除资格证书
     *
     * @param credIds 需要删除的资格证书主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEmpCredentialsByCredIds(Long[] credIds) {
        List<Long> docIds = new ArrayList<>();
        for (Long credId : credIds) {
            EmpCredentials ec = empCredentialsMapper.selectEmpCredentialsByCredId(credId);
            docIds.add(ec.getCredAualificationAnnex());
            docIds.addAll(ec.getCredRegistAnnex());
        }
        empDocumentMapper.removeInfoByDocIds(docIds.toArray(new Long[docIds.size()]));

        return empCredentialsMapper.deleteEmpCredentialsByCredIds(credIds);
    }

    /**
     * 删除资格证书信息
     *
     * @param credId 资格证书主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEmpCredentialsByCredId(Long credId) {
        EmpCredentials empCredentials = empCredentialsMapper.selectEmpCredentialsByCredId(credId);
        List<Long> docIds = empCredentials.getCredRegistAnnex();
        docIds.add(empCredentials.getCredAualificationAnnex());
        empDocumentMapper.removeInfoByDocIds(docIds.toArray(new Long[docIds.size()]));
        return empCredentialsMapper.deleteEmpCredentialsByCredId(credId);
    }


    /**
     * 添加提醒标识
     *
     * @param Credentials
     * @return 结果
     */
    private List<CredentialsDetailsVO> addReminder(List<CredentialsDetailsVO> Credentials) {
        //获取设置的到期时间参数
        Integer reminderTime = Integer.valueOf(configMapper.checkConfigKeyUnique("label.empCredentials.reminderTime").getConfigValue());
        for (CredentialsDetailsVO ec : Credentials) {
            if (ec.getCredExpTime() != null && ec.getRemainingDays() > 0 && DateUtils.differentDaysByMillisecond(new Date(), ec.getCredExpTime()) < reminderTime) {
                ec.setReminder(1);
            } else {
                ec.setReminder(0);
            }
        }
        return Credentials;
    }

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Override
    public ImportResultVO uploadPostInfosFile(MultipartFile multipartFile, Long userId, String username) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.THREE.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<CredentialsAddVO> excelDate = getExcelDate(multipartFile, CredentialsAddVO.class);
        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertPostInfos(excelDate.getList());
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_FAIL_INFO.getKey(), "employee", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_INFO.getKey(), "employee", multipartFile, username).getFileId();

        fileImportRecord.setSuccessCount(successCount);
        fileImportRecord.setFailureCount(errorCount);
        fileImportRecord.setTotalCount(successCount + errorCount);
        fileImportRecord.setOriginFileId(empInfoFileId);
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

    private void batchInsertPostInfos(List<CredentialsAddVO> credentialInfos) {

        for (CredentialsAddVO credentialsAddVO : credentialInfos) {
            EmpCredentials credentials = new EmpCredentials();
            BeanUtils.copyProperties(credentialsAddVO, credentials);

//            Employee employee = employeeMapper.findInfoByHistoryEmpIdCard(credentialsAddVO.getCredEmpIdcard());
//            credentials.setCredEmpName(employee.getEmpName());

            Employee employee = employeeMapper.findEmpInfoByEmpName(credentialsAddVO.getCredEmpName());
            credentials.setCredEmpId(employee.getEmpId());
            credentials.setCredEmpIdcard(employee.getEmpIdcard());

            List<SysDictData> cred_types = dictDataMapper.selectDictDataByType("cred_type");
            for (SysDictData cred_type : cred_types) {
                if (cred_type.getDictLabel().equals(credentialsAddVO.getCredType())) {
                    credentials.setCredType(cred_type.getDictValue());
                }
            }


//            for (CredentialsTypeEnum value : CredentialsTypeEnum.values()) {
//                if (value.getValue().equals(credentialsAddVO.getCredType())) {
//                    credentials.setCredType(value.getKey());
//                }
//            }
            credentials.setCreateTime(DateUtils.getNowDate());
            credentials.setDisabled(0);
            insertEmpCredentials(credentials);
        }
    }

    @Autowired
    private CredentialsExcelImportVerifyHandler credentialsExcelImportVerifyHandler;


    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<CredentialsAddVO> getExcelDate(MultipartFile file, Class<CredentialsAddVO> postImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(credentialsExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), postImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = credentialsExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }

    public void downloadExcelTemplate(HttpServletResponse response) {
//        InputStream inputStream = null;
//        try {
//            ClassPathResource classPathResource = new ClassPathResource("static/credentials.xlsx");
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

            List<String> typeDic = dictDataMapper.selectDictDataByType("cred_type").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(2, typeDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
            List<CredentialsTemplateVO> list = new ArrayList<>();

            String fileName = URLEncoder.encode("证书模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, CredentialsTemplateVO.class).
                    registerWriteHandler(downWriteHandler).sheet("证书模板").doWrite(list);
        } catch (IOException e) {
            throw new ServiceException("下载证书模板失败");
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

}
