package com.yuantu.labor.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.bean.BeanUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.domain.SalaryHistory;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.SalaryHisExcelImportVerifyHandler;
import com.yuantu.labor.handler.TemplateCellWriteHandler;
import com.yuantu.labor.mapper.EmpDocumentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.mapper.SalaryHistoryMapper;
import com.yuantu.labor.service.ISalaryHistoryService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yuantu.labor.service.impl.EmployeeServiceImpl.setMyCellStyle;

/**
 * 薪级变动Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@Service
public class SalaryHistoryServiceImpl implements ISalaryHistoryService {
    @Autowired
    private SalaryHistoryMapper salaryHistoryMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    private static final Logger log = LoggerFactory.getLogger(SalaryHistoryServiceImpl.class);




    /**
     * 查询薪级变动列表
     *
     * @param vo 薪级变动
     * @return 薪级变动
     */
    @Override
    public List<SalaryHistory> selectSalaryHistoryList(SalaryHisQueryVO vo) {
        List<SalaryHistory> list = salaryHistoryMapper.selectSalaryHistoryList(vo);
        if (list != null && list.size() > 0) {
            List<Long> empIds = list.stream().map(SalaryHistory::getHisEmpId).collect(Collectors.toList());
            Map<Long, Employee> employeeMap = employeeMapper.selectEmployeeInfosByIds(empIds).stream().collect(Collectors.toMap(Employee::getEmpId, Function.identity()));

            for (SalaryHistory salaryHistory : list) {
                Employee existEmp = employeeMap.get(salaryHistory.getHisEmpId());
                if (existEmp == null) {
                    salaryHistory.setIsChange(false);
                } else {
                    salaryHistory.setIsChange(true);
                }
                if (StringUtils.isNotEmpty(salaryHistory.getHisEmpIdcard())) {
                    salaryHistory.setHisEmpIdcard(Base64.desensitize(salaryHistory.getHisEmpIdcard()));
                }
            }
        }

        return list;
    }



}
