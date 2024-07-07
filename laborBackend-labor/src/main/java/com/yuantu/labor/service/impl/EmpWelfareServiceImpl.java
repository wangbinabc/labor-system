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
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.domain.InsuranceConfiguration;
import com.yuantu.labor.handler.EmpWelfareExcelImportVerifyHandler;
import com.yuantu.labor.mapper.EmpWelfareMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.mapper.InsuranceConfigurationMapper;
import com.yuantu.labor.service.IEmpWelfareService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工福利Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-08
 */
@Service
public class EmpWelfareServiceImpl implements IEmpWelfareService {
    @Autowired
    private EmpWelfareMapper empWelfareMapper;

    @Autowired
    private InsuranceConfigurationMapper insuranceConfigurationMapper;

    @Autowired
    private EmployeeMapper employeeMapper;


    /**
     * 查询员工福利
     *
     * @param welfareId 员工福利主键
     * @return 员工福利
     */
    @Override
    public EmpWelfare selectEmpWelfareByWelfareId(Long welfareId) {
        return empWelfareMapper.selectEmpWelfareByWelfareId(welfareId);
    }

    /**
     * 查询员工福利列表
     *
     * @param empWelfare 员工福利
     * @return 员工福利
     */
    @Override
    public List<EmpWelfare> selectEmpWelfareList(EmpWelfare empWelfare)
    {
        return isChange(empWelfareMapper.selectEmpWelfareList(empWelfare));
    }

    private List<EmpWelfare> isChange(List<EmpWelfare> list){
        for (EmpWelfare welfare : list){
            Employee e = employeeMapper.selectEmployeeByEmpId(welfare.getWelfareEmpId());
            if (e==null){
                welfare.setIsChange(false);
            }
        }

        return list;
    }

    @Override
    public List<EmpWelfare> findExportInfos(ExportVO export) {

        return empWelfareMapper.findExportInfos(export);
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<EmpWelfare> exportInfos = findExportInfos(export);


        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }
        //-------------------导出不脱敏----------------------
        List<Employee> employeeList = employeeMapper.findAllEmployees();
        Map<Long, Employee> empMap =  employeeList.stream().collect(Collectors.
                toMap(Employee::getEmpId, Function.identity()));

        for (EmpWelfare ea:exportInfos){
            ea.setWelfareEmpIdcard(empMap.get(ea.getWelfareEmpId())!=null && empMap.get(ea.getWelfareEmpId()).getEmpIdcard()!=null ?empMap.get(ea.getWelfareEmpId()).getEmpIdcard():"card_is_Null");
        }
        //----------------------------------------
        EmpWelfareChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<EmpWelfare>> groupedData = exportInfos.stream()
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
            List<EmpWelfare> exportList = groupedData.get(key);
            for (EmpWelfare exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = EmpWelfare.class.getDeclaredField(fieldName);
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

        File baseDir = new File("员工福利信息");
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
            List<EmpWelfare> empWelfares = groupedData.get(key);
            for (EmpWelfare exportInfo : empWelfares){
                if (StringUtils.isNotEmpty(exportInfo.getWelfareEmpIdcard()) && !exportInfo.getWelfareEmpIdcard().equals("card_is_Null")) {
                    exportInfo.setWelfareEmpIdcard(Base64.desensitize(exportInfo.getWelfareEmpIdcard()));
                }
            }
            ExcelUtil<EmpWelfare> util = new ExcelUtil<>(EmpWelfare.class);
            util.init(empWelfares, "员工福利信息", excelName, Excel.Type.EXPORT);
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

    private EmpWelfareChangeDicVO changeDicValue(List<EmpWelfare> exportInfos, String fieldName) {
        Field[] declaredFields = EmpWelfare.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (EmpWelfare exportInfo : exportInfos) {
                try {
                    Field field = EmpWelfare.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpWelfareChangeDicVO empChangeDic = new EmpWelfareChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        EmpWelfareChangeDicVO empChangeDic = new EmpWelfareChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }

    /**
     * 新增员工福利
     *
     * @param empWelfare 员工福利
     * @return 结果
     */
    @Override
    public int insertEmpWelfare(EmpWelfare empWelfare) {
        if(empWelfare.getWelfareEmpId()!=null){
            Employee e = employeeMapper.selectEmployeeByEmpId(empWelfare.getWelfareEmpId());
            empWelfare.setWelfareEmpIdcard(e.getEmpIdcard());
            empWelfare.setWelfareEmpName(e.getEmpName());}
        else {
            Employee e = employeeMapper.findEmpInfoByEmpName(empWelfare.getWelfareEmpName());
            empWelfare.setWelfareEmpIdcard(e.getEmpIdcard());
            empWelfare.setWelfareEmpId(e.getEmpId());

        }
        empWelfare.setCreateTime(DateUtils.getNowDate());
        return empWelfareMapper.insertEmpWelfare(empWelfare);
    }

    /**
     * 修改员工福利
     *
     * @param empWelfare 员工福利
     * @return 结果
     */
    @Override
    public int updateEmpWelfare(EmpWelfare empWelfare) {
        empWelfare.setUpdateTime(DateUtils.getNowDate());
        return empWelfareMapper.updateEmpWelfare(empWelfare);
    }

    /**
     * 五险配置
     *
     * @param insuranceConfigurations 员工福利
     * @return 结果
     */
    @Transactional
    @Override
    public int baseConfig(List<InsuranceConfiguration> insuranceConfigurations) {
        for (InsuranceConfiguration insuranceConfiguration : insuranceConfigurations) {
            InsuranceConfiguration query = new InsuranceConfiguration();
            query.setType(insuranceConfiguration.getType());
            List<InsuranceConfiguration> record = insuranceConfigurationMapper.selectInsuranceConfigurationList(query);
            if (record == null || record.size() == 0) {
                insuranceConfigurationMapper.insertInsuranceConfiguration(insuranceConfiguration);
            } else {
                insuranceConfiguration.setCreateBy(null);
                insuranceConfigurationMapper.updateInsuranceConfiguration(insuranceConfiguration);

            }
        }

        return insuranceConfigurations.size();
    }

    /**
     * 批量删除员工福利
     *
     * @param welfareIds 需要删除的员工福利主键
     * @return 结果
     */
    @Override
    public int deleteEmpWelfareByWelfareIds(Long[] welfareIds) {
        return empWelfareMapper.deleteEmpWelfareByWelfareIds(welfareIds);
    }

    /**
     * 删除员工福利信息
     *
     * @param welfareId 员工福利主键
     * @return 结果
     */
    @Override
    public int deleteEmpWelfareByWelfareId(Long welfareId) {
        return empWelfareMapper.deleteEmpWelfareByWelfareId(welfareId);
    }


    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private FileService fileService;


    @Override
    public ImportResultVO uploadWelfareInfosFile(MultipartFile multipartFile, Long userId, String username) {
        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.FOURTEEN.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<EmpWelfareAddVO> excelDate = getExcelDate(multipartFile, EmpWelfareAddVO.class);
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_WELFARE_INFO.getKey(), "welfare", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }

        Long fileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_WELFARE_INFO.getKey(), "welfare", multipartFile, username).getFileId();
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

    private void batchInsertPostInfos(List<EmpWelfareAddVO> empWelfareAddVOS) {

        for (EmpWelfareAddVO empWelfareAddVO : empWelfareAddVOS) {
            EmpWelfare empWelfare = new EmpWelfare();

            BeanUtils.copyProperties(empWelfareAddVO, empWelfare);
//            Employee employee = employeeMapper.findInfoByHistoryEmpIdCard(empWelfareAddVO.getWelfareEmpIdcard());
//            empWelfare.setWelfareEmpId(employee.getEmpId());
//            empWelfare.setCreateTime(DateUtils.getNowDate());
            insertEmpWelfare(empWelfare);
        }
    }


    @Autowired
    private EmpWelfareExcelImportVerifyHandler empWelfareExcelImportVerifyHandler;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<EmpWelfareAddVO> getExcelDate(MultipartFile file, Class<EmpWelfareAddVO> postImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(empWelfareExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), postImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = empWelfareExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
