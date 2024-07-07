package com.yuantu.web.controller.labor;


import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.page.TableDataInfo;

import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.service.ITrainProjectService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 培训项目Controller
 *
 * @author ruoyi
 * @date 2023-09-14
 */
@Api("培训项目管理")
@RestController
@RequestMapping("/labor/project")
public class TrainProjectController extends BaseController {
    @Autowired
    private ITrainProjectService trainProjectService;

    /**
     * 筛选培训项目列表
     * 培训项目名称:projectName;
     * 主要培训内容/课程:projectContent;
     * 年度:projectYear;
     * 责任部门:projectDeptId;
     * 责任部门名称:projectDeptName;
     * 培训性质:projectNature;
     * 培训方式:projectMethod;
     * 项目分类:projectClassify;
     * 是否完成:projectIsfinish;
     * 开始修改时间:beginUpdateTime;
     * 结束修改时间:endUpdateTime;
     */
    @ApiOperation("筛选条件查询培训项目列表")
    //@PreAuthorize("@ss.hasPermi('labor:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(TrainProjectQueryVO vo) {
        startPage();
        List<TrainProject> list = trainProjectService.selectTrainProjectListByQueryVO(vo);
        return getDataTable(list);
    }
}
