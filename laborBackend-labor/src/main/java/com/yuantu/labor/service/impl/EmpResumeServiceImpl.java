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
import com.yuantu.labor.cenum.ResumeTypeEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.*;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEmpResumeService;
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
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 履历Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-12
 */
@Service
public class EmpResumeServiceImpl implements IEmpResumeService {

    private static final Logger log = LoggerFactory.getLogger(EmpResumeServiceImpl.class);

    @Autowired
    private EmpResumeMapper empResumeMapper;

    @Autowired
    private EmpResumeExcelImportVerifyHandler empResumeExcelImportVerifyHandler;

    @Autowired
    private ResumeSocialExcelImportVerifyHandler resumeSocialExcelImportVerifyHandler;

    @Autowired
    private ResumeProjectExcelImportVerifyHandler resumeProjectExcelImportVerifyHandler;

    @Autowired
    private ResumeUnitExcelImportVerifyHandler resumeUnitExcelImportVerifyHandler;

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployingUnitsMapper unitsMapper;


    @Autowired
    private FileService fileService;


    /**
     * 查询履历
     *
     * @param resuId 履历主键
     * @return 履历
     */
    @Override
    public EmpResume selectEmpResumeByResuId(Integer resuId) {
        EmpResume resume = empResumeMapper.selectEmpResumeByResuId(resuId);
        if (resume != null) {
            if (StringUtils.isNotEmpty(resume.getResuEmpIdcard())) {
                resume.setResuEmpIdcard(Base64.desensitize(resume.getResuEmpIdcard()));
            }
        }
        return resume;
    }

    /**
     * 查询履历列表
     *
     * @param empResume 履历
     * @return 履历
     */
    @Override
    public List<EmpResume> selectEmpResumeList(EmpResume empResume) {
        return empResumeMapper.selectEmpResumeList(empResume);
    }

    /**
     * 新增履历
     *
     * @param empResume 履历
     * @return 结果
     */
    @Override
    public int insertEmpResume(EmpResume empResume) {
        Date now = new Date();
        empResume.setCreateTime(now);
        empResume.setResuUpdateTime(now);
        return empResumeMapper.insertEmpResume(empResume);
    }

    /**
     * 修改履历
     *
     * @param empResume 履历
     * @return 结果
     */
    @Override
    public int updateEmpResume(EmpResume empResume) {
        Date now = new Date();
        empResume.setUpdateTime(now);
        empResume.setResuUpdateTime(now);
        return empResumeMapper.updateEmpResume(empResume);
    }

    /**
     * 批量删除履历
     *
     * @param resuIds 需要删除的履历主键
     * @return 结果
     */
    @Override
    public int deleteEmpResumeByResuIds(Integer[] resuIds) {
        return empResumeMapper.deleteEmpResumeByResuIds(resuIds);
    }

    /**
     * 删除履历信息
     *
     * @param resuId 履历主键
     * @return 结果
     */
    @Override
    public int deleteEmpResumeByResuId(Integer resuId) {
        return empResumeMapper.deleteEmpResumeByResuId(resuId);
    }

    @Override
    public List<EmpResume> selectEmpResumeListByWhere(EmpResumeVO resumeVO) {

        List<EmpResume> empResumes = empResumeMapper.selectEmpResumeListByWhere(resumeVO);
        List<Long> deptIds = empResumes.stream().filter(s -> s.getResuDept() != null).filter(s -> s.getResuDept().matches("\\d+")).map(s -> {
            String resuDept = s.getResuDept();
            return Long.parseLong(resuDept);
        }).collect(Collectors.toList());
        Map<Long, Department> deptMap = new HashMap<>(16);
        Map<Long, EmployingUnits> unitsMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            List<Department> deptInfos = departmentMapper.findDeptInfosByDeptIds(deptIds);
            deptMap = deptInfos.stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
            List<Long> unitIds = deptInfos.stream().map(Department::getDeptUnitId).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(unitIds)) {
                unitsMap = unitsMapper.findUnitInfoByUnitIds(unitIds).stream().
                        collect(Collectors.toMap(EmployingUnits::getUnitId, Function.identity()));
            }
        }
        List<Long> empIds = empResumes.stream().map(EmpResume::getResuEmpId).collect(Collectors.toList());
        Map<Long, Employee> employeeMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(empIds)) {
            employeeMap = employeeMapper.selectEmployeeInfosByIds(empIds).stream().collect(Collectors.
                    toMap(Employee::getEmpId, Function.identity()));
        }
        for (EmpResume empResume : empResumes) {
            Employee existEmp = employeeMap.get(empResume.getResuEmpId());
            if (existEmp == null) {
                empResume.setIsChange(false);
            } else {
                empResume.setIsChange(true);
            }
            if (StringUtils.isNotEmpty(empResume.getResuEmpIdcard())) {
                empResume.setResuEmpIdcard(Base64.desensitize(empResume.getResuEmpIdcard()));
            }
            String resuDept = empResume.getResuDept();
            if (StringUtils.isNotBlank(resuDept) && resuDept.matches("\\d+")) {
                Department department = deptMap.getOrDefault(Long.valueOf(resuDept), new Department());
                empResume.setResuDeptName(department.getDeptName());
                Long unitId = department.getDeptUnitId();
                if (unitId != null) {
                    empResume.setResuWorkUnitName(unitsMap.getOrDefault(unitId, new EmployingUnits()).getUnitName());
                }
            }
        }
        return empResumes;
    }

    @Override
    public void downloadTemplate(HttpServletResponse response, String type) {

        if (ResumeTypeEnum.ONE.getKey().equals(type)) {
            ExcelUtil<ResumeSocialTemplateVO> util = new ExcelUtil<>(ResumeSocialTemplateVO.class);
            util.importTemplateExcel(response, "履历数据");
        }
        if (ResumeTypeEnum.TWO.getKey().equals(type)) {
            InputStream inputStream = null;
            try {
                ClassPathResource classPathResource = new ClassPathResource("static/ResumeProjectTemplate.xlsx");
                inputStream = classPathResource.getInputStream();
                ExcelUtil.downLoadExcel("empResume", response, WorkbookFactory.create(inputStream));
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
        if (ResumeTypeEnum.THREE.getKey().equals(type)) {
            ExcelUtil<ResumeUnitTemplateVO> util = new ExcelUtil<>(ResumeUnitTemplateVO.class);
            util.importTemplateExcel(response, "履历数据");
        }
    }

    @Override
    public void downloadResumeExcel(HttpServletResponse response, String type) {

        if (ResumeTypeEnum.ONE.getKey().equals(type)) {
            ExcelUtil<ResumeSocialTemplateVO> util = new ExcelUtil<>(ResumeSocialTemplateVO.class);
            util.importTemplateExcel(response, "履历数据");
        }
        if (ResumeTypeEnum.TWO.getKey().equals(type)) {
            List<String> resuProjectTypeDic = dictDataMapper.selectDictDataByType("resu_project_type").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());
            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(5, resuProjectTypeDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            // 防止中文乱码：import java.net.URLEncoder


            try {
                String fileName = URLEncoder.encode("项目履历模板", "UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
                EasyExcel.write(response.getOutputStream(), ResumeProjectExcelVO.class).
                        registerWriteHandler(downWriteHandler).sheet("项目履历信息").doWrite(new ArrayList<>());
            } catch (IOException e) {
                throw new ServiceException("下载员工项目履历信息模板失败");
            }
        }
        if (ResumeTypeEnum.THREE.getKey().equals(type)) {
            ExcelUtil<ResumeUnitTemplateVO> util = new ExcelUtil<>(ResumeUnitTemplateVO.class);
            util.importTemplateExcel(response, "履历数据");
        }
    }

    @Override
    public ImportResultVO importEmpResumeData(MultipartFile multipartFile, LoginUser loginUser, String type) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.FOUR.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(loginUser.getUserId());
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ImportResultVO importResult = new ImportResultVO();

        if (ResumeTypeEnum.ONE.getKey().equals(type)) {
            ExcelImportResult<ResumeSocialImportVO> excelDate = getExcelDateForSocial(multipartFile, ResumeSocialImportVO.class);

            List<ErrorForm> failReport = excelDate.getFailList().stream()
                    .map(entity -> {
                        int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                        return new ErrorForm(line, entity.getErrorMsg());
                    }).collect(Collectors.toList());

            String username = loginUser.getUsername();
            if (!CollectionUtils.isEmpty(excelDate.getList())) {
                List<EmpResumeImportVO> empResumeImportInfos = new ArrayList<>();
                List<ResumeSocialImportVO> excelDateList = excelDate.getList();
                for (ResumeSocialImportVO resumeSocialImport : excelDateList) {
                    EmpResumeImportVO empResumeImport = new EmpResumeImportVO();
                    BeanUtils.copyProperties(resumeSocialImport, empResumeImport);
                    empResumeImportInfos.add(empResumeImport);
                }
                batchInsertEmpResumeInfos(loginUser.getUsername(), empResumeImportInfos, type);
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
                    FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_RESUME_FAIL_INFO.getKey(), "empResume", file, username);
                    failFileId = failFileInfo.getFileId();
                    failFileUrl = failFileInfo.getFileUrl();
                }
                if (successCount == 0) {
                    fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
                }
            }
            Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_RESUME_INFO.getKey(), "empResume", multipartFile, username).getFileId();

            fileImportRecord.setSuccessCount(successCount);
            fileImportRecord.setFailureCount(errorCount);
            fileImportRecord.setTotalCount(totalCount);
            fileImportRecord.setOriginFileId(empInfoFileId);
            fileImportRecord.setFailFileId(failFileId);
            fileImportRecordMapper.updateFileImportRecord(fileImportRecord);

            importResult.setTotalCount(totalCount);
            importResult.setSuccessCount(successCount);
            importResult.setErrorCount(errorCount);
            importResult.setFailFileId(failFileId);
            importResult.setFailFileUrl(failFileUrl);
        }

        if (ResumeTypeEnum.TWO.getKey().equals(type)) {
            ExcelImportResult<ResumeProjectImportVO> excelDate = getExcelDateForProject(multipartFile, ResumeProjectImportVO.class);

            List<ErrorForm> failReport = excelDate.getFailList().stream()
                    .map(entity -> {
                        int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                        return new ErrorForm(line, entity.getErrorMsg());
                    }).collect(Collectors.toList());

            String username = loginUser.getUsername();
            if (!CollectionUtils.isEmpty(excelDate.getList())) {
                List<EmpResumeImportVO> empResumeImportInfos = new ArrayList<>();
                List<ResumeProjectImportVO> excelDateList = excelDate.getList();
                for (ResumeProjectImportVO resumeProjectImport : excelDateList) {
                    EmpResumeImportVO empResumeImport = new EmpResumeImportVO();
                    BeanUtils.copyProperties(resumeProjectImport, empResumeImport);
                    empResumeImportInfos.add(empResumeImport);
                }
                batchInsertEmpResumeInfos(loginUser.getUsername(), empResumeImportInfos, type);
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
                    FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_RESUME_FAIL_INFO.getKey(), "empResume", file, username);
                    failFileId = failFileInfo.getFileId();
                    failFileUrl = failFileInfo.getFileUrl();
                }
                if (successCount == 0) {
                    fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
                }
            }
            Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_RESUME_INFO.getKey(), "empResume", multipartFile, username).getFileId();

            fileImportRecord.setSuccessCount(successCount);
            fileImportRecord.setFailureCount(errorCount);
            fileImportRecord.setTotalCount(totalCount);
            fileImportRecord.setOriginFileId(empInfoFileId);
            fileImportRecord.setFailFileId(failFileId);
            fileImportRecordMapper.updateFileImportRecord(fileImportRecord);


            importResult.setTotalCount(totalCount);
            importResult.setSuccessCount(successCount);
            importResult.setErrorCount(errorCount);
            importResult.setFailFileId(failFileId);
            importResult.setFailFileUrl(failFileUrl);
        }

        if (ResumeTypeEnum.THREE.getKey().equals(type)) {
            ExcelImportResult<ResumeUnitImportVO> excelDate = getExcelDateForUnit(multipartFile, ResumeUnitImportVO.class);

            List<ErrorForm> failReport = excelDate.getFailList().stream()
                    .map(entity -> {
                        int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                        return new ErrorForm(line, entity.getErrorMsg());
                    }).collect(Collectors.toList());

            String username = loginUser.getUsername();
            if (!CollectionUtils.isEmpty(excelDate.getList())) {
                List<EmpResumeImportVO> empResumeImportInfos = new ArrayList<>();
                List<ResumeUnitImportVO> excelDateList = excelDate.getList();
                for (ResumeUnitImportVO resumeUnitImport : excelDateList) {
                    EmpResumeImportVO empResumeImport = new EmpResumeImportVO();
                    BeanUtils.copyProperties(resumeUnitImport, empResumeImport);
                    empResumeImportInfos.add(empResumeImport);
                }
                batchInsertEmpResumeInfos(loginUser.getUsername(), empResumeImportInfos, type);
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
                    FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_RESUME_FAIL_INFO.getKey(), "empResume", file, username);
                    failFileId = failFileInfo.getFileId();
                    failFileUrl = failFileInfo.getFileUrl();
                }
                if (successCount == 0) {
                    fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
                }
            }
            Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_RESUME_INFO.getKey(), "empResume", multipartFile, username).getFileId();

            fileImportRecord.setSuccessCount(successCount);
            fileImportRecord.setFailureCount(errorCount);
            fileImportRecord.setTotalCount(totalCount);
            fileImportRecord.setOriginFileId(empInfoFileId);
            fileImportRecord.setFailFileId(failFileId);
            fileImportRecordMapper.updateFileImportRecord(fileImportRecord);


            importResult.setTotalCount(totalCount);
            importResult.setSuccessCount(successCount);
            importResult.setErrorCount(errorCount);
            importResult.setFailFileId(failFileId);
            importResult.setFailFileUrl(failFileUrl);
        }

        return importResult;
    }


    private void batchInsertEmpResumeInfos(String username, List<EmpResumeImportVO> empResumeImportInfos, String type) {

        List<EmpResume> empResumes = new ArrayList<>();
        //List<String> empIdCards = empResumeImportInfos.stream().map(EmpResumeImportVO::getResuEmpIdcard).collect(Collectors.toList());
        //Map<String, Employee> employeeMap = employeeMapper.findInfoByIdCards(empIdCards).stream().collect(Collectors.
        //        toMap(Employee::getEmpIdcard, Function.identity()));
        List<String> empNameList = empResumeImportInfos.stream().map(EmpResumeImportVO::getResuEmpName).collect(Collectors.toList());
        Map<String, Employee> existEmpMap = employeeMapper.findInfoEmpNames(empNameList).stream().collect(Collectors.toMap(Employee::getEmpName, Function.identity()));

        Date now = new Date();
        for (EmpResumeImportVO empResumeImportInfo : empResumeImportInfos) {
            EmpResume empResume = new EmpResume();
            empResume.setCreateBy(username);
            empResume.setCreateTime(now);
            empResume.setResuUpdateTime(now);
            empResume.setDisabled(false);
            BeanUtils.copyProperties(empResumeImportInfo, empResume);
            //Employee existEmployee = employeeMap.getOrDefault(empResumeImportInfo.getResuEmpIdcard(), new Employee());
            String empName = empResumeImportInfo.getResuEmpName();
            Employee existEmployee = existEmpMap.getOrDefault(empName, new Employee());
            Long empId = existEmployee.getEmpId();
            empResume.setResuEmpId(empId);
            //empResume.setResuEmpName(existEmployee.getEmpName());
            empResume.setResuEmpIdcard(existEmpMap.get(empName).getEmpIdcard());
            empResume.setResuType(type);
            //处理字典值
            List<SysDictData> resuProjectTypeDic = dictDataMapper.selectDictDataByType("resu_project_type");
            empResume.setResuProjectType(resuProjectTypeDic.stream().filter(s -> s.getDictLabel().equals(empResume.getResuProjectType())).findFirst().orElse(new SysDictData()).getDictValue());

            empResumes.add(empResume);
        }
        empResumeMapper.batchInsertEmpResumes(empResumes);
    }


    private ExcelImportResult<ResumeSocialImportVO> getExcelDateForSocial(MultipartFile file, Class<ResumeSocialImportVO> resumeSocialClass) {
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(resumeSocialExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), resumeSocialClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = resumeSocialExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }


    private ExcelImportResult<ResumeProjectImportVO> getExcelDateForProject(MultipartFile file, Class<ResumeProjectImportVO> resumeProjectClass) {
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(resumeProjectExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), resumeProjectClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = resumeProjectExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }


    private ExcelImportResult<ResumeUnitImportVO> getExcelDateForUnit(MultipartFile file, Class<ResumeUnitImportVO> resumeUnitClass) {
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(resumeUnitExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), resumeUnitClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = resumeUnitExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }

    private ExcelImportResult<EmpResumeImportVO> getExcelDate(MultipartFile file, Class<EmpResumeImportVO> empResumeImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(empResumeExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), empResumeImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = empResumeExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }


    @Override
    public List<EmpResume> selectEmpResumeExportInfos(EmpResumeExportVO empResumeExport) {
        List<EmpResume> list = empResumeMapper.findExportInfosByEmpIds(empResumeExport.getEmpIds(), empResumeExport.getType());
        for (EmpResume empResume : list) {
            if (StringUtils.isNotEmpty(empResume.getResuEmpIdcard())) {
                empResume.setResuEmpIdcard(Base64.desensitize(empResume.getResuEmpIdcard()));
            }
        }

        return list;
    }

    @Override
    public void exportDivide(HttpServletResponse response, EmpResumeExportDivideVO empResumeExportDivide) {

        List<EmpResume> empResumes = empResumeMapper.findExportInfosByEmpIds(empResumeExportDivide.getEmpIds(), empResumeExportDivide.getType());
        /**
         for(EmpResume empResume:empResumes ){
         if(empResume.getResuEmpIdcard() != null) {
         empResume.setResuEmpIdcard(Base64.desensitize(empResume.getResuEmpIdcard()));
         }
         }
         **/

        if (CollectionUtils.isEmpty(empResumes)) {
            return;
        }

        EmpResumeChangeDicVO empChangeDic = changeDicValue(empResumes, empResumeExportDivide.getFieldName());
        empResumes = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = empResumeExportDivide.getFieldName();
        String fieldName = empResumeExportDivide.getFieldName();

        if (ResumeTypeEnum.ONE.getKey().equals(empResumeExportDivide.getType())) {
            List<ResumeSocialExportVO> resumeSocials = new ArrayList<>();
            for (EmpResume empResume : empResumes) {
                ResumeSocialExportVO resumeSocial = new ResumeSocialExportVO();
                BeanUtils.copyProperties(empResume, resumeSocial);
                resumeSocials.add(resumeSocial);
            }
            Map<String, List<ResumeSocialExportVO>> groupedData = resumeSocials.stream()
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
            File baseDir = new File("履历信息");
            for (String key : groupedData.keySet()) {
                String excelName = empResumeExportDivide.getExcelName();
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
                List<ResumeSocialExportVO> empResumeInfos = groupedData.get(key);
                for (ResumeSocialExportVO vo : empResumeInfos) {
                    if (StringUtils.isNotEmpty(vo.getResuEmpIdcard())) {
                        vo.setResuEmpIdcard(Base64.desensitize(vo.getResuEmpIdcard()));
                    }
                }
                ExcelUtil<ResumeSocialExportVO> util = new ExcelUtil<ResumeSocialExportVO>(ResumeSocialExportVO.class);
                util.init(empResumeInfos, "履历信息", excelName, Excel.Type.EXPORT);
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

        if (ResumeTypeEnum.TWO.getKey().equals(empResumeExportDivide.getType())) {
            List<ResumeProjectTemplateVO> resumeProjects = new ArrayList<>();
            for (EmpResume empResume : empResumes) {
                ResumeProjectTemplateVO resumeSocial = new ResumeProjectTemplateVO();
                BeanUtils.copyProperties(empResume, resumeSocial);
                resumeProjects.add(resumeSocial);
            }
            Map<String, List<ResumeProjectTemplateVO>> groupedData = resumeProjects.stream()
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
            for (String key : groupedData.keySet()) {
                List<ResumeProjectTemplateVO> exportList = groupedData.get(key);
                for (ResumeProjectTemplateVO exportInfo : exportList) {

                    if (!CollectionUtils.isEmpty(dictDataInfos)) {
                        try {
                            Field field = ResumeProjectTemplateVO.class.getDeclaredField(fieldName);
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


            File baseDir = new File("履历信息");
            for (String key : groupedData.keySet()) {
                String excelName = empResumeExportDivide.getExcelName();
                if (excelName.contains("#")) {
                    excelName = excelName.replace("#", chineseName == null ? "" : chineseName);
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
                List<ResumeProjectTemplateVO> empResumeInfos = groupedData.get(key);
                for (ResumeProjectTemplateVO vo : empResumeInfos) {
                    if (StringUtils.isNotEmpty(vo.getResuEmpIdcard())) {
                        vo.setResuEmpIdcard(Base64.desensitize(vo.getResuEmpIdcard()));
                    }
                }
                ExcelUtil<ResumeProjectTemplateVO> util = new ExcelUtil<ResumeProjectTemplateVO>(ResumeProjectTemplateVO.class);
                util.init(empResumeInfos, "履历信息", excelName, Excel.Type.EXPORT);
                util.writeSheet();
                Workbook wb = util.getWb();
                MultipartFile multipartFile = FileUtils.workbookToCommonsMultipartFile(wb, key + ".xlsx");
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

        if (ResumeTypeEnum.THREE.getKey().equals(empResumeExportDivide.getType())) {
            List<ResumeUnitExportVO> resumeUnits = new ArrayList<>();
            for (EmpResume empResume : empResumes) {
                ResumeUnitExportVO resumeUnit = new ResumeUnitExportVO();
                BeanUtils.copyProperties(empResume, resumeUnit);
                resumeUnits.add(resumeUnit);
            }
            Map<String, List<ResumeUnitExportVO>> groupedData = resumeUnits.stream()
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
            File baseDir = new File("履历信息");
            for (String key : groupedData.keySet()) {
                String excelName = empResumeExportDivide.getExcelName();
                if (excelName.contains("#")) {
                    excelName = excelName.replace("#", chineseName == null ? "" : chineseName);
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
                List<ResumeUnitExportVO> empResumeInfos = groupedData.get(key);
                for (ResumeUnitExportVO vo : empResumeInfos) {
                    if (StringUtils.isNotEmpty(vo.getResuEmpIdcard())) {
                        vo.setResuEmpIdcard(Base64.desensitize(vo.getResuEmpIdcard()));
                    }
                }
                ExcelUtil<ResumeUnitExportVO> util = new ExcelUtil<ResumeUnitExportVO>(ResumeUnitExportVO.class);
                util.init(empResumeInfos, "履历信息", excelName, Excel.Type.EXPORT);
                util.writeSheet();
                Workbook wb = util.getWb();
                MultipartFile multipartFile = FileUtils.workbookToCommonsMultipartFile(wb, key + ".xlsx");
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

    }

    @Autowired
    private SysDictDataMapper dictDataMapper;

    private EmpResumeChangeDicVO changeDicValue(List<EmpResume> empResumes, String fieldName) {
        Field[] declaredFields = EmpResume.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (EmpResume exportInfo : empResumes) {
                try {
                    Field field = EmpResume.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpResumeChangeDicVO empChangeDic = new EmpResumeChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(empResumes);
                    return empChangeDic;
                }
            }
        }
        EmpResumeChangeDicVO empChangeDic = new EmpResumeChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(empResumes);
        return empChangeDic;
    }
}
