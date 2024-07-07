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
import com.yuantu.common.utils.bean.BeanUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.TrainProjectExcelImportVerifyHandler;
import com.yuantu.labor.handler.TrainProjectRecordExcelImportVerifyHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.ITrainProjectService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 培训项目Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-14
 */
@Service
public class TrainProjectServiceImpl implements ITrainProjectService {
    @Autowired
    private TrainProjectMapper trainProjectMapper;

    private static final Logger log = LoggerFactory.getLogger(TrainProjectServiceImpl.class);


    @Override
    public List<TrainProject> selectTrainProjectListByQueryVO(TrainProjectQueryVO vo) {
        return trainProjectMapper.selectTrainProjectListByQueryVO(vo);
    }




}
