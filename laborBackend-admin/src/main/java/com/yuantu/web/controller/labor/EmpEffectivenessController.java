package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.vo.EmpProfitTemplateVO;
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
import com.yuantu.labor.domain.EmpEffectiveness;
import com.yuantu.labor.service.IEmpEffectivenessService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 人员效能Controller
 *
 * @author ruoyi
 * @date 2023-11-06
 */
@Api("人员效能管理")
@RestController
@RequestMapping("/labor/empEffectiveness")
public class EmpEffectivenessController extends BaseController {
    @Autowired
    private IEmpEffectivenessService empEffectivenessService;

    /**
     * 查询人员效能列表
     */
    //@PreAuthorize("@ss.hasPermi('labor:empEffectiveness:list')")
    @GetMapping("/list")
    @ApiOperation("查询人员效能列表")
    public TableDataInfo list(EmpEffectiveness empEffectiveness) {
        startPage();
        List<EmpEffectiveness> list = empEffectivenessService.selectEmpEffectivenessList(empEffectiveness);
        return getDataTable(list);
    }

    /**
     * 导出人员效能列表
     */
    @PreAuthorize("@ss.hasPermi('labor:empEffectiveness:export')")
    @Log(title = "人员效能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EmpEffectiveness empEffectiveness) {
        List<EmpEffectiveness> list = empEffectivenessService.selectEmpEffectivenessList(empEffectiveness);
        ExcelUtil<EmpEffectiveness> util = new ExcelUtil<EmpEffectiveness>(EmpEffectiveness.class);
        util.exportExcel(response, list, "人员效能数据");
    }

    /**
     * 获取人员效能详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:empEffectiveness:query')")
    @ApiOperation("获取人员效能详细信息")
    @GetMapping(value = "/{effId}")
    public AjaxResult getInfo(@PathVariable("effId") Long effId) {
        return success(empEffectivenessService.selectEmpEffectivenessByEffId(effId));
    }

    /**
     * 新增人员效能
     */
    //  @PreAuthorize("@ss.hasPermi('labor:empEffectiveness:add')")
    @Log(title = "人员效能", businessType = BusinessType.INSERT)
    @ApiOperation("新增人员效能")
    @PostMapping
    public AjaxResult add(@RequestBody EmpEffectiveness empEffectiveness) {
        return toAjax(empEffectivenessService.insertEmpEffectiveness(empEffectiveness));
    }

    /**
     * 修改人员效能
     */
    @PreAuthorize("@ss.hasPermi('labor:empEffectiveness:edit')")
    @ApiOperation("修改人员效能")
    @Log(title = "人员效能", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmpEffectiveness empEffectiveness) {
        return toAjax(empEffectivenessService.updateEmpEffectiveness(empEffectiveness));
    }

    /**
     * 删除人员效能
     */
    @PreAuthorize("@ss.hasPermi('labor:empEffectiveness:remove')")
    @ApiOperation("删除人员效能")
    @Log(title = "人员效能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{effIds}")
    public AjaxResult remove(@PathVariable Long[] effIds) {
        return toAjax(empEffectivenessService.deleteEmpEffectivenessByEffIds(effIds));
    }


    @ApiOperation("下载人员利润值导入模板")
    @GetMapping("/download/template")
    public void downloadExcel(HttpServletResponse response) {
        ExcelUtil<EmpProfitTemplateVO> util = new ExcelUtil<>(EmpProfitTemplateVO.class);
        util.importTemplateExcel(response, "员工利润值导入模板");
    }


    @ApiOperation("上传人员利润值信息")
    @PostMapping("/upload")
    public AjaxResult uploadEmpProfitInfos(@RequestBody MultipartFile file) {
        LoginUser loginUser = getLoginUser();
        return success(empEffectivenessService.uploadEmpProfitInfosFile(file, loginUser));
    }



}
