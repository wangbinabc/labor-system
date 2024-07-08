package com.yuantu.web.controller.labor;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.SecurityUtils;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.service.IDepartmentService;
import com.yuantu.labor.vo.DepartmentVO;
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
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 部门Controller
 * 
 * @author ruoyi
 * @date 2023-09-06
 */
@Api("部门管理")
@RestController
@RequestMapping("/labor/department")
public class DepartmentController extends BaseController
{
    @Autowired
    private IDepartmentService departmentService;

    /**
     * 查询部门列表
     */
    @ApiOperation("查询部门列表（显示单位名）")
   @PreAuthorize("@ss.hasPermi('labor:department:list')")
    @GetMapping("/list")
    public TableDataInfo list(Department department)
    {

        startPage();
        List<DepartmentVO> list = departmentService.selectDepartmentVOList(department);
        return getDataTable(list);
    }

    /**
     * 导出部门列表
     */
   @PreAuthorize("@ss.hasPermi('labor:department:export')")
    @Log(title = "部门", businessType = BusinessType.EXPORT)
    @ApiOperation("导出部门列表，（显示单位名）")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Department department,int templateOnly)
    {
        if (templateOnly==0){
            List<Department> list =new ArrayList<>();
            Department template =   new Department();
            template.setDeptId(Long.valueOf(1));
            template.setDeptName("办公室(示例)");
            template.setDeptType("1");
            template.setDeptUnitId(Long.valueOf(1));
            list.add(template);
            ExcelUtil<Department> util = new ExcelUtil<Department>(Department.class);
         //   response.setHeader("Content-Disposition", "attachment; filename="+"部门导入模板"+".xls");
          //  util.encodingFilename("部门导入模板");
            util.exportExcel(response, list, "部门数据-示例");
        }else {
            List<DepartmentVO> list = departmentService.selectDepartmentVOList(department);
            ExcelUtil<DepartmentVO> util = new ExcelUtil<DepartmentVO>(DepartmentVO.class);
         //   util.encodingFilename("部门信息表");
            util.exportExcel(response, list, "部门数据");
        }
    }

    /**
     * 获取部门详细信息
     */
    @PreAuthorize("@ss.hasPermi('labor:department:query')")
    @ApiOperation("获取部门详细信息，（显示单位名）")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable("deptId") Long deptId)
    {
        return success(departmentService.selectDepartmentVOByDeptId(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('labor:department:add')")
    @ApiOperation("新增部门")
    @Log(title = "部门", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Department department)
    {
      //  department.setUpdateBy(getUsername());
        department.setCreateBy(getUsername());
        department.setCreateTime(DateUtils.getNowDate());
        return toAjax(departmentService.insertDepartment(department));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('labor:department:edit')")
    @Log(title = "部门", businessType = BusinessType.UPDATE)
    @ApiOperation("修改部门")
    @PutMapping
    public AjaxResult edit(@RequestBody Department department)
    {
        department.setUpdateBy(getUsername());
        department.setUpdateTime(DateUtils.getNowDate());
        return toAjax(departmentService.updateDepartment(department));
    }

    /**
     * 逻辑删除部门
     */
    @PreAuthorize("@ss.hasPermi('labor:department:remove')")
    @ApiOperation("逻辑删除部门")
    @Log(title = "部门", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deptIds}")
    public AjaxResult remove(@PathVariable Long[] deptIds)
    {

        return toAjax(departmentService.deleteDepartmentByDeptIds(deptIds));
    }
}
