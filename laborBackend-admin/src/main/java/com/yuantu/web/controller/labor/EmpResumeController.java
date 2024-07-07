package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.cenum.ResumeTypeEnum;
import com.yuantu.labor.domain.EmpResume;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.service.IEmpResumeService;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
 * 履历Controller
 *
 * @author ruoyi
 * @date 2023-09-12
 */
@Api("履历管理")
@RestController
@RequestMapping("/labor/resume")
public class EmpResumeController extends BaseController {
    @Autowired
    private IEmpResumeService empResumeService;

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询履历列表
     */
    @ApiOperation("查询履历列表")
    @PreAuthorize("@ss.hasPermi('labor:resume:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmpResume empResume) {
        startPage();
        List<EmpResume> list = empResumeService.selectEmpResumeList(empResume);
        return getDataTable(list);
    }

    /**
     * 导出履历列表
     */
    @ApiOperation("导出履历列表")
    @PreAuthorize("@ss.hasPermi('labor:resume:export')")
    @Log(title = "履历", businessType = BusinessType.EXPORT)
    @PostMapping("/export/{resuIds}")
    public void export(HttpServletResponse response, @PathVariable Integer[] resuIds) {
        EmpResumeVO vo = new EmpResumeVO();
        vo.setResuIds(resuIds);
        List<EmpResume> list = empResumeService.selectEmpResumeListByWhere(vo);
        ExcelUtil<EmpResume> util = new ExcelUtil<EmpResume>(EmpResume.class);
        util.exportExcel(response, list, "履历数据");
    }

    /**
     * 获取履历详细信息
     */
    @ApiOperation("根据id获取履历详细信息")
    @PreAuthorize("@ss.hasPermi('labor:resume:getInfo')")
    @GetMapping(value = "/getInfo/{resuId}")
    public AjaxResult getInfo(@PathVariable("resuId") Integer resuId) {
        return success(empResumeService.selectEmpResumeByResuId(resuId));
    }


    @ApiOperation("下载履历模板 type 1社会 2项目 3本单位")
    @PostMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response, @RequestParam String type) {
        //下载自动模板
        empResumeService.downloadResumeExcel(response, type);
        //下载静态模板
        //empResumeService.downloadTemplate(response, type);
    }


    @ApiOperation("导入履历数据 type 1社会 2项目 3本单位")
    @PostMapping("/importData")
    public AjaxResult importData(@RequestBody MultipartFile file, @RequestParam String type) {
        LoginUser loginUser = getLoginUser();
        return success(empResumeService.importEmpResumeData(file, loginUser, type));
    }


    /**
     * 导出员工列表
     */
    @ApiOperation("直接导出履历信息 type 1社会 2项目 3本单位")
    //@PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "履历", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody EmpResumeExportVO empResumeExport) {
        List<EmpResume> list = empResumeService.selectEmpResumeExportInfos(empResumeExport);
        if (ResumeTypeEnum.ONE.getKey().equals(empResumeExport.getType())) {
            ExcelUtil<ResumeSocialTemplateVO> util = new ExcelUtil<>(ResumeSocialTemplateVO.class);
            Field[] declaredFields = ResumeSocialTemplateVO.class.getDeclaredFields();
            List<String> fieldNamesWithExcel = new ArrayList<>();
            for (Field field : declaredFields) {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(Excel.class)) {
                    fieldNamesWithExcel.add(field.getName());
                }
            }
            List<String> excludeFieldNames = new ArrayList<>();
            List<String> fieldNames = empResumeExport.getFieldNames();
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
            List<ResumeSocialTemplateVO> resumeSocials = new ArrayList<>();
            for (EmpResume empResume : list) {
                ResumeSocialTemplateVO resumeSocial = new ResumeSocialTemplateVO();
                BeanUtils.copyProperties(empResume, resumeSocial);
                resumeSocials.add(resumeSocial);
            }
            util.exportExcel(response, resumeSocials, "履历数据");
        }
        if (ResumeTypeEnum.TWO.getKey().equals(empResumeExport.getType())) {
            ExcelUtil<ResumeProjectTemplateVO> util = new ExcelUtil<>(ResumeProjectTemplateVO.class);
            Field[] declaredFields = ResumeProjectTemplateVO.class.getDeclaredFields();
            List<String> fieldNamesWithExcel = new ArrayList<>();
            for (Field field : declaredFields) {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(Excel.class)) {
                    fieldNamesWithExcel.add(field.getName());
                }
            }
            List<String> excludeFieldNames = new ArrayList<>();
            excludeFieldNames.add("resuContext");
            List<String> fieldNames = empResumeExport.getFieldNames();
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
            List<ResumeProjectTemplateVO> resumeProjects = new ArrayList<>();
            for (EmpResume empResume : list) {
                ResumeProjectTemplateVO resumeProject = new ResumeProjectTemplateVO();
                BeanUtils.copyProperties(empResume, resumeProject);
                resumeProjects.add(resumeProject);
            }
            util.exportExcel(response, resumeProjects, "履历数据");
        }

        if (ResumeTypeEnum.THREE.getKey().equals(empResumeExport.getType())) {
            ExcelUtil<ResumeUnitTemplateVO> util = new ExcelUtil<>(ResumeUnitTemplateVO.class);
            Field[] declaredFields = ResumeUnitTemplateVO.class.getDeclaredFields();
            List<String> fieldNamesWithExcel = new ArrayList<>();
            for (Field field : declaredFields) {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(Excel.class)) {
                    fieldNamesWithExcel.add(field.getName());
                }
            }
            List<String> excludeFieldNames = new ArrayList<>();
            List<String> fieldNames = empResumeExport.getFieldNames();
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
            List<ResumeUnitTemplateVO> resumeUnits = new ArrayList<>();
            for (EmpResume empResume : list) {
                ResumeUnitTemplateVO resumeUnit = new ResumeUnitTemplateVO();
                BeanUtils.copyProperties(empResume, resumeUnit);
                resumeUnits.add(resumeUnit);
            }
            util.exportExcel(response, resumeUnits, "履历数据");
        }

    }


    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出履历信息")
    //@PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "借工", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody EmpResumeExportDivideVO empResumeExportDivide) {
        empResumeService.exportDivide(response, empResumeExportDivide);
    }


    /**
     * 新增履历
     */
    @ApiOperation("新增履历")
    @PreAuthorize("@ss.hasPermi('labor:resume:add')")
    @Log(title = "履历", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EmpResume empResume) {
        if (empResume.getResuEmpName() == null) {
            Employee emp = employeeService.selectEmployeeByEmpId(empResume.getResuEmpId());
            empResume.setResuEmpName(emp.getEmpName());
            empResume.setResuEmpIdcard(emp.getEmpIdcard());
        }
        return toAjax(empResumeService.insertEmpResume(empResume));
    }

    /**
     * 修改履历
     */
    @ApiOperation("修改履历")
    @PreAuthorize("@ss.hasPermi('labor:resume:edit')")
    @Log(title = "履历", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody EmpResume empResume) {
        return toAjax(empResumeService.updateEmpResume(empResume));
    }

    /**
     * 删除履历
     */
    @ApiOperation("删除履历")
    @PreAuthorize("@ss.hasPermi('labor:resume:remove')")
    @Log(title = "履历", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{resuIds}")
    public AjaxResult remove(@PathVariable Integer[] resuIds) {
        return toAjax(empResumeService.deleteEmpResumeByResuIds(resuIds));
    }

    /**
     * 模糊查询指定的履历数据
     *
     * @param queryVO
     * @return
     */
    @ApiOperation("根据条件查询指定的履历数据")
    @PreAuthorize("@ss.hasPermi('labor:resume:query')")
    @GetMapping("/queryResumes")
    public TableDataInfo queryResumes(EmpResumeVO queryVO) {
        startPage();
        List<EmpResume> resumeList = empResumeService.selectEmpResumeListByWhere(queryVO);
        return getDataTable(resumeList);
    }

    /**
     * 初始化新增页面
     *
     * @return
     */
    @ApiOperation("初始化新增履历页面数据，取得员工名称和身份证数据")
    @PreAuthorize("@ss.hasPermi('labor:resume:add')")
    @GetMapping("/initResume")
    public AjaxResult initResume() {
        //System.out.println("开始了");
        AjaxResult ajax = AjaxResult.success();
        List<EmpNameCardVO> empNameCardList = employeeService.selectEmpNameAndCard(new EmployeeInfoVO());
        ajax.put("empNameAndCardData", empNameCardList);
        return ajax;
    }

}
