
package com.yuantu.web.controller.labor;

import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.labor.service.IEmpInOutService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 进出信息Controller
 *
 * @author ruoyi
 * @date 2023-10-27
 */
@Api("进出信息统计管理")
@RestController
@RequestMapping("/labor/flow")
public class EmpInOutController extends BaseController {

    @Autowired
    private IEmpInOutService empInOutService;

    @ApiOperation("各部门进出人员统计")
    @GetMapping("/department")
    public AjaxResult getDeptFlowInfos(@RequestParam(required = false) Date startTime, @RequestParam(required = false) Date endTime) {
        return success(empInOutService.getDeptFlowInfos(startTime, endTime));
    }


    @ApiOperation("各部门新进、离职、到龄员工占比 1新进 2离职 3到龄")
    @GetMapping("/department/type")
    public AjaxResult getDeptFlowTypeInfos(@RequestParam(required = false) Date startTime, @RequestParam(required = false) Date endTime,
                                           @RequestParam String type) {
        return success(empInOutService.getDeptFlowTypeInfos(startTime, endTime, type));
    }

    @ApiOperation("各部门新进、离职、到龄员工变动情况 type:1新进 2离职 3到龄  timeRange: 1 按月 2 按年")
    @GetMapping("/department/change")
    public AjaxResult getDeptFlowTypeInfos(@RequestParam(required = false) Long deptId, @RequestParam String type, @RequestParam String timeRange) {
        return success(empInOutService.getDeptFlowChangeInfos(deptId, type, timeRange));
    }


    @ApiOperation("查询离职员工绩效信息")
    @PostMapping("/quit/employee/perform")
    public AjaxResult getQuitEmpPerformInfos(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                             @Validated @RequestBody DeptEmpSearchVO deptEmpSearch) {
        return success(empInOutService.getQuitEmpPerformInfos(pageNum, pageSize, deptEmpSearch));
    }


}
