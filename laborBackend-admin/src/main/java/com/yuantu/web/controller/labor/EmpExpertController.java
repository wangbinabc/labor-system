package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.EmpExpert;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.service.IEmpExpertService;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.*;
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
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Api("专家管理")
@RestController
@RequestMapping("/labor/expert")
public class EmpExpertController extends BaseController {
    @Autowired
    private IEmpExpertService empExpertService;

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询【请填写功能名称】列表
     */

    @ApiOperation("查询专家信息")
    @PreAuthorize("@ss.hasPermi('labor:expert:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmpExpert empExpert) {
        startPage();
        List<EmpExpert> list = empExpertService.selectEmpExpertList(empExpert);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */

    @ApiOperation("导出专家信息列表")
    @PreAuthorize("@ss.hasPermi('labor:expert:export')")
    @Log(title = "导出专家信息列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export/{expertIds}")
    public void export(HttpServletResponse response, @PathVariable Integer[] expertIds) {
        EmpExpertQueryVO vo = new EmpExpertQueryVO();
        vo.setExpertIds(expertIds);
        List<ExpertListVO> list = empExpertService.selectEmpExpertListByWhere(vo);
        ExcelUtil<ExpertListVO> util = new ExcelUtil<ExpertListVO>(ExpertListVO.class);
        util.exportExcel(response, list, "专家数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */

    @ApiOperation("根据id获取专家信息")
    @PreAuthorize("@ss.hasPermi('labor:expert:query')")
    @GetMapping(value = "/getInfo/{expertId}")
    public AjaxResult getInfo(@PathVariable("expertId") Integer expertId) {
        return success(empExpertService.selectEmpExpertByExpertId(expertId));
    }


    @ApiOperation("下载专家信息模板 ")
    @PostMapping("/download/template")
    public void importTemplate(HttpServletResponse response) {
        empExpertService.downloadExperExcel(response);
    }


    @ApiOperation("导入专家数据")
    @PostMapping("/importData")
    public AjaxResult importData(@RequestBody MultipartFile file) {
        LoginUser loginUser = getLoginUser();
        return success(empExpertService.importEmpExpertData(file, loginUser));
    }


    /**
     * 导出员工列表
     */
    @ApiOperation("直接导出专家信息")
    //@PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "专家", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody EmpExpertExportVO empExpertExport) {
        List<ExpertExportListVO> list = empExpertService.selectExportExportInfos(empExpertExport);
        ExcelUtil<ExpertExportListVO> util = new ExcelUtil<>(ExpertExportListVO.class);
        Field[] declaredFields = EmpExpert.class.getDeclaredFields();
        List<String> fieldNamesWithExcel = new ArrayList<>();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Excel.class)) {
                fieldNamesWithExcel.add(field.getName());
            }
        }
        List<String> excludeFieldNames = new ArrayList<>();
        List<String> fieldNames = empExpertExport.getFieldNames();
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
        util.exportExcel(response, list, "专家数据");
    }


    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出专家信息")
    //@PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "专家", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody EmpExpertExportDivideVO empExpertExportDivide) {
        empExpertService.exportDivide(response, empExpertExportDivide);
    }


    /**
     * 新增专家信息
     */

    @ApiOperation("新增专家信息")
    @PreAuthorize("@ss.hasPermi('labor:expert:add')")
    @Log(title = "新增专家", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EmpExpert empExpert) {
        if (empExpert.getExpertEmpName() == null) {
            Employee emp = employeeService.selectEmployeeByEmpId(empExpert.getExpertEmpId());
            empExpert.setExpertEmpName(emp.getEmpName());
            empExpert.setExpertEmpIdcard(emp.getEmpIdcard());
        }
        String username = getUsername();
        return toAjax(empExpertService.insertEmpExpert(empExpert, username));
    }

    /**
     * 修改【请填写功能名称】
     */
    @ApiOperation("修改专家信息")
    @PreAuthorize("@ss.hasPermi('labor:expert:edit')")
    @Log(title = "【专家修改】", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody EmpExpert empExpert) {
        String username = getUsername();
        return toAjax(empExpertService.updateEmpExpert(empExpert, username));
    }

    /**
     * 删除【请填写功能名称】
     */

    @ApiOperation("删除专家信息")
    @PreAuthorize("@ss.hasPermi('labor:expert:remove')")
    @Log(title = "【删除专家】", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{expertIds}")
    public AjaxResult remove(@PathVariable Integer[] expertIds) {
        return toAjax(empExpertService.deleteEmpExpertByExpertIds(expertIds));
    }

    /**
     * 根据条件查询专家列表
     *
     * @param queryVO
     * @return
     */
    @ApiOperation("根据条件查询专家信息")
    @PreAuthorize("@ss.hasPermi('labor:expert:query')")
    @GetMapping("/queryExperts")
    public TableDataInfo queryExperts(EmpExpertQueryVO queryVO) {
        startPage();
        List<ExpertListVO> list = empExpertService.selectEmpExpertListByWhere(queryVO);
        return getDataTable(list);
    }

    /**
     * 初始化专家新增和页面
     *
     * @return
     */
    @ApiOperation("初始化新增专家信息，取得员工名称和身份证数据")
    @PreAuthorize("@ss.hasPermi('labor:expert:add')")
    @GetMapping("/initExpert")
    public AjaxResult initExpert() {
        System.out.println("开始了");
        AjaxResult ajax = AjaxResult.success();
        EmployeeInfoVO infoVO = new EmployeeInfoVO();
        /**
        List<String> statusList = new ArrayList<>();

        statusList.add("1");
        statusList.add("2");
        statusList.add("5");
        statusList.add("6");
        statusList.add("7");
        infoVO.setEmpStatusList(statusList);
         **/
        List<EmpNameCardVO> empNameCardList = employeeService.selectEmpNameAndCard(infoVO);
        ajax.put("empNameAndCardData", empNameCardList);
        return ajax;
    }

    /**
     * 专家数量统计
     *
     * @return
     */
    @ApiOperation("专家数量统计,包括：各部门专家数量统计和各称号专家数量统计")
    @PreAuthorize("@ss.hasPermi('labor:expert:query')")
    @GetMapping("/counting")
    public AjaxResult counting() {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("deptCountData", empExpertService.countByDept());
        ajax.put("titleCountData", empExpertService.countByTitle());

        return ajax;
    }

}
