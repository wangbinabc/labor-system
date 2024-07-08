package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpSalary;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.SalaryConfig;
import com.yuantu.labor.service.IEmpSalaryService;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.service.ISalaryConfigService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.service.impl.SysDictDataServiceImpl;
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
import java.util.Calendar;
import java.util.List;

/**
 * 员工酬薪主Controller
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@ApiOperation("员工薪资管理")
@RestController
@RequestMapping("/labor/salary")
public class EmpSalaryController extends BaseController {
    @Autowired
    private IEmpSalaryService empSalaryService;

    /**
     * 查询员工酬薪主列表
     */
    @ApiOperation("查询员工酬薪主列表")
//    @PreAuthorize("@ss.hasPermi('labor:salary:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmpSalary empSalary) {
        startPage();
        List<EmpSalary> list = empSalaryService.selectEmpSalaryList(empSalary);
        return getDataTable(list);
    }

    @GetMapping("/nameOrId")
// 2 、输入姓名或者身份证查找
    public TableDataInfo selectNameOrId( EmpSalary empSalary){
        List<EmpSalary> list =empSalaryService.selectByNameOrId(empSalary);
      return  getDataTable(list);
    }

    //1薪资构成配置
}

