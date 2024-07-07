package com.yuantu.web.controller.labor;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;


import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpDocument;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.service.IFileService;
import com.yuantu.labor.service.ITrainProjectService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.yuantu.common.utils.DateUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.labor.domain.TrainResult;
import com.yuantu.labor.service.ITrainResultService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 培训成果Controller
 *
 * @author ruoyi
 * @date 2023-09-25
 */
@Api("培训成果管理")
@RestController
@RequestMapping("/labor/result")
public class TrainResultController extends BaseController {
    @Autowired
    private ITrainResultService trainResultService;

    /**
     * 查询培训成果列表
     */
    @ApiOperation("查询培训成果列表")
    @GetMapping("/list")
    public TableDataInfo list(TrainResult trainResult) {
        startPage();
        List<TrainResult> list = trainResultService.selectTrainResultList(trainResult);
        return getDataTable(list);
    }



}
