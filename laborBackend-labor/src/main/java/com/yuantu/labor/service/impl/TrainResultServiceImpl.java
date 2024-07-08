package com.yuantu.labor.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.bean.BeanUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.cenum.EmpDocTypeEnum;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.TrainResultExcelImportVerifyHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.yuantu.labor.service.ITrainResultService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 培训成果Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-25
 */
@Service
public class TrainResultServiceImpl implements ITrainResultService {
    @Autowired
    private TrainResultMapper trainResultMapper;

    @Autowired
    private EmpDocumentMapper empDocumentMapper;
    private static final Logger log = LoggerFactory.getLogger(TrainResultServiceImpl.class);



    /**
     * 查询培训成果列表
     *
     * @param trainResult 培训成果
     * @return 培训成果
     */
    @Override
    public List<TrainResult> selectTrainResultList(TrainResult trainResult) {
        List<TrainResult> trainResults = trainResultMapper.selectTrainResultList(trainResult);
        List<Long> trainIds = trainResults.stream().map(s -> {
            Integer resultId = s.getResultId();
            return resultId.longValue();
        }).collect(Collectors.toList());
        Map<Long, List<EmpDocument>> docMap = empDocumentMapper.findDocInfosByEmpIdsAndTypes(trainIds, Collections.
                singletonList(EmpDocTypeEnum.SEVEN.getKey())).stream().collect(Collectors.groupingBy(EmpDocument::getDocEmpId));
        for (TrainResult result : trainResults) {
            long trainId = result.getResultId().longValue();
            Optional<EmpDocument> document = docMap.getOrDefault(trainId, new ArrayList<>()).stream().findFirst();
            if (document.isPresent()) {
                FileVO file = new FileVO();
                EmpDocument docInfo = document.get();
                file.setFileId(docInfo.getDocId());
                file.setFileName(docInfo.getDocName());
                file.setFileUrl(docInfo.getDocAnnexPath());
                file.setFileType(docInfo.getDocType());
                result.setFileVO(file);
            }
        }
        return trainResultMapper.selectTrainResultList(trainResult);
    }

}
