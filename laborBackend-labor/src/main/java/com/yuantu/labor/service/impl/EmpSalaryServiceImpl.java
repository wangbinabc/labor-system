package com.yuantu.labor.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
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
import com.yuantu.labor.mapper.EmpSalaryMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.mapper.SalaryConfigMapper;
import com.yuantu.labor.service.IEmpSalaryService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工酬薪主Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Service
public class EmpSalaryServiceImpl implements IEmpSalaryService {
    @Autowired
    private EmpSalaryMapper empSalaryMapper;

    /**
     * 查询员工酬薪主列表
     *
     * @param empSalary 员工酬薪主
     * @return 员工酬薪主
     */
    @Override
    public List<EmpSalary> selectEmpSalaryList(EmpSalary empSalary) {
        return empSalaryMapper.selectEmpSalaryList(empSalary);
    }



    @Override
    public BigDecimal sumSalaryItemBySalaryType(Integer salaryId, String SalaryType) {
        List<EmpSalaryItem> itemList = empSalaryMapper.selectEmpSalaryItemListBySalaryId(salaryId);
        BigDecimal sum = BigDecimal.ZERO;
        if (itemList != null && itemList.size() > 0) {
            for (EmpSalaryItem item : itemList) {
                if (item.getItemConfTypeLabel().equals(SalaryType)) {
                    sum = sum.add(item.getItemValue());
                }
            }
        }
        return sum;
    }

    @Override
    public String sumLastYearNetPay(EmpSalary empSalary) {
        return empSalaryMapper.sumLastYearNetPay(empSalary);
    }

    @Override
    public List<SalaryChartVO> countPastYearNetPay(EmpSalaryQueryVO queryVO) {
        return empSalaryMapper.countPastYearNetPay(queryVO);
    }

}



