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
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.service.IEmployingUnitsService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 用工单位Controller
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Api("用工单位管理")
@RestController
@RequestMapping("/labor/units")
public class EmployingUnitsController extends BaseController
{
    @Autowired
    private IEmployingUnitsService employingUnitsService;

    /**
     * 查询用工单位列表
     */
   @PreAuthorize("@ss.hasPermi('labor:units:list')")
    @GetMapping("/list")
    @ApiOperation("查询用工单位列表")
    public TableDataInfo list(EmployingUnits employingUnits)
    {
        startPage();
        List<EmployingUnits> list = employingUnitsService.selectEmployingUnitsList(employingUnits);
        return getDataTable(list);
    }

    /**
     * 获取用工单位及所属部门详细信息
     */
//    @PreAuthorize("@ss.hasPermi('labor:units:list')")
    @ApiOperation("获取用工单位及所属部门详细信息")
    @GetMapping(value = "/listUnitsAndDepartments")
    public AjaxResult listUnitsAndDepartments(EmployingUnits employingUnits)
    {
        return success(employingUnitsService.selectEmployingUnitAndDeptsList(employingUnits));
    }



    /**
     * 导出用工单位列表
     */
    @PreAuthorize("@ss.hasPermi('labor:units:export')")
    @ApiOperation("导出用工单位列表")
    @Log(title = "用工单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EmployingUnits employingUnits)
    {
        List<EmployingUnits> list = employingUnitsService.selectEmployingUnitsList(employingUnits);
        ExcelUtil<EmployingUnits> util = new ExcelUtil<EmployingUnits>(EmployingUnits.class);
        util.exportExcel(response, list, "用工单位数据");
    }

    /**
     * 获取用工单位详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:units:query')")
    @ApiOperation("获取用工单位详细信息")
    @GetMapping(value = "/{unitId}")
    public AjaxResult getInfo(@PathVariable("unitId") Long unitId)
    {
        return success(employingUnitsService.selectEmployingUnitsByUnitId(unitId));
    }

    /**
     * 新增用工单位
     */
    @PreAuthorize("@ss.hasPermi('labor:units:add')")
    @ApiOperation("新增用工单位")
    @Log(title = "用工单位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmployingUnits employingUnits)
    {
        employingUnits.setCreateBy(getUsername());
        employingUnits.setCreateTime(DateUtils.getNowDate());
        return toAjax(employingUnitsService.insertEmployingUnits(employingUnits));
    }

    /**
     * 修改用工单位
     */
    @PreAuthorize("@ss.hasPermi('labor:units:edit')")
    @ApiOperation("修改用工单位")
    @Log(title = "用工单位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmployingUnits employingUnits)
    {
        employingUnits.setUpdateBy(getUsername());
        employingUnits.setUpdateTime(DateUtils.getNowDate());
        return toAjax(employingUnitsService.updateEmployingUnits(employingUnits));
    }

    /**
     * 逻辑删除用工单位
     */
    @PreAuthorize("@ss.hasPermi('labor:units:remove')")
    @Log(title = "用工单位", businessType = BusinessType.DELETE)
    @ApiOperation("删除用工单位")
	@DeleteMapping("/{unitIds}")
    public AjaxResult remove(@PathVariable Long[] unitIds)
    {
        return toAjax(employingUnitsService.deleteEmployingUnitsByUnitIds(unitIds));
    }


    /**
     * 查询用工单位及部门有关信息
     */
    @ApiOperation("查询用工单位及部门有关信息")
    @GetMapping("/tree")
    public AjaxResult searchUnitDepartmentInfo()
    {
        return success(employingUnitsService.searchUnitDepartmentInfo());
    }

}
