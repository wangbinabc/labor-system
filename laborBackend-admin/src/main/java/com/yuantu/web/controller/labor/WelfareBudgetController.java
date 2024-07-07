package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.yuantu.labor.domain.WelfareBudget;
import com.yuantu.labor.service.IWelfareBudgetService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 福利保障信息执行Controller
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
@Api("福利保障信息执行管理")
@RestController
@RequestMapping("/labor/welbud")
public class WelfareBudgetController extends BaseController
{
    @Autowired
    private IWelfareBudgetService welfareBudgetService;

    /**
     * 查询福利保障信息执行列表
     */
    @ApiOperation("查询福利保障信息执行列表")
    @PreAuthorize("@ss.hasPermi('labor:welbud:list')")
    @GetMapping("/list")
    public TableDataInfo list(WelfareBudget welfareBudget)
    {
        startPage();
        List<WelfareBudget> list = welfareBudgetService.selectWelfareBudgetList(welfareBudget);
        return getDataTable(list);
    }

    /**
     * 根据月份查询福利保障信息执行数据
     * @param welfareBudget
     * @return
     */
    @ApiOperation("根据月份查询福利保障信息执行数据")
    @PreAuthorize("@ss.hasPermi('labor:welbud:query')")
    @GetMapping("/getInfoByMonth")
    public AjaxResult getInfoByMonth(WelfareBudget welfareBudget)
    {
        AjaxResult ajax = AjaxResult.success();
        WelfareBudget newBudget= welfareBudgetService.getWelfareBudgetByMonth(welfareBudget);
        return ajax.put("data",newBudget);

    }

    /**
     * 导出福利保障信息执行列表
     */
    @ApiOperation("导出福利保障信息执行列表")
    @PreAuthorize("@ss.hasPermi('labor:welbud:export')")
    @Log(title = "福利保障信息执行", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WelfareBudget welfareBudget)
    {
        List<WelfareBudget> list = welfareBudgetService.selectWelfareBudgetList(welfareBudget);
        ExcelUtil<WelfareBudget> util = new ExcelUtil<WelfareBudget>(WelfareBudget.class);
        util.exportExcel(response, list, "福利保障信息执行数据");
    }

    /**
     * 获取福利保障信息执行详细信息
     */
    @ApiOperation("获取福利保障信息执行详细信息")
    @PreAuthorize("@ss.hasPermi('labor:welbud:query')")
    @GetMapping(value = "/getInfo/{welbudId}")
    public AjaxResult getInfo(@PathVariable("welbudId") Integer welbudId)
    {
        return success(welfareBudgetService.selectWelfareBudgetByWelbudId(welbudId));
    }

    /**
     * 新增福利保障信息执行
     */
    @ApiOperation("新增福利保障信息执行")
    @PreAuthorize("@ss.hasPermi('labor:welbud:add')")
    @Log(title = "福利保障信息执行", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WelfareBudget welfareBudget)
    {
        welfareBudgetService.deleteWelfareBudgetByMonth(welfareBudget.getWelbudYearMonth());

        String username = getUsername();
        return toAjax(welfareBudgetService.insertWelfareBudget(welfareBudget,username));
    }

    /**
     * 修改福利保障信息执行
     */
    @ApiOperation("修改福利保障信息执行")
    @PreAuthorize("@ss.hasPermi('labor:welbud:edit')")
    @Log(title = "福利保障信息执行", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody WelfareBudget welfareBudget)
    {
        String username = getUsername();
        return toAjax(welfareBudgetService.updateWelfareBudget(welfareBudget,username));
    }

    /**
     * 删除福利保障信息执行
     */
    @ApiOperation("删除福利保障信息执行")
    @PreAuthorize("@ss.hasPermi('labor:welbud:remove')")
    @Log(title = "福利保障信息执行", businessType = BusinessType.DELETE)
	@DeleteMapping("/remove/{welbudIds}")
    public AjaxResult remove(@PathVariable Integer[] welbudIds)
    {
        return toAjax(welfareBudgetService.deleteWelfareBudgetByWelbudIds(welbudIds));
    }
}
