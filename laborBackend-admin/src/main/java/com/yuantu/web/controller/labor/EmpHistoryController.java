package com.yuantu.web.controller.labor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.constant.HttpStatus;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.ListPageUtil;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.service.IEmpHistoryService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 员工快照Controller
 *
 * @author ruoyi
 * @date 2023-09-19
 */
@Api("员工快照管理")
@RestController
@RequestMapping("/labor/history")
public class EmpHistoryController extends BaseController {
    @Autowired
    private IEmpHistoryService empHistoryService;


    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询员工快照列表
     */
    // @PreAuthorize("@ss.hasPermi('labor:history:list')")
    @ApiOperation("查询员工快照列表")
    @GetMapping("/list")
    public TableDataInfo list(EmpHistoryQueryParamsVO vo) {
        startPage();
        List<EmpHistory> list = empHistoryService.selectEmpHistoryList(vo);
        return getDataTable(list);
    }


    @ApiOperation(value = "下载员工信息模板")
    @GetMapping("/excel/download")
    public void downloadExcel(HttpServletResponse response) {
        try {
            employeeService.downloadExcel(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation("导入员工历史信息")
    @PostMapping("/upload")
    public AjaxResult uploadEmployeeInfos(@RequestParam String yearMonth, @RequestBody MultipartFile file) {
        LoginUser loginUser = getLoginUser();
        return success(empHistoryService.uploadEmpHisInfosFile(yearMonth, file, loginUser));
    }


    @ApiOperation("复制人员历史信息(按月份)")
    @PostMapping("/month/copy")
    public AjaxResult empHistoryMonthCopy(@RequestBody MonthTimeVO monthTime) {
        LoginUser loginUser = getLoginUser();
        return success(empHistoryService.empHistoryMonthCopy(monthTime, loginUser));
    }


    /**
     * 获取员工快照详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:history:query')")
    @ApiOperation("获取员工快照详细信息")
    @GetMapping(value = "/{historyId}")
    public AjaxResult getInfo(@PathVariable("historyId") Long historyId) {
        return success(empHistoryService.selectEmpHistoryByHistoryId(historyId));
    }


    /**
     * 修改员工快照
     */
    @PreAuthorize("@ss.hasPermi('labor:history:edit')")
    @Log(title = "员工快照", businessType = BusinessType.UPDATE)
    @ApiOperation("修改员工快照")
    @PutMapping
    public AjaxResult edit(@RequestBody EmpDetailHistoryVO empHistory) {
        Long userId = getUserId();
        return toAjax(empHistoryService.updateEmpHistory(empHistory, userId));
    }

    /**
     * 删除员工快照
     */
    @PreAuthorize("@ss.hasPermi('labor:history:remove')")
    @ApiOperation("删除员工快照")
    @Log(title = "员工快照", businessType = BusinessType.DELETE)
    @DeleteMapping("/{historyIds}")
    public AjaxResult remove(@PathVariable Long[] historyIds) {
        return toAjax(empHistoryService.deleteEmpHistoryByHistoryIds(historyIds));
    }


    /**
     * 对比查询员工快照,返回员工变动类型信息
     */
    //  @PreAuthorize("@ss.hasPermi('labor:history:list')")
    @ApiOperation("对比查询员工变动类型信息")
    @GetMapping("/listChangeType")
    public TableDataInfo listChangeType(HistoryChangeTypeSearchVO search) {


        List<EmpHistoryInfoVO> list = empHistoryService.selectEmpHistoryChangeTypeList(search);
        if (search.getChangeType() != null) {
            list = list.stream().filter(item -> item.getChangeType().equals(search.getChangeType())).collect(Collectors.toList());
        }

        List reuslt;
        if (search.getPageNum() != 0) {
            reuslt = ListPageUtil.pageResult(list, search.getPageSize(), search.getPageNum());
        } else {
            reuslt = list;
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(reuslt);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 导出对比查询数据
     */
    //   @PreAuthorize("@ss.hasPermi('labor:history:export')")
    @Log(title = "员工快照", businessType = BusinessType.EXPORT)
    @ApiOperation("导出对比查询数据,参数startYearMonth 查询开始年月,endYearMonth 查询结束年月,changeType 变动类型")
    @PostMapping("/export")
    public void export(HttpServletResponse response, HistoryChangeTypeSearchVO search) {
        List<EmpHistoryInfoVO> list = empHistoryService.selectEmpHistoryChangeTypeList(search);
        if (search.getChangeType() != null) {
            list = list.stream().filter(item -> item.getChangeType().equals(search.getChangeType())).collect(Collectors.toList());
        }

        ExcelUtil<EmpHistoryInfoVO> util = new ExcelUtil<EmpHistoryInfoVO>(EmpHistoryInfoVO.class);
        util.exportExcel(response, list, "员工快照对比数据");
    }


    /**
     * 新增员工快照
     */
//    @PreAuthorize("@ss.hasPermi('labor:history:add')")
//    @Log(title = "员工快照", businessType = BusinessType.INSERT)
//    @ApiOperation("新增员工快照")
//    @PostMapping
//    public AjaxResult add(@RequestBody EmpHistory empHistory)
//    {
//        return toAjax(empHistoryService.insertEmpHistory(empHistory));
//    }


}
