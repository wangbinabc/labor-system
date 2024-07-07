package com.yuantu.labor.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.HealthRecordsExcelImportVerifyHandler;
import com.yuantu.labor.mapper.EmpDocumentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.mapper.HealthRecordsMapper;
import com.yuantu.labor.service.IHealthRecordsService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工健康档案Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-09
 */
@Service
public class HealthRecordsServiceImpl implements IHealthRecordsService {
    @Autowired
    private HealthRecordsMapper healthRecordsMapper;

    @Autowired
    private EmpDocumentMapper empDocumentMapper;


    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询员工健康档案
     *
     * @param healthId 员工健康档案主键
     * @return 员工健康档案
     */
    @Override
    public HealthRecords selectHealthRecordsByHealthId(Long healthId) {
        HealthRecords healthRecords = healthRecordsMapper.selectHealthRecordsByHealthId(healthId);
        if (healthRecords != null) {
            EmpDocument ed = empDocumentMapper.selectEmpDocumentByDocId(healthRecords.getAnnexPath());
            healthRecords.setEmpDocument(ed);
        }
        return healthRecords;
    }


    private List<HealthRecords> isChange(List<HealthRecords> list) {
        for (HealthRecords healthRecords : list) {
            Employee e = employeeMapper.selectEmployeeByEmpId(healthRecords.getHealthEmpId());
            if (e == null) {
                healthRecords.setIsChange(false);
            }
        }

        return list;
    }

    @Override
    public List<HealthRecords> findExportInfos(ExportVO export) {

        return healthRecordsMapper.findExportInfos(export);
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<HealthRecords> exportInfos = findExportInfos(export);


        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }
        //-------------------导出不脱敏----------------------
        List<Employee> employeeList = employeeMapper.findAllEmployees();
        Map<Long, Employee> empMap = employeeList.stream().collect(Collectors.
                toMap(Employee::getEmpId, Function.identity()));

        for (HealthRecords ea : exportInfos) {
            ea.setHealthEmpIdcard((empMap.get(ea.getHealthEmpId()) != null && empMap.get(ea.getHealthEmpId()).getEmpIdcard() != null ? empMap.get(ea.getHealthEmpId()).getEmpIdcard() : "card_is_Null"));
        }
        //----------------------------------------

        HealthRecordsChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<HealthRecords>> groupedData = exportInfos.stream()
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
            List<HealthRecords> exportList = groupedData.get(key);
            for (HealthRecords exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = HealthRecords.class.getDeclaredField(fieldName);
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

        File baseDir = new File("员工健康档案");
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
            List<HealthRecords> list = groupedData.get(key);
            for (HealthRecords healthRecords : list) {
                if (StringUtils.isNotEmpty(healthRecords.getHealthEmpIdcard()) && !healthRecords.getHealthEmpIdcard().equals("card_is_Null")) {
                    healthRecords.setHealthEmpIdcard(Base64.desensitize(healthRecords.getHealthEmpIdcard()));
                }
            }
            ExcelUtil<HealthRecords> util = new ExcelUtil<>(HealthRecords.class);
            util.init(list, "员工健康档案", excelName, Excel.Type.EXPORT);
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


    private HealthRecordsChangeDicVO changeDicValue(List<HealthRecords> exportInfos, String fieldName) {
        Field[] declaredFields = HealthRecords.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (HealthRecords exportInfo : exportInfos) {
                try {
                    Field field = HealthRecords.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    HealthRecordsChangeDicVO empChangeDic = new HealthRecordsChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        HealthRecordsChangeDicVO empChangeDic = new HealthRecordsChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }

    /**
     * 查询员工健康档案列表
     *
     * @param healthRecordsVO 员工健康档案
     * @return 员工健康档案
     */
    @Override
    public List<HealthRecords> selectHealthRecordsList(HealthRecordsVO healthRecordsVO) {
        List<HealthRecords> list = healthRecordsMapper.selectHealthRecordsList(healthRecordsVO);
        for (HealthRecords h : list) {
            EmpDocument ed = empDocumentMapper.selectEmpDocumentByDocId(h.getAnnexPath());
            h.setEmpDocument(ed);
        }
        return isChange(list);
    }

    /**
     * 新增员工健康档案
     *
     * @param healthRecords 员工健康档案
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHealthRecords(HealthRecords healthRecords) {
        if (healthRecords.getHealthEmpId() != null) {
            Employee e = employeeMapper.selectEmployeeByEmpId(healthRecords.getHealthEmpId());
            healthRecords.setHealthEmpIdcard(e.getEmpIdcard());
            healthRecords.setHealthEmpName(e.getEmpName());
        } else {
            Employee e = employeeMapper.findEmpInfoByEmpName(healthRecords.getHealthEmpName());
            healthRecords.setHealthEmpId(e.getEmpId());
            healthRecords.setHealthEmpIdcard(e.getEmpIdcard());
        }
        int line = healthRecordsMapper.insertHealthRecords(healthRecords);
        if (healthRecords.getAnnexPath() != null) {
            List<Long> list = new ArrayList<>();
            list.add(healthRecords.getAnnexPath());
            empDocumentMapper.bindEmpInfos(healthRecords.getHealthId(), list);
        }
        //  healthRecords.setCreateTime(DateUtils.getNowDate());
        return line;
    }

    /**
     * 修改员工健康档案
     *
     * @param healthRecords 员工健康档案
     * @return 结果
     */
    @Override
    @Transactional
    public int updateHealthRecords(HealthRecords healthRecords) {

        HealthRecords old = healthRecordsMapper.selectHealthRecordsByHealthId(healthRecords.getHealthId());
        if (healthRecords.getAnnexPath() != null) {
            if (old.getAnnexPath() != null && !old.getAnnexPath().equals(healthRecords.getAnnexPath())) {
                empDocumentMapper.removeInfoByDocId(old.getAnnexPath());
                List<Long> list = new ArrayList<>();
                list.add(healthRecords.getAnnexPath());
                empDocumentMapper.bindEmpInfos(healthRecords.getHealthId(), list);
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(healthRecords.getHealthId()), Collections.singletonList("11"));
        }
        //  healthRecords.setUpdateTime(DateUtils.getNowDate());
        return healthRecordsMapper.updateHealthRecords(healthRecords);
    }

    /**
     * 批量删除员工健康档案
     *
     * @param healthIds 需要删除的员工健康档案主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteHealthRecordsByHealthIds(Long[] healthIds) {
        List<Long> docIds = new ArrayList<>();
        for (Long healthId : healthIds) {
            HealthRecords healthRecords = healthRecordsMapper.selectHealthRecordsByHealthId(healthId);
            docIds.add(healthRecords.getAnnexPath());
        }
        empDocumentMapper.removeInfoByDocIds(docIds.toArray(new Long[docIds.size()]));
        return healthRecordsMapper.deleteHealthRecordsByHealthIds(healthIds);
    }

    /**
     * 删除员工健康档案信息
     *
     * @param healthId 员工健康档案主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteHealthRecordsByHealthId(Long healthId) {
        HealthRecords healthRecords = healthRecordsMapper.selectHealthRecordsByHealthId(healthId);
        empDocumentMapper.removeInfoByDocId(healthRecords.getAnnexPath());
        return healthRecordsMapper.deleteHealthRecordsByHealthId(healthId);
    }


    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private FileService fileService;


    @Override
    public ImportResultVO uploadHealthRecordsInfosFile(MultipartFile multipartFile, Long userId, String username) {
        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.FIFTEEN.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<HealthRecordsAddVO> excelDate = getExcelDate(multipartFile, HealthRecordsAddVO.class);
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_HEALTH_INFO.getKey(), "health", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }

        Long fileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_HEALTH_INFO.getKey(), "health", multipartFile, username).getFileId();
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

    private void batchInsertPostInfos(List<HealthRecordsAddVO> healthRecordsAddVOS) {

        for (HealthRecordsAddVO healthRecordsAddVO : healthRecordsAddVOS) {
            HealthRecords healthRecord = new HealthRecords();
            BeanUtils.copyProperties(healthRecordsAddVO, healthRecord);
//            if (healthRecordsAddVO.getHealthEmpIdcard()!=null){
//                Employee e = employeeMapper.findInfoByEmpIdCard(healthRecordsAddVO.getHealthEmpIdcard());
//                healthRecord.setHealthEmpId(e.getEmpId());
//            }else {

            //   }
            insertHealthRecords(healthRecord);
        }
    }


    @Autowired
    private HealthRecordsExcelImportVerifyHandler healthRecordsExcelImportVerifyHandler;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<HealthRecordsAddVO> getExcelDate(MultipartFile file, Class<HealthRecordsAddVO> importClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(healthRecordsExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), importClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = healthRecordsExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
