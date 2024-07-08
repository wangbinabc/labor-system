package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.InsuranceConfiguration;
import com.yuantu.labor.service.IEmpWelfareService;
import com.yuantu.labor.service.IInsuranceConfigurationService;
import com.yuantu.labor.vo.EmpWelfareSimpleVO;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
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
import java.util.Date;
import java.util.List;

/**
 * 员工福利Controller
 *
 * @author ruoyi
 * @date 2023-10-08
 */
@Api("员工福利管理")
@RestController
@RequestMapping("/labor/welfare")
public class EmpWelfareController extends BaseController {
    @Autowired
    private IEmpWelfareService empWelfareService;

    @Autowired
    private IInsuranceConfigurationService iInsuranceConfigurationService;


//    @Autowired
//    private IBudgetService iBudgetService;

    /**
     * 查询员工福利列表
     */
   //  @PreAuthorize("@ss.hasPermi('labor:welfare:list')")
    @GetMapping("/list")
    @ApiOperation("查询员工福利列表")
    public TableDataInfo list(EmpWelfare empWelfare) {
        startPage();
        List<EmpWelfare> list = empWelfareService.selectEmpWelfareList(empWelfare);
        return getDataTable(list);
    }


    @PreAuthorize("@ss.hasPermi('labor:welfare:import')")
    @ApiOperation("下载员工福利导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {

        ExcelUtil<EmpWelfareSimpleVO> util = new ExcelUtil<>(EmpWelfareSimpleVO.class);
        util.importTemplateExcel(response, "员工福利导入模板");
    }

    @PreAuthorize("@ss.hasPermi('labor:welfare:import')")
    @ApiOperation("导入员工福利信息")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(empWelfareService.uploadWelfareInfosFile(file,userId,username));
    }

    /**
     * 表格拆分导出员工福利信息
     */
    @ApiOperation("表格拆分导出员工福利信息")
   // @PreAuthorize("@ss.hasPermi('labor:welfare:export')")
    @Log(title = "员工福利", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        empWelfareService.exportDivide(response, export);
    }


    /**
     * 导出员工福利列表
     */
    @PreAuthorize("@ss.hasPermi('labor:welfare:export')")
    @Log(title = "员工福利", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出员工福利列表")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
            List<EmpWelfare>  list =  empWelfareService.findExportInfos(export);
            ExcelUtil<EmpWelfare> util = new ExcelUtil<>(EmpWelfare.class);
            Field[] declaredFields = EmpWelfare.class.getDeclaredFields();
            List<String> fieldNamesWithExcel = new ArrayList<>();
            for (Field field : declaredFields) {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(Excel.class)) {
                    fieldNamesWithExcel.add(field.getName());
                }
            }
            List<String> excludeFieldNames = new ArrayList<>();
            List<String> fieldNames = export.getFieldNames();
            if (!CollectionUtils.isEmpty(fieldNames)) {
                for (String s : fieldNamesWithExcel) {
                    if (!fieldNames.contains(s)) {
                        excludeFieldNames.add(s);
                    }
                }
                if (!CollectionUtils.isEmpty(excludeFieldNames)) {
                    String[] excludeArr = new String[excludeFieldNames.size()];
                    excludeFieldNames.toArray(excludeArr);
                    util.hideColumn(excludeArr);
                }
            }
            util.exportExcel(response, list, "员工福利数据");;
        }
    /**
     * 获取员工福利详细信息
     */
     @PreAuthorize("@ss.hasPermi('labor:welfare:query')")
    @GetMapping(value = "/{welfareId}")
    @ApiOperation("获取员工福利详细信息")
    public AjaxResult getInfo(@PathVariable("welfareId") Long welfareId) {
        return success(empWelfareService.selectEmpWelfareByWelfareId(welfareId));
    }

    /**
     * 福利保障信息管理查询
     */
  //  @PreAuthorize("@ss.hasPermi('labor:welfare:query')")
//    @GetMapping(value = "/budget")
//    @ApiOperation("福利保障信息管理,年度无参默认为当前年度")
//    public AjaxResult getInfo(Budget budget) {
//        List<Budget> list = new ArrayList<>();
//        String[] budgetTypes   = new String[]{"5","6","7","8"};
//
//        if (budget.getBudgetYear()==null){
//            budget.setBudgetYear(Long.valueOf(new SimpleDateFormat("yyyy").format(DateUtils.getNowDate())));
//        }
//
//        for (String budgetType : budgetTypes){
//            Budget search = new Budget();
//            search.setBudgetTypeId(budgetType);
//            search.setBudgetCompanyId(budget.getBudgetCompanyId());
//            search.setBudgetYear(budget.getBudgetYear());
//            list.addAll(iBudgetService.selectBudgetList(search));
//        }
//        return success(list);
//    }


    /**
     * 新增员工福利
     */
      @PreAuthorize("@ss.hasPermi('labor:welfare:add')")
    @Log(title = "员工福利", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增员工福利")
    public AjaxResult add(@RequestBody EmpWelfare empWelfare) {
        //     empWelfare.setUpdateBy(getUsername());
        empWelfare.setCreateBy(getUsername());
        return toAjax(empWelfareService.insertEmpWelfare(empWelfare));
    }

    /**
     * 修改员工福利
     */
    @PreAuthorize("@ss.hasPermi('labor:welfare:edit')")
    @Log(title = "员工福利", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改员工福利")
    public AjaxResult edit(@RequestBody EmpWelfare empWelfare) {
        empWelfare.setUpdateBy(getUsername());
        empWelfare.setUpdateTime(DateUtils.getNowDate());
        return toAjax(empWelfareService.updateEmpWelfare(empWelfare));
    }


    /**
     * 五险配置
     */
    @PreAuthorize("@ss.hasPermi('labor:welfare:insuranceConfig')")
    @Log(title = "员工福利", businessType = BusinessType.UPDATE)
    @PostMapping("/insuranceConfig")
    @ApiOperation("五险配置,类型 1养老保险 2医疗保险 3失业保险 4工商保险 5生育保险")
    public AjaxResult insuranceConfig(@RequestBody List<InsuranceConfiguration> insuranceConfigurations) {
        if (insuranceConfigurations != null && insuranceConfigurations.size() != 0) {
            Date now = new Date();
            for (InsuranceConfiguration insuranceConfiguration : insuranceConfigurations) {
                insuranceConfiguration.setUpdateBy(getUsername());
                insuranceConfiguration.setCreateBy(getUsername());
                insuranceConfiguration.setCreateTime(now);
                insuranceConfiguration.setUpdateTime(now);
            }
            return toAjax(empWelfareService.baseConfig(insuranceConfigurations));
        }
        return AjaxResult.error();
    }

    /**
     * 五险查询
     */
      @PreAuthorize("@ss.hasPermi('labor:welfare:insuranceQuery')")
    @GetMapping("/insuranceQuery")
    @ApiOperation("五险查询")
    public TableDataInfo insuranceConfig(InsuranceConfiguration insuranceConfiguration) {
        startPage();
        List<InsuranceConfiguration> list = iInsuranceConfigurationService.selectInsuranceConfigurationList(insuranceConfiguration);
        return getDataTable(list);
    }


    /**
     * 删除员工福利
     */
    @PreAuthorize("@ss.hasPermi('labor:welfare:remove')")
    @Log(title = "员工福利", businessType = BusinessType.DELETE)
    @DeleteMapping("/{welfareIds}")
    @ApiOperation("删除员工福利")
    public AjaxResult remove(@PathVariable Long[] welfareIds) {
        return toAjax(empWelfareService.deleteEmpWelfareByWelfareIds(welfareIds));
    }
}
