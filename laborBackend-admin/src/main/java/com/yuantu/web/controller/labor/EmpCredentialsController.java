package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.service.IEmpCredentialsService;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.CredentialsCountParamsVO;
import com.yuantu.labor.vo.CredentialsDetailsVO;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.system.mapper.SysConfigMapper;
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
 * 资格证书Controller
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
@RestController
@Api("资格证书管理")
@RequestMapping("/labor/credentials")
public class EmpCredentialsController extends BaseController
{
    @Autowired
    private IEmpCredentialsService empCredentialsService;


    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private SysConfigMapper configMapper;
    /**
     * 查询资格证书列表详情
     */
   // @PreAuthorize("@ss.hasPermi('labor:credentials:list')")
    @GetMapping("/list")
    @ApiOperation("查询资格证书列表详情及统计证书详情穿透")
    public TableDataInfo list(EmpCredentials empCredentials)
    {
        Integer reminderTime = Integer.valueOf(configMapper.checkConfigKeyUnique("label.empCredentials.reminderTime").getConfigValue());
        empCredentials.setReminderTime(reminderTime);
        startPage();
        List<CredentialsDetailsVO> list = empCredentialsService.selectEmpCredentialsVOList(empCredentials);
        return getDataTable(list);
    }


    /**
     * 查询即将到期证书数量
     */
    // @PreAuthorize("@ss.hasPermi('labor:credentials:list')")
    @GetMapping("/due")
    @ApiOperation("查询即将到期证书数量")
    public AjaxResult due(EmpCredentials empCredentials)
    {
        List<CredentialsDetailsVO> list = empCredentialsService.selectEmpCredentialsVOList(empCredentials);
     //   startPage();
        List<CredentialsDetailsVO> duelist = new ArrayList<>();
        for (CredentialsDetailsVO credentialsDetailsVO:list){
            if (credentialsDetailsVO.getReminder()==1){
                duelist.add(credentialsDetailsVO);
            }
        }
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(duelist);
//        rspData.setTotal(duelist.size());
        return success(duelist);
    }

    /**
     * 统计查询资格证书数量
     */
 //  @PreAuthorize("@ss.hasPermi('labor:credentials:count')")
    @GetMapping("/count")
    @ApiOperation("统计查询资格证书数量")
    public AjaxResult count(CredentialsCountParamsVO credentialsCountParamsVO)
    {
        return success(empCredentialsService.countEmpCredentials(credentialsCountParamsVO));
    }


  //  @PreAuthorize("@ss.hasPermi('labor:credentials:import')")
    @ApiOperation("下载资格证书导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {
        empCredentialsService.downloadExcelTemplate(response);
//        ExcelUtil<CredentialsSimpleVO> util = new ExcelUtil<>(CredentialsSimpleVO.class);
//        util.importTemplateExcel(response, "资格证书导入模板");

    }

    @ApiOperation("导入资格证书信息")
    @PreAuthorize("@ss.hasPermi('labor:credentials:import')")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(empCredentialsService.uploadPostInfosFile(file,userId,username));
    }

    /**
     * 表格拆分导出员工信息
     */
   @ApiOperation("表格拆分导出员工资格证书信息")
 //   @PreAuthorize("@ss.hasPermi('labor:credentials:export')")
    @Log(title = "员工资格证书", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        empCredentialsService.exportDivide(response, export);
    }


    /**
     * 导出资格证书列表
     */
//    @PreAuthorize("@ss.hasPermi('labor:credentials:export')")
    @Log(title = "资格证书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出资格证书列表")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<EmpCredentials>  list =  empCredentialsService.findExportInfos(export);
        for (EmpCredentials ec:list){
           Employee e =  employeeService.selectEmployeeByEmpId(ec.getCredEmpId());
           ec.setDeptName(e.getEmpDeptName());
        }
        ExcelUtil<EmpCredentials> util = new ExcelUtil<>(EmpCredentials.class);
        Field[] declaredFields = EmpCredentials.class.getDeclaredFields();
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
        util.exportExcel(response, list, "资格证书数据");;
    }

    /**
     * 获取资格证书详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:credentials:query')")
    @GetMapping(value = "/{credId}")
    @ApiOperation("获取资格证书详细信息")
    public AjaxResult getInfo(@PathVariable("credId") Long credId)
    {
        return success(empCredentialsService.selectEmpCredentialsByCredId(credId));
    }

    /**
     * 新增资格证书
     */
    @PreAuthorize("@ss.hasPermi('labor:credentials:add')")
    @Log(title = "资格证书", businessType = BusinessType.INSERT)
    @ApiOperation("新增资格证书,**_annex参数值为上传附件表的ID")
    @PostMapping
    public AjaxResult add(@RequestBody EmpCredentials empCredentials)
    {
        empCredentials.setCreateBy(getUsername());
        empCredentials.setCreateTime(DateUtils.getNowDate());
        return toAjax(empCredentialsService.insertEmpCredentials(empCredentials));
    }

    /**
     * 修改资格证书
     */
    @PreAuthorize("@ss.hasPermi('labor:credentials:edit')")
    @Log(title = "资格证书", businessType = BusinessType.UPDATE)
    @ApiOperation("修改资格证书,**_annex参数值为上传附件表的ID")
    @PutMapping
    public AjaxResult edit(@RequestBody EmpCredentials empCredentials)
    {
        empCredentials.setUpdateBy(getUsername());
        empCredentials.setUpdateTime(DateUtils.getNowDate());
        return toAjax(empCredentialsService.updateEmpCredentials(empCredentials));
    }

    /**
     * 删除资格证书
     */
    @PreAuthorize("@ss.hasPermi('labor:credentials:remove')")
    @ApiOperation("删除资格证书,**_annex参数值为上传附件表的ID")
    @Log(title = "资格证书", businessType = BusinessType.DELETE)
	@DeleteMapping("/{credIds}")
    public AjaxResult remove(@PathVariable Long[] credIds)
    {
        return toAjax(empCredentialsService.deleteEmpCredentialsByCredIds(credIds));
    }
}
