package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.utils.SecurityUtils;
import com.yuantu.labor.domain.TrainMaterialDir;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.service.IFileService;
import com.yuantu.labor.service.ITrainMaterialDirService;
import com.yuantu.labor.service.ITrainProjectService;
import com.yuantu.labor.vo.TreeSelectVO;
import com.yuantu.system.service.ISysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.labor.domain.TrainMaterials;
import com.yuantu.labor.service.ITrainMaterialsService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 培训项目材料Controller
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Api("培训材料管理")
@RestController
@RequestMapping("/labor/materials")
public class TrainMaterialsController extends BaseController {
    @Autowired
    private ITrainMaterialsService trainMaterialsService;

    /**
     * 查询培训项目材料列表
     */
    @ApiOperation("查询培训项目材料列表")
    //@PreAuthorize("@ss.hasPermi('labor:materials:list')")
    @GetMapping("/list")
    public TableDataInfo list(TrainMaterials trainMaterials) {

        startPage();
        List<TrainMaterials> list = trainMaterialsService.selectTrainMaterialsList(trainMaterials);
        return getDataTable(list);
    }




}
