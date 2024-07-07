package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.domain.LoanWorkerHistory;
import com.yuantu.labor.service.*;
import com.yuantu.labor.vo.*;
import com.yuantu.system.service.ISysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 借工Controller
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Api("借工管理")
@RestController
@RequestMapping("/labor/worker/history")
public class LoanWorkerHistoryController extends BaseController {



    @Autowired
    private ILoanWorkerHistoryService loanWorkerHistoryService;


    @Autowired
    private IDepartmentService departmentService;


    @Autowired
    private ISysDictDataService dictDataService;


    /**
     * 获取借工详细信息
     */
    @ApiOperation("根据id获取借工历史详细信息")
    //@PreAuthorize("@ss.hasPermi('labor:worker:getInfo')")
    @GetMapping(value = "/getInfo/{historyId}")
    public AjaxResult getInfo(@PathVariable("historyId") Long historyId) {
        return success(loanWorkerHistoryService.selectLoanWorkerByHistoryId(historyId));
    }


    /**
     * 修改借工
     */
    @ApiOperation("修改借工历史数据")
   // @PreAuthorize("@ss.hasPermi('labor:worker:edit')")
    @Log(title = "借工", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody LoanWorkerHistory loanWorker) {

        Department dept = departmentService.selectDepartmentByDeptId(loanWorker.getLoanApplyDeptId());
        loanWorker.setLoanApplyDeptName(dept.getDeptName());

        String unitName = dictDataService.selectDictLabel("loan_unit",loanWorker.getLoanApplyUnitId());
        loanWorker.setLoanApplyUnitName(unitName);

        String username = getUsername();
        return toAjax(loanWorkerHistoryService.updateLoanWorkerHistory(loanWorker, username));
    }

    /**
     * 删除借工
     */
    @ApiOperation("删除借工历史数据")
   // @PreAuthorize("@ss.hasPermi('labor:worker:remove')")
    @Log(title = "借工", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestBody List<Long> historyIds) {
        return toAjax(loanWorkerHistoryService.deleteLoanWorkerByHistoryIds(historyIds));
    }

    /**
     * 查询借工列表
     *
     * @param queryVO
     * @return
     */
    @ApiOperation("根据条件查询借工历史数据列表")
    //@PreAuthorize("@ss.hasPermi('labor:worker:queryWorker')")
    @GetMapping("/queryWorker")
    public TableDataInfo queryList(LoanWorkerQueryVO queryVO) {
        startPage();
        List<LoanWorkerListVO> loanWorkerList = loanWorkerHistoryService.selectLoanWorkersListByWhere(queryVO);
        //System.out.println("table" + loanWorkerList.toString());

        return getDataTable(loanWorkerList);

    }



}
