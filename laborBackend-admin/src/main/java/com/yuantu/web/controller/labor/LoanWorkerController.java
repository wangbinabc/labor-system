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
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.service.IDepartmentService;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.service.IEmployingUnitsService;
import com.yuantu.labor.service.ILoanWorkerService;
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
@RequestMapping("/labor/worker")
public class LoanWorkerController extends BaseController {
    @Autowired
    private ILoanWorkerService loanWorkerService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IEmployingUnitsService employingUnitsService;

    @Autowired
    private ISysDictDataService dictDataService;
    /**
     * 查询借工列表
     */

    @ApiOperation("查询借工列表")
    @PreAuthorize("@ss.hasPermi('labor:worker:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanWorker loanWorker) {
        startPage();
        List<LoanWorker> list = loanWorkerService.selectLoanWorkerList(loanWorker);
        return getDataTable(list);
    }

    /**
     * 导出借工列表
     */
    @ApiOperation("导出借工列表")
    //@PreAuthorize("@ss.hasPermi('labor:worker:export')")
    @Log(title = "借工", businessType = BusinessType.EXPORT)
    @GetMapping("/export/{loanIds}")
    public void export(HttpServletResponse response, @PathVariable Integer[] loanIds) {
        LoanWorkerQueryVO vo = new LoanWorkerQueryVO();
        vo.setLoanIds(loanIds);
        List<LoanWorkerListVO> list = loanWorkerService.selectLoanWorkersListByWhere(vo);
        ExcelUtil<LoanWorkerListVO> util = new ExcelUtil<LoanWorkerListVO>(LoanWorkerListVO.class);
        util.exportExcel(response, list, "借工数据");
    }


    @ApiOperation("下载借工模板")
    @PostMapping("/download/template")
    public void importTemplate(HttpServletResponse response) {
        loanWorkerService.downloadLoanWorkerExcel(response);
    }


    @ApiOperation("导入借工数据")
    //@PreAuthorize("@ss.hasPermi('labor:worker:importData')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) {
        LoginUser loginUser = getLoginUser();
        return success(loanWorkerService.importLoanWorkData(file, loginUser));
    }


    /**
     * 导出员工列表
     */
    @ApiOperation("直接导出借工信息")
    //@PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "借工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody LoanWorkerExportVO loanWorkerExport) {
        List<LoanWorkerExportListVO> list = loanWorkerService.selectLoanWorkerExportInfos(loanWorkerExport);

        ExcelUtil<LoanWorkerExportListVO> util = new ExcelUtil<>(LoanWorkerExportListVO.class);
        Field[] declaredFields = LoanWorkerExportListVO.class.getDeclaredFields();

        List<String> fieldNamesWithExcel = new ArrayList<>();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Excel.class)) {
                fieldNamesWithExcel.add(field.getName());
            }
        }
        List<String> excludeFieldNames = new ArrayList<>();
        List<String> fieldNames = loanWorkerExport.getFieldNames();
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
        util.exportExcel(response, list, "借工数据");
    }


    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出借工信息")
    //@PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "借工", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody LoanWorkerExportDivideVO loanWorkerExportDivide) {
        loanWorkerService.exportDivide(response, loanWorkerExportDivide);
    }


    /**
     * 导入
     *
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
//    @ApiOperation("导入借工模板数据")
//    @PreAuthorize("@ss.hasPermi('labor:worker:importData')")
//    @PostMapping("/importData")
//    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//        ExcelUtil<LoanWorker> util = new ExcelUtil<LoanWorker>(LoanWorker.class);
//        List<LoanWorker> loanWorkerList = util.importExcel(file.getInputStream());
//        String message = loanWorkerService.importLoanWork(loanWorkerList);
//        return success(message);
//    }


    /**
     * 获取借工详细信息
     */

    @ApiOperation("根据id获取借工详细信息")
    @PreAuthorize("@ss.hasPermi('labor:worker:getInfo')")
    @GetMapping(value = "/getInfo/{loanId}")
    public AjaxResult getInfo(@PathVariable("loanId") Long loanId) {
        return success(loanWorkerService.selectLoanWorkerByLoanId(loanId));
    }

    /**
     * 新增借工
     */
    @ApiOperation("新增借工数据")
    @PreAuthorize("@ss.hasPermi('labor:worker:add')")
    @Log(title = "借工", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody LoanWorker loanWorker) {

        Department dept = departmentService.selectDepartmentByDeptId(Long.valueOf(loanWorker.getLoanApplyDeptId()));
        loanWorker.setLoanApplyDeptName(dept.getDeptName());

        String unitName = dictDataService.selectDictLabel("loan_unit",loanWorker.getLoanApplyUnitId());
        loanWorker.setLoanApplyUnitName(unitName);

        String username = getUsername();
        return toAjax(loanWorkerService.insertLoanWorker(loanWorker, username));
    }

    /**
     * 修改借工
     */

    @ApiOperation("修改借工数据")
    @PreAuthorize("@ss.hasPermi('labor:worker:edit')")
    @Log(title = "借工", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody LoanWorker loanWorker) {

        Department dept = departmentService.selectDepartmentByDeptId(loanWorker.getLoanApplyDeptId());
        loanWorker.setLoanApplyDeptName(dept.getDeptName());


        String unitName = dictDataService.selectDictLabel("loan_unit",loanWorker.getLoanApplyUnitId());
        loanWorker.setLoanApplyUnitName(unitName);

        String username = getUsername();
        return toAjax(loanWorkerService.updateLoanWorker(loanWorker, username));
    }

    @ApiOperation("查询借工简要数据")
    @GetMapping("/simple")
    public AjaxResult searchSimpleInfos() {
        return success(loanWorkerService.searchSimpleInfos());
    }


    /**
     * 删除借工
     */

    @ApiOperation("删除借工")
    @PreAuthorize("@ss.hasPermi('labor:worker:remove')")
    @Log(title = "借工", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{loanIds}")
    public AjaxResult remove(@PathVariable Integer[] loanIds) {
        return toAjax(loanWorkerService.deleteLoanWorkerByLoanIds(loanIds));

    }

    /**
     * 查询借工列表
     *
     * @param queryVO
     * @return
     */
    @ApiOperation("根据条件查询借工数据列表")
    @PreAuthorize("@ss.hasPermi('labor:worker:queryWorker')")
    @GetMapping("/queryWorker")
    public TableDataInfo queryList(LoanWorkerQueryVO queryVO) {
        startPage();
        List<LoanWorkerListVO> loanWorkerList = loanWorkerService.selectLoanWorkersListByWhere(queryVO);
        //System.out.println("table" + loanWorkerList.toString());

        return getDataTable(loanWorkerList);

    }

    /**
     * 初始化借工页面，取得员工，部门，用工单位列表
     *
     * @return
     */
    @ApiOperation("初始化新增借工页面数据，取得员工，部门，用工单位数据")
    @PreAuthorize("@ss.hasPermi('labor:worker:initAdd')")
    @GetMapping("/initAdd")
    public AjaxResult initAdd() {
        //System.out.println("开始了");
        AjaxResult ajax = AjaxResult.success();

        List<DepartmentVO> deptList = departmentService.selectDepartmentVOList(new Department());
        SysDictData dictData = new SysDictData();
        dictData.setDictType("loan_unit");
        List<SysDictData> employingUnitsList = dictDataService.selectDictDataList(dictData);
        ajax.put("deptData", deptList);
        ajax.put("employingUnitsData", employingUnitsList);

        return ajax;
    }

    /**
     * 初始化修改页面
     *
     * @param loanId
     * @return
     */
    @ApiOperation("初始化修改借工页面数据，取得借工，部门和用工单位数据")
    @PreAuthorize("@ss.hasPermi('labor:worker:initEdit')")
    @GetMapping("/initEdit/{loanId}")
    public AjaxResult initEdit(@PathVariable("loanId") Long loanId) {
        System.out.println("开始了");
        AjaxResult ajax = AjaxResult.success();
        LoanWorker loanWorker = loanWorkerService.selectLoanWorkerByLoanId(loanId);
        List<DepartmentVO> deptList = departmentService.selectDepartmentVOList(new Department());
        SysDictData dictData = new SysDictData();
        dictData.setDictType("loan_unit");
        List<SysDictData> employingUnitsList = dictDataService.selectDictDataList(dictData);
        ajax.put("loanWorderData", loanWorker);
        ajax.put("deptData", deptList);
        ajax.put("employingUnitsData", employingUnitsList);
        return ajax;
    }

    /**
     * 借工数量统计
     */
    @ApiOperation("借工数量统计,包括：各部门借工数量统计和各借工单位借工数量统计")
    @PreAuthorize("@ss.hasPermi('labor:worker:count')")
    @GetMapping("/counting")
    public AjaxResult counting() {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("deptCountData", loanWorkerService.countByDept());
        ajax.put("unitCountData", loanWorkerService.countByUnit());

        return ajax;
    }
}
