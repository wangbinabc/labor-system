package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.service.IEmployingUnitsService;
import com.yuantu.labor.vo.BudgetQueryVO;
import com.yuantu.labor.vo.EmpNameCardVO;
import com.yuantu.labor.vo.EmployeeInfoVO;
import com.yuantu.system.service.ISysDictDataService;
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
import com.yuantu.labor.domain.Budget;
import com.yuantu.labor.service.IBudgetService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 预算主Controller
 * 
 * @author ruoyi
 * @date 2023-10-07
 */
@Api("预算管理")
@RestController
@RequestMapping("/labor/budget")
public class BudgetController extends BaseController
{
    @Autowired
    private IBudgetService budgetService;


    /**
     * 查询预算主列表
     */
    @ApiOperation("查询预算列表")
    //@PreAuthorize("@ss.hasPermi('labor:budget:list')")
    @GetMapping("/list")
    public TableDataInfo list(Budget budget)
    {
        //hello
        startPage();
        List<Budget> list = budgetService.selectBudgetList(budget);
        return getDataTable(list);
    }

    /**
     * 查询所有的预算列表（无预算类别和年份）
     * 查询匹配的预算列表（预算类别和年份）
     * @param budgetTypeId
     * @param budgetYear
     * @return
     */
    @ApiOperation("搜索匹配的预算列表")
    @GetMapping("/listMatch")
    public TableDataInfo listMatch(String budgetTypeId, Long budgetYear) {
        List<Budget> list = budgetService.selectMatchBudgetList(budgetTypeId, budgetYear);
        return getDataTable(list);
    }

    /**
     * 删除budget记录
     * @param budgetId
     */
    @ApiOperation("删除budget记录")
    @GetMapping("/delList")
    public void delList(Integer budgetId) {
        if (budgetId != null) {
            budgetService.delList(budgetId);
        }
    }
}
