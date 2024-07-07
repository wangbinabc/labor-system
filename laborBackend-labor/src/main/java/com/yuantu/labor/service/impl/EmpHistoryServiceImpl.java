package com.yuantu.labor.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.ObjectUtil;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.labor.cenum.*;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.EmpHistoryExcelImportVerifyHandler;
import com.yuantu.labor.handler.EmployeeExcelImportVerifyHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IFileService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysConfigMapper;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.service.IEmpHistoryService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 员工快照Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-19
 */
@Service
public class EmpHistoryServiceImpl implements IEmpHistoryService {
    @Autowired
    private EmpHistoryMapper empHistoryMapper;


    @Autowired
    private EmployeeMapper employeeMapper;


    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private EmpHistoryExcelImportVerifyHandler empHistoryExcelImportVerifyHandler;


    @Autowired
    private SysDictDataMapper dictDataMapper;


    @Autowired
    private ChinaAddressMapper chinaAddressMapper;


    @Autowired
    private DepartmentMapper departmentMapper;


    @Autowired
    private PositionInfoMapper positionInfoMapper;


    @Autowired
    private FamilyRelationsMapper familyRelationsMapper;

    @Autowired
    private FamilyRelationsHistoryMapper familyRelationsHistoryMapper;


    private static final Logger log = LoggerFactory.getLogger(EmpHistoryServiceImpl.class);

    /**
     * 查询员工快照
     *
     * @param historyId 员工快照主键
     * @return 员工快照
     */
    @Override
    public EmpDetailHistoryVO selectEmpHistoryByHistoryId(Long historyId) {
        EmpDetailHistoryVO empDetailHistory = new EmpDetailHistoryVO();
        EmpHistory empHistory = empHistoryMapper.selectEmpHistoryByHistoryId(historyId);
        BeanUtils.copyProperties(empHistory, empDetailHistory);
        empDetailHistory.setEmpHistoryId(empHistory.getHistoryId());
        empDetailHistory.setHistoryYearMonth(empHistory.getHistoryYearMoth());
        String historyYearMoth = empDetailHistory.getHistoryYearMonth();
        List<FamilyRelationsHistory> familyRelationsHistories = familyRelationsHistoryMapper.findInfosByEmpIdAndHistoryYearMonth(historyId, historyYearMoth);
        List<EmpFamilyVO> empFamilyInfos = new ArrayList<>();
        for (FamilyRelationsHistory familyRelationsHistory : familyRelationsHistories) {
            EmpFamilyVO empFamily = new EmpFamilyVO();
            BeanUtils.copyProperties(familyRelationsHistory, empFamily);
            empFamilyInfos.add(empFamily);
        }
        empDetailHistory.setEmpFamilyInfos(empFamilyInfos);
        return empDetailHistory;
    }

    /**
     * 查询员工快照列表
     *
     * @param vo 员工快照
     * @return 员工快照
     */
    @Override
    public List<EmpHistory> selectEmpHistoryList(EmpHistoryQueryParamsVO vo) {
        return empHistoryMapper.selectEmpHistoryList(vo);
    }

    /**
     * 按起始时间查询员工快照列表，并返回结果
     *
     * @return 员工快照集合
     */
    public List<EmpHistoryInfoVO> selectEmpHistoryChangeTypeList(HistoryChangeTypeSearchVO search) {
        List<EmpHistoryInfoVO> resultlist = new ArrayList<>();
        if (search == null || search.getStartYearMonth() == null || new SimpleDateFormat("yyyy-MM").format(DateUtils.getNowDate()).equals(search.getStartYearMonth())) {
            List<Employee> list = employeeMapper.selectEmployeeList(null);

            for (Employee employee : list) {
                EmpHistoryInfoVO empHistoryInfoVO = new EmpHistoryInfoVO();
                BeanUtils.copyProperties(employee, empHistoryInfoVO);
                empHistoryInfoVO.setChangeType(EmpChangeTypeEnum.NOCHANGE.getValue());
                String idCard = null;
                if (StringUtils.isNotEmpty(employee.getEmpIdcard())) {
                    //   idCard = employee.getEmpIdcard().substring(0, 4) + "XXXXXXXXXX" + employee.getEmpIdcard().substring(14, 18);
                    idCard = com.yuantu.common.utils.sign.Base64.desensitize(employee.getEmpIdcard());
                }
                empHistoryInfoVO.setEmpIdcard(idCard);
                resultlist.add(empHistoryInfoVO);
            }
            return resultlist;
        }


        List<EmpHistoryInfoVO> startEmpHistorys = empHistoryMapper.selectEmpHistoryListByYearMonth(search.getStartYearMonth());
        List<EmpHistoryInfoVO> endEmpHistorys = null;
        if (search.getEndYearMonth() == null || search.getEndYearMonth().equals(new SimpleDateFormat("yyyy-MM").format(DateUtils.getNowDate()))) {
//            Employee search = new Employee();
//            search.setDisabled(true);
            List<Employee> list = employeeMapper.selectEmployeeList(null);
            endEmpHistorys = new ArrayList<>();

            for (Employee employee : list) {
                EmpHistoryInfoVO empHistoryInfoVO = new EmpHistoryInfoVO();
                BeanUtils.copyProperties(employee, empHistoryInfoVO);
                String idCard = null;
                if (StringUtils.isNotEmpty(employee.getEmpIdcard())) {
                    //          idCard = employee.getEmpIdcard().substring(0, 4) + "XXXXXXXXXX".toLowerCase() + employee.getEmpIdcard().substring(14, 18);
                    idCard = com.yuantu.common.utils.sign.Base64.desensitize(employee.getEmpIdcard());
                }
                empHistoryInfoVO.setEmpIdcard(idCard);
                endEmpHistorys.add(empHistoryInfoVO);
            }
        } else {
            endEmpHistorys = empHistoryMapper.selectEmpHistoryListByYearMonth(search.getEndYearMonth());
        }


        //"变动类型 无变动0 新增1 减少2 调动3
        for (EmpHistoryInfoVO startev : startEmpHistorys) {
            startev.setChangeType(EmpChangeTypeEnum.REDUCE.getValue());
            resultlist.add(startev);
        }

        for (EmpHistoryInfoVO endev : endEmpHistorys) {
            if (!resultlist.contains(endev)) {
                endev.setChangeType(EmpChangeTypeEnum.ADD.getValue());
                resultlist.add(endev);
            } else {
                int index = resultlist.indexOf(endev);
                EmpHistoryInfoVO ev = resultlist.get(index);
                if (ev.getEmpDeptId() == null) {
                    resultlist.get(index).setChangeType(endev.getEmpDeptId() != null ? EmpChangeTypeEnum.MOBILIZE.getValue() : "-1");
                } else if (ev.getEmpEmployingUnits() == null) {
                    resultlist.get(index).setChangeType(endev.getEmpEmployingUnits() != null ? EmpChangeTypeEnum.MOBILIZE.getValue() : "-1");
                } else if (ev.getEmpPosition() == null) {
                    resultlist.get(index).setChangeType(endev.getEmpPosition() != null ? EmpChangeTypeEnum.MOBILIZE.getValue() : "-1");
                } else if (ev.getEmpDeptId().equals(endev.getEmpDeptId())
                        && ev.getEmpEmployingUnits().equals(endev.getEmpEmployingUnits())
                        && ev.getEmpPosition().equals(endev.getEmpPosition())) {
                    resultlist.get(index).setChangeType(EmpChangeTypeEnum.NOCHANGE.getValue());
                } else {
                    resultlist.remove(index);
                    endev.setChangeType(EmpChangeTypeEnum.MOBILIZE.getValue());
                    resultlist.add(endev);
                }
            }
        }
        return resultlist;


    }

    /**
     * 新增员工快照
     *
     * @param empHistory 员工快照
     * @return 结果
     */
    @Override
    public int insertEmpHistory(EmpHistory empHistory) {
        empHistory.setCreateTime(DateUtils.getNowDate());
        return empHistoryMapper.insertEmpHistory(empHistory);
    }

    /**
     * 修改员工快照
     *
     * @param empHistory 员工快照
     * @return 结果
     */
    @Override
    public Boolean updateEmpHistory(EmpDetailHistoryVO empHistory, Long userId) {

        String historyYearMoth = empHistory.getHistoryYearMonth();
        Employee employee = new Employee();
        BeanUtils.copyProperties(empHistory, employee);
        EmpAddVO empAdd = new EmpAddVO();
        BeanUtils.copyProperties(empHistory, empAdd);
        checkEmpSalaryLevelForUpdate(empAdd);
        String empIdcard = employee.getEmpIdcard();

        Date now = new Date();
        if (StringUtils.isNotEmpty(empIdcard)) {
            int age = DateUtils.calculateAge(empIdcard);
            char sexChar = empIdcard.charAt(empIdcard.length() - 2);
            int sexNum = Integer.parseInt(String.valueOf(sexChar));
            if (sexNum % 2 == 0) {
                employee.setEmpGender(EmpGenderEnum.FEMALE.getKey());
            } else {
                employee.setEmpGender(EmpGenderEnum.MALE.getKey());
            }
            employee.setEmpAge(age);
        }
        changeEmpStatus(empAdd.getEmpHiredate(), employee, employee.getEmpIdcard());

        if (!CollectionUtils.isEmpty(empAdd.getNativePlace())) {
            employee.setNativePlace(StringUtils.join(empAdd.getNativePlace(), ","));
        }
        if (!CollectionUtils.isEmpty(empAdd.getBirthPlace())) {
            employee.setBirthPlace(StringUtils.join(empAdd.getBirthPlace(), ","));
        }
        if (empAdd.getEmpDeptId() != null) {
            String deptName = departmentMapper.selectDepartmentByDeptId(empAdd.getEmpDeptId()).getDeptName();
            employee.setEmpDeptName(deptName);
        }

        EmpHistory empHistoryInfo = new EmpHistory();
        empHistoryInfo.setHistoryYearMoth(historyYearMoth);
        empHistoryInfo.setUpdateBy(userId + "");
        empHistoryInfo.setUpdateTime(now);
        BeanUtils.copyProperties(employee, empHistoryInfo);
        empHistoryInfo.setHistoryId(empHistory.getEmpHistoryId());
        empHistoryMapper.updateEmpHistory(empHistoryInfo);

        List<EmpFamilyVO> empFamilies = empHistory.getEmpFamilyInfos();

        // 添加员工亲属历史信息
        familyRelationsHistoryMapper.removeInfosByEmpId(empHistory.getEmpHistoryId(), historyYearMoth);
        if (!CollectionUtils.isEmpty(empFamilies)) {
            List<FamilyRelationsHistory> familyRelations = new ArrayList<>();
            for (EmpFamilyVO empFamily : empFamilies) {
                FamilyRelationsHistory familyRelation = new FamilyRelationsHistory();
                BeanUtils.copyProperties(empFamily, familyRelation);
                familyRelation.setFamEmpId(empHistory.getEmpHistoryId());
                familyRelation.setFamEmpName(employee.getEmpName());
                if (empFamily.getFamAge() != null && !"".equals(empFamily.getFamAge())) {
                    String regex = "\\d+";
                    if (empFamily.getFamAge().matches(regex)) {
                        familyRelation.setFamAge(Long.parseLong(empFamily.getFamAge()));
                    }
                }
                familyRelation.setHistoryYearMonth(historyYearMoth);
                familyRelations.add(familyRelation);
            }
            familyRelationsHistoryMapper.batchInsertFamilyInfos(familyRelations);
        }
        return true;

    }

    private void checkEmpSalaryLevelForUpdate(EmpAddVO empAdd) {
        String empSalaryLevel = empAdd.getEmpSalaryLevel();
        if (empSalaryLevel != null) {
            String empPosition = empAdd.getEmpPosition();
            String empPositionLevel = empAdd.getEmpPositionLevel();
            String empPositionCategory = empAdd.getEmpPositionCategory();
            PositionInfo positionInfo = positionInfoMapper.findInfoByReferenceParams(empPosition, empPositionLevel,
                    empPositionCategory);
            if (positionInfo != null) {
                String salaryHigh = positionInfo.getSalaryHigh();
                if (!StringUtils.isEmpty(salaryHigh) && Integer.parseInt(salaryHigh) < Integer.parseInt(empSalaryLevel)) {
                    empAdd.setEmpSalaryLevel(salaryHigh);
                }
                String salaryLow = positionInfo.getSalaryLow();
                if (!StringUtils.isEmpty(salaryLow) && Integer.parseInt(salaryLow) > Integer.parseInt(empSalaryLevel)) {
                    empAdd.setEmpSalaryLevel(salaryLow);
                }
            }
        }
    }


    /**
     * 批量删除员工快照
     *
     * @param historyIds 需要删除的员工快照主键
     * @return 结果
     */
    @Override
    public int deleteEmpHistoryByHistoryIds(Long[] historyIds) {
        return empHistoryMapper.deleteEmpHistoryByHistoryIds(historyIds);
    }

    /**
     * 删除员工快照信息
     *
     * @param historyId 员工快照主键
     * @return 结果
     */
    @Override
    public int deleteEmpHistoryByHistoryId(Long historyId) {
        return empHistoryMapper.deleteEmpHistoryByHistoryId(historyId);
    }


    @Override
    public ImportResultVO uploadEmpHisInfosFile(String yearMonth, MultipartFile multipartFile, LoginUser loginUser) {
        if (StringUtils.isEmpty(yearMonth)) {
            throw new ServiceException("上传时间不可为空");
        }
        String regex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])$";
        if (!yearMonth.matches(regex)) {
            throw new ServiceException("上传时间格式不正确");
        }
        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.ONE.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(loginUser.getUserId());
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<EmpImportVO> excelDate = getExcelDate(multipartFile, EmpImportVO.class);
        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertEmpHisInfos(loginUser.getUserId(), excelDate.getList(), yearMonth);
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_HIS_FAIL_INFO.getKey(), "empHis", file, loginUser.getUsername());
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_HIS_INFO.getKey(), "empHis", multipartFile, loginUser.getUsername()).getFileId();

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

    @Override
    public Boolean empHistoryMonthCopy(MonthTimeVO monthTime, LoginUser loginUser) {

        Date copyMonth = monthTime.getCopyMonth();
        Date beforeMonth = monthTime.getBeforeMonth();
        Date afterMonth = monthTime.getAfterMonth();
        if (beforeMonth.after(afterMonth)) {
            throw new ServiceException("复制月份错误，后一个月份不能小于前一个月份");
        }
        if ((copyMonth.before(beforeMonth) && copyMonth.after(afterMonth)) || (copyMonth.equals(beforeMonth) || copyMonth.equals(afterMonth))) {
            throw new ServiceException("复制月份错误，复制数据月份不能在前后月份之间");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String beforeMonthStr = dateFormat.format(beforeMonth);
        String afterMonthStr = dateFormat.format(afterMonth);
        Date now = new Date();
        String copyMonthStr = dateFormat.format(copyMonth);
        List<EmpHistory> existEmpHistoryInfos = empHistoryMapper.selectEmpDetailHistoryInfosBetweenYearMonth(beforeMonthStr, afterMonthStr);
        if (!CollectionUtils.isEmpty(existEmpHistoryInfos)) {
            throw new ServiceException("被赋值月份已存在数据，无法进行数据复制，请您清空该月数据后再进行操作");
        }
        List<EmpHistory> empHistoryInfos = empHistoryMapper.selectEmpDetailHistoryInfosByYearMonth(copyMonthStr);
        if (CollectionUtils.isEmpty(empHistoryInfos)) {
            throw new ServiceException("当前月份尚未生成历史数据（每个月月底系统生成或手动生成），无法进行复制，请您选择之前的月份进行操作");
        }
        List<Long> empHistoryIds = empHistoryInfos.stream().map(EmpHistory::getHistoryId).collect(Collectors.toList());
        Map<Long, List<FamilyRelationsHistory>> familyRelationHistoryMap = familyRelationsHistoryMapper.
                findInfosByEmpIdsAndHistoryYearMonth(empHistoryIds, copyMonthStr).stream().
                collect(Collectors.groupingBy(FamilyRelationsHistory::getFamEmpId));

        Long userId = loginUser.getUserId();

        LocalDate startDate = LocalDate.parse(beforeMonthStr + "-01");
        LocalDate endDate = LocalDate.parse(afterMonthStr + "-01");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth currentMonth = YearMonth.from(startDate);
        for (EmpHistory empHistoryInfo : empHistoryInfos) {
            empHistoryInfo.setOldEmpHistoryId(empHistoryInfo.getHistoryId());
            empHistoryInfo.setEmpCreateBy(userId);
            empHistoryInfo.setCreateTime(now);
        }
        while (!currentMonth.isAfter(YearMonth.from(endDate))) {
            System.out.println(currentMonth.format(formatter));
            String currentMonthStr = currentMonth.format(formatter);
            for (EmpHistory empHistoryInfo : empHistoryInfos) {
                empHistoryInfo.setHistoryId(null);
                empHistoryInfo.setHistoryYearMoth(currentMonthStr);
            }
            empHistoryMapper.batchInsertHistoryInfos(empHistoryInfos);
            Map<Long, EmpHistory> empHistoryMap = empHistoryInfos.stream().collect(Collectors.toMap(EmpHistory::getOldEmpHistoryId,
                    Function.identity(), (s1, s2) -> s2));
            Map<Long, List<FamilyRelationsHistory>> familyRelationHistoryCopyMap = new HashMap<>(16);
            for (Long empHistoryId : familyRelationHistoryMap.keySet()) {
                Long historyId = empHistoryMap.getOrDefault(empHistoryId, new EmpHistory()).getHistoryId();
                List<FamilyRelationsHistory> familyRelationHistoryList = familyRelationHistoryMap.get(empHistoryId);
                List<FamilyRelationsHistory> familyRelationHistoryCopyList = new ArrayList<>(familyRelationHistoryList.size());
                for (FamilyRelationsHistory familyRelationsHistory : familyRelationHistoryList) {
                    FamilyRelationsHistory relationsHistory = new FamilyRelationsHistory();
                    BeanUtils.copyProperties(familyRelationsHistory, relationsHistory);
                    relationsHistory.setFamEmpId(historyId);
                    relationsHistory.setHistoryId(null);
                    relationsHistory.setHistoryYearMonth(currentMonthStr);
                    familyRelationHistoryCopyList.add(relationsHistory);
                }
                familyRelationHistoryCopyMap.put(historyId, familyRelationHistoryCopyList);
            }
            List<FamilyRelationsHistory> familyRelationsHistoryList = new ArrayList<>();
            for (Long empHistoryId : familyRelationHistoryCopyMap.keySet()) {
                List<FamilyRelationsHistory> familyRelationsHistories = familyRelationHistoryCopyMap.get(empHistoryId);
                familyRelationsHistoryList.addAll(familyRelationsHistories);
            }
            if (!CollectionUtils.isEmpty(familyRelationsHistoryList)) {
                familyRelationsHistoryMapper.batchInsertFamilyRelationsHistory(familyRelationsHistoryList);
            }
            currentMonth = currentMonth.plusMonths(1);
        }
        return true;
    }


    public static void main(String[] args) {

        String beforeMonthStr = "2020-01";
        String afterMonthStr = "2020-12";
        LocalDate startDate = LocalDate.parse(beforeMonthStr + "-01");
        LocalDate endDate = LocalDate.parse(afterMonthStr + "-01");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth currentMonth = YearMonth.from(startDate);

        while (!currentMonth.isAfter(YearMonth.from(endDate))) {
            System.out.println(currentMonth.format(formatter));
            currentMonth = currentMonth.plusMonths(1);
        }
    }

    private ExcelImportResult<EmpImportVO> getExcelDate(MultipartFile file, Class<EmpImportVO> empImportVOClass) {
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            params.setStartRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(empHistoryExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), empImportVOClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = empHistoryExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }


    private void batchInsertEmpHisInfos(Long userId, List<EmpImportVO> empImportInfos, String yearMonth) {
        //empHistoryMapper.removeEmpHistoryListByYearMonth(yearMonth);
        for (EmpImportVO empImportInfo : empImportInfos) {
            EmpAddVO empAdd = transformEmpImportInfoToEmpAdd(empImportInfo);
            List<EmpFamilyVO> familyInfos = new ArrayList<>();
            EmpFamilyVO empFamilyOne = new EmpFamilyVO();
            String oneFamAppellation = empImportInfo.getOneFamAppellation();
            String oneFamPhone = empImportInfo.getOneFamPhone();
            empFamilyOne.setFamAppellation(oneFamAppellation);
            empFamilyOne.setFamContactPhone(oneFamPhone);
            if (!ObjectUtil.areAllFieldsNull(empFamilyOne)) {
                empFamilyOne.setFamEmpSort(1);
                familyInfos.add(empFamilyOne);
            }
            EmpFamilyVO empFamilyTwo = new EmpFamilyVO();
            String twoFamAppellation = empImportInfo.getTwoFamAppellation();
            String twoFamPhone = empImportInfo.getTwoFamPhone();
            empFamilyTwo.setFamAppellation(twoFamAppellation);
            empFamilyTwo.setFamContactPhone(twoFamPhone);
            if (!ObjectUtil.areAllFieldsNull(empFamilyTwo)) {
                empFamilyTwo.setFamEmpSort(2);
                familyInfos.add(empFamilyTwo);
            }
            if (!CollectionUtils.isEmpty(familyInfos)) {
                List<SysDictData> empAppellationDic = dictDataMapper.selectDictDataByType("emp_appellation");
                for (EmpFamilyVO familyInfo : familyInfos) {
                    for (SysDictData empAppellation : empAppellationDic) {
                        if (empAppellation.getDictLabel().equals(familyInfo.getFamAppellation())) {
                            familyInfo.setFamAppellation(empAppellation.getDictValue());
                        }
                    }
                }
            }
            if (empImportInfo.getNativePlace() != null) {
                List<Long> addressIds = new ArrayList<>();
                String[] split = empImportInfo.getNativePlace().split("/");
                int length = split.length;
                if (length == 1) {
                    ChinaAddress chinaAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (chinaAddress != null) {
                        addressIds.add(chinaAddress.getId());
                        empAdd.setNativePlace(addressIds);
                    } else {
                        empAdd.setNativePlace(null);
                    }
                }
                if (length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            empAdd.setNativePlace(addressIds);
                        } else {
                            empAdd.setNativePlace(null);
                        }
                    } else {
                        empAdd.setNativePlace(null);
                    }
                }
                if (length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            ChinaAddress threeAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[2], 3, twoAddress.getId());
                            if (threeAddress != null) {
                                addressIds.add(threeAddress.getId());
                                empAdd.setNativePlace(addressIds);
                            } else {
                                empAdd.setNativePlace(null);
                            }
                        } else {
                            empAdd.setNativePlace(null);
                        }
                    } else {
                        empAdd.setNativePlace(null);
                    }
                }

            }
            if (empImportInfo.getBirthPlace() != null) {
                List<Long> addressIds = new ArrayList<>();
                String[] split = empImportInfo.getBirthPlace().split("/");
                int length = split.length;
                if (length == 1) {
                    ChinaAddress chinaAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (chinaAddress != null) {
                        addressIds.add(chinaAddress.getId());
                        empAdd.setBirthPlace(addressIds);
                    } else {
                        empAdd.setBirthPlace(null);
                    }
                }
                if (length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            empAdd.setBirthPlace(addressIds);
                        } else {
                            empAdd.setBirthPlace(null);
                        }
                    } else {
                        empAdd.setBirthPlace(null);
                    }
                }
                if (length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            ChinaAddress threeAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[2], 3, twoAddress.getId());
                            if (threeAddress != null) {
                                addressIds.add(threeAddress.getId());
                                empAdd.setBirthPlace(addressIds);
                            } else {
                                empAdd.setBirthPlace(null);
                            }
                        } else {
                            empAdd.setBirthPlace(null);
                        }
                    } else {
                        empAdd.setBirthPlace(null);
                    }
                }
            }
            empAdd.setEmpFamilyInfos(familyInfos);
            insertEmpHisInfo(empAdd, userId, yearMonth);
        }

    }

    private void insertEmpHisInfo(EmpAddVO empAdd, Long userId, String yearMonth) {
        Employee employee = new Employee();
        Date now = new Date();
        BeanUtils.copyProperties(empAdd, employee);
        if (!CollectionUtils.isEmpty(empAdd.getNativePlace())) {
            employee.setNativePlace(StringUtils.join(empAdd.getNativePlace(), ","));
        }
        if (!CollectionUtils.isEmpty(empAdd.getBirthPlace())) {
            employee.setBirthPlace(StringUtils.join(empAdd.getBirthPlace(), ","));
        }
        String empIdcard = empAdd.getEmpIdcard();
        if (StringUtils.isNotEmpty(empIdcard)) {
            int age = DateUtils.calculateAge(empIdcard);
            char sexChar = empIdcard.charAt(empIdcard.length() - 2);
            int sexNum = Integer.parseInt(String.valueOf(sexChar));
            if (sexNum % 2 == 0) {
                employee.setEmpGender(EmpGenderEnum.FEMALE.getKey());
            } else {
                employee.setEmpGender(EmpGenderEnum.MALE.getKey());
            }
            employee.setEmpAge(age);
        }
        if (empAdd.getEmpDeptId() != null) {
            String deptName = departmentMapper.selectDepartmentByDeptId(empAdd.getEmpDeptId()).getDeptName();
            employee.setEmpDeptName(deptName);
        }

        String empPosition = employee.getEmpPosition();
        String empPositionLevel = employee.getEmpPositionLevel();
        String empPositionCategory = employee.getEmpPositionCategory();
        PositionInfo positionInfo = positionInfoMapper.findInfoByReferenceParams(empPosition, empPositionLevel,
                empPositionCategory);
        if (positionInfo != null) {
            String salaryHigh = positionInfo.getSalaryHigh();
            String salaryLow = positionInfo.getSalaryLow();
            if (salaryHigh != null) {
                employee.setSalaryLevelMax(salaryHigh);
            }
            if (salaryLow != null) {
                employee.setSalaryLevelMin(salaryLow);
            }
            String empSalaryLevel = employee.getEmpSalaryLevel();
            if (empSalaryLevel != null) {
                if (StringUtils.isNotEmpty(salaryLow)) {
                    if (Integer.parseInt(empSalaryLevel) < Integer.parseInt(salaryLow)) {
                        employee.setEmpSalaryLevel(salaryLow);
                    }
                }
                if (StringUtils.isNotEmpty(salaryHigh)) {
                    if (Integer.parseInt(empSalaryLevel) > Integer.parseInt(salaryHigh)) {
                        employee.setEmpSalaryLevel(salaryHigh);
                    }
                }
            }
        }
        changeEmpStatus(empAdd.getEmpHiredate(), employee, empIdcard);
        //  checkEmpSalaryLevelForInsert(employee);
        employee.setEmpCreateBy(userId);
        employee.setEmpCreateTime(now);
        employee.setEmpUpdateTime(now);
        // excel批量导入默认人员类型为外包
        if (employee.getEmpCategory() == null) {
            employee.setEmpCategory(EmpCategoryEnum.ONE.getKey());
        }
        if (employee.getEmpStatus() == null) {
            employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
        }
        employee.setDisabled(false);
        EmpHistory empHistory = new EmpHistory();
        empHistory.setHistoryYearMoth(yearMonth);
        BeanUtils.copyProperties(employee, empHistory);
        empHistory.setBirthDate(employee.getBirthDate());
        empHistory.setBirthDate(employee.getBirthDate());
        empHistory.setEmpAge(employee.getEmpAge());
        empHistory.setEmpExpireTime(employee.getEmpExpireTime());
        empHistory.setCreateTime(now);
        empHistory.setDisabled(0);
        Employee existEmployee = employeeMapper.findEmpInfoByEmpName(empHistory.getEmpName());
        if (existEmployee != null) {
            empHistory.setEmpId(existEmployee.getEmpId());
        }
        empHistoryMapper.deleteEmpInfoByEmpNameAndYearMoth(empHistory.getEmpName(), yearMonth);
        empHistoryMapper.insertEmpHistory(empHistory);
        // 添加员工亲属历史信息
        familyRelationsHistoryMapper.removeInfosByEmpId(empHistory.getHistoryId(), yearMonth);
        List<EmpFamilyVO> empFamilies = empAdd.getEmpFamilyInfos();
        if (!CollectionUtils.isEmpty(empFamilies)) {
            List<FamilyRelationsHistory> familyRelations = new ArrayList<>();
            for (EmpFamilyVO empFamily : empFamilies) {
                FamilyRelationsHistory familyRelation = new FamilyRelationsHistory();
                BeanUtils.copyProperties(empFamily, familyRelation);
                familyRelation.setFamEmpId(empHistory.getHistoryId());
                familyRelation.setFamEmpName(employee.getEmpName());
                if (empFamily.getFamAge() != null && !"".equals(empFamily.getFamAge())) {
                    String regex = "\\d+";
                    if (empFamily.getFamAge().matches(regex)) {
                        familyRelation.setFamAge(Long.parseLong(empFamily.getFamAge()));
                    }
                }
                familyRelation.setHistoryYearMonth(yearMonth);
                familyRelations.add(familyRelation);
            }
            familyRelationsHistoryMapper.batchInsertFamilyInfos(familyRelations);
        }
    }


    private void changeEmpStatus(Date empHireDate, Employee employee, String empIdcard) {

        String manRetireAge = configMapper.checkConfigKeyUnique("labor.man.retireAge").getConfigValue();
        String womanRetireAge = configMapper.checkConfigKeyUnique("labor.woman.retireAge").getConfigValue();
        Date expireTime = null;
        Date now = new Date();
        Date empExpireTime = employee.getEmpExpireTime();
        if (empExpireTime != null) {
            long days = calculateDays(now, empExpireTime);
            employee.setRetireReminder(days + "");
        }
        if (StringUtils.isNotEmpty(empIdcard)) {
            expireTime = DateUtils.calculateExpireTime(empIdcard, employee.getEmpGender(), manRetireAge, womanRetireAge);
            // 身份证中的年份
            int birthYear = Integer.parseInt(empIdcard.substring(6, 10));
            // 身份证中的月份
            int birthMonth = Integer.parseInt(empIdcard.substring(10, 12));
            // 身份证中的日期
            int birthDay = Integer.parseInt(empIdcard.substring(12, 14));
            // 获取出生日期
            LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
            employee.setBirthDate(Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            long days = calculateDays(now, expireTime);
            employee.setRetireReminder(days + "");
            employee.setEmpExpireTime(expireTime);
        }
        String empPosition = employee.getEmpPosition();
        List<String> empPositionDinInfos = dictDataMapper.selectDictDataByType("emp_position").stream().
                filter(s -> {
                    String dictLabel = s.getDictLabel();
                    return dictLabel.contains("造价") || dictLabel.contains("监理") || dictLabel.contains("安全质量监督")
                            || dictLabel.contains("监造") || dictLabel.contains("设计") || dictLabel.contains("招标");
                }).map(SysDictData::getDictValue).collect(Collectors.toList());

        if (empPositionDinInfos.contains(empPosition) && StringUtils.isNotEmpty(empIdcard)) {
            Date hireLimit = DateUtils.calculateExpireTime(empIdcard, employee.getEmpGender(), "65", "63");
            employee.setHireLimitDate(hireLimit);
        }

        String retireDays = configMapper.checkConfigKeyUnique("labor.employee.retireDays").getConfigValue();
        String empStatus = employee.getEmpStatus();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");


        if (EmpStatusEnum.ON_POSITION.getKey().equals(empStatus) && empIdcard == null && empHireDate != null) {
            String empHireDataStr = format.format(empHireDate);
            String nowStr = format.format(now);
            if (empHireDataStr.equals(nowStr)) {
                employee.setEmpStatus(EmpStatusEnum.NEW_POSITION.getKey());
            }
        }
        if (expireTime != null) {
            long intervalDays = DateUtils.calculateDays(now, expireTime);
            long intervalNum = expireTime.getTime() - now.getTime();
            if (EmpStatusEnum.ON_POSITION.getKey().equals(empStatus)) {
                if (intervalNum <= 0) {
                    employee.setEmpStatus(EmpStatusEnum.RE_EMPLOY.getKey());
                } else {
                    if (intervalDays <= Long.parseLong(retireDays)) {
                        employee.setEmpStatus(EmpStatusEnum.ALMOST.getKey());
                    } else {
                        if (empHireDate != null) {
                            String empHireDataStr = format.format(empHireDate);
                            String nowStr = format.format(now);
                            if (empHireDataStr.equals(nowStr)) {
                                employee.setEmpStatus(EmpStatusEnum.NEW_POSITION.getKey());
                            } else {
                                employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
                            }
                        }
                    }
                }
            } else if (EmpStatusEnum.EXPIRE.getKey().equals(empStatus)) {
                if (intervalNum <= 0) {
                    employee.setEmpStatus(EmpStatusEnum.EXPIRE.getKey());
                } else {
                    throw new ServiceException("员工状态校验未通过");
                }
            }
        }
    }

    private long calculateDays(Date startTime, Date endTime) {
        long startMillis = startTime.getTime();
        long endMillis = endTime.getTime();
        long diffMillis = endMillis - startMillis;
        long diffDays = diffMillis / (1000 * 60 * 60 * 24);
        return diffDays;

    }


    private EmpAddVO transformEmpImportInfoToEmpAdd(EmpImportVO empImportInfo) {
        EmpAddVO empAdd = new EmpAddVO();
        BeanUtils.copyProperties(empImportInfo, empAdd);

//        List<SysDictData> empTypes = dictDataMapper.selectDictDataByType("emp_type");
//        for (SysDictData empType : empTypes) {
//            if (empType.getDictLabel().equals(empImportInfo.getEmpType())) {
//                empAdd.setEmpType(empType.getDictValue());
//            }
//        }
        if (empImportInfo.getEmpStatus() != null) {
            List<SysDictData> empStatusInfos = dictDataMapper.selectDictDataByType("emp_status");
            for (SysDictData empStatusInfo : empStatusInfos) {
                if (empStatusInfo.getDictLabel().equals(empImportInfo.getEmpStatus())) {
                    empAdd.setEmpStatus(empStatusInfo.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpSalaryLevel() != null) {
            List<SysDictData> empSalaryLevels = dictDataMapper.selectDictDataByType("emp_salary_level");
            for (SysDictData empSalaryLevel : empSalaryLevels) {
                if (empSalaryLevel.getDictLabel().equals(empImportInfo.getEmpSalaryLevel())) {
                    empAdd.setEmpSalaryLevel(empSalaryLevel.getDictValue());
                }
            }
        }


        if (empImportInfo.getEmpEmployingUnits() != null) {
            List<SysDictData> hireCompanies = dictDataMapper.selectDictDataByType("hire_company");
            for (SysDictData hireCompany : hireCompanies) {
                if (hireCompany.getDictLabel().equals(empImportInfo.getEmpEmployingUnits())) {
                    empAdd.setEmpEmployingUnits(hireCompany.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpHrCompany() != null) {
            List<SysDictData> hrCompanies = dictDataMapper.selectDictDataByType("emp_hr_company");
            for (SysDictData hrCompany : hrCompanies) {
                if (hrCompany.getDictLabel().equals(empImportInfo.getEmpHrCompany())) {
                    empAdd.setEmpHrCompany(hrCompany.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPosition() != null) {
            List<SysDictData> empPositions = dictDataMapper.selectDictDataByType("emp_position");
            for (SysDictData empPosition : empPositions) {
                if (empPosition.getDictLabel().equals(empImportInfo.getEmpPosition())) {
                    empAdd.setEmpPosition(empPosition.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPositionCategory() != null) {
            List<SysDictData> empPositionCategoryDic = dictDataMapper.selectDictDataByType("emp_position_category");
            for (SysDictData empPositionCategory : empPositionCategoryDic) {
                if (empPositionCategory.getDictLabel().equals(empImportInfo.getEmpPositionCategory())) {
                    empAdd.setEmpPositionCategory(empPositionCategory.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPositionLevel() != null) {
            List<SysDictData> empPositionLevels = dictDataMapper.selectDictDataByType("emp_position_level");
            for (SysDictData empPositionLevel : empPositionLevels) {
                if (empPositionLevel.getDictLabel().equals(empImportInfo.getEmpPositionLevel())) {
                    empAdd.setEmpPositionLevel(empPositionLevel.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPoliticalStatus() != null) {
            List<SysDictData> empPoliticalStatusInfos = dictDataMapper.selectDictDataByType("emp_political_status");
            for (SysDictData empPoliticalStatusInfo : empPoliticalStatusInfos) {
                if (empPoliticalStatusInfo.getDictLabel().equals(empImportInfo.getEmpPoliticalStatus())) {
                    empAdd.setEmpPoliticalStatus(empPoliticalStatusInfo.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpEducation() != null) {
            List<SysDictData> empEducations = dictDataMapper.selectDictDataByType("emp_education");
            for (SysDictData empEducation : empEducations) {
                if (empEducation.getDictLabel().equals(empImportInfo.getEmpEducation())) {
                    empAdd.setEmpEducation(empEducation.getDictValue());
                }
                if (empEducation.getDictLabel().equals(empImportInfo.getHighestEducation())) {
                    empAdd.setHighestEducation(empEducation.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpGender() != null) {
            List<SysDictData> empSexDic = dictDataMapper.selectDictDataByType("emp_sex");
            for (SysDictData empSex : empSexDic) {
                if (empSex.getDictLabel().equals(empImportInfo.getEmpGender())) {
                    empAdd.setEmpGender(empSex.getDictValue());
                }
            }
        }


//        List<SysDictData> empCategories = dictDataMapper.selectDictDataByType("emp_category");
//        for (SysDictData empCategory : empCategories) {
//            if (empCategory.getDictLabel().equals(empImportInfo.getEmpCategory())) {
//                empAdd.setEmpCategory(empCategory.getDictValue());
//            }
//        }
        if (empImportInfo.getEmpTitle() != null) {
            List<SysDictData> empTitles = dictDataMapper.selectDictDataByType("emp_title");
            for (SysDictData empTitle : empTitles) {
                if (empTitle.getDictLabel().equals(empImportInfo.getEmpTitle())) {
                    empAdd.setEmpTitle(empTitle.getDictValue());
                }
            }
        }

        String empUnitDeptName = empImportInfo.getEmpDeptName();
        String regex = "^(.)+-(.)+$";
        if (empUnitDeptName != null) {
            if (empUnitDeptName.matches(regex)) {
                String[] split = empUnitDeptName.split("-");
                String empUnit = split[0];
                String empDept = split[1];
                Department department = departmentMapper.findDepartmentInfoByUnitAndDeptName(empUnit, empDept);
                if (department != null) {
                    empAdd.setEmpDeptId(department.getDeptId());
                    empAdd.setEmpDeptName(department.getDeptName());
                }
            } else {
                Department department = departmentMapper.findDepartmentInfoByName(empUnitDeptName);
                if (department != null) {
                    empAdd.setEmpDeptId(department.getDeptId());
                    empAdd.setEmpDeptName(department.getDeptName());
                }
            }
        }
        return empAdd;

    }

}
