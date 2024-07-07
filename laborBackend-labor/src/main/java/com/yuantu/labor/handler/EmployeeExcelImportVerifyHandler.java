package com.yuantu.labor.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.labor.cenum.EmpGenderEnum;
import com.yuantu.labor.cenum.EmpStatusEnum;
import com.yuantu.labor.domain.Department;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.EmployingUnits;
import com.yuantu.labor.mapper.DepartmentMapper;
import com.yuantu.labor.mapper.EmployeeMapper;
import com.yuantu.labor.mapper.EmployingUnitsMapper;
import com.yuantu.labor.vo.EmpImportVO;
import com.yuantu.labor.vo.IdCheckVO;
import com.yuantu.system.mapper.SysConfigMapper;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class EmployeeExcelImportVerifyHandler implements IExcelVerifyHandler<EmpImportVO> {

    private final ThreadLocal<List<IdCheckVO>> threadLocal = new ThreadLocal<>();

//    @Autowired
//    private PayMajorMapper payMajorMapper;
//    @Autowired
//    private PayFeeMapper feeMapper;
//    @Autowired
//    private ISysDictTypeService dictTypeService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployingUnitsMapper employingUnitsMapper;

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(EmpImportVO empImport) {
        StringJoiner joiner = new StringJoiner(",");
        String empUnitDeptName = empImport.getEmpDeptName();
        //String regex = "^([\\u4E00-\\u9FA5-])+-([\\u4E00-\\u9FA5-])+$";
        String regex = "^(.)+-(.)+$";
        if (empUnitDeptName != null) {
            if (!empUnitDeptName.matches(regex)) {
                // joiner.add("所属公司-用工部门单元格数据不符合规范");
            } else {
                String[] split = empUnitDeptName.split("-");
                String empDept = split[1];
                Department department = departmentMapper.findDepartmentInfoByName(empDept);

                if (department == null) {
                    joiner.add("系统不存在该部门名称");
                }

                String empUnit = split[0];
                EmployingUnits unit = employingUnitsMapper.findUnitInfoByName(empUnit);

                if (unit == null) {
                    joiner.add("系统不存在该单位名称");
                }

                Department existDept = departmentMapper.findDepartmentInfoByUnitAndDeptName(empUnit, empDept);
                if (existDept == null) {
                    joiner.add(empUnit + "下" + empDept + "不存在");
                }
            }
        }
        String regx = "([\\u4e00-\\u9fa5]+)(\\/[\\u4e00-\\u9fa5]+)*";
        if (empImport.getNativePlace() != null) {
            String nativePlace = empImport.getNativePlace();
            if (!nativePlace.matches(regx)) {
                joiner.add("籍贯数据不符合规范");
            }
        }
        if (empImport.getBirthPlace() != null) {
            String birthPlace = empImport.getBirthPlace();
            if (!birthPlace.matches(regx)) {
                joiner.add("出生地数据不符合规范");
            }
        }

        List<SysDictData> empStatusDicDataInfos = dictDataMapper.selectDictDataByType("emp_status");
        if (empImport.getEmpStatus() != null) {
            List<String> empStatusDicInfos = empStatusDicDataInfos.
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empStatusDicInfos.contains(empImport.getEmpStatus())) {
                joiner.add("员工状态字典值验证不通过");
            }
        }


        if (empImport.getEmpSalaryLevel() != null) {
            List<String> empSalaryLevels = dictDataMapper.selectDictDataByType("emp_salary_level").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empSalaryLevels.contains(empImport.getEmpSalaryLevel())) {
                joiner.add("员工薪级字典值验证不通过");
            }
        }


        if (empImport.getEmpEmployingUnits() != null) {
            List<String> hireCompanies = dictDataMapper.selectDictDataByType("hire_company").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!hireCompanies.contains(empImport.getEmpEmployingUnits())) {
                joiner.add("用工公司字典值验证不通过");
            }
        }

        if (empImport.getEmpHrCompany() != null) {
            List<String> hrCompanies = dictDataMapper.selectDictDataByType("emp_hr_company").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!hrCompanies.contains(empImport.getEmpHrCompany())) {
                joiner.add("劳务公司字典值验证不通过");
            }

        }

        if (empImport.getEmpPosition() != null) {
            List<String> empPositions = dictDataMapper.selectDictDataByType("emp_position").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empPositions.contains(empImport.getEmpPosition())) {
                joiner.add("岗位名称字典值验证不通过");
            }

        }

        if (empImport.getEmpPositionCategory() != null) {
            List<String> empPositionCategoryDic = dictDataMapper.selectDictDataByType("emp_position_category").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empPositionCategoryDic.contains(empImport.getEmpPositionCategory())) {
                joiner.add("岗位类别字典值验证不通过");
            }
        }

        if (empImport.getEmpPositionLevel() != null) {
            List<String> empPositionLevels = dictDataMapper.selectDictDataByType("emp_position_level").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empPositionLevels.contains(empImport.getEmpPositionLevel())) {
                joiner.add("岗级字典值验证不通过");
            }
        }

        if (empImport.getEmpPoliticalStatus() != null) {
            List<String> empPoliticalStatusInfos = dictDataMapper.selectDictDataByType("emp_political_status").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empPoliticalStatusInfos.contains(empImport.getEmpPoliticalStatus())) {
                joiner.add("政治面貌字典值验证不通过");
            }
        }

        if (empImport.getEmpEducation() != null) {

            List<String> empEducations = dictDataMapper.selectDictDataByType("emp_education").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empEducations.contains(empImport.getEmpEducation())) {
                joiner.add("学历字典值验证不通过");
            }

        }

        if (empImport.getEmpGender() != null) {
            List<String> empSexDic = dictDataMapper.selectDictDataByType("emp_sex").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empSexDic.contains(empImport.getEmpGender())) {
                joiner.add("性别字典值验证不通过");
            }
        }


//        List<SysDictData> empCategories = dictDataMapper.selectDictDataByType("emp_category");
//        for (SysDictData empCategory : empCategories) {
//            if (empCategory.getDictLabel().equals(empImportInfo.getEmpCategory())) {
//                empAdd.setEmpCategory(empCategory.getDictValue());
//            }
//        }
        if (empImport.getEmpTitle() != null) {

            List<String> empTitles = dictDataMapper.selectDictDataByType("emp_title").
                    stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
            if (!empTitles.contains(empImport.getEmpTitle())) {
                joiner.add("职称字典值验证不通过");
            }
        }

        String empStatus = empImport.getEmpStatus();
        Date now = new Date();

        String expireKey = EmpStatusEnum.EXPIRE.getKey();
        String expireValue = null;
        for (SysDictData empStatusDicDataInfo : empStatusDicDataInfos) {
            String dictValue = empStatusDicDataInfo.getDictValue();
            if (expireKey.equals(dictValue)) {
                expireValue = empStatusDicDataInfo.getDictLabel();
            }
        }
        if (expireValue != null && expireValue.equals(empStatus)) {
            String empIdcard = empImport.getEmpIdcard();
            if (empIdcard != null) {
                String manRetireAge = configMapper.checkConfigKeyUnique("labor.man.retireAge").getConfigValue();
                String womanRetireAge = configMapper.checkConfigKeyUnique("labor.woman.retireAge").getConfigValue();
                char sexChar = empIdcard.charAt(empIdcard.length() - 2);
                int sexNum = Integer.parseInt(String.valueOf(sexChar));
                String sexStr;
                if (sexNum % 2 == 0) {
                    sexStr = EmpGenderEnum.FEMALE.getKey();
                } else {
                    sexStr = EmpGenderEnum.MALE.getKey();
                }
                Date expireTime = DateUtils.calculateExpireTime(empIdcard, sexStr, manRetireAge, womanRetireAge);
                long intervalNum = expireTime.getTime() - now.getTime();
                if (intervalNum > 0) {
                    joiner.add("员工状态校验未通过");
                }
            }
        }

        String empIdcard = empImport.getEmpIdcard();
        if (empIdcard != null) {
            String empName = empImport.getEmpName();
            if (empName != null) {
                Employee existEmp = employeeMapper.findEmpInfoByEmpName(empName);
                if (existEmp == null) {
                    existEmp = employeeMapper.findInfoByEmpIdCard(empIdcard);
                    if (existEmp != null) {
                        joiner.add("员工身份证号已存在");
                    }
                }
            }
        }

        List<IdCheckVO> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new ArrayList<>();
        }

        threadLocalVal.forEach(e -> {
            int lineNumber = e.getRowNum() + 1;
            if (e.getName() != null && e.getName().equals(empImport.getEmpName())) {
                joiner.add("员工姓名与第" + lineNumber + "行重复");
            }
            if (e.getEmpCode() != null && e.getEmpCode().equals(empImport.getEmpCode())) {
                joiner.add("协同编码与第" + lineNumber + "行重复");
            }
            if (e.getEmpIdCard() != null && e.getEmpIdCard().equals(empImport.getEmpIdcard())) {
                joiner.add("身份证号码与第" + lineNumber + "行重复");
            }
        });


        // 添加本行数据对象到ThreadLocal中
        threadLocalVal.add(new IdCheckVO(empImport.getEmpIdcard(), empImport.getEmpName(), empImport.getRowNum(), empImport.getEmpCode()));
        threadLocal.set(threadLocalVal);

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }

    public ThreadLocal<List<IdCheckVO>> getThreadLocal() {
        return threadLocal;
    }


}
