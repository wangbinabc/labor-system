package com.yuantu.web.controller.labor;


import com.yuantu.common.constant.HttpStatus;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.domain.DeptValue;
import com.yuantu.labor.domain.FamilyRelations;
import com.yuantu.labor.service.IDeptValueService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/labor/dept/value")
@Api("部门产值管理")
public class DeptValueController extends BaseController {

    @Autowired
    private IDeptValueService deptValueService;


    /**
     * 查询部门产值列表
     */
    @ApiOperation("查询部门产值列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('dept:value:list')")
    public TableDataInfo list(DeptValueSearchVO deptValue) {
        startPage();
        List<DeptValueVO> list = deptValueService.findDeptValueInfos(deptValue);
        return getDataTable(list);
    }


    /**
     * 获取获取详细信息
     */
    @ApiOperation("获取部门产值有关信息")
    @GetMapping(value = "/detail")
    @PreAuthorize("@ss.hasPermi('dept:value:detail')")
    public AjaxResult getInfo(@RequestParam Long deptValueId) {
        return success(deptValueService.findDeptValueInfo(deptValueId));
    }


    /**
     * 修改部门产值有关信息
     */
    @ApiOperation("修改部门产值有关信息")
    @PostMapping(value = "/update")
    @PreAuthorize("@ss.hasPermi('dept:value:update')")
    public AjaxResult updateDeptValue(@Validated @RequestBody DeptValueAddVO deptValueAdd) {
        Long userId = getUserId();
        return success(deptValueService.updateDeptValue(deptValueAdd, userId));
    }


    /**
     * 删除部门产值有关信息
     */
    @ApiOperation("删除部门产值有关信息")
    @PostMapping(value = "/remove")
    @PreAuthorize("@ss.hasPermi('dept:value:remove')")
    public AjaxResult removeDeptValue(@RequestBody List<Long> deptValueIds) {
        return success(deptValueService.removeDeptValueInfos(deptValueIds));
    }


}
