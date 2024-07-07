package com.yuantu.labor.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.handler.EmpProfitExcelImportVerifyHandler;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.vo.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.EmpEffectivenessMapper;
import com.yuantu.labor.domain.EmpEffectiveness;
import com.yuantu.labor.service.IEmpEffectivenessService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 人员效能Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-06
 */
@Service
public class EmpEffectivenessServiceImpl implements IEmpEffectivenessService {
    @Autowired
    private EmpEffectivenessMapper empEffectivenessMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询人员效能
     *
     * @param effId 人员效能主键
     * @return 人员效能
     */
    @Override
    public EmpEffectiveness selectEmpEffectivenessByEffId(Long effId) {
        return empEffectivenessMapper.selectEmpEffectivenessByEffId(effId);
    }

    /**
     * 查询人员效能列表
     *
     * @param empEffectiveness 人员效能
     * @return 人员效能
     */
    @Override
    public List<EmpEffectiveness> selectEmpEffectivenessList(EmpEffectiveness empEffectiveness) {
        return empEffectivenessMapper.selectEmpEffectivenessList(empEffectiveness);
    }

    /**
     * 新增人员效能
     *
     * @param empEffectiveness 人员效能
     * @return 结果
     */
    @Override
    public int insertEmpEffectiveness(EmpEffectiveness empEffectiveness) {
        empEffectiveness.setCreateTime(DateUtils.getNowDate());
        return empEffectivenessMapper.insertEmpEffectiveness(empEffectiveness);
    }

    /**
     * 修改人员效能
     *
     * @param empEffectiveness 人员效能
     * @return 结果
     */
    @Override
    public int updateEmpEffectiveness(EmpEffectiveness empEffectiveness) {
        empEffectiveness.setUpdateTime(DateUtils.getNowDate());
        return empEffectivenessMapper.updateEmpEffectiveness(empEffectiveness);
    }

    /**
     * 批量删除人员效能
     *
     * @param effIds 需要删除的人员效能主键
     * @return 结果
     */
    @Override
    public int deleteEmpEffectivenessByEffIds(Long[] effIds) {
        return empEffectivenessMapper.deleteEmpEffectivenessByEffIds(effIds);
    }

    /**
     * 删除人员效能信息
     *
     * @param effId 人员效能主键
     * @return 结果
     */
    @Override
    public int deleteEmpEffectivenessByEffId(Long effId) {
        return empEffectivenessMapper.deleteEmpEffectivenessByEffId(effId);
    }

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;


    @Autowired
    private FileService fileService;

    @Override
    public ImportResultVO uploadEmpProfitInfosFile(MultipartFile multipartFile, LoginUser loginUser) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.EIGHTEEN.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(loginUser.getUserId());
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<EmpProfitImportVO> excelDate = getExcelDate(multipartFile, EmpProfitImportVO.class);

        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertEmpProfitInfos(loginUser.getUserId(), excelDate.getList());
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_PROFIT_FAIL_INFO.getKey(), "empProfit", file, loginUser.getUsername());
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_PROFIT_INFO.getKey(), "empProfit", multipartFile, loginUser.getUsername()).getFileId();

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

    private void batchInsertEmpProfitInfos(Long userId, List<EmpProfitImportVO> profitImportInfos) {

        List<EmpEffectiveness> empEffectivenessInfos = new ArrayList<>();
        List<String> empNames = profitImportInfos.stream().map(EmpProfitImportVO::getEmpName).collect(Collectors.toList());
        Map<String, Employee> employeeMap = employeeMapper.findInfoEmpNames(empNames).stream().collect(Collectors.
                toMap(Employee::getEmpName, Function.identity()));
        for (EmpProfitImportVO profitImportInfo : profitImportInfos) {
            empEffectivenessMapper.removeInfosByEmpNameAndTime(profitImportInfo.getEmpName(), profitImportInfo.getYearMonth());
        }
        Date now = new Date();
        for (EmpProfitImportVO profitImportInfo : profitImportInfos) {
            EmpEffectiveness empEffectiveness = new EmpEffectiveness();
            Employee employee = employeeMap.getOrDefault(profitImportInfo.getEmpName(), new Employee());
            empEffectiveness.setEffEmpId(employee.getEmpId());
            empEffectiveness.setEffEmpIdcard(employee.getEmpIdcard());
            empEffectiveness.setEffYearMonth(profitImportInfo.getYearMonth());
            empEffectiveness.setEffEmpProfitValue(profitImportInfo.getProfitValue().toString());
            empEffectiveness.setDisabled(0);
            empEffectiveness.setCreateTime(now);
            empEffectiveness.setCreateBy(userId.toString());
            empEffectivenessInfos.add(empEffectiveness);
        }
        empEffectivenessMapper.insertBatch(empEffectivenessInfos);
    }

    @Autowired
    private EmpProfitExcelImportVerifyHandler empProfitExcelImportVerifyHandler;

    private ExcelImportResult<EmpProfitImportVO> getExcelDate(MultipartFile file, Class<EmpProfitImportVO> empProfitImportClass) {
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(empProfitExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), empProfitImportClass, params);
        } catch (Exception e) {
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<EmpProfitCheckVO>> threadLocal = empProfitExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }
}
