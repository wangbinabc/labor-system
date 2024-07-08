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
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.cenum.PerfCycleEnum;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.domain.ProvicePerformance;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.ProvicePerformanceExcelImportVerifyHandler;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.mapper.ProvicePerformanceMapper;
import com.yuantu.labor.service.IProvicePerformanceService;
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
import java.util.stream.Collectors;

/**
 * 省公司考核Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Service
public class ProvicePerformanceServiceImpl implements IProvicePerformanceService {
    @Autowired
    private ProvicePerformanceMapper provicePerformanceMapper;

    /**
     * 查询省公司考核
     *
     * @param ppId 省公司考核主键
     * @return 省公司考核
     */
    @Override
    public ProvicePerformance selectProvicePerformanceByPpId(Long ppId) {
        return provicePerformanceMapper.selectProvicePerformanceByPpId(ppId);
    }

    /**
     * 查询省公司考核列表
     *
     * @param provicePerformance 省公司考核
     * @return 省公司考核
     */
    @Override
    public List<ProvicePerformance> selectProvicePerformanceList(ProvicePerformance provicePerformance) {
        return provicePerformanceMapper.selectProvicePerformanceList(provicePerformance);
    }

    @Override
    public  List<ProvicePerformance> selectProvicePerformanceListByScreen(ProvicePerformanceScreenVO provicePerformanceScreenVO){
        return provicePerformanceMapper.selectProvicePerformanceListByScreen(provicePerformanceScreenVO);
    }

    /**
     * 新增省公司考核
     *
     * @param provicePerformance 省公司考核
     * @return 结果
     */
    @Override
    public int insertProvicePerformance(ProvicePerformance provicePerformance) {
        // provicePerformance.setCreateTime(DateUtils.getNowDate());
        Department department = departmentMapper.selectDepartmentByDeptId(provicePerformance.getPpDeptId());
        provicePerformance.setPpDeptName(department.getDeptName());
        return provicePerformanceMapper.insertProvicePerformance(provicePerformance);
    }

    @Override
    public List<ProvicePerformance> findExportInfos(ExportVO export) {

        return provicePerformanceMapper.findExportInfos(export);
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<ProvicePerformance> exportInfos = findExportInfos(export);
        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }

        ProvincePerformChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<ProvicePerformance>> groupedData = exportInfos.stream()
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

        String fieldName = exportDivide.getFieldName();
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        for (String key : groupedData.keySet()) {
            List<ProvicePerformance> exportList = groupedData.get(key);
            for (ProvicePerformance exportInfo : exportList) {
                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = ProvicePerformance.class.getDeclaredField(fieldName);
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


        File baseDir = new File("省公司考核信息");
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
            List<ProvicePerformance> list = groupedData.get(key);
            ExcelUtil<ProvicePerformance> util = new ExcelUtil<>(ProvicePerformance.class);
            util.init(list, "省公司考核信息", excelName, Excel.Type.EXPORT);
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

    private ProvincePerformChangeDicVO changeDicValue(List<ProvicePerformance> exportInfos, String fieldName) {
        Field[] declaredFields = ProvicePerformance.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (ProvicePerformance exportInfo : exportInfos) {
                try {
                    Field field = ProvicePerformance.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    ProvincePerformChangeDicVO empChangeDic = new ProvincePerformChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        ProvincePerformChangeDicVO empChangeDic = new ProvincePerformChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }

    /**
     * 修改省公司考核
     *
     * @param provicePerformance 省公司考核
     * @return 结果
     */
    @Override
    public int updateProvicePerformance(ProvicePerformance provicePerformance) {
        //   provicePerformance.setUpdateTime(DateUtils.getNowDate());
        Department department = departmentMapper.selectDepartmentByDeptId(provicePerformance.getPpDeptId());
        provicePerformance.setPpDeptName(department.getDeptName());
        return provicePerformanceMapper.updateProvicePerformance(provicePerformance);
    }

    /**
     * 批量删除省公司考核
     *
     * @param ppIds 需要删除的省公司考核主键
     * @return 结果
     */
    @Override
    public int deleteProvicePerformanceByPpIds(Long[] ppIds) {
        return provicePerformanceMapper.deleteProvicePerformanceByPpIds(ppIds);
    }

    /**
     * 删除省公司考核信息
     *
     * @param ppId 省公司考核主键
     * @return 结果
     */
    @Override
    public int deleteProvicePerformanceByPpId(Long ppId) {
        return provicePerformanceMapper.deleteProvicePerformanceByPpId(ppId);
    }

    public void downloadExcelTemplate(HttpServletResponse response) {
//        InputStream inputStream = null;
//        try {
//            ClassPathResource classPathResource = new ClassPathResource("static/provicePerformance.xlsx");
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

            List<String> cycleDic = dictDataMapper.selectDictDataByType("performance_cycle").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

//            List<String> levelDic = dictDataMapper.selectDictDataByType("performance_level").stream().
//                    map(SysDictData::getDictLabel).collect(Collectors.toList());
            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(3, cycleDic);
        //    downDropMap.put(4, levelDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
            List<ProvicePerformanceTemplateVO> list = new ArrayList<>();

            String fileName = URLEncoder.encode("绩效模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, ProvicePerformanceTemplateVO.class).
                    registerWriteHandler(downWriteHandler).sheet("绩效模板").doWrite(list);
        } catch (IOException e) {
            throw new ServiceException("下载绩效模板失败");
        }finally {
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

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ImportResultVO uploadProvicePerformanceInfosFile(MultipartFile multipartFile, Long userId, String username) {
        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.THIRTEEN.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<ProvicePerformanceAddVO> excelDate = getExcelDate(multipartFile, ProvicePerformanceAddVO.class);
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_PROVINCE_PERFORM_FAIL_INFO.getKey(), "province_perform", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }

        Long fileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_PROFIT_INFO.getKey(), "province_perform", multipartFile, username).getFileId();
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

    private void batchInsertPostInfos(List<ProvicePerformanceAddVO> performanceAddVOS) {

        for (ProvicePerformanceAddVO provicePerformanceAddVO : performanceAddVOS) {
            ProvicePerformance provicePerformance = new ProvicePerformance();

            BeanUtils.copyProperties(provicePerformanceAddVO, provicePerformance);
//            for (PerfCycleEnum value : PerfCycleEnum.values()) {
//                if (value.getValue().equals(provicePerformanceAddVO.getPpCycle())) {
//                    provicePerformance.setPpCycle(value.getKey());
//                }
//            }

            List<SysDictData> performance_cycles = dictDataMapper.selectDictDataByType("performance_cycle");
            for (SysDictData performance_cycle : performance_cycles) {
                if (performance_cycle.getDictLabel().equals(provicePerformanceAddVO.getPpCycle())) {
                    provicePerformance.setPpCycle(performance_cycle.getDictValue());
                }
            }

            //----------11.8日修改----------
            String[] names= provicePerformanceAddVO.getPpDeptName().split("-");
            Department department = departmentMapper.findDepartmentInfoByUnitAndDeptName(names[0], names[1]);
            provicePerformance.setPpDeptId(department.getDeptId());
            provicePerformance.setCreateTime(DateUtils.getNowDate());

//            String[] provicNames= provicePerformanceAddVO.getPpProvinceDeptName().split("-");
//            Department proviceDepartment = departmentMapper.findDepartmentInfoByUnitAndDeptName(provicNames[0],provicNames[1]);
//        provicePerformance.setPpProvinceDeptId(proviceDepartment.getDeptId());

            insertProvicePerformance(provicePerformance);
        }
    }


    @Autowired
    private ProvicePerformanceExcelImportVerifyHandler provicePerformanceExcelImportVerifyHandler;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<ProvicePerformanceAddVO> getExcelDate(MultipartFile file, Class<ProvicePerformanceAddVO> importClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(provicePerformanceExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), importClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = provicePerformanceExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
