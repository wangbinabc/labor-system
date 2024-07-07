package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.core.domain.entity.SysDictData;
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
import com.yuantu.labor.domain.SalaryConfig;
import com.yuantu.labor.service.ISalaryConfigService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 薪酬构成配置Controller
 * 
 * @author ruoyi
 * @date 2023-09-26
 */
@Api("薪酬配置管理")
@RestController
@RequestMapping("/labor/salaryconf")
public class SalaryConfigController extends BaseController
{
    @Autowired
    private ISalaryConfigService salaryConfigService;

    @Autowired
    private ISysDictDataService SysDictDataService;
    /**
     * 查询薪酬构成配置列表
     */
    @ApiOperation("查询薪酬构成配置列表")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalaryConfig salaryConfig)
    {
        startPage();
        List<SalaryConfig> list = salaryConfigService.selectSalaryConfigList(salaryConfig);
        return getDataTable(list);
    }

    /**
     * 导出薪酬构成配置列表
     */
    @ApiOperation("导出薪酬构成配置列表")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:export')")
    @Log(title = "薪酬构成配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalaryConfig salaryConfig)
    {
        List<SalaryConfig> list = salaryConfigService.selectSalaryConfigList(salaryConfig);
        ExcelUtil<SalaryConfig> util = new ExcelUtil<SalaryConfig>(SalaryConfig.class);
        util.exportExcel(response, list, "薪酬构成配置数据");
    }

    /**
     * 获取薪酬构成配置详细信息
     */
    @ApiOperation("获取薪酬构成配置详细信息")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:query')")
    @GetMapping(value = "/getInfo/{confId}")
    public AjaxResult getInfo(@PathVariable("confId") Integer confId)
    {
        return success(salaryConfigService.selectSalaryConfigByConfId(confId));
    }

    /**
     * 新增薪酬构成配置
     */
    @ApiOperation("新增薪酬构成配置")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:add')")
    @Log(title = "薪酬构成配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SalaryConfig salaryConfig)
    {
        //判断是否相同薪酬构成名称
        int result = salaryConfigService.checkConfigUnique(salaryConfig);
        if(result>0){
            return  error("增加'"+salaryConfig.getConfItem()+"'失败，薪酬构成名称已经存在");
        }
        String configTypeLabel = SysDictDataService.selectDictLabel("salary_type",salaryConfig.getConfTypeValue());
        salaryConfig.setConfTypeLabel(configTypeLabel);
        return toAjax(salaryConfigService.insertSalaryConfig(salaryConfig));
    }

    /**
     * 修改薪酬构成配置
     */
    @ApiOperation("修改薪酬构成配置")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:edit')")
    @Log(title = "薪酬构成配置", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody SalaryConfig salaryConfig)
    {
        int result = salaryConfigService.checkConfigUnique(salaryConfig);
        if(result>0){
            return  error("修改'"+salaryConfig.getConfItem()+"'失败，薪酬构成名称已经存在");
        }
        String configTypeLabel = SysDictDataService.selectDictLabel("salary_type",salaryConfig.getConfTypeValue());
        salaryConfig.setConfTypeLabel(configTypeLabel);
        return toAjax(salaryConfigService.updateSalaryConfig(salaryConfig));
    }

    /**
     * 删除薪酬构成配置
     */
    @ApiOperation("删除薪酬构成配置")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:remove')")
    @Log(title = "薪酬构成配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/remove/{confIds}")
    public AjaxResult remove(@PathVariable Integer[] confIds)
    {
        return toAjax(salaryConfigService.deleteSalaryConfigByConfIds(confIds));
    }

    /**
     * 初始化新增页面，返回薪酬分类集合
     * @return
     */
    @ApiOperation(" 初始化新增页面，返回薪酬分类集合")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:query')")
    @GetMapping("/initAdd")
    public AjaxResult initAdd() {
        //根据salary_type，查询出工资分类集合
        SysDictData dictData = new SysDictData();
        dictData.setDictType("salary_type");
        List<SysDictData> disctData = SysDictDataService.selectDictDataList(dictData);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("salaryTypeData",disctData);
        return ajax;
    }

    @ApiOperation("初始化修改页面，返回返回薪酬分类集合，薪酬配置对象")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:query')")
    @GetMapping("/initEdit/{confId}")
    public  AjaxResult initEdit(@PathVariable("confId") Integer confId){
        //取得薪酬配置对象
        SalaryConfig salaryConfig = salaryConfigService.selectSalaryConfigByConfId(confId);
        //根据salary_type，查询出工资分类集合
        SysDictData dictData = new SysDictData();
        dictData.setDictType("salary_type");
        List<SysDictData> disctData = SysDictDataService.selectDictDataList(dictData);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("salaryTypeData",disctData);
        ajax.put("salaryConfigData",salaryConfig);
        return ajax;
    }

    /**
     * 初始化管理页面，返回薪酬分类集合
     * @return
     */
    @ApiOperation("初始化管理页面，返回薪酬分类集合")
    @PreAuthorize("@ss.hasPermi('labor:salaryconf:query')")
    @GetMapping("/getSalaryTypes")
    public AjaxResult getSalaryTypes() {
        //根据salary_type，查询出工资分类集合
        SysDictData dictData = new SysDictData();
        dictData.setDictType("salary_type");
        List<SysDictData> disctData = SysDictDataService.selectDictDataList(dictData);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("salaryTypeData",disctData);
        return ajax;
    }
}
