package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.labor.domain.TrainExamLibrary;
import com.yuantu.labor.service.ITrainExamLibraryService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 题库Controller
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
@RestController
@Api("题库管理")
@RequestMapping("/labor/library")
public class TrainExamLibraryController extends BaseController
{
    @Autowired
    private ITrainExamLibraryService trainExamLibraryService;

    /**
     * 查询题库列表
     */
    //@PreAuthorize("@ss.hasPermi('labor:library:list')")
    @GetMapping("/list")
    @ApiOperation("查询题库列表")
    public TableDataInfo list(TrainExamLibrary trainExamLibrary)
    {
        startPage();
        List<TrainExamLibrary> list = trainExamLibraryService.selectTrainExamLibraryList(trainExamLibrary);
        return getDataTable(list);
    }
}
