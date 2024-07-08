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
import com.yuantu.labor.domain.EmpSalary;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.PostExcelImportVerifyHandler;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.mapper.PostHistoryMapper;
import com.yuantu.labor.service.IPostHistoryService;
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
 * 职位变更Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Service
public class PostHistoryServiceImpl implements IPostHistoryService {
    @Autowired
    private PostHistoryMapper postHistoryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private PostExcelImportVerifyHandler postExcelImportVerifyHandler;

    /**
     * 查询职位变更
     *
     * @param phId 职位变更主键
     * @return 职位变更
     */
    @Override
    public PostHistory selectPostHistoryByPhId(Long phId) {
        return postHistoryMapper.selectPostHistoryByPhId(phId);
    }

    /**
     * 查询职位变更列表
     *
     * @param postHistory 职位变更
     * @return 职位变更
     */
    @Override
    public List<PostHistory> selectPostHistoryList(PostHistory postHistory) {
        return isChange(postHistoryMapper.selectPostHistoryList(postHistory));
    }


    @Override
    public List<PostHistory> findExportInfos(ExportVO export) {

        return postHistoryMapper.findExportInfos(export);
    }

    @Override
    public void exportDivide(HttpServletResponse response, ExportDivideVO exportDivide) {
        ExportVO export = new ExportVO();
        BeanUtils.copyProperties(exportDivide, export);
        List<PostHistory> exportInfos = findExportInfos(export);


        if (CollectionUtils.isEmpty(exportInfos)) {
            return;
        }

        //-------------------导出不脱敏----------------------
        List<Employee> employeeList = employeeMapper.findAllEmployees();
        Map<Long, Employee> empMap = employeeList.stream().collect(Collectors.
                toMap(Employee::getEmpId, Function.identity()));

        for (PostHistory ea : exportInfos) {
            ea.setPhEmpIdcard(empMap.get(ea.getPhEmpId()) != null && empMap.get(ea.getPhEmpId()).getEmpIdcard() != null ? empMap.get(ea.getPhEmpId()).getEmpIdcard() : "card_is_Null");
        }
        //----------------------------------------
        PostHistoryChangeDicVO empChangeDic = changeDicValue(exportInfos, exportDivide.getFieldName());
        exportInfos = empChangeDic.getExportInfos();
        String chineseName = empChangeDic.getChineseName();

        String groupByProperty = exportDivide.getFieldName();
        Map<String, List<PostHistory>> groupedData = exportInfos.stream()
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
            List<PostHistory> exportList = groupedData.get(key);
            for (PostHistory exportInfo : exportList) {
                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = PostHistory.class.getDeclaredField(fieldName);
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

        File baseDir = new File("职位变更信息");
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
            List<PostHistory> list = groupedData.get(key);
            //----------脱敏---------
            for (PostHistory postHistory : list) {
                if (StringUtils.isNotEmpty(postHistory.getPhEmpIdcard()) && !postHistory.getPhEmpIdcard().equals("card_is_Null")) {
                    postHistory.setPhEmpIdcard(Base64.desensitize(postHistory.getPhEmpIdcard()));
                }
            }
            ExcelUtil<PostHistory> util = new ExcelUtil<>(PostHistory.class);
            util.init(list, "职位变更信息", excelName, Excel.Type.EXPORT);
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

    private PostHistoryChangeDicVO changeDicValue(List<PostHistory> exportInfos, String fieldName) {
        Field[] declaredFields = PostHistory.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (PostHistory exportInfo : exportInfos) {
                try {
                    Field field = PostHistory.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    PostHistoryChangeDicVO empChangeDic = new PostHistoryChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        PostHistoryChangeDicVO empChangeDic = new PostHistoryChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setExportInfos(exportInfos);
        return empChangeDic;
    }

    @Override
    public Map countPostHistoryByYears(Long years) {

        List<PostChangesCountVO> list = postHistoryMapper.countPostHistoryByYears(years);
        Long nowYear = Long.valueOf(new SimpleDateFormat("yyyy").format(DateUtils.getNowDate()));


        Map<String, List<PostChangesCountVO>> map = new HashMap<>();
        for (PostChangesCountVO pcv : list) {
            if (!map.containsKey(pcv.getPhAdjustType())) {
                List<PostChangesCountVO> vals = new ArrayList<>();
                vals.add(pcv);
                map.put(pcv.getPhAdjustType(), vals);
            } else {
                List<PostChangesCountVO> vals = map.get(pcv.getPhAdjustType());
                vals.add(pcv);
            }
        }

        List<PostChangesCountVO> ups = map.get("1") != null ? map.get("1") : new ArrayList<>();
        List<PostChangesCountVO> downs = map.get("3") != null ? map.get("3") : new ArrayList<>();

        for (Long i = nowYear; i > nowYear - years; i--) {
            PostChangesCountVO p = new PostChangesCountVO();
            p.setPhAdjustYear(i.toString());
            if (!ups.contains(p)) {
                p.setCountResult("0");
                p.setPhAdjustType("1");
                ups.add(p);
            }

        }

        for (Long i = nowYear; i > nowYear - years; i--) {
            PostChangesCountVO p = new PostChangesCountVO();
            p.setPhAdjustYear(i.toString());
            if (!downs.contains(p)) {
                p.setCountResult("0");
                p.setPhAdjustType("3");
                downs.add(p);
            }
        }

        Collections.sort(ups, (up1, up2) -> (up2.getPhAdjustYear().compareTo(up1.getPhAdjustYear())));
        Collections.sort(downs, (down1, down2) -> (down2.getPhAdjustYear().compareTo(down1.getPhAdjustYear())));

        Map count = new HashMap();
        count.put("promotionCount", ups);
        count.put("declineCount", downs);
        count.put("thisYearPromotionCount", ups.get(0));
        count.put("thisYearDeclineCount", downs.get(0));
        count.put("lastYearPromotionCount", ups.get(1));
        count.put("lastYearDeclineCount", downs.get(1));
        return count;
    }

    @Override
    public List<PostHistory> selectPostHistoryListByParamsVo(PostQueryParamsVo paramsVo) {
        return isChange(postHistoryMapper.selectPostHistoryListByParamsVo(paramsVo));
    }

    /**
     * 新增职位变更
     *
     * @param postHistory 职位变更，必须大于一个月
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPostHistory(PostHistory postHistory) {
        if (postHistory.getPhEmpId() != null) {
            Employee e = employeeMapper.selectEmployeeByEmpId(postHistory.getPhEmpId());
            postHistory.setPhEmpIdcard(e.getEmpIdcard());
            postHistory.setPhEmpName(e.getEmpName());
        }
//        else if (postHistory.getPhEmpIdcard()!=null){
//            Employee e = employeeMapper.findInfoByEmpIdCard(postHistory.getPhEmpIdcard());
//            postHistory.setPhEmpId(e.getEmpId());
//            postHistory.setPhEmpName(e.getEmpName());
//        }
        else {
            Employee e = employeeMapper.findEmpInfoByEmpName(postHistory.getPhEmpName());
            postHistory.setPhEmpId(e.getEmpId());
            postHistory.setPhEmpIdcard(e.getEmpIdcard());
        }

        //   postHistory.setCreateTime(DateUtils.getNowDate());
        //设置查询条件
        PostHistory search = new PostHistory();
        search.setPhEmpIdcard(postHistory.getPhEmpIdcard());
        search.setPhAdjustDate(postHistory.getPhAdjustDate());
        search.setPhEmpId(postHistory.getPhEmpId());


        List<PostHistory> list = postHistoryMapper.selectPostHistoryList(search);

        //记录数为0,正常插入
        if (list.size() == 0) {
            return postHistoryMapper.insertPostHistory(postHistory);
        } else {
            PostHistory exists = list.get(0);
            if (postHistory.getInsertType() == 1 && exists.getInsertType() != 1) {
                postHistoryMapper.deletePostHistoryByPhId(exists.getPhId());
                return postHistoryMapper.insertPostHistory(postHistory);
            } else {
                throw new ServiceException("同一提交日期只能存在一条记录,插入失败!");
            }
        }
    }

    /**
     * 修改职位变更
     *
     * @param postHistory 职位变更
     * @return 结果
     */
    @Override
    public int updatePostHistory(PostHistory postHistory) {
//        //设置查询条件
//        PostHistory search = new PostHistory();
//        search.setPhEmpIdcard(postHistory.getPhEmpIdcard());
//        search.setPhAdjustDate(postHistory.getPhAdjustDate());
//        search.setPhEmpId(postHistory.getPhEmpId());
//        List<PostHistory> list=  postHistoryMapper.selectPostHistoryList(search);
//        if (list.size()!=0){
//            throw new ServiceException("此员工提交日期已有记录,修改失败,请删除后再次添加记录!");
//        }
        //    postHistory.setUpdateTime(DateUtils.getNowDate());
        postHistory.setPhEmpIdcard(null);
        return postHistoryMapper.updatePostHistory(postHistory);
    }

    /**
     * 批量删除职位变更
     *
     * @param phIds 需要删除的职位变更主键
     * @return 结果
     */
    @Override
    public int deletePostHistoryByPhIds(Long[] phIds) {
        return postHistoryMapper.deletePostHistoryByPhIds(phIds);
    }

    /**
     * 删除职位变更信息
     *
     * @param phId 职位变更主键
     * @return 结果
     */
    @Override
    public int deletePostHistoryByPhId(Long phId) {
        return postHistoryMapper.deletePostHistoryByPhId(phId);
    }


    @Override
    public ImportResultVO uploadPostInfosFile(MultipartFile multipartFile, Long userId, String username) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.ONE.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<PostHistorySimpleVO> excelDate = getExcelDate(multipartFile, PostHistorySimpleVO.class);
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
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_POST_INFO.getKey(), "post", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long fileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_POST_INFO.getKey(), "post", multipartFile, username).getFileId();

        fileImportRecord.setOriginFileId(fileId);
        fileImportRecord.setSuccessCount(successCount);
        fileImportRecord.setFailureCount(errorCount);
        fileImportRecord.setTotalCount(successCount + errorCount);
        //   fileImportRecord.setOriginFileId(empInfoFileId);
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

    private void batchInsertPostInfos(List<PostHistorySimpleVO> postImportInfos) {

        for (PostHistorySimpleVO postHistorySimpleVO : postImportInfos) {
            PostHistory postHistory = new PostHistory();
            BeanUtils.copyProperties(postHistorySimpleVO, postHistory);
//            for (PostChangeTypeEnum value : PostChangeTypeEnum.values()) {
//                if (value.getValue().equals(postHistorySimpleVO.getPhAdjustType())) {
//                    postHistory.setPhAdjustType(value.getKey());
//                }
//            }

            List<SysDictData> postLevels = dictDataMapper.selectDictDataByType("emp_position_level");
            for (SysDictData postLevel : postLevels) {
                if (postLevel.getDictLabel().equals(postHistorySimpleVO.getPhDestinPostLevel())) {
                    postHistory.setPhDestinPostLevel(postLevel.getDictValue());
                }
                if (postLevel.getDictLabel().equals(postHistorySimpleVO.getPhOriginPostLevel())) {
                    postHistory.setPhOriginPostLevel(postLevel.getDictValue());
                }
            }

            List<SysDictData> postNames = dictDataMapper.selectDictDataByType("emp_position");
            for (SysDictData postName : postNames) {
                if (postName.getDictLabel().equals(postHistorySimpleVO.getPhOriginPostName())) {
                    postHistory.setPhOriginPostName(postName.getDictValue());
                }
                if (postName.getDictLabel().equals(postHistorySimpleVO.getPhDestinPostName())) {
                    postHistory.setPhDestinPostName(postName.getDictValue());
                }
            }

            List<SysDictData> adjustTypes = dictDataMapper.selectDictDataByType("post_history_type");
            for (SysDictData adjustType : adjustTypes) {
                if (adjustType.getDictLabel().equals(postHistorySimpleVO.getPhAdjustType())) {
                    postHistory.setPhAdjustType(adjustType.getDictValue());
                }
            }
            postHistory.setInsertType(Long.valueOf(2));
            postHistory.setCreateTime(DateUtils.getNowDate());

            insertPostHistory(postHistory);
        }
    }


    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private ExcelImportResult<PostHistorySimpleVO> getExcelDate(MultipartFile file, Class<PostHistorySimpleVO> postImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(postExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), postImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = postExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }


    public void downloadExcelTemplate(HttpServletResponse response) {
//        InputStream inputStream = null;
//        try {
//            ClassPathResource classPathResource = new ClassPathResource("static/post.xlsx");
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

            List<String> positionDic = dictDataMapper.selectDictDataByType("emp_position").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

            List<String> levelDic = dictDataMapper.selectDictDataByType("emp_position_level").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

            List<String> typeDic = dictDataMapper.selectDictDataByType("post_history_type").stream().
                    map(SysDictData::getDictLabel).collect(Collectors.toList());

            Map<Integer, List<String>> downDropMap = new HashMap<>(16);
            downDropMap.put(1, positionDic);
            downDropMap.put(3, positionDic);
            downDropMap.put(2, levelDic);
            downDropMap.put(4, levelDic);
            downDropMap.put(6, typeDic);
            DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);
            List<PostHistoryTemplateVO> list = new ArrayList<>();

            String fileName = URLEncoder.encode("岗位变动模板", "UTF-8");

            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, PostHistoryTemplateVO.class).
                    registerWriteHandler(downWriteHandler).sheet("岗位变动模板").doWrite(list);
        } catch (IOException e) {
            throw new ServiceException("下载岗位变动模板失败");
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

    public List<PostHistory> selectPostHistoryListByScreen(PostHistoryScreenVO postHistoryScreenVO) {
        return isChange(postHistoryMapper.selectPostHistoryListByScreen(postHistoryScreenVO));
    }

    private List<PostHistory> isChange(List<PostHistory> list) {
        for (PostHistory postHistory : list) {
            Employee e = employeeMapper.selectEmployeeByEmpId(postHistory.getPhEmpId());
            if (e == null) {
                postHistory.setIsChange(false);
            }
        }

        return list;
    }

}
