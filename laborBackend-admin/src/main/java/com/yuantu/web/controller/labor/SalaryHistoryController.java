package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.SalaryHistory;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.service.ISalaryHistoryService;
import com.yuantu.labor.service.ITrainProjectService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 薪级变动Controller
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@Api("薪级变动管理")
@RestController
@RequestMapping("/labor/salaryhis")
public class SalaryHistoryController extends BaseController {
    @Autowired
    private ISalaryHistoryService salaryHistoryService;
    /**
     * 查询薪级变动列表
     */
    @ApiOperation("查询薪级变动列表")
   // @PreAuthorize("@ss.hasPermi('labor:salaryhis:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalaryHisQueryVO vo) {
        startPage();
        List<SalaryHistory> list = salaryHistoryService.selectSalaryHistoryList(vo);
        return getDataTable(list);
    }
}
