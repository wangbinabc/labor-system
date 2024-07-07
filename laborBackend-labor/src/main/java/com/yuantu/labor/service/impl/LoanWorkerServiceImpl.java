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
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.LoanWorkExcelImportVerifyHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.ILoanWorkerService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import com.yuantu.system.service.ISysDictDataService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 借工Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Service
public class LoanWorkerServiceImpl implements ILoanWorkerService {
    private static final Logger log = LoggerFactory.getLogger(LoanWorkerServiceImpl.class);

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private LoanWorkExcelImportVerifyHandler loanWorkExcelImportVerifyHandler;

    @Autowired
    private EmployingUnitsMapper unitsMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询借工
     *
     * @param loanId 借工主键
     * @return 借工
     */
    @Override
    public LoanWorker selectLoanWorkerByLoanId(Long loanId) {
        LoanWorker loanWorker = loanWorkerMapper.selectLoanWorkerByLoanId(loanId);
        /**
         if(loanWorker!=null){
         if(loanWorker.getLoanEmpIdcard()!=null){
         loanWorker.setLoanEmpIdcard(Base64.desensitize(loanWorker.getLoanEmpIdcard()));
         }
         }
         **/
        return loanWorker;
    }

    /**
     * 查询借工列表
     *
     * @param loanWorker 借工
     * @return 借工
     */
    @Override
    public List<LoanWorker> selectLoanWorkerList(LoanWorker loanWorker) {
        return loanWorkerMapper.selectLoanWorkerList(loanWorker);
    }

    /**
     * 新增借工
     *
     * @param loanWorker 借工
     * @return 结果
     */
    @Override
    public int insertLoanWorker(LoanWorker loanWorker, String userName) {

        if (StringUtils.isNotEmpty(loanWorker.getLoanEmpName())) {
            Employee employee = employeeMapper.findEmpInfoByEmpName(loanWorker.getLoanEmpName());
            if (employee != null) {
                throw new ServiceException("该借工人员姓名已在员工数据中存在");
            }
            LoanWorker existLoanWork = loanWorkerMapper.findInfoByName(loanWorker.getLoanEmpName());
            if (existLoanWork != null) {
                throw new ServiceException("该借工人员姓名已在借工数据中存在");
            }
        }
        Date now = new Date();
        loanWorker.setCreateTime(now);
        loanWorker.setCreateBy(userName);
        loanWorker.setDisabled(false);
        loanWorker.setUpdateBy(userName);
        loanWorker.setUpdateTime(now);
        return loanWorkerMapper.insertLoanWorker(loanWorker);
    }

    /**
     * 修改借工
     *
     * @param loanWorker 借工
     * @return 结果
     */
    @Override
    public int updateLoanWorker(LoanWorker loanWorker, String username) {
        Date now = new Date();
        loanWorker.setUpdateTime(now);
        loanWorker.setUpdateBy(username);
        return loanWorkerMapper.updateLoanWorker(loanWorker);
    }

    /**
     * 批量删除借工
     *
     * @param loanIds 需要删除的借工主键
     * @return 结果
     */
    @Override
    public int deleteLoanWorkerByLoanIds(Integer[] loanIds) {
        return loanWorkerMapper.deleteLoanWorkerByLoanIds(loanIds);
    }

    /**
     * 删除借工信息
     *
     * @param loanId 借工主键
     * @return 结果
     */
    @Override
    public int deleteLoanWorkerByLoanId(Integer loanId) {
        return loanWorkerMapper.deleteLoanWorkerByLoanId(loanId);
    }

    /**
     * 根据条件查询借工信息
     *
     * @param queryVO
     * @return
     */
    @Override
    public List<LoanWorkerListVO> selectLoanWorkersListByWhere(LoanWorkerQueryVO queryVO) {

        List<LoanWorkerListVO> loadWorkerlist = loanWorkerMapper.selectLoanWorkersListByWhere(queryVO);
        Map<Long, Department> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        if (!CollectionUtils.isEmpty(loadWorkerlist)) {
            for (LoanWorkerListVO vo : loadWorkerlist) {
                if (vo.getLoanApplyDeptId() != null) {
                    vo.setLoanApplyDeptName(deptMap.getOrDefault(vo.getLoanApplyDeptId(), new Department()).getDeptName());
                }
                if (StringUtils.isNotEmpty(vo.getLoanEmpIdcard())) {
                    vo.setLoanEmpIdcard(Base64.desensitize(vo.getLoanEmpIdcard()));
                }
            }
        }
        return loadWorkerlist;
    }


    @Override
    public ImportResultVO importLoanWorkData(MultipartFile multipartFile, LoginUser loginUser) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.TWO.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(loginUser.getUserId());
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<LoanWorkerSimpleVO> excelDate = getExcelDate(multipartFile, LoanWorkerSimpleVO.class);
        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());

        String username = loginUser.getUsername();
        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertLoanWorkInfos(loginUser.getUsername(), excelDate.getList());
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.LOAN_WORKER_FAIL_INFO.getKey(), "loanWorker", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.LOAN_WORKER_INFO.getKey(), "loanWorker", multipartFile, username).getFileId();

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

    private void batchInsertLoanWorkInfos(String userName, List<LoanWorkerSimpleVO> loanWorkerSimpleList) {

        List<SysDictData> hireCompanyList = sysDictDataMapper.selectDictDataByType("loan_unit");

        List<SysDictData> genderList = sysDictDataMapper.selectDictDataByType("emp_sex");

        List<SysDictData> educationList = sysDictDataMapper.selectDictDataByType("emp_education");

        List<SysDictData> titleList = sysDictDataMapper.selectDictDataByType("emp_title");

        List<SysDictData> positionList = sysDictDataMapper.selectDictDataByType("emp_position");

        List<LoanWorker> loanWorkers = new ArrayList<>();
        Date now = new Date();
        for (LoanWorkerSimpleVO loanWorkerSimple : loanWorkerSimpleList) {
            LoanWorker loanWorker = new LoanWorker();
            BeanUtils.copyProperties(loanWorkerSimple, loanWorker);
            loanWorker.setDisabled(false);
            loanWorker.setCreateBy(userName);
            loanWorker.setCreateTime(now);
            loanWorker.setUpdateBy(userName);
            loanWorker.setUpdateTime(now);

            String loanApplyDeptName = loanWorkerSimple.getLoanApplyUnitDeptName();
            String regex = "^(.)+-(.)+$";
            if (loanApplyDeptName.matches(regex)) {
                String[] applySplit = loanApplyDeptName.split("-");
                String applyUnitName = applySplit[0];
                String applyDeptName = applySplit[1];
                Department department = departmentMapper.findDepartmentInfoByUnitAndDeptName(applyUnitName, applyDeptName);
                if (department != null) {
                    loanWorker.setLoanApplyDeptId(department.getDeptId());
                    loanWorker.setLoanApplyDeptName(department.getDeptName());
                }
            } else {
                Department department = departmentMapper.findDepartmentInfoByName(loanApplyDeptName);
                if (department != null) {
                    loanWorker.setLoanApplyDeptId(department.getDeptId());
                    loanWorker.setLoanApplyDeptName(department.getDeptName());
                }
            }


            String applyUnitName = loanWorkerSimple.getLoanApplyUnitName();
            for (SysDictData hireCompany : hireCompanyList) {
                if (applyUnitName.equals(hireCompany.getDictLabel())) {
                    loanWorker.setLoanApplyUnitId(hireCompany.getDictValue());
                    break;
                }
            }
            String gender = loanWorkerSimple.getLoanEmpGender();
            for (SysDictData genderDict : genderList) {
                if (genderDict.getDictLabel().equals(gender)) {
                    loanWorker.setLoanEmpGender(genderDict.getDictValue());
                    break;
                }
            }

            String education = loanWorkerSimple.getLoanEmpEducation();
            for (SysDictData educationDict : educationList) {
                if (educationDict.getDictLabel().equals(education)) {
                    loanWorker.setLoanEmpEducation(educationDict.getDictValue());
                    break;
                }
            }
            String title = loanWorkerSimple.getLoanEmpTitle();
            for (SysDictData titleDict : titleList) {
                if (titleDict.getDictLabel().equals(title)) {
                    loanWorker.setLoanEmpTitle(titleDict.getDictValue());
                    break;
                }
            }

            String position = loanWorkerSimple.getLoanNewPost();
            for (SysDictData positionDict : positionList) {
                if (positionDict.getDictLabel().equals(position)) {
                    loanWorker.setLoanNewPost(positionDict.getDictValue());
                    break;
                }
            }

            loanWorkers.add(loanWorker);
        }
        loanWorkerMapper.batchInsertInfos(loanWorkers);
    }

    private ExcelImportResult<LoanWorkerSimpleVO> getExcelDate(MultipartFile file, Class<LoanWorkerSimpleVO> loanWorkerSimpleClass) {
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(loanWorkExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), loanWorkerSimpleClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = loanWorkExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }

    @Override
    public List<PieChartVO> countByDept() {
        return loanWorkerMapper.countByDept();
    }

    @Override
    public List<PieChartVO> countByUnit() {
        return loanWorkerMapper.countByUnit();
    }

    @Override
    public List<LoanWorkerExportListVO> selectLoanWorkerExportInfos(LoanWorkerExportVO loanWorkerExport) {

        List<SysDictData> genderList = sysDictDataMapper.selectDictDataByType("emp_sex");

        List<SysDictData> educationList = sysDictDataMapper.selectDictDataByType("emp_education");

        List<SysDictData> titleList = sysDictDataMapper.selectDictDataByType("emp_title");

        List<SysDictData> positionList = sysDictDataMapper.selectDictDataByType("emp_position");
        List<LoanWorkerExportListVO> loadWorkerlist = loanWorkerMapper.findExportInfosLoanEmpIds(loanWorkerExport.getLoanIds());

        if (!CollectionUtils.isEmpty(loadWorkerlist)) {
            for (int i = 0; i < loadWorkerlist.size(); i++) {
                LoanWorkerExportListVO vo = loadWorkerlist.get(i);
                if (StringUtils.isNotEmpty(vo.getLoanEmpIdcard())) {
                    vo.setLoanEmpIdcard(Base64.desensitize(vo.getLoanEmpIdcard()));
                }

                String gender = vo.getLoanEmpGender();
                for (SysDictData genderDict : genderList) {
                    if (genderDict.getDictValue().equals(gender)) {
                        vo.setLoanEmpGender(genderDict.getDictLabel());
                        break;
                    }
                }

                String education = vo.getLoanEmpEducation();
                for (SysDictData educationDict : educationList) {
                    if (educationDict.getDictValue().equals(education)) {
                        vo.setLoanEmpEducation(educationDict.getDictLabel());
                        break;
                    }
                }
                String title = vo.getLoanEmpTitle();
                for (SysDictData titleDict : titleList) {
                    if (titleDict.getDictValue().equals(title)) {
                        vo.setLoanEmpTitle(titleDict.getDictLabel());
                        break;
                    }
                }

                String position = vo.getLoanNewPost();
                for (SysDictData positionDict : positionList) {
                    if (positionDict.getDictValue().equals(position)) {
                        vo.setLoanNewPost(positionDict.getDictLabel());
                        break;
                    }
                }
            }
        }
        return loadWorkerlist;
    }

    @Override
    public void exportDivide(HttpServletResponse response, LoanWorkerExportDivideVO loanWorkerExportDivide) {

        List<LoanWorkerExportListVO> loanWorkers = loanWorkerMapper.findExportInfosLoanEmpIds(loanWorkerExportDivide.getLoanIds());
        if (CollectionUtils.isEmpty(loanWorkers)) {
            return;
        }
        List<SysDictData> genderList = sysDictDataMapper.selectDictDataByType("emp_sex");

        List<SysDictData> educationList = sysDictDataMapper.selectDictDataByType("emp_education");

        List<SysDictData> titleList = sysDictDataMapper.selectDictDataByType("emp_title");
        for (LoanWorkerExportListVO exportInfo : loanWorkers) {
            List<SysDictData> positionList = sysDictDataMapper.selectDictDataByType("emp_position");
            String gender = exportInfo.getLoanEmpGender();
            for (SysDictData genderDict : genderList) {
                if (genderDict.getDictValue().equals(gender)) {
                    exportInfo.setLoanEmpGender(genderDict.getDictLabel());
                    break;
                }
            }

            String education = exportInfo.getLoanEmpEducation();
            for (SysDictData educationDict : educationList) {
                if (educationDict.getDictValue().equals(education)) {
                    exportInfo.setLoanEmpEducation(educationDict.getDictLabel());
                    break;
                }
            }
            String title = exportInfo.getLoanEmpTitle();
            for (SysDictData titleDict : titleList) {
                if (titleDict.getDictValue().equals(title)) {
                    exportInfo.setLoanEmpTitle(titleDict.getDictLabel());
                    break;
                }
            }

            String position = exportInfo.getLoanNewPost();
            for (SysDictData positionDict : positionList) {
                if (positionDict.getDictValue().equals(position)) {
                    exportInfo.setLoanNewPost(positionDict.getDictLabel());
                    break;
                }
            }
        }

        LoanWorkChangeDicVO empChangeDic = changeDicValue(loanWorkers, loanWorkerExportDivide.getFieldName());
        loanWorkers = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = loanWorkerExportDivide.getFieldName();
        Map<String, List<LoanWorkerExportListVO>> groupedData = loanWorkers.stream()
                .collect(Collectors.groupingBy(emp -> {
                    try {
                        // 使用反射获取指定属性的值
                        Object value = emp.getClass().getMethod("get" +
                                groupByProperty.substring(0, 1).toUpperCase() +
                                groupByProperty.substring(1)).invoke(emp);
                        if (value == null) {
                            return "";
                        }
                        if (value instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            return sdf.format((Date) value);
                        }
                        return value.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 处理获取属性值异常
                        return null;
                    }
                }));

        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        String fieldName = loanWorkerExportDivide.getFieldName();
        for (String key : groupedData.keySet()) {
            List<LoanWorkerExportListVO> exportList = groupedData.get(key);
            for (LoanWorkerExportListVO exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = LoanWorkerExportListVO.class.getDeclaredField(fieldName);
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


        File baseDir = new File("借工信息");
        for (String key : groupedData.keySet()) {
            String excelName = loanWorkerExportDivide.getExcelName();
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
            List<LoanWorkerExportListVO> loanWorkerInfos = groupedData.get(key);
            for (LoanWorkerExportListVO exportInfo : loanWorkerInfos) {
                if (StringUtils.isNotEmpty(exportInfo.getLoanEmpIdcard())) {
                    exportInfo.setLoanEmpIdcard(Base64.desensitize(exportInfo.getLoanEmpIdcard()));
                }

            }

            ExcelUtil<LoanWorkerExportListVO> util = new ExcelUtil<LoanWorkerExportListVO>(LoanWorkerExportListVO.class);
            util.init(loanWorkerInfos, "借工信息", excelName, Excel.Type.EXPORT);
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

    private LoanWorkChangeDicVO changeDicValue(List<LoanWorkerExportListVO> loanWorkers, String fieldName) {
        Field[] declaredFields = LoanWorkChangeDicVO.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (LoanWorkerExportListVO exportInfo : loanWorkers) {
                try {
                    Field field = LoanWorkerExportListVO.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    LoanWorkChangeDicVO empChangeDic = new LoanWorkChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(loanWorkers);
                    return empChangeDic;
                }
            }
        }
        LoanWorkChangeDicVO empChangeDic = new LoanWorkChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(loanWorkers);
        return empChangeDic;
    }

    @Override
    public void downloadLoanWorkerExcel(HttpServletResponse response) {
        List<String> hireCompanyDic = sysDictDataMapper.selectDictDataByType("loan_unit").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());
        List<DepartmentVO> departmentVOS = departmentMapper.selectDepartmentVOList(new Department());
        List<String> deptUnitList = new ArrayList<>();
        for (DepartmentVO vo : departmentVOS) {
            deptUnitList.add(vo.getDeptUnitName() + "-" + vo.getDeptName());
        }

        List<String> genderDic = sysDictDataMapper.selectDictDataByType("emp_sex").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> educationDic = sysDictDataMapper.selectDictDataByType("emp_education").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> titleDic = sysDictDataMapper.selectDictDataByType("emp_title").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> positionDic = sysDictDataMapper.selectDictDataByType("emp_position").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());
        Map<Integer, List<String>> downDropMap = new HashMap<>(16);
        downDropMap.put(1, hireCompanyDic);
        downDropMap.put(4, deptUnitList);
        downDropMap.put(6, genderDic);
        downDropMap.put(8, educationDic);
        downDropMap.put(10, titleDic);
        downDropMap.put(12, positionDic);

        DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        OutputStream outputStream = null;

        List<LoanWorkerExcelVO> list = new ArrayList<>();
        /**
         LoanWorkerExcelVO vo = new LoanWorkerExcelVO();
         vo.setRowId(1);
         if(hireCompanyDic!=null){
         vo.setLoanApplyUnitName(hireCompanyDic.get(0));
         }

         vo.setLoanEmpName("测试姓名_张三");
         vo.setLoanEmpIdcard("320105199111012323");

         if(deptUnitList!=null) {
         vo.setLoanApplyUnitDeptName(deptUnitList.get(0));
         }

         vo.setLoanApplyOffice("测试科");
         //执业资格证书	拟从事岗位（监理员/监理师）	拟派驻现场(片区)	拟入场时间（年月日）	备注
         if(genderDic!=null){
         vo.setLoanEmpGender(genderDic.get(0));
         }

         vo.setLoanEmpAge(30);

         if(educationDic!=null){
         vo.setLoanEmpEducation(educationDic.get(0));
         }

         vo.setLoanEmpSpeciality("电子信息");

         if(titleDic!=null){
         vo.setLoanEmpTitle(titleDic.get(0));
         }

         vo.setLoanEmpCredentials("苏建监专2019057777");

         if(positionDic!=null){
         vo.setLoanNewPost(positionDic.get(0));
         }

         vo.setLoanArrageArea("苏州测试区");

         vo.setLoanBeginTime(new Date(2023-1900,10,01));
         list.add(vo);
         **/

        try {
            String fileName = URLEncoder.encode("借工模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, LoanWorkerExcelVO.class).
                    registerWriteHandler(downWriteHandler).sheet("借工信息").doWrite(list);
        } catch (IOException e) {
            throw new ServiceException("下载借工信息模板失败");
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

    @Override
    public List<EmpNameCardVO> searchSimpleInfos() {
        List<EmpNameCardVO> result = new ArrayList<>();
        List<LoanWorker> currentInfos = loanWorkerMapper.findCurrentInfos();
        for (LoanWorker currentInfo : currentInfos) {
            EmpNameCardVO empNameCard = new EmpNameCardVO();
            empNameCard.setEmpId(currentInfo.getLoanId());
            empNameCard.setEmpName(currentInfo.getLoanEmpName());

            if (!StringUtils.isEmpty(currentInfo.getLoanEmpIdcard())) {
                empNameCard.setEmpIdcard(Base64.desensitize(currentInfo.getLoanEmpIdcard()));
            }
            result.add(empNameCard);
        }
        return result;
    }
}
