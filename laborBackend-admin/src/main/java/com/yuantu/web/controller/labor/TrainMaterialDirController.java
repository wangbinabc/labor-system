package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.labor.domain.TrainMaterialDir;
import com.yuantu.labor.service.ITrainMaterialDirService;
/**
 * 材料目录Controller
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
@Api("材料目录管理")
@RestController
@RequestMapping("/labor/dir")
public class TrainMaterialDirController extends BaseController
{
    @Autowired
    private ITrainMaterialDirService trainMaterialDirService;

    /**
     * 查询材料目录列表
     */
    @ApiOperation("查询材料目录列表")
   // @PreAuthorize("@ss.hasPermi('labor:dir:list')")
    @GetMapping("/list")
    public AjaxResult list(TrainMaterialDir trainMaterialDir)
    {
        AjaxResult ajax = AjaxResult.success();
        List<TrainMaterialDir> list = trainMaterialDirService.selectTrainMaterialDirList(trainMaterialDir);
        List<TrainMaterialDir> treeList = trainMaterialDirService.buildMenuTree(list);
         ajax.put("treeList",treeList);
         return  ajax;
    }



    /**
     * 获取材料目录详细信息
     */
    @ApiOperation("获取材料目录详细信息")
   // @PreAuthorize("@ss.hasPermi('labor:dir:query')")
    @GetMapping(value = "/getInfo/{dirId}")
    public AjaxResult getInfo(@PathVariable("dirId") Integer dirId)
    {
        return success(trainMaterialDirService.selectTrainMaterialDirByDirId(dirId));
    }


}
