package com.yuantu.web.controller.labor;

import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.StabilityQueryVO;
import com.yuantu.labor.vo.StabilityResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ApiOperation("稳定度分析")
@RestController
@RequestMapping("/labor/stability")
public class EmpStabilityAnalysisController extends BaseController {
    @Autowired
    private IEmployeeService employeeService;
    @ApiOperation("查询部门稳定度")
    @PreAuthorize("@ss.hasPermi('labor:stability:list')")
    @GetMapping("/deptlist")
    public TableDataInfo deptlist(StabilityQueryVO queryVO){
        startPage();
        List<StabilityResultVO> list = employeeService.getDeptStabilityData(queryVO);
        return getDataTable(list);
    }
    @ApiOperation("查询单位稳定度")
    @PreAuthorize("@ss.hasPermi('labor:stability:list')")
    @GetMapping("/unitlist")
    public TableDataInfo unitlist(StabilityQueryVO queryVO){
        startPage();
        List<StabilityResultVO> list = employeeService.getUnitStabilityData(queryVO);
        return getDataTable(list);
    }

}
