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
import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.handler.DeptPerformanceExcelImportVerifyHandler;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.DeptPerformanceMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.service.IDeptPerformanceService;
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
 * 部门绩效Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Service
public class DeptPerformanceServiceImpl implements IDeptPerformanceService {
    @Autowired
    private DeptPerformanceMapper deptPerformanceMapper;

    /**
     * 查询部门绩效
     *
     * @param dpId 部门绩效主键
     * @return 部门绩效
     */
    @Override
    public DeptPerformance selectDeptPerformanceByDpId(Long dpId) {
        return deptPerformanceMapper.selectDeptPerformanceByDpId(dpId);
    }

    @Override
    public List<DeptPerformance> selectDeptPerformanceListByCycle(DeptPerformance deptPerformance){
        List<DeptPerformance> list = deptPerformanceMapper.selectDeptPerformanceListByCycle(deptPerformance);

        return list;
    }

    public List<DeptPerformance> selectDeptPerformanceListByScreen(DeptPerformanceScreenVO deptPerformanceScreenVO){
        return deptPerformanceMapper.selectDeptPerformanceListByScreen(deptPerformanceScreenVO);
    }


    @Override
    public List<DeptPerformance> findExportInfos(ExportVO export) {

        return deptPerformanceMapper.findExportInfos(export);
    }


    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<DeptPerformance> exportInfos = deptPerformanceMapper.findExportInfos(export);
        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }
        DeptPerformChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();


        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<DeptPerformance>> groupedData = exportInfos.stream()
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
            List<DeptPerformance> exportList = groupedData.get(key);
            for (DeptPerformance exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = DeptPerformance.class.getDeclaredField(fieldName);
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

        File baseDir = new File("部门绩效信息");
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
            List<DeptPerformance> deptPerformances = groupedData.get(key);
            ExcelUtil<DeptPerformance> util = new ExcelUtil<DeptPerformance>(DeptPerformance.class);
            util.init(deptPerformances, "部门绩效信息", excelName, Excel.Type.EXPORT);
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

    private DeptPerformChangeDicVO changeDicValue(List<DeptPerformance> exportInfos, String fieldName) {

        Field[] declaredFields = DeptPerformance.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (DeptPerformance exportInfo : exportInfos) {
                try {
                    Field field = DeptPerformance.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    DeptPerformChangeDicVO empChangeDic = new DeptPerformChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        DeptPerformChangeDicVO empChangeDic = new DeptPerformChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }


    /**
     * 查询部门绩效列表
     *
     * @param deptPerformance 部门绩效
     * @return 部门绩效
     */
    @Override
    public List<DeptPerformance> selectDeptPerformanceList(DeptPerformance deptPerformance) {

        return deptPerformanceMapper.selectDeptPerformanceList(deptPerformance);
    }


    @Override
    public List<DeptPerformance> selectDeptPerformanceListByParamsVO(DeptPerformanceParamsVO deptPerformanceParamsVO) {
        return deptPerformanceMapper.selectDeptPerformanceListByParamsVO(deptPerformanceParamsVO);
    }



    /**
     * 查询部门年度和季度绩效列表
     *
     * @param deptPerformance 部门绩效
     * @return 部门绩效集合
     */
    @Override
    public List<CountDeptPerformanceVO> countDeptPerformances(DeptPerformance deptPerformance) {
        return deptPerformanceMapper.countDeptPerformances(deptPerformance);
    }


    /**
     * 新增部门绩效
     *
     * @param deptPerformance 部门绩效
     * @return 结果
     */
    @Override
    public int insertDeptPerformance(DeptPerformance deptPerformance) {
        // deptPerformance.setCreateTime(DateUtils.getNowDate());

        return deptPerformanceMapper.insertDeptPerformance(deptPerformance);
    }

    /**
     * 修改部门绩效
     *
     * @param deptPerformance 部门绩效
     * @return 结果
     */
    @Override
    public int updateDeptPerformance(DeptPerformance deptPerformance) {
        //      deptPerformance.setUpdateTime(DateUtils.getNowDate());
        return deptPerformanceMapper.updateDeptPerformance(deptPerformance);
    }

    /**
     * 批量删除部门绩效
     *
     * @param dpIds 需要删除的部门绩效主键
     * @return 结果
     */
    @Override
    public int deleteDeptPerformanceByDpIds(Long[] dpIds) {
        return deptPerformanceMapper.deleteDeptPerformanceByDpIds(dpIds);
    }

    /**
     * 删除部门绩效信息
     *
     * @param dpId 部门绩效主键
     * @return 结果
     */
    @Override
    public int deleteDeptPerformanceByDpId(Long dpId) {
        return deptPerformanceMapper.deleteDeptPerformanceByDpId(dpId);
    }

    public void downloadExcelTemplate(HttpServletResponse response) {
//        InputStream inputStream = null;
//        try {
//            ClassPathResource classPathResource = new ClassPathResource("static/deptPerformance.xlsx");
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

            List<String> levelDic = dictDataMapper.selectDictDataByType("performance_level").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());
            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(3, cycleDic);
            downDropMap.put(4, levelDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
            List<DeptPerformanceTemplateVO> list = new ArrayList<>();

            String fileName = URLEncoder.encode("绩效模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, DeptPerformanceTemplateVO.class).
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
    public ImportResultVO uploadDeptPerformanceInfosFile(MultipartFile multipartFile, Long userId, String username) {
        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.SIX.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<DeptPerformanceAddVO> excelDate = getExcelDate(multipartFile, DeptPerformanceAddVO.class);
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

    private void batchInsertPostInfos(List<DeptPerformanceAddVO> deptPerformanceAddVOs) {

        for (DeptPerformanceAddVO deptPerformanceAddVO : deptPerformanceAddVOs) {
            DeptPerformance deptPerformance = new DeptPerformance();

            BeanUtils.copyProperties(deptPerformanceAddVO, deptPerformance);
//            for (PerfCycleEnum value : PerfCycleEnum.values()) {
//                if (value.getValue().equals(deptPerformanceAddVO.getDpCycle())) {
//                    deptPerformance.setDpCycle(value.getKey());
//                }
//            }

            List<SysDictData> performance_cycles = dictDataMapper.selectDictDataByType("performance_cycle");
            for (SysDictData performance_cycle : performance_cycles) {
                if (performance_cycle.getDictLabel().equals(deptPerformanceAddVO.getDpCycle())) {
                    deptPerformance.setDpCycle(performance_cycle.getDictValue());
                }
            }

            Department department = departmentMapper.findDepartmentInfoByUnitAndDeptName(deptPerformanceAddVO.getUnitName(), deptPerformanceAddVO.getDpDeptName());
            deptPerformance.setDpDeptId(department.getDeptId());
            deptPerformance.setCreateTime(DateUtils.getNowDate());

            insertDeptPerformance(deptPerformance);
        }
    }


    @Autowired
    private DeptPerformanceExcelImportVerifyHandler deptPerformanceExcelImportVerifyHandler;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<DeptPerformanceAddVO> getExcelDate(MultipartFile file, Class<DeptPerformanceAddVO> postImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(deptPerformanceExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), postImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = deptPerformanceExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
