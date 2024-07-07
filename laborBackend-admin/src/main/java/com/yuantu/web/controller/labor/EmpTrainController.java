package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpTrain;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.service.IEmpTrainService;
import com.yuantu.labor.service.IEmployeeService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 培训记录Controller
 * 
 * @author ruoyi
 * @date 2023-09-22
 */
@Api("培训记录管理")
@RestController
@RequestMapping("/labor/train")
public class EmpTrainController extends BaseController
{
    @Autowired
    private IEmpTrainService empTrainService;

    /**
     * 查询培训记录列表
     */

    @ApiOperation("查询培训记录列表")
    //@PreAuthorize("@ss.hasPermi('labor:train:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmpTrain empTrain)
    {
        startPage();
        List<EmpTrain> list = empTrainService.selectEmpTrainList(empTrain);
        return getDataTable(list);
    }

}
