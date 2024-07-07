package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.constant.HttpStatus;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.mapper.ChinaAddressMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.FamilyRelationsHistoryMapper;
import com.yuantu.labor.mapper.FamilyRelationsMapper;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工Controller
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Api("员工管理")
@RestController
@RequestMapping("/labor/employee")
public class EmployeeController extends BaseController {
    @Autowired
    private IEmployeeService employeeService;


    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private FamilyRelationsMapper familyRelationsMapper;

    @Autowired
    private ChinaAddressMapper chinaAddressMapper;

    @Autowired
    private FamilyRelationsHistoryMapper familyRelationsHistoryMapper;

    /**
     * 查询员工列表
     */
    @ApiOperation("查询员工列表")
    //  @PreAuthorize("@ss.hasPermi('labor:employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmpSearchVO employee) {
        if (StringUtils.isNotEmpty(employee.getContactPhone1())
                || StringUtils.isNotEmpty(employee.getContactPhone2())
                || StringUtils.isNotEmpty(employee.getFamAppellation1())
                || StringUtils.isNotEmpty(employee.getFamAppellation2())
        ) {
            List<FamilyRelations> familyRelations = familyRelationsMapper.searchFamilyInfo(employee);
            if (CollectionUtils.isEmpty(familyRelations)) {
                TableDataInfo rspData = new TableDataInfo();
                rspData.setCode(HttpStatus.SUCCESS);
                rspData.setMsg("查询成功");
                rspData.setRows(Collections.emptyList());
                rspData.setTotal(0L);
                return rspData;
            }
            List<Long> empIds = familyRelations.stream().map(FamilyRelations::getFamEmpId).collect(Collectors.toList());
            employee.setEmpIds(empIds);
        }
        startPage();
        List<EmployeeVO> list = employeeService.selectEmployees(employee);
        return getDataTable(list);
    }

    /**
     * 查询员工列表
     */
    @ApiOperation("查询员工历史信息列表")
    // @PreAuthorize("@ss.hasPermi('labor:employee:list')")
    @GetMapping("/list/history")
    public TableDataInfo listForHistory(EmpSearchVO employee) {
        // List<Long> empIds = employeeMapper.findAllEmployees().stream().map(Employee::getEmpId).collect(Collectors.toList());
        List<Long> empIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(employee.getContactPhone1())
                || StringUtils.isNotEmpty(employee.getContactPhone2())
                || StringUtils.isNotEmpty(employee.getFamAppellation1())
                || StringUtils.isNotEmpty(employee.getFamAppellation2())
        ) {
            List<FamilyRelationsHistory> familyRelations = familyRelationsHistoryMapper.searchFamilyInfo(employee);
            if (CollectionUtils.isEmpty(familyRelations)) {
                TableDataInfo rspData = new TableDataInfo();
                rspData.setCode(HttpStatus.SUCCESS);
                rspData.setMsg("查询成功");
                rspData.setRows(Collections.emptyList());
                rspData.setTotal(0L);
                return rspData;
            }
            empIds = familyRelations.stream().map(FamilyRelationsHistory::getFamEmpId).collect(Collectors.toList());
        }
//        if (CollectionUtils.isEmpty(empIds)) {
//            TableDataInfo rspData = new TableDataInfo();
//            rspData.setCode(HttpStatus.SUCCESS);
//            rspData.setMsg("查询成功");
//            rspData.setRows(Collections.emptyList());
//            rspData.setTotal(0L);
//            return rspData;
//        }
        employee.setEmpIds(empIds);

        startPage();
        List<EmpHistoryVO> list = employeeService.selectEmployeeHistory(employee);
        return getDataTable(list);
    }


    /**
     * 获取员工详细信息
     */
    @ApiOperation("获取员工详细信息")
    // @PreAuthorize("@ss.hasPermi('labor:employee:query')")
    @GetMapping(value = "/{empId}")
    public AjaxResult getInfo(@PathVariable("empId") Long empId) {
        return success(employeeService.selectEmployeeInfoByEmpId(empId));
    }

    /**
     * 获取省市区信息
     */
    @ApiOperation("获取省市区")
    // @PreAuthorize("@ss.hasPermi('labor:employee:query')")
    @GetMapping(value = "/china-address")
    public AjaxResult getChinaAddress() {
        return success(employeeService.getChinaAddress());
    }



    @ApiOperation("对员工身份信息做加解密处理 true 表示加密 false 表示解密")
    @GetMapping(value = "/deal")
    public AjaxResult dealEmpIdCardInfo(@RequestParam Boolean isEncode) {
        return success(employeeService.dealEmpIdCardInfo(isEncode));
    }


    /**
     * 新增员工
     */
    @ApiOperation("新增修改员工")
    // @PreAuthorize("@ss.hasPermi('labor:employee:add')")
    @Log(title = "员工", businessType = BusinessType.INSERT)
    @PostMapping("/update")
    public AjaxResult add(@RequestBody EmpAddVO employee) {
        return success(employeeService.insertEmployee(employee, getUserId(), getUsername(), true));
    }

    /**
     * 查询员工亲属信息
     */
    @ApiOperation("查询员工亲属信息")
    @PostMapping("/family")
    public AjaxResult searchEmpFamilyInfos(@RequestParam Long employeeId) {
        return success(employeeService.searchEmpFamilyInfos(employeeId));
    }


    /**
     * 删除员工
     */
    @ApiOperation("删除员工")
    //  @PreAuthorize("@ss.hasPermi('labor:employee:remove')")
    @Log(title = "员工", businessType = BusinessType.DELETE)
    @DeleteMapping("/{empIds}")
    public AjaxResult remove(@PathVariable Long[] empIds) {
        return toAjax(employeeService.deleteEmployeeByEmpIds(empIds));
    }


    @ApiOperation("查询本月员工情况人数")
    @GetMapping("/month/count")
    public AjaxResult countEmpNum() {
        return success(employeeService.countEmpNum());
    }


    @ApiOperation("批量离职员工")
    @PostMapping("/fire")
    public AjaxResult batchFireEmployees(@RequestBody List<EmpChangeStatusVO> empChangeStatus) {
        return success(employeeService.batchFireEmployees(empChangeStatus));
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


    @ApiOperation("上传员工信息")
    @PostMapping("/upload")
    public AjaxResult uploadEmployeeInfos(@RequestBody MultipartFile file) {
        Long userId = getUserId();
        String username = getUsername();
        return success(employeeService.uploadEmployeeInfosFile(file, userId, username));
    }


    /**
     * 导出员工列表
     */
    @ApiOperation("直接导出员工信息")
    // @PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody EmpExportVO empExport) {
        List<Employee> list = employeeService.selectEmployeeExportInfos(empExport);
        list.stream().filter(f -> StringUtils.isNotBlank(f.getEmpIdcard())).forEach(e -> e.setEmpIdcard(Base64.desensitize(e.getEmpIdcard())));
        transFormDicForEmployee(list);
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        Field[] declaredFields = Employee.class.getDeclaredFields();
        List<String> fieldNamesWithExcel = new ArrayList<>();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Excel.class)) {
                fieldNamesWithExcel.add(field.getName());
            }
        }
        List<String> excludeFieldNames = new ArrayList<>();
        List<String> fieldNames = empExport.getFieldNames();
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
        util.exportExcel(response, list, "员工数据");
    }

    private void transFormDicForEmployee(List<Employee> list) {

        List<SysDictData> empStatusDicList = dictDataMapper.selectDictDataByType("emp_status");

        List<SysDictData> hireCompanyDicList = dictDataMapper.selectDictDataByType("hire_company");

        List<SysDictData> empHrCompanyDicList = dictDataMapper.selectDictDataByType("emp_hr_company");

        List<SysDictData> empCategoryDicList = dictDataMapper.selectDictDataByType("emp_category");

        List<SysDictData> empPositionDicList = dictDataMapper.selectDictDataByType("emp_position");

        List<SysDictData> empPositionCategoryDicList = dictDataMapper.selectDictDataByType("emp_position_category");

        List<SysDictData> empPositionLevelDicList = dictDataMapper.selectDictDataByType("emp_position_level");

        List<SysDictData> empSalaryLevelDicList = dictDataMapper.selectDictDataByType("emp_salary_level");

        List<SysDictData> empPoliticalDicList = dictDataMapper.selectDictDataByType("emp_political_status");

        List<SysDictData> empEducationDicList = dictDataMapper.selectDictDataByType("emp_education");

        List<SysDictData> empTitleDicList = dictDataMapper.selectDictDataByType("emp_title");

        List<SysDictData> empSexDicList = dictDataMapper.selectDictDataByType("emp_sex");

        for (Employee employee : list) {
            String empStatus = employee.getEmpStatus();
            for (SysDictData empStatusDic : empStatusDicList) {
                if (empStatusDic.getDictValue().equals(empStatus)) {
                    employee.setEmpStatus(empStatusDic.getDictLabel());
                }
            }
            String hireCompany = employee.getEmpEmployingUnits();
            for (SysDictData hireCompanyDic : hireCompanyDicList) {
                if (hireCompanyDic.getDictValue().equals(hireCompany)) {
                    employee.setEmpEmployingUnits(hireCompanyDic.getDictLabel());
                }
            }

            String empHrCompany = employee.getEmpHrCompany();
            for (SysDictData hrCompanyDic : empHrCompanyDicList) {
                if (hrCompanyDic.getDictValue().equals(empHrCompany)) {
                    employee.setEmpHrCompany(hrCompanyDic.getDictLabel());
                }
            }
            String empCategory = employee.getEmpCategory();
            for (SysDictData categoryDic : empCategoryDicList) {
                if (categoryDic.getDictValue().equals(empCategory)) {
                    employee.setEmpCategory(categoryDic.getDictLabel());
                }
            }

            String empPosition = employee.getEmpPosition();
            for (SysDictData positionDic : empPositionDicList) {
                if (positionDic.getDictValue().equals(empPosition)) {
                    employee.setEmpPosition(positionDic.getDictLabel());
                }
            }

            String positionCategory = employee.getEmpPositionCategory();
            for (SysDictData positionCategoryDic : empPositionCategoryDicList) {
                if (positionCategoryDic.getDictValue().equals(positionCategory)) {
                    employee.setEmpPositionCategory(positionCategoryDic.getDictLabel());
                }
            }

            String empPositionLevel = employee.getEmpPositionLevel();
            for (SysDictData positionLevelDic : empPositionLevelDicList) {
                if (positionLevelDic.getDictValue().equals(empPositionLevel)) {
                    employee.setEmpPositionLevel(positionLevelDic.getDictLabel());
                }
            }

            String empSalaryLevel = employee.getEmpSalaryLevel();
            for (SysDictData salaryLevelDic : empSalaryLevelDicList) {
                if (salaryLevelDic.getDictValue().equals(empSalaryLevel)) {
                    employee.setEmpSalaryLevel(salaryLevelDic.getDictLabel());
                }
            }

            String empPoliticalStatus = employee.getEmpPoliticalStatus();
            for (SysDictData politicalStatusDic : empPoliticalDicList) {
                if (politicalStatusDic.getDictValue().equals(empPoliticalStatus)) {
                    employee.setEmpPoliticalStatus(politicalStatusDic.getDictLabel());
                }
            }

            String empEducation = employee.getEmpEducation();
            String highestEducation = employee.getHighestEducation();
            for (SysDictData educationDic : empEducationDicList) {
                if (educationDic.getDictValue().equals(empEducation)) {
                    employee.setEmpEducation(educationDic.getDictLabel());
                }
                if (educationDic.getDictValue().equals(highestEducation)) {
                    employee.setHighestEducation(educationDic.getDictLabel());
                }
            }

            String empTitle = employee.getEmpTitle();
            for (SysDictData empTitleDic : empTitleDicList) {
                if (empTitleDic.getDictValue().equals(empTitle)) {
                    employee.setEmpTitle(empTitleDic.getDictLabel());
                }
            }

            String empGender = employee.getEmpGender();
            for (SysDictData empSexDic : empSexDicList) {
                if (empSexDic.getDictValue().equals(empGender)) {
                    employee.setEmpGender(empSexDic.getDictLabel());
                }
            }

            if (employee.getNativePlace() != null) {
                String nativePlace = employee.getNativePlace();
                String[] split = nativePlace.split(",");
                if (split.length == 1) {
                    ChinaAddress address = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    if (address != null) {
                        employee.setNativePlace(address.getName());
                    }
                }
                if (split.length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    if (oneAddress != null && twoAddress != null) {
                        employee.setNativePlace(oneAddress.getName() + "/" + twoAddress.getName());
                    }
                }
                if (split.length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    ChinaAddress threeAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[2]));
                    if (oneAddress != null && twoAddress != null && threeAddress != null) {
                        employee.setNativePlace(oneAddress.getName() + "/" + twoAddress.getName() + "/" + threeAddress.getName());
                    }
                }
            }
            if (employee.getBirthPlace() != null) {
                String birthPlace = employee.getBirthPlace();
                String[] split = birthPlace.split(",");
                if (split.length == 1) {
                    ChinaAddress address = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    if (address != null) {
                        employee.setBirthPlace(address.getName());
                    }
                }
                if (split.length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    if (oneAddress != null && twoAddress != null) {
                        employee.setBirthPlace(oneAddress.getName() + "/" + twoAddress.getName());
                    }
                }
                if (split.length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    ChinaAddress threeAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[2]));
                    if (oneAddress != null && twoAddress != null && threeAddress != null) {
                        employee.setBirthPlace(oneAddress.getName() + "/" + twoAddress.getName() + "/" + threeAddress.getName());
                    }
                }
            }

        }
    }


    /**
     * 导出员工历史信息列表
     */
    @ApiOperation("直接导出员工历史信息")
    //  @PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export/history")
    public void exportForHistory(HttpServletResponse response, @RequestBody EmpExportVO empExport) {
        List<EmpHistory> list = employeeService.selectEmployeeHistoryExportInfos(empExport);
        list.stream().filter(f -> StringUtils.isNotBlank(f.getEmpIdcard())).forEach(e -> e.setEmpIdcard(Base64.desensitize(e.getEmpIdcard())));
        transFormDicForEmpHistory(list);

        ExcelUtil<EmpHistory> util = new ExcelUtil<EmpHistory>(EmpHistory.class);
        Field[] declaredFields = EmpHistory.class.getDeclaredFields();
        List<String> fieldNamesWithExcel = new ArrayList<>();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Excel.class)) {
                fieldNamesWithExcel.add(field.getName());
            }
        }
        List<String> excludeFieldNames = new ArrayList<>();
        List<String> fieldNames = empExport.getFieldNames();
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
        util.exportExcel(response, list, "员工历史信息数据");
    }

    private void transFormDicForEmpHistory(List<EmpHistory> list) {

        List<SysDictData> empStatusDicList = dictDataMapper.selectDictDataByType("emp_status");

        List<SysDictData> hireCompanyDicList = dictDataMapper.selectDictDataByType("hire_company");

        List<SysDictData> empHrCompanyDicList = dictDataMapper.selectDictDataByType("emp_hr_company");

        List<SysDictData> empCategoryDicList = dictDataMapper.selectDictDataByType("emp_category");

        List<SysDictData> empPositionDicList = dictDataMapper.selectDictDataByType("emp_position");

        List<SysDictData> empPositionCategoryDicList = dictDataMapper.selectDictDataByType("emp_position_category");

        List<SysDictData> empPositionLevelDicList = dictDataMapper.selectDictDataByType("emp_position_level");

        List<SysDictData> empSalaryLevelDicList = dictDataMapper.selectDictDataByType("emp_salary_level");

        List<SysDictData> empPoliticalDicList = dictDataMapper.selectDictDataByType("emp_political_status");

        List<SysDictData> empEducationDicList = dictDataMapper.selectDictDataByType("emp_education");

        List<SysDictData> empTitleDicList = dictDataMapper.selectDictDataByType("emp_title");

        List<SysDictData> empSexDicList = dictDataMapper.selectDictDataByType("emp_sex");

        for (EmpHistory empHistory : list) {
            String empStatus = empHistory.getEmpStatus();
            for (SysDictData empStatusDic : empStatusDicList) {
                if (empStatusDic.getDictValue().equals(empStatus)) {
                    empHistory.setEmpStatus(empStatusDic.getDictLabel());
                }
            }
            String hireCompany = empHistory.getEmpEmployingUnits();
            for (SysDictData hireCompanyDic : hireCompanyDicList) {
                if (hireCompanyDic.getDictValue().equals(hireCompany)) {
                    empHistory.setEmpEmployingUnits(hireCompanyDic.getDictLabel());
                }
            }

            String empHrCompany = empHistory.getEmpHrCompany();
            for (SysDictData hrCompanyDic : empHrCompanyDicList) {
                if (hrCompanyDic.getDictValue().equals(empHrCompany)) {
                    empHistory.setEmpHrCompany(hrCompanyDic.getDictLabel());
                }
            }

            String empCategory = empHistory.getEmpCategory();
            for (SysDictData categoryDic : empCategoryDicList) {
                if (categoryDic.getDictValue().equals(empCategory)) {
                    empHistory.setEmpCategory(categoryDic.getDictLabel());
                }
            }

            String empPosition = empHistory.getEmpPosition();
            for (SysDictData positionDic : empPositionDicList) {
                if (positionDic.getDictValue().equals(empPosition)) {
                    empHistory.setEmpPosition(positionDic.getDictLabel());
                }
            }

            String positionCategory = empHistory.getEmpPositionCategory();
            for (SysDictData positionCategoryDic : empPositionCategoryDicList) {
                if (positionCategoryDic.getDictValue().equals(positionCategory)) {
                    empHistory.setEmpPositionCategory(positionCategoryDic.getDictLabel());
                }
            }

            String empPositionLevel = empHistory.getEmpPositionLevel();
            for (SysDictData positionLevelDic : empPositionLevelDicList) {
                if (positionLevelDic.getDictValue().equals(empPositionLevel)) {
                    empHistory.setEmpPositionLevel(positionLevelDic.getDictLabel());
                }
            }

            String empSalaryLevel = empHistory.getEmpSalaryLevel();
            for (SysDictData salaryLevelDic : empSalaryLevelDicList) {
                if (salaryLevelDic.getDictValue().equals(empSalaryLevel)) {
                    empHistory.setEmpSalaryLevel(salaryLevelDic.getDictLabel());
                }
            }

            String empPoliticalStatus = empHistory.getEmpPoliticalStatus();
            for (SysDictData politicalStatusDic : empPoliticalDicList) {
                if (politicalStatusDic.getDictValue().equals(empPoliticalStatus)) {
                    empHistory.setEmpPoliticalStatus(politicalStatusDic.getDictLabel());
                }
            }

            String empEducation = empHistory.getEmpEducation();
            String highestEducation = empHistory.getHighestEducation();
            for (SysDictData educationDic : empEducationDicList) {
                if (educationDic.getDictValue().equals(empEducation)) {
                    empHistory.setEmpEducation(educationDic.getDictLabel());
                }
                if (educationDic.getDictValue().equals(highestEducation)) {
                    empHistory.setHighestEducation(educationDic.getDictLabel());
                }
            }

            String empTitle = empHistory.getEmpTitle();
            for (SysDictData empTitleDic : empTitleDicList) {
                if (empTitleDic.getDictValue().equals(empTitle)) {
                    empHistory.setEmpTitle(empTitleDic.getDictLabel());
                }
            }

            String empGender = empHistory.getEmpGender();
            for (SysDictData empSexDic : empSexDicList) {
                if (empSexDic.getDictValue().equals(empGender)) {
                    empHistory.setEmpGender(empSexDic.getDictLabel());
                }
            }
            if (empHistory.getNativePlace() != null) {
                String nativePlace = empHistory.getNativePlace();
                String[] split = nativePlace.split(",");
                if (split.length == 1) {
                    ChinaAddress address = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    if (address != null) {
                        empHistory.setNativePlace(address.getName());
                    }
                }
                if (split.length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    if (oneAddress != null && twoAddress != null) {
                        empHistory.setNativePlace(oneAddress.getName() + "/" + twoAddress.getName());
                    }
                }
                if (split.length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    ChinaAddress threeAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[2]));
                    if (oneAddress != null && twoAddress != null && threeAddress != null) {
                        empHistory.setNativePlace(oneAddress.getName() + "/" + twoAddress.getName() + "/" + threeAddress.getName());
                    }
                }
            }
            if (empHistory.getBirthPlace() != null) {
                String birthPlace = empHistory.getBirthPlace();
                String[] split = birthPlace.split(",");
                if (split.length == 1) {
                    ChinaAddress address = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    if (address != null) {
                        empHistory.setBirthPlace(address.getName());
                    }
                }
                if (split.length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    if (oneAddress != null && twoAddress != null) {
                        empHistory.setBirthPlace(oneAddress.getName() + "/" + twoAddress.getName());
                    }
                }
                if (split.length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[0]));
                    ChinaAddress twoAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[1]));
                    ChinaAddress threeAddress = chinaAddressMapper.selectChinaAddressById(Long.parseLong(split[2]));
                    if (oneAddress != null && twoAddress != null && threeAddress != null) {
                        empHistory.setBirthPlace(oneAddress.getName() + "/" + twoAddress.getName() + "/" + threeAddress.getName());
                    }
                }
            }
        }

    }

    /**
     * 表格拆分导出员工信息
     */
    @ApiOperation("表格拆分导出员工信息")
    //  @PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody EmpExportDivideVO empExport) {
        employeeService.exportDivide(response, empExport);
    }


    /**
     * 表格拆分导出员工历史信息
     */
    @ApiOperation("表格拆分导出员工历史信息")
    //  @PreAuthorize("@ss.hasPermi('labor:employee:export')")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export/history/divide")
    public void exportHistoryDivide(HttpServletResponse response, @RequestBody EmpExportDivideVO empExport) {
        employeeService.exportHistoryDivide(response, empExport);
    }

    @ApiOperation("下载员工示例文件模版")
    @GetMapping("/file/template/download")
    public void downloadEmployeeTemplateInfos(HttpServletResponse response) {
        employeeService.downloadFileTemplate(response);
    }


    @ApiOperation("上传员工文件信息")
    @PostMapping("/upload/file")
    public AjaxResult uploadEmployeeFileInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(employeeService.uploadEmployeeFileInfosFile(file, username, userId));
    }


    @ApiOperation("下载员工文件信息")
    @PostMapping("/download/file")
    public void downloadEmployeeFileInfos(HttpServletResponse response, @RequestBody EmpFileVO empFile) {
        employeeService.downloadEmployeeFileInfosFile(response, empFile);
    }


    @ApiOperation(value = "下载员工下载证件模板")
    @GetMapping("/excel/template/download")
    public void downloadExcelTemplate(HttpServletResponse response) {
        try {
            employeeService.downloadExcelTemplate(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("根据上传模版信息匹配员工证件信息")
    @PostMapping("/match/file/info")
    public AjaxResult matchEmpFileInfosWithinTemplate(@RequestBody MultipartFile file) {
        Long userId = getUserId();
        return success(employeeService.matchEmpFileInfosWithinTemplate(file, userId));
    }


    /**
     * 查询模版匹配情况列表
     */
    @ApiOperation("查询模板匹配情况列表")
    @GetMapping("/match/template/list")
    public AjaxResult matchTemplateList(MatchSearchVO matchSearch) {
        Long userId = getUserId();
        return success(employeeService.selectMatchTemplateList(matchSearch, userId));
    }

    @ApiOperation("修改匹配员工身份证信息")
    @PostMapping("/match/template/update")
    public AjaxResult updateMatchTemplateInfo(@RequestBody MatchUpdateVO matchUpdate) {
        Long userId = getUserId();
        return success(employeeService.updateMatchTemplateInfo(matchUpdate, userId));
    }


    @ApiOperation("批量删除匹配信息")
    @PostMapping("/match/template/remove")
    public AjaxResult removeMatchTemplateInfos(@RequestBody List<Long> idList) {

        return success(employeeService.removeMatchTemplateInfos(idList));
    }

    @ApiOperation("查询匹配数量信息")
    @GetMapping("/match/template/info")
    public AjaxResult findMatchTemplateNums() {
        Long userId = getUserId();
        return success(employeeService.findMatchTemplateNums(userId));
    }


    /**
     * 删除员工
     */
    @ApiOperation("删除员工亲属信息")
    // @PreAuthorize("@ss.hasPermi('labor:employee:remove')")
    @Log(title = "员工亲属", businessType = BusinessType.DELETE)
    @PostMapping("/remove/fam")
    public AjaxResult removeFamilyInfo(@RequestBody Long[] famIds) {
        return toAjax(employeeService.deleteEmpFamilyByFamId(famIds));
    }


    /**
     * 根据学历，年龄，性别，职称等类型查询数据库，生成json格式文件
     * 输入的参数包括： empDeptId（部门id），empEducation(学历），empGender(性别)，empTitle（职称），empAgeInterval(年龄区间)
     * empAgeInterval（年龄区间）格式约定：0-30,30-40,40-50,50-60,60-100
     * 返回的5个JSON对象，包括：deptSet（部门集）educationSet（学历集），genderSet（性别集），titleSet（职称集），ageSet（年龄集）
     *
     * @param empVO，
     * @return AjaxResult
     */
    @ApiOperation("根据学历，年龄，性别，职称等类型查询数据库，生成json格式文件")
    @PreAuthorize("@ss.hasPermi('labor:employee:query')")
    @GetMapping("/typeAnalysis")
    public AjaxResult typeAnalysis(EmployeeInfoVO empVO) {
        System.out.println("empVO:" + empVO);
        AjaxResult ajax = AjaxResult.success();
        //处理年龄区间
        if (empVO != null && empVO.getEmpAgeInterval() != null) {
            String[] ageArray = empVO.getEmpAgeInterval().split("-");
            empVO.setMinAge(Integer.parseInt(ageArray[0]));
            empVO.setMaxAge(Integer.parseInt(ageArray[1]));

        }
        //去数据库查询，返回的是带有五个结果集的MAP
        HashMap<String, Collection<PieChartVO>> empMap = employeeService.selectEmployeeByEmployeeInfoVO(empVO);
        //解析结果集，存到ajax对象
        Set<Map.Entry<String, Collection<PieChartVO>>> empSet = empMap.entrySet();
        Iterator<Map.Entry<String, Collection<PieChartVO>>> empIter = empSet.iterator();

        while (empIter.hasNext()) {
            Map.Entry<String, Collection<PieChartVO>> empEntry = empIter.next();
            ajax.put(empEntry.getKey(), empEntry.getValue());
        }

        return ajax;
    }


}
