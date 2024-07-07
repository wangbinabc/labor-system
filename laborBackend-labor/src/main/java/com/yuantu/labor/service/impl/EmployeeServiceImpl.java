package com.yuantu.labor.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.ObjectUtil;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.*;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.EmployeeExcelImportVerifyHandler;
import com.yuantu.labor.handler.MySequentialOutStream;
import com.yuantu.labor.handler.TemplateCellWriteHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysConfigMapper;
import com.yuantu.system.mapper.SysDictDataMapper;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-06
 */

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private FamilyRelationsMapper familyRelationsMapper;

    @Autowired
    private FamilyRelationsHistoryMapper familyRelationsHistoryMapper;

    @Autowired
    private EmpDocumentMapper empDocumentMapper;

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private PostHistoryMapper postHistoryMapper;

    @Autowired
    private EmpHistoryMapper empHistoryMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmpImportHistoryMapper empImportHistoryMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private PositionInfoMapper positionInfoMapper;

    @Autowired
    private SalaryHistoryMapper salaryHistoryMapper;

    @Autowired
    private EmployingUnitsMapper unitsMapper;

    @Autowired
    private ChinaAddressMapper chinaAddressMapper;

    @Autowired
    private EmployeeExcelImportVerifyHandler employeeExcelImportVerifyHandler;

    @Autowired
    private EmpResumeMapper empResumeMapper;

    @Autowired
    private LoanWorkerMapper loanWorkerMapper;


    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);


    /**
     * 查询员工
     *
     * @param empId 员工主键
     * @return 员工
     */
    @Override
    public Employee selectEmployeeByEmpId(Long empId) {

        return employeeMapper.selectEmployeeByEmpId(empId);

    }

    @Override
    public EmpDetailVO selectEmployeeInfoByEmpId(Long empId) {
        EmpDetailVO empDetail = new EmpDetailVO();
        Employee employee = employeeMapper.selectEmployeeByEmpId(empId);
        if (employee == null) {
            throw new ServiceException("员工信息不存在");
        }
        BeanUtils.copyProperties(employee, empDetail);
        if (employee.getNativePlace() != null) {
            String[] strArray = employee.getNativePlace().split(",");
            List<Long> nativePlace = new ArrayList<>();
            for (int i = 0; i < strArray.length; i++) {
                nativePlace.add(Long.parseLong(strArray[i]));
            }
            empDetail.setNativePlace(nativePlace);
        }
        if (employee.getBirthPlace() != null) {
            String[] strArray = employee.getBirthPlace().split(",");
            List<Long> birthPlace = new ArrayList<>();
            for (int i = 0; i < strArray.length; i++) {
                birthPlace.add(Long.parseLong(strArray[i]));
            }
            empDetail.setBirthPlace(birthPlace);
        }
        List<FamilyRelations> familyRelations = familyRelationsMapper.findFamilyInfoByEmpId(empId);
        List<EmpFamilyVO> empFamilies = new ArrayList<>();
        for (FamilyRelations familyRelation : familyRelations) {
            EmpFamilyVO empFamily = new EmpFamilyVO();
            BeanUtils.copyProperties(familyRelation, empFamily);
            if (familyRelation.getFamAge() != null) {
                empFamily.setFamAge(String.valueOf(familyRelation.getFamAge()));
            }
            empFamilies.add(empFamily);
        }
        empDetail.setEmpFamilyInfos(empFamilies);

        List<String> docTypes = new ArrayList<>();
        docTypes.add(EmpDocTypeEnum.ONE.getKey());
        docTypes.add(EmpDocTypeEnum.TWO.getKey());
        docTypes.add(EmpDocTypeEnum.THREE.getKey());
        docTypes.add(EmpDocTypeEnum.FOUR.getKey());
        docTypes.add(EmpDocTypeEnum.THIRTY_FOUR.getKey());
        docTypes.add(EmpDocTypeEnum.THIRTY_FIVE.getKey());
        List<EmpDocument> empDocuments = empDocumentMapper.findDocInfosByEmpIdAndTypes(empId, docTypes);
        EmpDocument avatarDoc = empDocuments.stream().filter(s -> FileTypeEnum.PHOTO.getKey().equals(s.getDocType())).findFirst().
                orElse(new EmpDocument());
        List<EmpDocument> idFiles = empDocuments.stream().filter(s -> FileTypeEnum.ID_CARD.getKey().equals(s.getDocType()))
                .collect(Collectors.toList());
        List<EmpDocument> paperFiles = empDocuments.stream().filter(s -> FileTypeEnum.PAPER.getKey().equals(s.getDocType()))
                .collect(Collectors.toList());
        List<EmpDocument> educationFiles = empDocuments.stream().filter(s -> FileTypeEnum.EDUCATION.getKey().equals(s.getDocType()))
                .collect(Collectors.toList());
        List<EmpDocument> recordFiles = empDocuments.stream().filter(s -> FileTypeEnum.EMP_RECORD.getKey().equals(s.getDocType()))
                .collect(Collectors.toList());
        List<EmpDocument> otherFiles = empDocuments.stream().filter(s -> FileTypeEnum.EMP_OTHER.getKey().equals(s.getDocType()))
                .collect(Collectors.toList());

        empDetail.setAvatarDocId(avatarDoc.getDocId());
        empDetail.setAvatarDocUrl(avatarDoc.getDocAnnexPath());

        List<FileVO> idCardDocList = new ArrayList<>();
        for (EmpDocument idFile : idFiles) {
            FileVO file = new FileVO();
            file.setFileName(idFile.getDocName());
            file.setFileId(idFile.getDocId());
            file.setFileUrl(idFile.getDocAnnexPath());
            idCardDocList.add(file);
        }
        List<FileVO> educateDocList = new ArrayList<>();
        for (EmpDocument educationFile : educationFiles) {
            FileVO file = new FileVO();
            file.setFileName(educationFile.getDocName());
            file.setFileId(educationFile.getDocId());
            file.setFileUrl(educationFile.getDocAnnexPath());
            educateDocList.add(file);
        }
        List<FileVO> paperDocList = new ArrayList<>();
        for (EmpDocument paperFile : paperFiles) {
            FileVO file = new FileVO();
            file.setFileName(paperFile.getDocName());
            file.setFileId(paperFile.getDocId());
            file.setFileUrl(paperFile.getDocAnnexPath());
            paperDocList.add(file);
        }

        List<FileVO> recordDocList = new ArrayList<>();
        for (EmpDocument recordFile : recordFiles) {
            FileVO file = new FileVO();
            file.setFileName(recordFile.getDocName());
            file.setFileId(recordFile.getDocId());
            file.setFileUrl(recordFile.getDocAnnexPath());
            recordDocList.add(file);
        }
        List<FileVO> otherDocList = new ArrayList<>();
        for (EmpDocument otherFile : otherFiles) {
            FileVO file = new FileVO();
            file.setFileName(otherFile.getDocName());
            file.setFileId(otherFile.getDocId());
            file.setFileUrl(otherFile.getDocAnnexPath());
            otherDocList.add(file);
        }
        empDetail.setIdCardDocList(idCardDocList);
        empDetail.setPaperDocList(paperDocList);
        empDetail.setEducateDocList(educateDocList);
        empDetail.setRecordDocList(recordDocList);
        empDetail.setOtherDocList(otherDocList);

        Long empDeptId = empDetail.getEmpDeptId();
        if (empDeptId != null) {
            Department department = departmentMapper.selectDepartmentByDeptId(empDeptId);
            if (department != null) {
                empDetail.setEmpDeptName(department.getDeptName());
            }
        }
        return empDetail;
    }

    /**
     * 查询员工列表
     *
     * @param employee 员工
     * @return 员工
     */
    @Override
    public List<Employee> selectEmployeeList(EmpSearchVO employee) {

//        String keyword = employee.getKeyword();
//        String regex = "\\d{17}[0-9Xx]";
//        if (keyword != null) {
//            if (keyword.matches(regex)) {
//                employee.setEmpIdcard(keyword);
//            } else {
//                employee.setEmpName(keyword);
//            }
//        }
        return employeeMapper.selectEmployeeInfoList(employee);
    }


    @Override
    public List<EmployeeVO> selectEmployees(EmpSearchVO employee) {
        if (!CollectionUtils.isEmpty(employee.getNativePlace())) {
            employee.setNativePlaceStr(StringUtils.join(employee.getNativePlace(), ","));
        }
        if (!CollectionUtils.isEmpty(employee.getBirthPlace())) {
            employee.setBirthPlaceStr(StringUtils.join(employee.getBirthPlace(), ","));
        }
        List<Employee> employees = employeeMapper.selectEmployees(employee);

        List<Long> deptIds = employees.stream().map(Employee::getEmpDeptId).collect(Collectors.toList());
        Map<Long, Department> departmentMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            departmentMap = departmentMapper.findDeptInfosByDeptIds(deptIds).stream().
                    collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        }
        List<FamilyRelations> familyRelations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(employees)) {
            List<Long> empIds = employees.stream().map(Employee::getEmpId).collect(Collectors.toList());
            familyRelations = familyRelationsMapper.selectFamilyInfosByEmpIds(empIds);
        }
        Map<Long, List<FamilyRelations>> familyMap = familyRelations.stream().collect(Collectors.groupingBy(FamilyRelations::getFamEmpId));

        Map<Long, Department> finalDepartmentMap = departmentMap;
        return employees.stream().map(data -> {
            EmployeeVO employeeVO = new EmployeeVO();
            BeanUtils.copyProperties(data, employeeVO);
            employeeVO.setEmpIdcard(StringUtils.isNotBlank(data.getEmpIdcard()) ? Base64.desensitize(data.getEmpIdcard()) : "");
            employeeVO.setEmpFamilyInfos(familyMap.getOrDefault(data.getEmpId(), new ArrayList<>()).stream().sorted(
                    Comparator.comparing(FamilyRelations::getFamEmpSort)
            ).collect(Collectors.toList()));
            String nativePlace = data.getNativePlace();
            if (nativePlace != null) {
                String[] splitOne = nativePlace.split(",");
                List<Long> nativeList = new ArrayList<>();
                for (int i = 0; i < splitOne.length; i++) {
                    nativeList.add(Long.parseLong(splitOne[i]));
                }
                employeeVO.setNativePlace(nativeList);
            }
            String birthPlace = data.getBirthPlace();
            if (birthPlace != null) {
                String[] splitTwo = birthPlace.split(",");
                List<Long> birthList = new ArrayList<>();
                for (int i = 0; i < splitTwo.length; i++) {
                    birthList.add(Long.parseLong(splitTwo[i]));
                }
                employeeVO.setBirthPlace(birthList);
            }
            String retireReminder = data.getRetireReminder();
            if (retireReminder != null) {
                int i = Integer.parseInt(retireReminder);
                if (i <= 0) {
                    employeeVO.setRetireReminder("已退休");
                }
                if (i > 0) {
                    employeeVO.setRetireReminder("距离退休还剩" + i + "天");
                }
            }
            Long empDeptId = employeeVO.getEmpDeptId();
            if (empDeptId != null) {
                employeeVO.setEmpDeptName(finalDepartmentMap.getOrDefault(empDeptId, new Department()).getDeptName());
            }
            return employeeVO;
        }).collect(Collectors.toList());
    }


    @Override
    public List<EmpHistory> selectEmployeeHistoryList(EmpSearchVO employee) {
//        String keyword = employee.getKeyword();
//        String regex = "\\d{17}[0-9Xx]";
//        if (keyword != null) {
//            if (keyword.matches(regex)) {
//                employee.setEmpIdcard(keyword);
//            } else {
//                employee.setEmpName(keyword);
//            }
//        }
        List<EmpHistory> empHistories = empHistoryMapper.selectEmployeeInfoHistoryList(employee);
        for (EmpHistory empHistory : empHistories) {
            System.out.println(empHistory);
        }
        return empHistories;
    }

    @Override
    public List<EmpHistoryVO> selectEmployeeHistory(EmpSearchVO employee) {
        if (!CollectionUtils.isEmpty(employee.getNativePlace())) {
            employee.setNativePlaceStr(StringUtils.join(employee.getNativePlace(), ","));
        }
        if (!CollectionUtils.isEmpty(employee.getBirthPlace())) {
            employee.setBirthPlaceStr(StringUtils.join(employee.getBirthPlace(), ","));
        }
        List<EmpHistory> empHistories = empHistoryMapper.selectEmployeeInfoHistory(employee);

        List<FamilyRelationsHistory> familyRelations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(empHistories)) {
            List<Long> empIds = empHistories.stream().map(EmpHistory::getHistoryId).collect(Collectors.toList());
            familyRelations = familyRelationsHistoryMapper.selectFamilyInfosByEmpIds(empIds);
        }
        Map<Long, List<FamilyRelationsHistory>> familyMap = familyRelations.stream().collect(Collectors.groupingBy(FamilyRelationsHistory::getFamEmpId));

        return empHistories.stream().map(data -> {
            EmpHistoryVO employeeVO = new EmpHistoryVO();
            BeanUtils.copyProperties(data, employeeVO);
            employeeVO.setEmpIdcard(StringUtils.isNotBlank(data.getEmpIdcard()) ? Base64.desensitize(data.getEmpIdcard()) : "");
            employeeVO.setEmpFamilyInfos(familyMap.getOrDefault(data.getHistoryId(), new ArrayList<>()).stream().sorted(
                    Comparator.comparing(FamilyRelationsHistory::getFamEmpSort)
            ).collect(Collectors.toList()));

            String nativePlace = data.getNativePlace();
            if (nativePlace != null) {
                String[] splitOne = nativePlace.split(",");
                List<Long> nativeList = new ArrayList<>();
                for (int i = 0; i < splitOne.length; i++) {
                    nativeList.add(Long.parseLong(splitOne[i]));
                }
                employeeVO.setNativePlace(nativeList);
            }
            String birthPlace = data.getBirthPlace();
            if (birthPlace != null) {
                String[] splitTwo = birthPlace.split(",");
                List<Long> birthList = new ArrayList<>();
                for (int i = 0; i < splitTwo.length; i++) {
                    birthList.add(Long.parseLong(splitTwo[i]));
                }
                employeeVO.setBirthPlace(birthList);
            }
            String retireReminder = data.getRetireReminder();
            if (retireReminder != null) {
                int i = Integer.parseInt(retireReminder);
                if (i <= 0) {
                    employeeVO.setRetireReminder("已退休");
                }
                if (i > 0) {
                    employeeVO.setRetireReminder("距离退休还剩" + i + "天");
                }
            }
            return employeeVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Employee> selectEmployeeExportInfos(EmpExportVO empExport) {
        List<Employee> result = employeeMapper.findEmpExportInfos(empExport);
        Map<Long, Department> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        for (Employee employee : result) {
            if (employee.getEmpDeptId() != null) {
                employee.setEmpDeptName(deptMap.getOrDefault(employee.getEmpDeptId(), new Department()).getDeptName());
            }
        }
        return result;
    }

    @Override
    public List<EmpHistory> selectEmployeeHistoryExportInfos(EmpExportVO empExport) {

        return empHistoryMapper.selectEmployeeInfoHistoryExportInfos(empExport);
    }


    @Override
    public void exportDivide(HttpServletResponse response, EmpExportDivideVO empExportDivide) {
        EmpExportVO empExport = new EmpExportVO();
        BeanUtils.copyProperties(empExportDivide, empExport);
        List<Employee> empExportInfos = employeeMapper.findEmpExportInfos(empExport);
        if (CollectionUtils.isEmpty(empExportInfos)) {
            return;
        }
        Map<Long, Department> deptMap = departmentMapper.findAllDeptInfos().stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
        for (Employee employee : empExportInfos) {
            if (employee.getEmpDeptId() != null) {
                employee.setEmpDeptName(deptMap.getOrDefault(employee.getEmpDeptId(), new Department()).getDeptName());
            }
        }
        String fieldName = empExportDivide.getFieldName();
        EmpChangeDicVO empChangeDic = changeDicValue(empExportInfos, fieldName);
        empExportInfos = empChangeDic.getEmpExportInfos();
        String chineseName = empChangeDic.getChineseName();

        Map<String, List<Employee>> groupedData = empExportInfos.stream()
                .collect(Collectors.groupingBy(emp -> {
                    try {
                        // 使用反射获取指定属性的值
                        Object value = emp.getClass().getMethod("get" +
                                fieldName.substring(0, 1).toUpperCase() +
                                fieldName.substring(1)).invoke(emp);
                        return value != null ? value.toString() : "";
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 处理获取属性值异常
                        return null;
                    }
                }));

        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);

        for (String key : groupedData.keySet()) {
            List<Employee> employees = groupedData.get(key);
            for (Employee exportInfo : employees) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = Employee.class.getDeclaredField(fieldName);
                        ReflectionUtils.makeAccessible(field);
                        Object value = field.get(exportInfo);
                        for (SysDictData dictDataInfo : dictDataInfos) {
                            if (value.toString().equals(dictDataInfo.getDictLabel())) {
                                field.set(exportInfo, dictDataInfo.getDictValue());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        for (String key : groupedData.keySet()) {
            List<Employee> employees = groupedData.get(key);
            transFormDicForEmployee(employees);
        }

        File baseDir = new File("人员信息");
        for (String key : groupedData.keySet()) {
            String excelName = empExportDivide.getExcelName();
            if (excelName.contains("#")) {
                excelName = excelName.replace("#", key);
            }
            File file = org.apache.commons.io.FileUtils.getFile(baseDir + File.separator + key + File.separator + excelName + ".xlsx");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<Employee> employees = groupedData.get(key);
            employees.stream().filter(f -> StringUtils.isNotBlank(f.getEmpIdcard())).forEach(e -> e.setEmpIdcard(Base64.desensitize(e.getEmpIdcard())));

            ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
            util.init(employees, "员工信息", excelName, Excel.Type.EXPORT);
            util.writeSheet();
            Workbook wb = util.getWb();
            MultipartFile multipartFile = FileUtils.workbookToCommonsMultipartFile(wb, excelName + ".xlsx");
            if (multipartFile != null) {
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileUtils.writeCompressedFileToResponse(response, baseDir);
        FileUtils.deleteFolder(baseDir);
    }

    private void changeNativeAndBirthPlace(List<Employee> employees) {
        for (Employee employee : employees) {
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


    private void transFormDicForEmployee(List<Employee> list) {

        List<SysDictData> empStatusDicList = dictDataMapper.selectDictDataByType("emp_status");

        List<SysDictData> hireCompanyDicList = dictDataMapper.selectDictDataByType("hire_company");

        List<SysDictData> hrCompanyDicList = dictDataMapper.selectDictDataByType("emp_hr_company");

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
            for (SysDictData hrCompanyDic : hrCompanyDicList) {
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
        }
    }

    private EmpChangeDicVO changeDicValue(List<Employee> exportInfos, String fieldName) {
        Field[] declaredFields = Employee.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (Employee exportInfo : exportInfos) {
                try {
                    Field field = Employee.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpChangeDicVO empChangeDic = new EmpChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setEmpExportInfos(exportInfos);
                    return empChangeDic;
                }
            }
        }
        changeNativeAndBirthPlace(exportInfos);
        EmpChangeDicVO empChangeDic = new EmpChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setEmpExportInfos(exportInfos);
        return empChangeDic;
    }

    @Override
    public void exportHistoryDivide(HttpServletResponse response, EmpExportDivideVO empExportDivide) {
        EmpExportVO empExport = new EmpExportVO();
        BeanUtils.copyProperties(empExportDivide, empExport);
        List<EmpHistory> empExportInfos = empHistoryMapper.selectEmployeeInfoHistoryExportInfos(empExport);
        if (CollectionUtils.isEmpty(empExportInfos)) {
            return;
        }

        EmpHistoryChangeDicVO empChangeDic = changeDicValueForHistory(empExportInfos, empExportDivide.getFieldName());
        empExportInfos = empChangeDic.getEmpHistoryExportInfos();
        String chineseName = empChangeDic.getChineseName();
        String groupByProperty = empExportDivide.getFieldName();
        Map<String, List<EmpHistory>> groupedData = empExportInfos.stream()
                .collect(Collectors.groupingBy(emp -> {
                    try {
                        // 使用反射获取指定属性的值
                        Object value = emp.getClass().getMethod("get" +
                                groupByProperty.substring(0, 1).toUpperCase() +
                                groupByProperty.substring(1)).invoke(emp);
                        return value != null ? value.toString() : "";
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 处理获取属性值异常
                        return null;
                    }
                }));

        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        String fieldName = empExportDivide.getFieldName();
        for (String key : groupedData.keySet()) {
            List<EmpHistory> exportList = groupedData.get(key);
            for (EmpHistory exportInfo : exportList) {

                if (!CollectionUtils.isEmpty(dictDataInfos)) {
                    try {
                        Field field = EmpHistory.class.getDeclaredField(fieldName);
                        ReflectionUtils.makeAccessible(field);
                        Object value = field.get(exportInfo);
                        for (SysDictData dictDataInfo : dictDataInfos) {
                            if (value.toString().equals(dictDataInfo.getDictLabel())) {
                                field.set(exportInfo, dictDataInfo.getDictValue());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (String key : groupedData.keySet()) {
            List<EmpHistory> empHistories = groupedData.get(key);
            transFormDicForEmpHistory(empHistories);
        }

        File baseDir = new File("人员历史信息");
        for (String key : groupedData.keySet()) {
            String excelName = empExportDivide.getExcelName();
            if (excelName.contains("#")) {
                excelName = excelName.replace("#", key);
            }
            File file = org.apache.commons.io.FileUtils.getFile(baseDir + File.separator + key + File.separator + excelName + ".xlsx");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<EmpHistory> empHistories = groupedData.get(key);

            empHistories.stream().filter(f -> StringUtils.isNotBlank(f.getEmpIdcard())).forEach(e -> e.setEmpIdcard(Base64.desensitize(e.getEmpIdcard())));
            ExcelUtil<EmpHistory> util = new ExcelUtil<EmpHistory>(EmpHistory.class);
            util.init(empHistories, "员工历史信息", excelName, Excel.Type.EXPORT);
            util.writeSheet();
            Workbook wb = util.getWb();
            MultipartFile multipartFile = FileUtils.workbookToCommonsMultipartFile(wb, excelName + ".xlsx");
            if (multipartFile != null) {
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileUtils.writeCompressedFileToResponse(response, baseDir);
        FileUtils.deleteFolder(baseDir);
    }

    private void changeNativeAndBirthPlaceForHistory(List<EmpHistory> empHistories) {

        for (EmpHistory empHistory : empHistories) {
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


    private void transFormDicForEmpHistory(List<EmpHistory> list) {

        List<SysDictData> empStatusDicList = dictDataMapper.selectDictDataByType("emp_status");

        List<SysDictData> hireCompanyDicList = dictDataMapper.selectDictDataByType("hire_company");

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
        }

    }


    private EmpHistoryChangeDicVO changeDicValueForHistory(List<EmpHistory> empExportInfos, String fieldName) {
        Field[] declaredFields = EmpHistory.class.getDeclaredFields();
        String chineseName = null;
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                chineseName = field.getAnnotation(Excel.class).name();
            }
        }
        List<SysDictData> dictDataInfos = dictDataMapper.findDictDataByDicTypeName(chineseName);
        if (!CollectionUtils.isEmpty(dictDataInfos)) {
            for (EmpHistory exportInfo : empExportInfos) {
                try {
                    Field field = EmpHistory.class.getDeclaredField(fieldName);
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(exportInfo);
                    for (SysDictData dictDataInfo : dictDataInfos) {
                        if (value.toString().equals(dictDataInfo.getDictValue())) {
                            field.set(exportInfo, dictDataInfo.getDictLabel());
                        }
                    }
                } catch (Exception e) {
                    EmpHistoryChangeDicVO empChangeDic = new EmpHistoryChangeDicVO();
                    empChangeDic.setChineseName(chineseName);
                    empChangeDic.setEmpHistoryExportInfos(empExportInfos);
                    return empChangeDic;
                }
            }
        }
        changeNativeAndBirthPlaceForHistory(empExportInfos);
        EmpHistoryChangeDicVO empChangeDic = new EmpHistoryChangeDicVO();
        empChangeDic.setChineseName(chineseName);
        empChangeDic.setEmpHistoryExportInfos(empExportInfos);
        return empChangeDic;

    }


    /**
     * 新增员工
     *
     * @param userId
     * @param flag   true 页面新增  false excel批量添加
     * @param empAdd 员工
     * @return 结果
     */
    @Override
    public Long insertEmployee(EmpAddVO empAdd, Long userId, String userName, Boolean flag) {

        Employee employee;
        Date now = new Date();
        if (empAdd.getEmpId() == null) {
            //查询借工表中的数据，是否员工姓名相同
            if (StringUtils.isNotEmpty(empAdd.getEmpName())) {
                LoanWorker loanWorker = loanWorkerMapper.findInfoByName(empAdd.getEmpName());
                if (loanWorker != null) {
                    throw new ServiceException("该员工姓名在借工数据中已存在");
                }
            }
            employee = new Employee();
            BeanUtils.copyProperties(empAdd, employee);
            if (!CollectionUtils.isEmpty(empAdd.getNativePlace())) {
                employee.setNativePlace(StringUtils.join(empAdd.getNativePlace(), ","));
            }
            if (!CollectionUtils.isEmpty(empAdd.getBirthPlace())) {
                employee.setBirthPlace(StringUtils.join(empAdd.getBirthPlace(), ","));
            }
            String empIdcard = empAdd.getEmpIdcard();
            if (StringUtils.isNotEmpty(empIdcard)) {
                int age = DateUtils.calculateAge(empIdcard);
                char sexChar = empIdcard.charAt(empIdcard.length() - 2);
                int sexNum = Integer.parseInt(String.valueOf(sexChar));
                if (sexNum % 2 == 0) {
                    employee.setEmpGender(EmpGenderEnum.FEMALE.getKey());
                } else {
                    employee.setEmpGender(EmpGenderEnum.MALE.getKey());
                }
                employee.setEmpAge(age);
            }
            Department dept = null;
            if (empAdd.getEmpDeptId() != null) {
                dept = departmentMapper.selectDepartmentByDeptId(empAdd.getEmpDeptId());
                employee.setEmpDeptName(dept.getDeptName());
            }

            String empPosition = employee.getEmpPosition();
            String empPositionLevel = employee.getEmpPositionLevel();
            String empPositionCategory = employee.getEmpPositionCategory();
            PositionInfo positionInfo = positionInfoMapper.findInfoByReferenceParams(empPosition, empPositionLevel,
                    empPositionCategory);
            if (positionInfo != null) {
                String salaryHigh = positionInfo.getSalaryHigh();
                String salaryLow = positionInfo.getSalaryLow();
                if (salaryHigh != null) {
                    employee.setSalaryLevelMax(salaryHigh);
                }
                if (salaryLow != null) {
                    employee.setSalaryLevelMin(salaryLow);
                }
                String empSalaryLevel = employee.getEmpSalaryLevel();
                if (empSalaryLevel != null) {
                    if (StringUtils.isNotEmpty(salaryLow)) {
                        if (Integer.parseInt(empSalaryLevel) < Integer.parseInt(salaryLow)) {
                            employee.setEmpSalaryLevel(salaryLow);
                        }
                    }
                    if (StringUtils.isNotEmpty(salaryHigh)) {
                        if (Integer.parseInt(empSalaryLevel) > Integer.parseInt(salaryHigh)) {
                            employee.setEmpSalaryLevel(salaryHigh);
                        }
                    }
                }
            }

            // 恢复删除员工的数据
            Employee emp = employeeMapper.findDeletedEmpInfoByEmpName(empAdd.getEmpName());
            if (emp != null) {
                try {
                    mergeObjects(employee, emp);
                    employee.setEmpUpdateBy(userId);
                    employee.setEmpUpdateTime(now);
                    employeeMapper.updateRecoveryEmployee(employee);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return employee.getEmpId();
            }

            Employee existEmployee = employeeMapper.findInfoByHistoryEmpName(empAdd.getEmpName());
            changeEmpStatus(empAdd.getEmpHiredate(), employee, empIdcard);

            if (!flag) {
                if (existEmployee == null) {
                    employee.setEmpCreateBy(userId);
                    employee.setEmpCreateTime(now);
                    employee.setEmpUpdateTime(now);
                    // excel批量导入默认人员类型为外包
                    if (employee.getEmpCategory() == null) {
                        employee.setEmpCategory(EmpCategoryEnum.ONE.getKey());
                    }
                    if (employee.getEmpStatus() == null) {
                        employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
                    }
                    employee.setDisabled(false);
                    employeeMapper.insertEmployee(employee);
                    //插入本单位履历信息 excel新增员工数据
                    if (employee.getEmpStatus() != null && EmpStatusEnum.ON_POSITION.getKey().equals(employee.getEmpStatus())) {
                        if (!(employee.getEmpDeptId() == null && employee.getEmpEmployingUnits() == null && employee.getEmpPosition() == null)) {
                            EmpResume empResume = new EmpResume();
                            empResume.setResuEmpId(employee.getEmpId());
                            empResume.setResuEmpName(employee.getEmpName());
                            empResume.setResuBeginDate(now);
                            if (employee.getEmpDeptId() != null) {
                                empResume.setResuDept(employee.getEmpDeptId().toString());
                            }
                            empResume.setResuType(ResumeTypeEnum.THREE.getKey());
                            empResume.setResuPosition(employee.getEmpPosition());
                            empResume.setCreateTime(now);
                            empResume.setCreateBy(userName);
                            empResume.setResuUpdateTime(now);
                            empResume.setDisabled(false);
                            empResumeMapper.insertEmpResume(empResume);
                        }
                    }
                } else {
                    //插入岗位变动历史
                    writePositionChangeInfo(empAdd, existEmployee, now);
                    // 插入薪酬变化历史
                    writeSalaryChangeInfo(empAdd, userName, existEmployee, now, empIdcard);
                    //插入本单位履历信息
                    if (EmpStatusEnum.ON_POSITION.getKey().equals(employee.getEmpStatus())) {
                        if ((empAdd.getEmpDeptId() != null && !empAdd.getEmpDeptId().equals(existEmployee.getEmpDeptId())) ||
                                (empAdd.getEmpPosition() != null && !empAdd.getEmpPosition().equals(existEmployee.getEmpPosition()))) {
                            Long empId = existEmployee.getEmpId();
                            List<EmpResume> empResumes = empResumeMapper.findInfosByEmpIds(Collections.singletonList(empId));
                            EmpResume existResume = empResumes.stream().filter(s -> s.getResuEndDate() == null).
                                    max(Comparator.comparing(EmpResume::getCreateTime)).orElse(null);
                            if (existResume != null) {
                                existResume.setResuEndDate(now);
                                existResume.setUpdateTime(now);
                                existResume.setUpdateBy(userName);
                                empResumeMapper.updateEmpResume(existResume);
                            }
                            EmpResume empResume = new EmpResume();
                            empResume.setResuEmpId(empId);
                            empResume.setResuEmpName(empAdd.getEmpName());
                            empResume.setResuBeginDate(now);
                            if (empAdd.getEmpDeptId() != null) {
                                empResume.setResuDept(empAdd.getEmpDeptId().toString());
                            }
                            empResume.setResuType(ResumeTypeEnum.THREE.getKey());
                            empResume.setResuPosition(empAdd.getEmpPosition());
                            empResume.setCreateTime(now);
                            empResume.setCreateBy(userName);
                            empResume.setResuUpdateTime(now);
                            empResume.setDisabled(false);
                            empResumeMapper.insertEmpResume(empResume);
                        }
                    }

                    // excel批量导入默认人员类型为外包
                    if (employee.getEmpCategory() == null) {
                        employee.setEmpCategory(EmpCategoryEnum.ONE.getKey());
                    }
                    if (employee.getEmpStatus() == null) {
                        employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
                    }
                    Long empId = existEmployee.getEmpId();
                    BeanUtils.copyProperties(employee, existEmployee);
                    existEmployee.setEmpSalaryLevel(empAdd.getEmpSalaryLevel());
                    // 若有岗位变动情况（薪酬上下限已在岗位变动的方法中重新设置），重新设置薪酬上下限
                    existEmployee.setSalaryLevelMin(empAdd.getSalaryLevelMin());
                    existEmployee.setSalaryLevelMax(empAdd.getSalaryLevelMax());
                    existEmployee.setEmpId(empId);
                    existEmployee.setEmpUpdateBy(userId);
                    existEmployee.setEmpUpdateTime(now);
                    employeeMapper.updateEmployee(existEmployee);
                }
            } else {
                Employee empIdCard = employeeMapper.findInfoByEmpIdCard(empIdcard);
                if (empIdCard != null) {
                    throw new ServiceException("该员工身份证信息已存在");
                }
                if (existEmployee != null) {
                    throw new ServiceException("该员工姓名已存在");
                } else {
                    employee.setEmpCreateBy(userId);
                    employee.setEmpCreateTime(now);
                    employee.setEmpUpdateTime(now);
                    employee.setDisabled(false);
                    employeeMapper.insertEmployee(employee);
                }
            }
            //插入本单位履历信息 页面新增员工数据
            if (employee.getEmpStatus() != null && EmpStatusEnum.ON_POSITION.getKey().equals(employee.getEmpStatus()) && flag) {
                if (!(employee.getEmpDeptId() == null && employee.getEmpEmployingUnits() == null && employee.getEmpPosition() == null)) {
                    EmpResume empResume = new EmpResume();
                    empResume.setResuEmpId(employee.getEmpId());
                    empResume.setResuEmpName(employee.getEmpName());
                    empResume.setResuBeginDate(now);
                    if (employee.getEmpDeptId() != null) {
                        empResume.setResuDept(employee.getEmpDeptId().toString());
                    }
                    empResume.setResuType(ResumeTypeEnum.THREE.getKey());
                    empResume.setResuPosition(employee.getEmpPosition());
                    empResume.setCreateTime(now);
                    empResume.setCreateBy(userName);
                    empResume.setResuUpdateTime(now);
                    empResume.setDisabled(false);
                    empResumeMapper.insertEmpResume(empResume);
                }
            }
        } else {
            employee = employeeMapper.selectEmployeeByEmpId(empAdd.getEmpId());
            if (employee == null) {
                throw new ServiceException("该员工不存在");
            }
            String empIdcard = empAdd.getEmpIdcard();
            if (empIdcard != null) {
                Employee existEmp = employeeMapper.findInfoByEmpIdCard(empIdcard);
                if (existEmp != null) {
                    Long existEmpId = existEmp.getEmpId();
                    if (!empAdd.getEmpId().equals(existEmpId)) {
                        throw new ServiceException("该身份证号已存在");
                    }
                }
            }
            checkEmpSalaryLevelForUpdate(empAdd);
            //插入岗位变动历史
            writePositionChangeInfo(empAdd, employee, now);
            // 插入薪酬变化历史
            writeSalaryChangeInfo(empAdd, userName, employee, now, empIdcard);

            Long newDeptId = empAdd.getEmpDeptId();
            String newPosition = empAdd.getEmpPosition();
            Long existDeptId = employee.getEmpDeptId();
            String existPosition = employee.getEmpPosition();
            // 记录离职时间
            if (!Objects.equals(employee.getEmpStatus(), empAdd.getEmpStatus()) &&
                    (EmpStatusEnum.FIRE.getKey().equals(empAdd.getEmpStatus()) || EmpStatusEnum.RESIGN.getKey().equals(empAdd.getEmpStatus()))) {
                employee.setEmpStatusUpdateTime(now);
            }

            BeanUtils.copyProperties(empAdd, employee);

            if (StringUtils.isNotEmpty(empIdcard)) {
                int age = DateUtils.calculateAge(empIdcard);
                char sexChar = empIdcard.charAt(empIdcard.length() - 2);
                int sexNum = Integer.parseInt(String.valueOf(sexChar));
                if (sexNum % 2 == 0) {
                    employee.setEmpGender(EmpGenderEnum.FEMALE.getKey());
                } else {
                    employee.setEmpGender(EmpGenderEnum.MALE.getKey());
                }
                employee.setEmpAge(age);
            }
            //更改员工状态不包括辞职和辞退
            changeEmpStatus(empAdd.getEmpHiredate(), employee, empIdcard);
            if (!Objects.equals(employee.getEmpStatus(), empAdd.getEmpStatus())) {
                employee.setEmpStatusUpdateTime(now);
            }

            //插入本单位履历信息
            if (EmpStatusEnum.ON_POSITION.getKey().equals(employee.getEmpStatus())) {
                if ((newDeptId != null && !newDeptId.equals(existDeptId)) ||
                        (newPosition != null && !newPosition.equals(existPosition))) {
                    Long empId = empAdd.getEmpId();
                    List<EmpResume> empResumes = empResumeMapper.findInfosByEmpIds(Collections.singletonList(empId));
                    EmpResume existResume = empResumes.stream().filter(s -> s.getResuEndDate() == null).
                            max(Comparator.comparing(EmpResume::getCreateTime)).orElse(null);
                    if (existResume != null) {
                        existResume.setResuEndDate(now);
                        existResume.setUpdateTime(now);
                        existResume.setUpdateBy(userName);
                        empResumeMapper.updateEmpResume(existResume);
                    }
                    EmpResume empResume = new EmpResume();
                    empResume.setResuEmpId(empAdd.getEmpId());
                    empResume.setResuEmpName(empAdd.getEmpName());
                    empResume.setResuBeginDate(now);
                    if (empAdd.getEmpDeptId() != null) {
                        empResume.setResuDept(empAdd.getEmpDeptId().toString());
                    }
                    empResume.setResuType(ResumeTypeEnum.THREE.getKey());
                    empResume.setResuPosition(empAdd.getEmpPosition());
                    empResume.setCreateTime(now);
                    empResume.setCreateBy(userName);
                    empResume.setResuUpdateTime(now);
                    empResume.setDisabled(false);
                    empResumeMapper.insertEmpResume(empResume);
                }
            }


            if (!CollectionUtils.isEmpty(empAdd.getNativePlace())) {
                employee.setNativePlace(StringUtils.join(empAdd.getNativePlace(), ","));
            }
            if (!CollectionUtils.isEmpty(empAdd.getBirthPlace())) {
                employee.setBirthPlace(StringUtils.join(empAdd.getBirthPlace(), ","));
            }
            if (empAdd.getEmpDeptId() != null) {
                String deptName = departmentMapper.selectDepartmentByDeptId(empAdd.getEmpDeptId()).getDeptName();
                employee.setEmpDeptName(deptName);
            }
            employee.setEmpUpdateBy(userId);
            employee.setEmpUpdateTime(now);

            employeeMapper.updateEmployee(employee);
        }

        Long empId = employee.getEmpId();
        // 添加员工亲属信息
        familyRelationsMapper.removeInfoByEmpId(empId);
        List<EmpFamilyVO> empFamilies = empAdd.getEmpFamilyInfos();
        if (!CollectionUtils.isEmpty(empFamilies)) {
            List<FamilyRelations> familyRelations = new ArrayList<>();
            for (EmpFamilyVO empFamily : empFamilies) {
                FamilyRelations familyRelation = new FamilyRelations();
                BeanUtils.copyProperties(empFamily, familyRelation);
                familyRelation.setFamEmpId(empId);
                familyRelation.setFamEmpName(employee.getEmpName());
                if (empFamily.getFamAge() != null && !"".equals(empFamily.getFamAge())) {
                    String regex = "\\d+";
                    if (empFamily.getFamAge().matches(regex)) {
                        familyRelation.setFamAge(Long.parseLong(empFamily.getFamAge()));
                    }

                }
                familyRelations.add(familyRelation);
            }
            familyRelationsMapper.batchInsertFamilyInfos(familyRelations);
        }
        //添加员工文件信息
        List<String> docTypes = new ArrayList<>();
        docTypes.add(EmpDocTypeEnum.ONE.getKey());
        docTypes.add(EmpDocTypeEnum.TWO.getKey());
        docTypes.add(EmpDocTypeEnum.THREE.getKey());
        docTypes.add(EmpDocTypeEnum.FOUR.getKey());
        docTypes.add(EmpDocTypeEnum.THIRTY_FOUR.getKey());
        docTypes.add(EmpDocTypeEnum.THIRTY_FIVE.getKey());
        List<EmpDocument> existDocList = empDocumentMapper.findDocInfosByEmpIdAndTypes(empId, docTypes);
        List<Long> removeIds = new ArrayList<>();
        List<Long> addIds = new ArrayList<>();

        if (empAdd.getAvatarDocId() != null) {
            Long avatarDocId = empAdd.getAvatarDocId();
            EmpDocument empDocument = empDocumentMapper.selectEmpDocumentByDocId(avatarDocId);
            employee.setEmpAvatarUrl(empDocument.getDocAnnexPath());
            employeeMapper.updateEmpAvatarUrl(employee);
            EmpDocument document = existDocList.stream().filter(s -> EmpDocTypeEnum.ONE.getKey().equals(s.getDocType())).findFirst().orElse(null);
            if (document == null) {
                addIds.add(avatarDocId);
            } else {
                if (!document.getDocId().equals(avatarDocId)) {
                    removeIds.add(document.getDocId());
                    addIds.add(avatarDocId);
                }
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(empId),
                    Arrays.asList(EmpDocTypeEnum.ONE.getKey()));
        }
        if (!CollectionUtils.isEmpty(empAdd.getIdCardDocList())) {
            List<Long> idCardDocList = empAdd.getIdCardDocList();
            List<Long> existIdDocList = existDocList.stream().filter(s -> EmpDocTypeEnum.TWO.getKey().equals(s.getDocType())).
                    map(EmpDocument::getDocId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(existIdDocList)) {
                addIds.addAll(empAdd.getIdCardDocList());
            } else {
                List<Long> addIdDocList = new ArrayList<>(idCardDocList);
                List<Long> removeIdDocList = new ArrayList<>(existIdDocList);
                addIdDocList.removeAll(existIdDocList);
                removeIdDocList.removeAll(idCardDocList);
                if (!CollectionUtils.isEmpty(addIdDocList)) {
                    addIds.addAll(addIdDocList);
                }
                if (!CollectionUtils.isEmpty(removeIdDocList)) {
                    removeIds.addAll(removeIdDocList);
                }
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(empId), Collections.singletonList(EmpDocTypeEnum.TWO.getKey()));
        }
        if (!CollectionUtils.isEmpty(empAdd.getEducateDocList())) {
            List<Long> educateDocList = empAdd.getEducateDocList();
            List<Long> existEduDocList = existDocList.stream().filter(s -> EmpDocTypeEnum.FOUR.getKey().
                    equals(s.getDocType())).map(EmpDocument::getDocId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(existEduDocList)) {
                addIds.addAll(educateDocList);
            } else {
                List<Long> addEduDocList = new ArrayList<>(educateDocList);
                List<Long> removeEduDocList = new ArrayList<>(existEduDocList);
                addEduDocList.removeAll(existEduDocList);
                removeEduDocList.removeAll(educateDocList);
                if (!CollectionUtils.isEmpty(addEduDocList)) {
                    addIds.addAll(addEduDocList);
                }
                if (!CollectionUtils.isEmpty(removeEduDocList)) {
                    removeIds.addAll(removeEduDocList);
                }
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(empId), Collections.singletonList(EmpDocTypeEnum.FOUR.getKey()));
        }
        if (!CollectionUtils.isEmpty(empAdd.getPaperDocList())) {
            List<Long> paperDocList = empAdd.getPaperDocList();
            List<Long> existPaperDocList = existDocList.stream().filter(s -> EmpDocTypeEnum.THREE.getKey().
                    equals(s.getDocType())).map(EmpDocument::getDocId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(existPaperDocList)) {
                addIds.addAll(paperDocList);
            } else {
                List<Long> addPaperDocList = new ArrayList<>(paperDocList);
                List<Long> removePaperDocList = new ArrayList<>(existPaperDocList);
                addPaperDocList.removeAll(existPaperDocList);
                removePaperDocList.removeAll(paperDocList);
                if (!CollectionUtils.isEmpty(addPaperDocList)) {
                    addIds.addAll(addPaperDocList);
                }
                if (!CollectionUtils.isEmpty(removePaperDocList)) {
                    removeIds.addAll(removePaperDocList);
                }
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(empId), Collections.singletonList(EmpDocTypeEnum.THREE.getKey()));
        }

        if (!CollectionUtils.isEmpty(empAdd.getRecordDocList())) {
            List<Long> recordDocList = empAdd.getRecordDocList();
            List<Long> existRecordDocList = existDocList.stream().filter(s -> EmpDocTypeEnum.THIRTY_FOUR.getKey().
                    equals(s.getDocType())).map(EmpDocument::getDocId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(existRecordDocList)) {
                addIds.addAll(recordDocList);
            } else {
                List<Long> addRecordDocList = new ArrayList<>(recordDocList);
                List<Long> removeRecordDocList = new ArrayList<>(existRecordDocList);
                addRecordDocList.removeAll(existRecordDocList);
                removeRecordDocList.removeAll(recordDocList);
                if (!CollectionUtils.isEmpty(addRecordDocList)) {
                    addIds.addAll(addRecordDocList);
                }
                if (!CollectionUtils.isEmpty(removeRecordDocList)) {
                    removeIds.addAll(removeRecordDocList);
                }
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(empId), Collections.singletonList(EmpDocTypeEnum.THIRTY_FOUR.getKey()));
        }

        if (!CollectionUtils.isEmpty(empAdd.getOtherDocList())) {
            List<Long> otherDocList = empAdd.getOtherDocList();
            List<Long> existOtherDocList = existDocList.stream().filter(s -> EmpDocTypeEnum.THIRTY_FIVE.getKey().
                    equals(s.getDocType())).map(EmpDocument::getDocId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(existOtherDocList)) {
                addIds.addAll(otherDocList);
            } else {
                List<Long> addOtherDocList = new ArrayList<>(otherDocList);
                List<Long> removeOtherDocList = new ArrayList<>(existOtherDocList);
                addOtherDocList.removeAll(existOtherDocList);
                removeOtherDocList.removeAll(otherDocList);
                if (!CollectionUtils.isEmpty(addOtherDocList)) {
                    addIds.addAll(addOtherDocList);
                }
                if (!CollectionUtils.isEmpty(removeOtherDocList)) {
                    removeIds.addAll(removeOtherDocList);
                }
            }
        } else {
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(empId), Collections.singletonList(EmpDocTypeEnum.THIRTY_FIVE.getKey()));
        }


        if (!CollectionUtils.isEmpty(addIds)) {
            empDocumentMapper.bindEmpInfos(empId, addIds);
        }
        if (!CollectionUtils.isEmpty(removeIds)) {
            empDocumentMapper.removeInfoByDocIdList(removeIds);
        }
        return employee.getEmpId();
    }

    public void mergeObjects(Object obj1, Object obj2) throws IllegalAccessException {
        Class<?> clazz = obj1.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value1 = field.get(obj1);
            Object value2 = field.get(obj2);
            if (value1 == null && value2 != null) {
                field.set(obj1, value2);
            }
        }
    }

    private void writeSalaryChangeInfo(EmpAddVO empAdd, String userName, Employee employee, Date now, String empIdcard) {
        SalaryHistory salaryHistory = new SalaryHistory();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String yearMonth = dateFormat.format(now);
        Long empId = employee.getEmpId();
        salaryHistory.setHisEmpId(empId);
        salaryHistory.setHisEmpIdcard(empIdcard);
        salaryHistory.setHisEmpName(employee.getEmpName());
        salaryHistory.setCreateBy(userName);
        salaryHistory.setCreateTime(now);
        salaryHistory.setHisYearMonth(yearMonth);
        if (empAdd.getEmpSalaryLevel() != null && !empAdd.getEmpSalaryLevel().equals(employee.getEmpSalaryLevel())) {
            if (empAdd.getEmpPosition() != null && empAdd.getEmpPosition().equals(employee.getEmpPosition()) && (empAdd.getEmpPositionLevel() != null
                    && empAdd.getEmpPositionLevel().equals(employee.getEmpPositionLevel()))) {
                salaryHistoryMapper.removeInfosByYearmonthAndEmpId(yearMonth, empId);
                PositionInfo positionInfo = positionInfoMapper.findInfoByReferenceParams(empAdd.getEmpPosition(),
                        empAdd.getEmpPositionLevel(), empAdd.getEmpPositionCategory());
                if (positionInfo != null) {
                    String salaryLow = positionInfo.getSalaryLow();
                    String salaryHigh = positionInfo.getSalaryHigh();
                    String nextLevel = empAdd.getEmpSalaryLevel();
                    if (Integer.parseInt(nextLevel) < Integer.parseInt(salaryLow)) {
                        nextLevel = salaryLow;
                    }
                    if (Integer.parseInt(nextLevel) > Integer.parseInt(salaryHigh)) {
                        nextLevel = salaryHigh;
                    }
                    empAdd.setEmpSalaryLevel(nextLevel);
                }
                salaryHistory.setHisIspostChange("1");
                String nextLevel = empAdd.getEmpSalaryLevel();
                String previousLevel = employee.getEmpSalaryLevel();
                salaryHistory.setHisPreviousLevel(previousLevel);
                salaryHistory.setHisNextLevel(nextLevel);
                if (previousLevel == null || Integer.parseInt(nextLevel) > Integer.parseInt(previousLevel)) {
                    salaryHistory.setHisChangeType(SalaryChangeTypeEnum.ONE.getKey());
                    salaryHistoryMapper.insertSalaryHistory(salaryHistory);
                }
                if (previousLevel != null && Integer.parseInt(nextLevel) < Integer.parseInt(previousLevel)) {
                    salaryHistory.setHisChangeType(SalaryChangeTypeEnum.TWO.getKey());
                    salaryHistoryMapper.insertSalaryHistory(salaryHistory);
                }
            }
        }
        if (empAdd.getEmpSalaryLevel() != null && empAdd.getEmpSalaryLevel().equals(employee.getEmpSalaryLevel())) {
            if (empAdd.getEmpPosition() != null && empAdd.getEmpPosition().equals(employee.getEmpPosition()) && (empAdd.getEmpPositionLevel() != null
                    && !empAdd.getEmpPositionLevel().equals(employee.getEmpPositionLevel()))) {
                salaryHistoryMapper.removeInfosByYearmonthAndEmpId(yearMonth, empId);
                String oldPositionLevel = employee.getEmpPositionLevel();
                String newPositionLevel = empAdd.getEmpPositionLevel();
                String newPositionCategory = empAdd.getEmpPositionCategory();
                salaryHistory.setHisIspostChange("1");
                List<SysDictData> empPositionLevelInfos = dictDataMapper.selectDictDataByType("emp_position_level");
                Long oldPositionLevelSort = empPositionLevelInfos.stream().filter(s -> oldPositionLevel.equals(s.getDictValue())).findFirst().get().getDictSort();
                Long newPositionLevelSort = empPositionLevelInfos.stream().filter(s -> newPositionLevel.equals(s.getDictValue())).findFirst().get().getDictSort();
                String empPosition = employee.getEmpPosition();
                PositionInfo positionInfo = positionInfoMapper.findInfoByReferenceParams(empPosition, newPositionLevel, newPositionCategory);
                if (oldPositionLevelSort != null && newPositionLevelSort != null) {
                    if (oldPositionLevelSort < newPositionLevelSort) {
                        salaryHistory.setHisChangeType(SalaryChangeTypeEnum.ONE.getKey());
                        String empSalaryLevel = empAdd.getEmpSalaryLevel();
                        if (positionInfo != null) {
                            String salaryLow = positionInfo.getSalaryLow();
                            String salaryHigh = positionInfo.getSalaryHigh();
                            String nextLevel = (Integer.parseInt(empSalaryLevel) + 1) + "";
                            if (Integer.parseInt(nextLevel) < Integer.parseInt(salaryLow)) {
                                nextLevel = salaryLow;
                            }
                            if (Integer.parseInt(nextLevel) > Integer.parseInt(salaryHigh)) {
                                nextLevel = salaryHigh;
                            }
                            salaryHistory.setHisPreviousLevel(empSalaryLevel);
                            salaryHistory.setHisNextLevel(nextLevel);
                            salaryHistory.setHisIspostChange("0");
                            salaryHistoryMapper.insertSalaryHistory(salaryHistory);
                            empAdd.setEmpSalaryLevel(nextLevel);
                        }
                    } else if (oldPositionLevelSort > newPositionLevelSort) {
                        salaryHistory.setHisChangeType(SalaryChangeTypeEnum.TWO.getKey());
                        String empSalaryLevel = empAdd.getEmpSalaryLevel();
                        if (positionInfo != null) {
                            String salaryLow = positionInfo.getSalaryLow();
                            String salaryHigh = positionInfo.getSalaryHigh();
                            String nextLevel = (Integer.parseInt(empSalaryLevel) - 1) + "";
                            if (Integer.parseInt(nextLevel) < Integer.parseInt(salaryLow)) {
                                nextLevel = salaryLow;
                            }
                            if (Integer.parseInt(nextLevel) > Integer.parseInt(salaryHigh)) {
                                nextLevel = salaryHigh;
                            }
                            salaryHistory.setHisPreviousLevel(empSalaryLevel);
                            salaryHistory.setHisNextLevel(nextLevel);
                            salaryHistory.setHisIspostChange("0");
                            salaryHistoryMapper.insertSalaryHistory(salaryHistory);
                            empAdd.setEmpSalaryLevel(nextLevel);
                        }
                    }
                }
            }
        }
    }

    private void writePositionChangeInfo(EmpAddVO empAdd, Employee employee, Date now) {
        if ((empAdd.getEmpPosition() != null && !empAdd.getEmpPosition().equals(employee.getEmpPosition())) ||
                (empAdd.getEmpPositionLevel() != null && !empAdd.getEmpPositionLevel().equals(employee.getEmpPositionLevel()))) {
            postHistoryMapper.removeCurrentDayInfo(employee.getEmpId());
            PostHistory postHistory = new PostHistory();
            postHistory.setPhEmpId(employee.getEmpId());
            postHistory.setPhEmpName(employee.getEmpName());
            postHistory.setPhOriginPostName(employee.getEmpPosition());
            postHistory.setPhDestinPostName(empAdd.getEmpPosition());
            String existPositionLevel = employee.getEmpPositionLevel();
            String newPositionLevel = empAdd.getEmpPositionLevel();
            postHistory.setPhOriginPostLevel(existPositionLevel);
            postHistory.setPhDestinPostLevel(newPositionLevel);
            postHistory.setInsertType(1L);
            if (empAdd.getEmpPosition() != null && !empAdd.getEmpPosition().equals(employee.getEmpPosition())) {
                String newLevel = empAdd.getEmpPositionLevel();
                String oldLevel = employee.getEmpPositionLevel();
                PositionInfo newPositionInfo = positionInfoMapper.findInfoByReferenceParams(empAdd.getEmpPosition(),
                        empAdd.getEmpPositionLevel(), empAdd.getEmpPositionCategory());
                // 设置薪级上下限
                if (newPositionInfo != null) {
                    empAdd.setSalaryLevelMax(newPositionInfo.getSalaryHigh());
                    empAdd.setSalaryLevelMin(newPositionInfo.getSalaryLow());
                }
                PositionInfo oldPositionInfo = positionInfoMapper.findInfoByReferenceParams(employee.getEmpPosition(),
                        employee.getEmpPositionLevel(), employee.getEmpPositionCategory());

                if (oldPositionInfo == null && newPositionInfo != null) {
                    postHistory.setPhAdjustType(PostChangeTypeEnum.ONE.getKey());
                }
                if (oldPositionInfo == null && newPositionInfo == null) {
                    postHistory.setPhAdjustType(PostChangeTypeEnum.TWO.getKey());
                }
                if (newPositionInfo == null && oldPositionInfo != null) {
                    postHistory.setPhAdjustType(PostChangeTypeEnum.THREE.getKey());
                }
                if (oldPositionInfo != null && newPositionInfo != null) {
                    Integer oldSort = oldPositionInfo.getPositionSort();
                    Integer newSort = newPositionInfo.getPositionSort();
                    if (oldSort == null && newSort != null) {
                        postHistory.setPhAdjustType(PostChangeTypeEnum.ONE.getKey());
                    }
                    if (oldSort == null && newSort == null) {
                        postHistory.setPhAdjustType(PostChangeTypeEnum.TWO.getKey());
                    }
                    if (oldSort != null && newSort == null) {
                        postHistory.setPhAdjustType(PostChangeTypeEnum.THREE.getKey());
                    }
                    if (oldSort != null && newSort != null) {
                        if (oldSort < newSort) {
                            postHistory.setPhAdjustType(PostChangeTypeEnum.ONE.getKey());
                        } else if (oldSort.equals(newSort)) {
                            List<SysDictData> empPositionLevelInfos = dictDataMapper.selectDictDataByType("emp_position_level");
                            Long oldLevelSort = empPositionLevelInfos.stream().filter(s -> oldLevel.equals(s.getDictValue())).findFirst().get().getDictSort();
                            Long newLevelSort = empPositionLevelInfos.stream().filter(s -> newLevel.equals(s.getDictValue())).findFirst().get().getDictSort();
                            if (oldLevelSort < newLevelSort) {
                                postHistory.setPhAdjustType(PostChangeTypeEnum.ONE.getKey());
                            } else if (oldLevelSort.equals(newLevelSort)) {
                                postHistory.setPhAdjustType(PostChangeTypeEnum.TWO.getKey());
                            } else {
                                postHistory.setPhAdjustType(PostChangeTypeEnum.THREE.getKey());
                            }
                        } else {
                            postHistory.setPhAdjustType(PostChangeTypeEnum.THREE.getKey());
                        }
                    }
                }
                postHistory.setPhAdjustDate(now);
                postHistory.setPhUpdateDate(now);
                postHistory.setDisabled(0);
                postHistoryMapper.insertPostHistory(postHistory);
            }
            if (empAdd.getEmpPositionLevel() != null && !empAdd.getEmpPositionLevel().equals(employee.getEmpPositionLevel())
                    && empAdd.getEmpPosition() != null && empAdd.getEmpPosition().equals(employee.getEmpPosition())) {
                String newLevel = empAdd.getEmpPositionLevel();
                String oldLevel = employee.getEmpPositionLevel();
                if (employee.getEmpPositionLevel() == null) {
                    postHistory.setPhAdjustType(PostChangeTypeEnum.ONE.getKey());
                } else {
                    List<SysDictData> empPositionLevelInfos = dictDataMapper.selectDictDataByType("emp_position_level");
                    Long oldLevelSort = empPositionLevelInfos.stream().filter(s -> oldLevel.equals(s.getDictValue())).findFirst().get().getDictSort();
                    Long newLevelSort = empPositionLevelInfos.stream().filter(s -> newLevel.equals(s.getDictValue())).findFirst().get().getDictSort();
                    if (oldLevelSort < newLevelSort) {
                        postHistory.setPhAdjustType(PostChangeTypeEnum.ONE.getKey());
                    } else if (oldLevelSort.equals(newLevelSort)) {
                        postHistory.setPhAdjustType(PostChangeTypeEnum.TWO.getKey());
                    } else {
                        postHistory.setPhAdjustType(PostChangeTypeEnum.THREE.getKey());
                    }
                }
                // 设置薪级上下限
                PositionInfo newPositionInfo = positionInfoMapper.findInfoByReferenceParams(empAdd.getEmpPosition(),
                        empAdd.getEmpPositionLevel(), empAdd.getEmpPositionCategory());
                if (newPositionInfo != null) {
                    empAdd.setSalaryLevelMax(newPositionInfo.getSalaryHigh());
                    empAdd.setSalaryLevelMin(newPositionInfo.getSalaryLow());
                }
                postHistory.setPhAdjustDate(now);
                postHistory.setPhUpdateDate(now);
                postHistory.setDisabled(0);
                postHistoryMapper.insertPostHistory(postHistory);
            }
        }
    }

    private void checkEmpSalaryLevelForUpdate(EmpAddVO empAdd) {
        String empSalaryLevel = empAdd.getEmpSalaryLevel();
        if (empSalaryLevel != null) {
            String empPosition = empAdd.getEmpPosition();
            String empPositionLevel = empAdd.getEmpPositionLevel();
            String empPositionCategory = empAdd.getEmpPositionCategory();
            PositionInfo positionInfo = positionInfoMapper.findInfoByReferenceParams(empPosition, empPositionLevel,
                    empPositionCategory);
            if (positionInfo != null) {
                String salaryHigh = positionInfo.getSalaryHigh();
                if (!StringUtils.isEmpty(salaryHigh) && Integer.parseInt(salaryHigh) < Integer.parseInt(empSalaryLevel)) {
                    empAdd.setEmpSalaryLevel(salaryHigh);
                }
                String salaryLow = positionInfo.getSalaryLow();
                if (!StringUtils.isEmpty(salaryLow) && Integer.parseInt(salaryLow) > Integer.parseInt(empSalaryLevel)) {
                    empAdd.setEmpSalaryLevel(salaryLow);
                }
            }
        }
    }

    private void checkEmpSalaryLevelForInsert(Employee employee) {
        String empSalaryLevel = employee.getEmpSalaryLevel();
        if (empSalaryLevel != null) {
            String salaryLevelMin = employee.getSalaryLevelMin();
            String salaryLevelMax = employee.getSalaryLevelMax();
            if (!StringUtils.isEmpty(salaryLevelMax) && Integer.parseInt(salaryLevelMax) < Integer.parseInt(empSalaryLevel)) {
                employee.setEmpSalaryLevel(salaryLevelMax);
            }
            if (!StringUtils.isEmpty(salaryLevelMin) && Integer.parseInt(salaryLevelMin) > Integer.parseInt(empSalaryLevel)) {
                employee.setEmpSalaryLevel(salaryLevelMin);
            }
        }
    }


    private void changeEmpStatus(Date empHireDate, Employee employee, String empIdcard) {

        String manRetireAge = configMapper.checkConfigKeyUnique("labor.man.retireAge").getConfigValue();
        String womanRetireAge = configMapper.checkConfigKeyUnique("labor.woman.retireAge").getConfigValue();
        Date expireTime = null;
        Date now = new Date();
        Date empExpireTime = employee.getEmpExpireTime();
        if (empExpireTime != null) {
            long days = calculateDays(now, empExpireTime);
            employee.setRetireReminder(days + "");
        }
        if (StringUtils.isNotEmpty(empIdcard)) {
            expireTime = DateUtils.calculateExpireTime(empIdcard, employee.getEmpGender(), manRetireAge, womanRetireAge);
            // 身份证中的年份
            int birthYear = Integer.parseInt(empIdcard.substring(6, 10));
            // 身份证中的月份
            int birthMonth = Integer.parseInt(empIdcard.substring(10, 12));
            // 身份证中的日期
            int birthDay = Integer.parseInt(empIdcard.substring(12, 14));
            // 获取出生日期
            LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
            employee.setBirthDate(Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            long days = calculateDays(now, expireTime);
            employee.setRetireReminder(days + "");
            employee.setEmpExpireTime(expireTime);
        }
        String empPosition = employee.getEmpPosition();
        List<String> empPositionDinInfos = dictDataMapper.selectDictDataByType("emp_position").stream().
                filter(s -> {
                    String dictLabel = s.getDictLabel();
                    return dictLabel.contains("造价") || dictLabel.contains("监理") || dictLabel.contains("安全质量监督")
                            || dictLabel.contains("监造") || dictLabel.contains("设计") || dictLabel.contains("招标");
                }).map(SysDictData::getDictValue).collect(Collectors.toList());

        if (empPositionDinInfos.contains(empPosition) && StringUtils.isNotEmpty(empIdcard)) {
            Date hireLimit = DateUtils.calculateExpireTime(empIdcard, employee.getEmpGender(), "65", "63");
            employee.setHireLimitDate(hireLimit);
        }

        String retireDays = configMapper.checkConfigKeyUnique("labor.employee.retireDays").getConfigValue();
        String empStatus = employee.getEmpStatus();
        if (empStatus == null) {
            employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
            empStatus = EmpStatusEnum.ON_POSITION.getKey();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        if (EmpStatusEnum.NEW_POSITION.getKey().equals(empStatus) && empHireDate != null) {
            String empHireDataStr = format.format(empHireDate);
            String nowStr = format.format(now);
            if (empHireDataStr.equals(nowStr)) {
                employee.setEmpStatus(EmpStatusEnum.NEW_POSITION.getKey());
            } else {
                employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
            }
        }
        if (EmpStatusEnum.ON_POSITION.getKey().equals(empStatus) && empIdcard == null && empHireDate != null) {
            String empHireDataStr = format.format(empHireDate);
            String nowStr = format.format(now);
            if (empHireDataStr.equals(nowStr)) {
                employee.setEmpStatus(EmpStatusEnum.NEW_POSITION.getKey());
            }
        }
        if (expireTime != null) {
            long intervalDays = DateUtils.calculateDays(now, expireTime);
            long intervalNum = expireTime.getTime() - now.getTime();
            if (EmpStatusEnum.ON_POSITION.getKey().equals(empStatus)) {
                if (intervalNum <= 0) {
                    employee.setEmpStatus(EmpStatusEnum.RE_EMPLOY.getKey());
                } else {
                    if (intervalDays <= Long.parseLong(retireDays)) {
                        employee.setEmpStatus(EmpStatusEnum.ALMOST.getKey());
                    } else {
                        if (empHireDate != null) {
                            String empHireDataStr = format.format(empHireDate);
                            String nowStr = format.format(now);
                            if (empHireDataStr.equals(nowStr)) {
                                employee.setEmpStatus(EmpStatusEnum.NEW_POSITION.getKey());
                            } else {
                                employee.setEmpStatus(EmpStatusEnum.ON_POSITION.getKey());
                            }
                        }
                    }
                }
            } else if (EmpStatusEnum.EXPIRE.getKey().equals(empStatus)) {
                if (intervalNum <= 0) {
                    employee.setEmpStatus(EmpStatusEnum.EXPIRE.getKey());
                } else {
                    throw new ServiceException("员工状态校验未通过");
                }
            }
        }
    }

    private long calculateDays(Date startTime, Date endTime) {
        long startMillis = startTime.getTime();
        long endMillis = endTime.getTime();
        long diffMillis = endMillis - startMillis;
        long diffDays = diffMillis / (1000 * 60 * 60 * 24);
        return diffDays;

    }


    /**
     * 批量删除员工
     *
     * @param empIds 需要删除的员工主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeByEmpIds(Long[] empIds) {
        Date now = new Date();
        List<Long> empIdList = Arrays.asList(empIds);
        List<String> docTypes = new ArrayList<>();
        docTypes.add("1");
        docTypes.add("2");
        docTypes.add("3");
        docTypes.add("4");
        empDocumentMapper.removeInfoByEmpIdsAndDocTypes(empIdList, docTypes);
        return employeeMapper.deleteEmployeeByEmpIds(empIds, now);
    }

    @Override
    public int deleteEmpFamilyByFamId(Long[] famIds) {
        return familyRelationsMapper.deleteFamilyRelationsByFamIds(famIds);
    }

    /**
     * 删除员工信息
     *
     * @param empId 员工主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeByEmpId(Long empId) {
        return employeeMapper.deleteEmployeeByEmpId(empId);
    }


    private int getLowerBound(String key) {
        if (key.equals("30以下")) {
            return -1;
        } else if (key.equals("30-39")) {
            return 30;
        } else if (key.equals("40-49")) {
            return 40;
        } else if (key.equals("50-60")) {
            return 50;
        } else if (key.equals("60以上")) {
            return 60;
        } else {
            // 默认情况下，将未知格式的键视为最大值
            return Integer.MAX_VALUE;
        }
    }

    /**
     * 根据employeeInfoVO的条件查询出员工集合
     *
     * @param employeeInfoVO
     * @return
     */
    @Override
    public HashMap<String, Collection<PieChartVO>> selectEmployeeByEmployeeInfoVO(EmployeeInfoVO employeeInfoVO) {
        HashMap<String, Collection<PieChartVO>> empMap = new HashMap<>();
        //定义五个HashMap，用来存储性别，学历， 部门，职称，年龄
        Map<String, PieChartVO> empEducationMap = new HashMap();
        Map<String, PieChartVO> empGenderMap = new HashMap();
        Map<String, PieChartVO> empDeptMap = new HashMap();
        Map<String, PieChartVO> empTitleMap = new HashMap<>();
        Map<String, PieChartVO> empAgeMap = new TreeMap<>((o1, o2) -> {
            int lowerBound1 = getLowerBound(o1);
            int lowerBound2 = getLowerBound(o2);
            return lowerBound1 - lowerBound2;
        });

        String[] ageRangeArry = {"30以下", "30-39", "40-49", "50-60", "60以上"};
        List<Employee> empList = employeeMapper.selectEmployeeByEmployeeInfoVO(employeeInfoVO);

        if (empList != null && empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                Employee emp = empList.get(i);
                //处理学历数据，PieChartVO（name,value）分别存储“学历”和数量，每次循环取出学历记录，封装在PieChartVO中，将PieChartVO对象
                //集合存储在Map中，学历名作为map的KEY,
                //               String empEdu = "";
//                for (EmpEducationEnum value : EmpEducationEnum.values()) {
//                    if (value.getKey().equals(emp.getEmpEducation())) {
//                        empEdu = value.getValue();
//                        break;
//                    }
//                }
                putMap(empEducationMap, emp.getEmpEducation());
                //处理性别数据
                //               String empGender = "男";
//                for (EmpGenderEnum value : EmpGenderEnum.values()) {
//                    if (value.getKey().equals(emp.getEmpGender())) {
//                        empGender = value.getValue();
//                    }
//                }
                putMap(empGenderMap, emp.getEmpGender());

                //处理部门数据
                String empDept = emp.getEmpDeptName();
                putMap(empDeptMap, empDept);

                //处理职称数据
//                String empTitle = "";
//                for (EmpTitleEnum value : EmpTitleEnum.values()) {
//                    if (value.getKey().equals(emp.getEmpTitle())) {
//                        empTitle = value.getValue();
//                        break;
//                    }
//                }
                putMap(empTitleMap, emp.getEmpTitle());

                //处理年龄数据

                Integer empAge = emp.getEmpAge();
                if (empAge != null) {
                    String empAgeKey = null;
                    if (empAge < 30) {
                        empAgeKey = ageRangeArry[0];
                    } else if (empAge < 40) {
                        empAgeKey = ageRangeArry[1];
                    } else if (empAge < 50) {
                        empAgeKey = ageRangeArry[2];
                    } else if (empAge <= 60) {
                        empAgeKey = ageRangeArry[3];
                    } else {
                        empAgeKey = ageRangeArry[4];
                    }
                    if (employeeInfoVO.getMinAge() != null && employeeInfoVO.getMaxAge() != null) {
                        Integer minAge = employeeInfoVO.getMinAge();
                        if (minAge == 30) {
                            empAgeKey = ageRangeArry[1];
                        }
                        if (minAge == 40) {
                            empAgeKey = ageRangeArry[2];
                        }
                        if (minAge == 50) {
                            empAgeKey = ageRangeArry[3];
                        }
                        putMap(empAgeMap, empAgeKey);
                    } else {
                        if (empAge == 30 || empAge == 40 || empAge == 50 || empAge == 60) {
                            if (empAge == 30) {
                                empAgeKey = ageRangeArry[1];
                                if (empAgeMap.containsKey(empAgeKey)) {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, empAgeMap.get(empAgeKey).getValue() + 1));
                                } else {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, 1));
                                }
                            }
                            if (empAge == 40) {
                                empAgeKey = ageRangeArry[2];
                                if (empAgeMap.containsKey(empAgeKey)) {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, empAgeMap.get(empAgeKey).getValue() + 1));
                                } else {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, 1));
                                }
                            }
                            if (empAge == 50) {
                                empAgeKey = ageRangeArry[3];
                                if (empAgeMap.containsKey(empAgeKey)) {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, empAgeMap.get(empAgeKey).getValue() + 1));
                                } else {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, 1));
                                }
                            }
                            if (empAge == 60) {
                                empAgeKey = ageRangeArry[3];
                                if (empAgeMap.containsKey(empAgeKey)) {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, empAgeMap.get(empAgeKey).getValue() + 1));
                                } else {
                                    empAgeMap.put(empAgeKey, new PieChartVO(empAgeKey, 1));
                                }
                            }
                        } else {
                            putMap(empAgeMap, empAgeKey);
                        }
                    }
                }
            }

            empMap.put("educationSet", empEducationMap.values());
            empMap.put("genderSet", empGenderMap.values());
            empMap.put("deptSet", empDeptMap.values());
            empMap.put("titleSet", empTitleMap.values());
            empMap.put("ageSet", empAgeMap.values());
        } else {
            empMap.put("educationSet", null);
            empMap.put("genderSet", null);
            empMap.put("deptSet", null);
            empMap.put("titleSet", null);
            empMap.put("ageSet", null);
        }

        return empMap;
    }

    @Override
    public List<EmpNameCardVO> selectEmpNameAndCard(EmployeeInfoVO employeeInfoVO) {
        List<EmpNameCardVO> list = employeeMapper.selectEmpNameAndCard(employeeInfoVO);
        for (EmpNameCardVO vo : list) {
            if (!StringUtils.isEmpty(vo.getEmpIdcard())) {
                vo.setEmpIdcard(Base64.desensitize(vo.getEmpIdcard()));
            }
        }
        return list;
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {

        List<String> empStatusDic = dictDataMapper.selectDictDataByType("emp_status").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        empStatusDic = empStatusDic.stream().filter(s -> {
            return "在职".equals(s) || "辞职".equals(s) || "辞退".equals(s) || "到龄".equals(s);
        }).collect(Collectors.toList());

        List<String> empSexDic = dictDataMapper.selectDictDataByType("emp_sex").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> hireCompanyDic = dictDataMapper.selectDictDataByType("hire_company").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empHrCompanyDic = dictDataMapper.selectDictDataByType("emp_hr_company").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());


        // List<String> empCategoryDic = dictDataMapper.selectDictDataByType("emp_category").stream().
//        map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empPositionDic = dictDataMapper.selectDictDataByType("emp_position").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());


        List<String> empPositionCategoryDic = dictDataMapper.selectDictDataByType("emp_position_category").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empPositionLevelDic = dictDataMapper.selectDictDataByType("emp_position_level").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());
        ;

        List<String> empSalaryLevelDic = dictDataMapper.selectDictDataByType("emp_salary_level").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empPoliticalDic = dictDataMapper.selectDictDataByType("emp_political_status").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empEducationDic = dictDataMapper.selectDictDataByType("emp_education").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empTitleDic = dictDataMapper.selectDictDataByType("emp_title").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        List<String> empAppellationDic = dictDataMapper.selectDictDataByType("emp_appellation").stream().
                map(SysDictData::getDictLabel).collect(Collectors.toList());

        Map<Integer, List<String>> downDropMap = new HashMap<>(16);
        downDropMap.put(2, empStatusDic);
        downDropMap.put(4, empSexDic);
        downDropMap.put(11, hireCompanyDic);
        // downDropMap.put(18, empCategoryDic);
        downDropMap.put(18, empPositionDic);
        downDropMap.put(15, empHrCompanyDic);
        downDropMap.put(16, empPositionCategoryDic);
        downDropMap.put(19, empPositionLevelDic);
        downDropMap.put(20, empSalaryLevelDic);
        downDropMap.put(21, empSalaryLevelDic);
        downDropMap.put(22, empSalaryLevelDic);
        downDropMap.put(23, empPoliticalDic);
        downDropMap.put(29, empEducationDic);
        downDropMap.put(30, empEducationDic);
        downDropMap.put(33, empTitleDic);
        downDropMap.put(44, empAppellationDic);
        downDropMap.put(46, empAppellationDic);
        DropDownWriteHandler downWriteHandler = new DropDownWriteHandler(downDropMap);

        try {
            int headHeight = EmpExportTemplateVO.getHeadHeight();
            HorizontalCellStyleStrategy cellStyleStrategy = setMyCellStyle();
            List<EmpExportTemplateVO> empList = new ArrayList<>();
            EmpExportTemplateVO empExportTemplate = new EmpExportTemplateVO();
            empExportTemplate.setOrderNum("1");
            empExportTemplate.setEmpName("张三");
            empExportTemplate.setEmpRemark("这个员工不错");
           // empExportTemplate.setEmpAge(36);
            empExportTemplate.setNation("汉族");
//            empExportTemplate.setEmpIdcard("320326198706153162");
            empExportTemplate.setBirthDate("1987/6/15");
            empExportTemplate.setDeptInfoTwo("高度机密");
            empExportTemplate.setNativePlace("江苏省/徐州市/铜山县");
//            empExportTemplate.setEmpTelephone("15951725456");
            empExportTemplate.setAccumulativeMonth(234);
            empList.add(empExportTemplate);


            EasyExcel.write(response.getOutputStream(), EmpExportTemplateVO.class).registerWriteHandler(new TemplateCellWriteHandler(headHeight))
                    .registerWriteHandler(cellStyleStrategy).
                    registerWriteHandler(downWriteHandler).sheet("员工信息").doWrite(empList);
        } catch (IOException e) {
            throw new ServiceException("下载员工信息模板失败");
        }

    }


    public static HorizontalCellStyleStrategy setMyCellStyle() {

        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        // 字体
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setWrapped(true);

        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容靠中对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭

        return horizontalCellStyleStrategy;
    }

    @Override
    public void downloadExcelTemplate(HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/empTemplate.xlsx");
            inputStream = classPathResource.getInputStream();
            ExcelUtil.downLoadExcel("empTemplate", response, WorkbookFactory.create(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void downloadFileTemplate(HttpServletResponse response) {
//        try {
//            ResourceLoader resourceLoader = new DefaultResourceLoader();
//            Resource resource = resourceLoader.getResource("classpath:static/template.rar");
//            InputStream inputStream = resource.getInputStream();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int length;
//            if ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            genRarFile(response, outputStream.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:static/template.rar");
            InputStream fis = resource.getInputStream();

//            File file = new File(filePath);
//            FileInputStream fis = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // 设置响应头信息
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"template.rar\"");

            // 将文件写入响应输出流
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            fis.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常
        }


    }

    @Override
    public void downloadEmployeeFileInfosFile(HttpServletResponse response, EmpFileVO empFile) {

        List<Long> empIds = empFile.getEmpIds();

        if (CollectionUtils.isEmpty(empIds)) {
            empIds = employeeMapper.findAllEmployees().stream().map(Employee::getEmpId).collect(Collectors.toList());
        }

        List<Employee> employees = employeeMapper.selectEmployeeInfosByIds(empIds);
        List<EmpDocument> empDocuments = empDocumentMapper.findDocInfosByEmpIdsAndTypes(empIds, empFile.getFileType());
        Map<Long, List<EmpDocument>> empDocMap = empDocuments.stream().collect(Collectors.groupingBy(EmpDocument::getDocEmpId));
        File rootDir = new File("员工证件信息");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        for (Employee employee : employees) {
            String empIdCard = employee.getEmpIdcard();
            File subDir = org.apache.commons.io.FileUtils.getFile(rootDir, empIdCard);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
            List<EmpDocument> documents = empDocMap.get(employee.getEmpId());
            if (!CollectionUtils.isEmpty(documents)) {
                Map<String, List<EmpDocument>> subDocMap = documents.stream().collect(Collectors.groupingBy(EmpDocument::getDocType));
                for (String docType : subDocMap.keySet()) {
                    String docTypeName = null;
                    if (EmpDocTypeEnum.ONE.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.ONE.getValue();
                    }
                    if (EmpDocTypeEnum.TWO.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.TWO.getValue();
                    }
                    if (EmpDocTypeEnum.THREE.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.THREE.getValue();
                    }
                    if (EmpDocTypeEnum.FOUR.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.FOUR.getValue();
                    }
                    if (EmpDocTypeEnum.FIVE.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.FIVE.getValue();
                    }
                    if (EmpDocTypeEnum.SIX.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.SIX.getValue();
                    }
                    if (EmpDocTypeEnum.SEVEN.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.SEVEN.getValue();
                    }
                    if (EmpDocTypeEnum.EIGHT.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.EIGHT.getValue();
                    }
                    List<EmpDocument> subEmpDocList = subDocMap.get(docType);
                    writeFileToDir(subDir, docTypeName, subEmpDocList);
                }
            }
        }
        FileUtils.writeCompressedFileToResponse(response, rootDir);
        FileUtils.deleteFolder(rootDir);
    }

    @Override
    public Boolean matchEmpFileInfosWithinTemplate(MultipartFile file, Long userId) {

        empImportHistoryMapper.removeInfoByCreateId(userId);
        ExcelImportResult<EmpExcelSimpleVO> excelImportResult = getExcelDateForEmpExcel(file, EmpExcelSimpleVO.class);
        if (excelImportResult == null) {
            return false;
        }
        List<EmpExcelSimpleVO> dataList = excelImportResult.getList();
        List<EmpImportHistory> empImportHistoryList = new ArrayList<>();
        for (EmpExcelSimpleVO empExcelSimple : dataList) {
            EmpImportHistory empImportHistory = new EmpImportHistory();
            Employee existEmployee = null;
            if (empExcelSimple.getEmpName() != null) {
                existEmployee = employeeMapper.findEmpInfoByEmpName(empExcelSimple.getEmpName());
            }
            if (empExcelSimple.getIdCard() != null) {
                existEmployee = employeeMapper.findInfoByEmpIdCard(empExcelSimple.getIdCard());
            }
            if (existEmployee == null) {
                empImportHistory.setStatus(MatchResultEnum.NO.getKey());
            } else {
                empImportHistory.setStatus(MatchResultEnum.YES.getKey());
                empImportHistory.setEmpId(existEmployee.getEmpId());
            }
            empImportHistory.setEmpName(empExcelSimple.getEmpName());
            empImportHistory.setIdCard(empExcelSimple.getIdCard());
            empImportHistory.setDisabled(false);
            empImportHistory.setCreateBy(userId);
            empImportHistory.setCreateTime(new Date());
            empImportHistoryList.add(empImportHistory);
        }
        empImportHistoryMapper.batchInsertInfos(empImportHistoryList);
        return true;
    }

    @Override
    public List<EmpImportHistoryVO> selectMatchTemplateList(MatchSearchVO matchSearch, Long userId) {

        String keyword = matchSearch.getKeyword();
        String regex = "\\d{17}[0-9Xx]";
        if (keyword != null) {
            if (keyword.matches(regex)) {
                matchSearch.setEmpIdCard(keyword);
            } else {
                matchSearch.setEmpName(keyword);
            }
        }
        return empImportHistoryMapper.selectMatchInfos(matchSearch, userId);
    }

    @Override
    public Boolean updateMatchTemplateInfo(MatchUpdateVO matchUpdate, Long userId) {

        EmpImportHistory empImportHistory = new EmpImportHistory();
        String empIdCard = matchUpdate.getEmpIdCard();
        Employee existEmployee = employeeMapper.findInfoByEmpIdCard(empIdCard);
        if (existEmployee == null) {
            empImportHistory.setStatus(MatchResultEnum.NO.getKey());
        } else {
            empImportHistory.setStatus(MatchResultEnum.YES.getKey());
            empImportHistory.setEmpId(existEmployee.getEmpId());
        }
        empImportHistory.setIdCard(empIdCard);
        empImportHistory.setUpdateBy(userId);
        empImportHistory.setUpdateTime(new Date());
        empImportHistoryMapper.updateEmpImportHistory(empImportHistory);
        return true;
    }

    @Override
    public Boolean removeMatchTemplateInfos(List<Long> idList) {
        empImportHistoryMapper.removeInfosByIds(idList);
        return true;
    }

    @Override
    public MatchResultVO findMatchTemplateNums(Long userId) {

        Long totalNums = empImportHistoryMapper.findTotalNums(userId);
        Long unMatchNums = empImportHistoryMapper.findUnmatchedNums(userId);
        MatchResultVO matchResult = new MatchResultVO();
        matchResult.setTotalNums(totalNums);
        matchResult.setUnMatchNums(unMatchNums);
        return matchResult;
    }


    @Override
    public void downloadEmpFileInfosWithinTemplate(MultipartFile file, HttpServletResponse response) {

        ExcelImportResult<EmpExcelSimpleVO> excelImportResult = getExcelDateForEmpExcel(file, EmpExcelSimpleVO.class);
        if (excelImportResult == null) {
            return;
        }
        List<EmpExcelSimpleVO> dataList = excelImportResult.getList();
        Map<String, List<EmpExcelSimpleVO>> dataMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(dataList)) {
            dataMap = dataList.stream().collect(Collectors.groupingBy(EmpExcelSimpleVO::getIdCard));
        }
        if (CollectionUtils.isEmpty(dataMap)) {
            return;
        }
        downloadEmpFileInfos(dataMap, response);
    }

    private void downloadEmpFileInfos(Map<String, List<EmpExcelSimpleVO>> dataMap, HttpServletResponse response) {

        File rootDir = new File("员工证件信息");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        for (String empIdCard : dataMap.keySet()) {
            File subDir = org.apache.commons.io.FileUtils.getFile(rootDir, empIdCard);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
            Employee employee = employeeMapper.findInfoByEmpIdCard(empIdCard);
            if (employee == null) {
                continue;
            }
            //  List<String> fileTypes = dataMap.get(empIdCard).stream().map(EmpExcelSimpleVO::getFileType).collect(Collectors.toList());
            List<String> docTypes = new ArrayList<>();
            docTypes.add(EmpDocTypeEnum.ONE.getKey());
            docTypes.add(EmpDocTypeEnum.TWO.getKey());
            docTypes.add(EmpDocTypeEnum.THREE.getKey());
            docTypes.add(EmpDocTypeEnum.FOUR.getKey());
//            for (String fileType : fileTypes) {
//                if (EmpDocTypeEnum.ONE.getValue().equals(fileType)) {
//                    docTypes.add(EmpDocTypeEnum.ONE.getKey());
//                }
//                if (EmpDocTypeEnum.TWO.getValue().equals(fileType)) {
//                    docTypes.add(EmpDocTypeEnum.TWO.getKey());
//                }
//                if (EmpDocTypeEnum.THREE.getValue().equals(fileType)) {
//                    docTypes.add(EmpDocTypeEnum.THREE.getKey());
//                }
//                if (EmpDocTypeEnum.FOUR.getValue().equals(fileType)) {
//                    docTypes.add(EmpDocTypeEnum.FOUR.getKey());
//                }
//            }
            List<EmpDocument> documents = new ArrayList<>();
            if (!CollectionUtils.isEmpty(docTypes)) {
                documents = empDocumentMapper.findDocInfosByEmpIdAndTypes(employee.getEmpId(), docTypes);
            }
            if (!CollectionUtils.isEmpty(documents)) {
                Map<String, List<EmpDocument>> subDocMap = documents.stream().collect(Collectors.groupingBy(EmpDocument::getDocType));
                for (String docType : subDocMap.keySet()) {
                    String docTypeName = null;
                    if (EmpDocTypeEnum.ONE.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.ONE.getValue();
                    }
                    if (EmpDocTypeEnum.TWO.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.TWO.getValue();
                    }
                    if (EmpDocTypeEnum.THREE.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.THREE.getValue();
                    }
                    if (EmpDocTypeEnum.FOUR.getKey().equals(docType)) {
                        docTypeName = EmpDocTypeEnum.FOUR.getValue();
                    }
                    List<EmpDocument> subEmpDocList = subDocMap.get(docType);
                    writeFileToDir(subDir, docTypeName, subEmpDocList);
                }
            }
        }
        FileUtils.writeCompressedFileToResponse(response, rootDir);
        FileUtils.deleteFolder(rootDir);
    }

    private ExcelImportResult<EmpExcelSimpleVO> getExcelDateForEmpExcel(MultipartFile file, Class<EmpExcelSimpleVO> empExcelSimpleClass) {

        ExcelImportResult<EmpExcelSimpleVO> excelImportResult = null;
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            excelImportResult = ExcelImportUtil.importExcelMore(file.getInputStream(), empExcelSimpleClass, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelImportResult;
    }


    @Value("${ruoyi.profile}")
    private String basePath;


    private void writeFileToDir(File subDir, String docTypeName, List<EmpDocument> subEmpDocList) {

        try {
            File docTypeDir = org.apache.commons.io.FileUtils.getFile(subDir, docTypeName);
            if (!docTypeDir.exists()) {
                docTypeDir.mkdirs();
            }
            for (EmpDocument document : subEmpDocList) {
                File file = org.apache.commons.io.FileUtils.getFile(docTypeDir, document.getDocName());
                if (!file.exists()) {
                    file.createNewFile();
                }
                File sourceFile = org.apache.commons.io.FileUtils.getFile(basePath + document.getDocAnnexPath());
                org.apache.commons.io.FileUtils.copyFile(sourceFile, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void zipDirectory(String sourceFolder, String parentFolder, ZipOutputStream zos) throws IOException {
//        File folder = new File(sourceFolder);
//        if (!folder.exists()) {
//            return;
//        }
//        String[] fileList = folder.list();
//        for (String fileName : fileList) {
//            File file = new File(folder, fileName);
//            if (file.isDirectory()) {
//
//                zipDirectory(file.getAbsolutePath(), parentFolder + file.getName() + "/", zos);
//            } else {
//                byte[] buffer = new byte[1024];
//                FileInputStream fis = new FileInputStream(file);
//                zos.putNextEntry(new ZipEntry(parentFolder + file.getName()));
//                int length;
//                while ((length = fis.read(buffer)) > 0) {
//                    zos.write(buffer, 0, length);
//                }
//                zos.flush();
//                zos.closeEntry();
//                fis.close();
//            }
//        }
//    }
//
//
//    private void addPhotosToDirectories(String rootFolder, String[] subFolders) throws IOException {
//
//        ResourceLoader resourceLoader = new DefaultResourceLoader();
//        Resource resource = resourceLoader.getResource("classpath:static/photo.jpg");
//
//        File photoFile = resource.getFile();
//        if (!photoFile.exists()) {
//            System.out.println("照片文件不存在！");
//            return;
//        }
//
//        for (String subFolder : subFolders) {
//            BufferedImage image = ImageIO.read(photoFile);
//            if (image == null) {
//                System.out.println("无法读取照片文件！");
//                return;
//            }
//
//            File subDir = new File(rootFolder + File.separator + subFolder);
//            if (!subDir.exists()) {
//                subDir.mkdir();
//            }
//
//            File destFile = new File(subDir, "photo.jpg");
//            ImageIO.write(image, "jpg", destFile);
//        }
//
//        System.out.println("照片添加成功！");
//    }

    @Override
    public ImportResultVO uploadEmployeeInfosFile(MultipartFile multipartFile, Long userId, String username) {

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.ONE.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");

        String nowStr = dateFormat.format(now);
        ExcelImportResult<EmpImportVO> excelDate = getExcelDate(multipartFile, EmpImportVO.class);
        List<ErrorForm> failReport = excelDate.getFailList().stream()
                .map(entity -> {
                    int line = entity.getRowNum() == null ? 0 : entity.getRowNum() + 1;
                    return new ErrorForm(line, entity.getErrorMsg());
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(excelDate.getList())) {
            batchInsertEmployeeInfos(userId, username, excelDate.getList());
        }

        int successCount = CollectionUtils.isEmpty(excelDate.getList()) ? 0 : excelDate.getList().size();
        int errorCount = CollectionUtils.isEmpty(failReport) ? 0 : failReport.size();

        Long failFileId = null;
        String failFileUrl = null;
        fileImportRecord.setImportStatus(FileImportStatusEnum.FINISHED.getKey());
        if (errorCount != 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("错误信息", "Sheet1", ExcelType.XSSF),
                    ErrorForm.class, failReport);
            MultipartFile file = FileUtils.workbookToCommonsMultipartFile(workbook, multipartFile.getOriginalFilename() + "错误信息" + nowStr + ".xlsx");
            if (file != null) {
                FileVO failFileInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_FAIL_INFO.getKey(), "employee", file, username);
                failFileId = failFileInfo.getFileId();
                failFileUrl = failFileInfo.getFileUrl();
            }
            if (successCount == 0) {
                fileImportRecord.setImportStatus(FileImportStatusEnum.FAIL.getKey());
            }
        }
        Long empInfoFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_INFO.getKey(), "employee", multipartFile, username).getFileId();

        fileImportRecord.setSuccessCount(successCount);
        fileImportRecord.setFailureCount(errorCount);
        fileImportRecord.setTotalCount(successCount + errorCount);
        fileImportRecord.setOriginFileId(empInfoFileId);
        fileImportRecord.setFailFileId(failFileId);
        fileImportRecordMapper.updateFileImportRecord(fileImportRecord);

        ImportResultVO importResult = new ImportResultVO();
        importResult.setTotalCount(successCount + errorCount);
        importResult.setSuccessCount(successCount);
        importResult.setErrorCount(errorCount);
        importResult.setFailFileId(failFileId);
        importResult.setFailFileUrl(failFileUrl);
        return importResult;
    }

    @Override
    public ImportResultVO uploadEmployeeFileInfosFile(MultipartFile multipartFile, String username, Long userId) {

        ImportResultVO importResult = new ImportResultVO();
        Integer totalCount = 0;
        Integer errorCount = 0;
        Integer successCount = 0;
        List<ErrorImportForm> errorImportForms = new ArrayList<>();

        Date now = new Date();
        FileImportRecord fileImportRecord = new FileImportRecord();
        fileImportRecord.setSuccessCount(0);
        fileImportRecord.setFailureCount(0);
        fileImportRecord.setTotalCount(0);
        fileImportRecord.setDisabled(false);
        fileImportRecord.setFileType(ImportFileTypeEnum.ONE.getKey());
        fileImportRecord.setImportStatus(FileImportStatusEnum.DURING.getKey());
        fileImportRecord.setCreatorId(userId);
        fileImportRecord.setCreatedTime(now);
        fileImportRecordMapper.insertFileImportRecord(fileImportRecord);

        String fileName = multipartFile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);  // 后缀名
        if (!("rar".equals(suffixName) || "7z".equals(suffixName) || "zip".equals(suffixName))) {
            throw new ServiceException("压缩文件格式不正确");
        }
        String destDir = "test";
        extractCompressFile(multipartFile, destDir);
        File file = new File(destDir);
        List<String> regex = Arrays.asList("个人照片", "身份证照片", "学历照片", "最高职称证书照片");
        List<String> nameIdStringInfos = getNameIdInfos(file, regex);
        List<String> idInfos = nameIdStringInfos.stream().map(s -> {
            String[] split = s.split("-");
            return split[0];
        }).distinct().collect(Collectors.toList());
        List<Employee> employees = new ArrayList<>();
        idInfos.forEach(d -> {
            Employee empInfoByEmpName = employeeMapper.findEmpInfoByEmpName(d);
            if (!ObjectUtils.isEmpty(empInfoByEmpName)) {
                employees.add(empInfoByEmpName);
            }
        });
        List<String> existIdInfos = employees.stream().map(Employee::getEmpName).collect(Collectors.toList());
        totalCount = idInfos.size();
        successCount = existIdInfos.size();
        errorCount = totalCount - successCount;
        if (errorCount != 0) {
            for (String nameIdStringInfo : nameIdStringInfos) {
                String[] split = nameIdStringInfo.split("-");
                String idStr = split[0];
                if (!existIdInfos.contains(idStr)) {
                    ErrorImportForm errorImport = new ErrorImportForm();
                    errorImport.setEmpName(split[0]);
//                    errorImport.setIdCard(split[1]);
                    errorImport.setReason("与系统中的员工数据未匹配成功");
                    errorImportForms.add(errorImport);
                }
            }
        }

        saveEmpFileInfos(file, username, regex);
        FileUtils.deleteFolder(file);
        FileVO errorInfo = new FileVO();
        if (!CollectionUtils.isEmpty(errorImportForms)) {
            ExcelUtil<ErrorImportForm> excelUtil = new ExcelUtil<ErrorImportForm>(ErrorImportForm.class);
            excelUtil.init(errorImportForms, "错误信息", "员工文件导入错误信息", Excel.Type.EXPORT);
            excelUtil.writeSheet();
            Workbook wb = excelUtil.getWb();
            MultipartFile errorFile = FileUtils.workbookToCommonsMultipartFile(wb, "员工文件导入错误信息.xlsx");
            if (errorFile != null) {
                errorInfo = fileService.upLoadEmpFile(FileTypeEnum.EMP_PAPER_FAIL_INFO.getKey(), "employee", errorFile, username);
            }
        }
        Long empPaperFileId = fileService.upLoadEmpFile(FileTypeEnum.EMP_PAPER_INFO.getKey(), "employee", multipartFile, username).getFileId();

        fileImportRecord.setSuccessCount(successCount);
        fileImportRecord.setFailureCount(errorCount);
        fileImportRecord.setTotalCount(totalCount);
        fileImportRecord.setOriginFileId(empPaperFileId);
        fileImportRecord.setFailFileId(errorInfo.getFileId());
        fileImportRecordMapper.updateFileImportRecord(fileImportRecord);

        importResult.setTotalCount(totalCount);
        importResult.setSuccessCount(successCount);
        importResult.setErrorCount(errorCount);
        importResult.setFailFileId(errorInfo.getFileId());
        importResult.setFailFileUrl(errorInfo.getFileUrl());

        return importResult;
    }

    public List<String> getNameIdInfos(File folder, List<String> regex) {
        List<String> idCardList = new ArrayList<>();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    String fileName = file.getName();
                    if (regex.contains(fileName)) {
                        String parentName = file.getParentFile().getName();
                        idCardList.add(parentName + "-" + fileName);
                    } else {
                        idCardList.addAll(getNameIdInfos(file, regex)); // 递归获取子文件夹内的文件身份证
                    }
                }
            }
        }
        return idCardList;
    }


    private void saveEmpFileInfos(File file, String username, List<String> regex) {
        if (file.isDirectory()) {
            if (regex.contains(file.getName())) {
                savaEmpInfosToDB(file, file.getParentFile().getName(), username);
            } else {
                File[] files = file.listFiles();
                for (File subFile : files) {
                    saveEmpFileInfos(subFile, username, regex);
                }
            }
        }
    }

    private void savaEmpInfosToDB(File file, String idCard, String username) {

        Employee employee = employeeMapper.findEmpInfoByEmpName(idCard);
        if (employee != null) {
            String fileName = file.getName();
            if ((!"个人照片".equals(fileName) && !"身份证照片".equals(fileName) && !"学历照片".equals(fileName)
                    && !"最高职称证书照片".equals(fileName))) {
                throw new ServiceException("文件目录名称不符合规范");
            }
            String fileType = null;
            if ("个人照片".equals(fileName)) {
                fileType = EmpDocTypeEnum.ONE.getKey();
            }
            if ("身份证照片".equals(fileName)) {
                fileType = EmpDocTypeEnum.TWO.getKey();
            }
            if ("最高职称证书照片".equals(fileName)) {
                fileType = EmpDocTypeEnum.THREE.getKey();
            }
            if ("学历照片".equals(fileName)) {
                fileType = EmpDocTypeEnum.FOUR.getKey();
            }
            File[] photoFiles = file.listFiles();
            List<Long> docIds = new ArrayList<>();
            for (File photoFile : photoFiles) {
                if (photoFile.isFile() && fileType != null) {
                    EmpDocument document = fileService.uploadUnCompressFile(fileType, "employee", photoFile, username);
                    docIds.add(document.getDocId());
                }
            }
            List<String> docTypes = Collections.singletonList(fileType);
            empDocumentMapper.removeInfoByEmpIdsAndDocTypes(Collections.singletonList(employee.getEmpId()), docTypes);
            empDocumentMapper.bindEmpInfos(employee.getEmpId(), docIds);
            EmpDocument document = empDocumentMapper.findDocInfosByEmpIdAndTypes(employee.getEmpId(), Collections.singletonList(
                    EmpDocTypeEnum.ONE.getKey())).stream().findFirst().orElse(new EmpDocument());
            employee.setEmpAvatarUrl(document.getDocAnnexPath());
            employeeMapper.updateEmpAvatarUrl(employee);
        }
    }


    //解压压缩文件
    public void extractCompressFile(MultipartFile sevenFile, String destDir) {
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        File file = new File("testInfo" + File.separator + "test.rar");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            //sevenFile.transferTo(file);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(sevenFile.getInputStream(), file);
            randomAccessFile = new RandomAccessFile(file, "r");
            if (randomAccessFile != null) {
                inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
                if (inArchive != null) {
                    ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
                    for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                        if (!item.isFolder()) {
                            File outputFile = new File(destDir, item.getPath());
                            outputFile.getParentFile().mkdirs();
                            FileOutputStream outputStream = new FileOutputStream(outputFile);
                            MySequentialOutStream mySequentialOutStream = new MySequentialOutStream(outputStream);
                            item.extractSlow(mySequentialOutStream); // 使用自定义的 ISequentialOutStream 实现类
                            mySequentialOutStream.close();
                            outputStream.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.deleteFolder(file);
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public List<FamilyRelations> searchEmpFamilyInfos(Long employeeId) {

        return familyRelationsMapper.findFamilyInfoByEmpId(employeeId);
    }

    @Override
    public List<EmpSimpleInfoVO> countEmpNum() {

        List<EmpSimpleInfoVO> result = new ArrayList<>();
        List<SysDictData> empStatusDicInfos = dictDataMapper.selectDictDataByType("emp_status");
        for (SysDictData empStatusDicInfo : empStatusDicInfos) {
            String dictValue = empStatusDicInfo.getDictValue();
            int statusNum = employeeMapper.findCurrentMonthStatusNum(dictValue);
            EmpSimpleInfoVO empSimpleInfo = new EmpSimpleInfoVO(empStatusDicInfo.getDictLabel(), statusNum);
            result.add(empSimpleInfo);
        }
//        int attendJobNum = employeeMapper.findAttendJobNumWithCurrentMonth();
//        EmpSimpleInfoVO attendEmp = new EmpSimpleInfoVO(empStatusDicInfos.stream().
//                filter(s -> s.getDictValue().equals(EmpStatusEnum.NEW_POSITION.getValue())).findFirst().orElse(new SysDictData()).getDictLabel(), attendJobNum);
//        int resignNum = employeeMapper.findResignNumWithCurrentMonth();
//        EmpSimpleInfoVO resignEmp = new EmpSimpleInfoVO(empStatusDicInfos.stream().
//                filter(s -> s.getDictValue().equals(EmpStatusEnum.RESIGN.getValue())).findFirst().orElse(new SysDictData()).getDictLabel(), resignNum);
//        int fireNum = employeeMapper.findFireNumWithCurrentMonth();
//        EmpSimpleInfoVO fireEmp = new EmpSimpleInfoVO(empStatusDicInfos.stream().
//                filter(s -> s.getDictValue().equals(EmpStatusEnum.FIRE.getValue())).findFirst().orElse(new SysDictData()).getDictLabel(), fireNum);
//        int soonExpiredNum = employeeMapper.findSoonExpiredNumWithCurrentMonth();
//        EmpSimpleInfoVO soonExpiredEmp = new EmpSimpleInfoVO(empStatusDicInfos.stream().
//                filter(s -> s.getDictValue().equals(EmpStatusEnum.ALMOST.getValue())).findFirst().orElse(new SysDictData()).getDictLabel(), soonExpiredNum);
//        int ExpiredNum = employeeMapper.findExpiredNumWithCurrentMonth();
//        EmpSimpleInfoVO ExpiredEmp = new EmpSimpleInfoVO(empStatusDicInfos.stream().
//                filter(s -> s.getDictValue().equals(EmpStatusEnum.EXPIRE.getValue())).findFirst().orElse(new SysDictData()).getDictLabel(), ExpiredNum);
//        int reEmployNum = employeeMapper.findReEmployNumWithCurrentMonth();
//        EmpSimpleInfoVO reEmployEmp = new EmpSimpleInfoVO(empStatusDicInfos.stream().
//                filter(s -> s.getDictValue().equals(EmpStatusEnum.RE_EMPLOY.getValue())).findFirst().orElse(new SysDictData()).getDictLabel(), reEmployNum);
//        result.add(attendEmp);
//        result.add(resignEmp);
//        result.add(fireEmp);
//        result.add(soonExpiredEmp);
//        result.add(ExpiredEmp);
//        result.add(reEmployEmp);
        return result;
    }

    @Override
    public Boolean batchFireEmployees(List<EmpChangeStatusVO> empChangeStatusInfos) {
        for (EmpChangeStatusVO empChangeStatusInfo : empChangeStatusInfos) {
            Long empId = empChangeStatusInfo.getEmpId();
            String empStatus = empChangeStatusInfo.getEmpStatus();
            Date fireTime = empChangeStatusInfo.getFireTime();
            employeeMapper.changeEmpStatusByEmpIdsAndEmpStatus(Collections.singletonList(empId), empStatus, fireTime);
            empHistoryMapper.changeEmpStatusByEmpIdsAndEmpStatus(Collections.singletonList(empId), empStatus, empChangeStatusInfo.getFireTime());
        }
        return true;
    }


    private void batchInsertEmployeeInfos(Long userId, String userName, List<EmpImportVO> empImportInfos) {

        for (EmpImportVO empImportInfo : empImportInfos) {
            EmpAddVO empAdd = transformEmpImportInfoToEmpAdd(empImportInfo);
            List<EmpFamilyVO> familyInfos = new ArrayList<>();
            EmpFamilyVO empFamilyOne = new EmpFamilyVO();
            String oneFamAppellation = empImportInfo.getOneFamAppellation();
            String oneFamPhone = empImportInfo.getOneFamPhone();
            empFamilyOne.setFamAppellation(oneFamAppellation);
            empFamilyOne.setFamContactPhone(oneFamPhone);
            if (!ObjectUtil.areAllFieldsNull(empFamilyOne)) {
                empFamilyOne.setFamEmpSort(1);
                familyInfos.add(empFamilyOne);
            }
            EmpFamilyVO empFamilyTwo = new EmpFamilyVO();
            String twoFamAppellation = empImportInfo.getTwoFamAppellation();
            String twoFamPhone = empImportInfo.getTwoFamPhone();
            empFamilyTwo.setFamAppellation(twoFamAppellation);
            empFamilyTwo.setFamContactPhone(twoFamPhone);
            if (!ObjectUtil.areAllFieldsNull(empFamilyTwo)) {
                empFamilyTwo.setFamEmpSort(2);
                familyInfos.add(empFamilyTwo);
            }
            if (!CollectionUtils.isEmpty(familyInfos)) {
                List<SysDictData> empAppellationDic = dictDataMapper.selectDictDataByType("emp_appellation");
                for (EmpFamilyVO familyInfo : familyInfos) {
                    for (SysDictData empAppellation : empAppellationDic) {
                        if (empAppellation.getDictLabel().equals(familyInfo.getFamAppellation())) {
                            familyInfo.setFamAppellation(empAppellation.getDictValue());
                        }
                    }
                }
            }
            if (empImportInfo.getNativePlace() != null) {
                List<Long> addressIds = new ArrayList<>();
                String[] split = empImportInfo.getNativePlace().split("/");
                int length = split.length;
                if (length == 1) {
                    ChinaAddress chinaAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (chinaAddress != null) {
                        addressIds.add(chinaAddress.getId());
                        empAdd.setNativePlace(addressIds);
                    } else {
                        empAdd.setNativePlace(null);
                    }
                }
                if (length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            empAdd.setNativePlace(addressIds);
                        } else {
                            empAdd.setNativePlace(null);
                        }
                    } else {
                        empAdd.setNativePlace(null);
                    }
                }
                if (length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            ChinaAddress threeAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[2], 3, twoAddress.getId());
                            if (threeAddress != null) {
                                addressIds.add(threeAddress.getId());
                                empAdd.setNativePlace(addressIds);
                            } else {
                                empAdd.setNativePlace(null);
                            }
                        } else {
                            empAdd.setNativePlace(null);
                        }
                    } else {
                        empAdd.setNativePlace(null);
                    }
                }

            }
            if (empImportInfo.getBirthPlace() != null) {
                List<Long> addressIds = new ArrayList<>();
                String[] split = empImportInfo.getBirthPlace().split("/");
                int length = split.length;
                if (length == 1) {
                    ChinaAddress chinaAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (chinaAddress != null) {
                        addressIds.add(chinaAddress.getId());
                        empAdd.setBirthPlace(addressIds);
                    } else {
                        empAdd.setBirthPlace(null);
                    }
                }
                if (length == 2) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            empAdd.setBirthPlace(addressIds);
                        } else {
                            empAdd.setBirthPlace(null);
                        }
                    } else {
                        empAdd.setBirthPlace(null);
                    }
                }
                if (length == 3) {
                    ChinaAddress oneAddress = chinaAddressMapper.findInfoByNameAndLevel(split[0], 1);
                    if (oneAddress != null) {
                        addressIds.add(oneAddress.getId());
                        ChinaAddress twoAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[1], 2, oneAddress.getId());
                        if (twoAddress != null) {
                            addressIds.add(twoAddress.getId());
                            ChinaAddress threeAddress = chinaAddressMapper.findInfoByNameAndLevelAndParentId(split[2], 3, twoAddress.getId());
                            if (threeAddress != null) {
                                addressIds.add(threeAddress.getId());
                                empAdd.setBirthPlace(addressIds);
                            } else {
                                empAdd.setBirthPlace(null);
                            }
                        } else {
                            empAdd.setBirthPlace(null);
                        }
                    } else {
                        empAdd.setBirthPlace(null);
                    }
                }
            }

            empAdd.setEmpFamilyInfos(familyInfos);
            insertEmployee(empAdd, userId, userName, false);
        }
    }


    private EmpAddVO transformEmpImportInfoToEmpAdd(EmpImportVO empImportInfo) {

        EmpAddVO empAdd = new EmpAddVO();
        BeanUtils.copyProperties(empImportInfo, empAdd);

//        List<SysDictData> empTypes = dictDataMapper.selectDictDataByType("emp_type");
//        for (SysDictData empType : empTypes) {
//            if (empType.getDictLabel().equals(empImportInfo.getEmpType())) {
//                empAdd.setEmpType(empType.getDictValue());
//            }
//        }
        if (empImportInfo.getEmpStatus() != null) {
            List<SysDictData> empStatusInfos = dictDataMapper.selectDictDataByType("emp_status");
            for (SysDictData empStatusInfo : empStatusInfos) {
                if (empStatusInfo.getDictLabel().equals(empImportInfo.getEmpStatus())) {
                    empAdd.setEmpStatus(empStatusInfo.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpSalaryLevel() != null) {
            List<SysDictData> empSalaryLevels = dictDataMapper.selectDictDataByType("emp_salary_level");
            for (SysDictData empSalaryLevel : empSalaryLevels) {
                if (empSalaryLevel.getDictLabel().equals(empImportInfo.getEmpSalaryLevel())) {
                    empAdd.setEmpSalaryLevel(empSalaryLevel.getDictValue());
                }
            }
        }


        if (empImportInfo.getEmpEmployingUnits() != null) {
            List<SysDictData> hireCompanies = dictDataMapper.selectDictDataByType("hire_company");
            for (SysDictData hireCompany : hireCompanies) {
                if (hireCompany.getDictLabel().equals(empImportInfo.getEmpEmployingUnits())) {
                    empAdd.setEmpEmployingUnits(hireCompany.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpHrCompany() != null) {
            List<SysDictData> hrCompanies = dictDataMapper.selectDictDataByType("emp_hr_company");
            for (SysDictData hrCompany : hrCompanies) {
                if (hrCompany.getDictLabel().equals(empImportInfo.getEmpHrCompany())) {
                    empAdd.setEmpHrCompany(hrCompany.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPosition() != null) {
            List<SysDictData> empPositions = dictDataMapper.selectDictDataByType("emp_position");
            for (SysDictData empPosition : empPositions) {
                if (empPosition.getDictLabel().equals(empImportInfo.getEmpPosition())) {
                    empAdd.setEmpPosition(empPosition.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPositionCategory() != null) {
            List<SysDictData> empPositionCategoryDic = dictDataMapper.selectDictDataByType("emp_position_category");
            for (SysDictData empPositionCategory : empPositionCategoryDic) {
                if (empPositionCategory.getDictLabel().equals(empImportInfo.getEmpPositionCategory())) {
                    empAdd.setEmpPositionCategory(empPositionCategory.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPositionLevel() != null) {
            List<SysDictData> empPositionLevels = dictDataMapper.selectDictDataByType("emp_position_level");
            for (SysDictData empPositionLevel : empPositionLevels) {
                if (empPositionLevel.getDictLabel().equals(empImportInfo.getEmpPositionLevel())) {
                    empAdd.setEmpPositionLevel(empPositionLevel.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpPoliticalStatus() != null) {
            List<SysDictData> empPoliticalStatusInfos = dictDataMapper.selectDictDataByType("emp_political_status");
            for (SysDictData empPoliticalStatusInfo : empPoliticalStatusInfos) {
                if (empPoliticalStatusInfo.getDictLabel().equals(empImportInfo.getEmpPoliticalStatus())) {
                    empAdd.setEmpPoliticalStatus(empPoliticalStatusInfo.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpEducation() != null) {
            List<SysDictData> empEducations = dictDataMapper.selectDictDataByType("emp_education");
            for (SysDictData empEducation : empEducations) {
                if (empEducation.getDictLabel().equals(empImportInfo.getEmpEducation())) {
                    empAdd.setEmpEducation(empEducation.getDictValue());
                }
                if (empEducation.getDictLabel().equals(empImportInfo.getHighestEducation())) {
                    empAdd.setHighestEducation(empEducation.getDictValue());
                }
            }
        }

        if (empImportInfo.getEmpGender() != null) {
            List<SysDictData> empSexDic = dictDataMapper.selectDictDataByType("emp_sex");
            for (SysDictData empSex : empSexDic) {
                if (empSex.getDictLabel().equals(empImportInfo.getEmpGender())) {
                    empAdd.setEmpGender(empSex.getDictValue());
                }
            }
        }


//        List<SysDictData> empCategories = dictDataMapper.selectDictDataByType("emp_category");
//        for (SysDictData empCategory : empCategories) {
//            if (empCategory.getDictLabel().equals(empImportInfo.getEmpCategory())) {
//                empAdd.setEmpCategory(empCategory.getDictValue());
//            }
//        }
        if (empImportInfo.getEmpTitle() != null) {
            List<SysDictData> empTitles = dictDataMapper.selectDictDataByType("emp_title");
            for (SysDictData empTitle : empTitles) {
                if (empTitle.getDictLabel().equals(empImportInfo.getEmpTitle())) {
                    empAdd.setEmpTitle(empTitle.getDictValue());
                }
            }
        }

        String empUnitDeptName = empImportInfo.getEmpDeptName();
        String regex = "^(.)+-(.)+$";
        if (empUnitDeptName != null) {
            if (empUnitDeptName.matches(regex)) {
                String[] split = empUnitDeptName.split("-");
                String empUnit = split[0];
                String empDept = split[1];
                Department department = departmentMapper.findDepartmentInfoByUnitAndDeptName(empUnit, empDept);
                if (department != null) {
                    empAdd.setEmpDeptId(department.getDeptId());
                    empAdd.setEmpDeptName(department.getDeptName());
                }
            } else {
                Department department = departmentMapper.findDepartmentInfoByName(empUnitDeptName);
                if (department != null) {
                    empAdd.setEmpDeptId(department.getDeptId());
                    empAdd.setEmpDeptName(department.getDeptName());
                }
            }
        }
        return empAdd;
    }


    private ExcelImportResult<EmpImportVO> getExcelDate(MultipartFile file, Class<EmpImportVO> empImportClass) {

        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            params.setStartRows(0);
            params.setNeedVerify(true);
            params.setVerifyHandler(employeeExcelImportVerifyHandler);
            return ExcelImportUtil.importExcelMore(file.getInputStream(), empImportClass, params);
        } catch (Exception e) {
            log.error("获取excel数据失败", e);
            throw new ServiceException("获取excel数据失败");
        } finally {
            // 清除threadLocal 防止内存泄漏
            ThreadLocal<List<IdCheckVO>> threadLocal = employeeExcelImportVerifyHandler.getThreadLocal();
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }

    /**
     * Map内，对象计数的方法，如果第一次key入map,数量记1，如果非第一次，数量累加
     *
     * @param map
     * @param mapKey
     */
    public void putMap(Map<String, PieChartVO> map, String mapKey) {
        if (mapKey != null && mapKey.length() > 0) {
            if (map.containsKey(mapKey)) {
                map.put(mapKey, new PieChartVO(mapKey, map.get(mapKey).getValue() + 1));
            } else {
                map.put(mapKey, new PieChartVO(mapKey, 1));
            }
        }
    }


    @Override
    public List<StabilityResultVO> getDeptStabilityData(StabilityQueryVO queryVO) {
        return employeeMapper.getDeptStabilityData(queryVO);
    }

    @Override
    public List<StabilityResultVO> getUnitStabilityData(StabilityQueryVO queryVO) {
        return employeeMapper.getUnitStabilityData(queryVO);
    }

    @Override
    public Boolean dealEmpIdCardInfo(Boolean isEncode) {

        if (isEncode) {
            List<Employee> allEmployees = employeeMapper.findAllEmployees();
            String regex = "\\d{17}[0-9Xx]";
            for (Employee employee : allEmployees) {
                String empIdcard = employee.getEmpIdcard();
                if (empIdcard != null && empIdcard.matches(regex)) {
                    employeeMapper.encodeEmployeeIdCardInfo(empIdcard, employee.getEmpId());
                }
            }
            List<EmpHistory> empHistories = empHistoryMapper.findAllEmployeeHistory();
            for (EmpHistory empHistory : empHistories) {
                String empIdcard = empHistory.getEmpIdcard();
                if (empIdcard != null && empIdcard.matches(regex)) {
                    empHistoryMapper.encodeEmpHistoryIdCardInfo(empHistory.getEmpIdcard(), empHistory.getHistoryId());
                }
            }
        } else {
            List<Employee> allEmployees = employeeMapper.findAllEmployees();
            String regex = "\\d{17}[0-9Xx]";
            for (Employee employee : allEmployees) {
                String empIdcard = employee.getEmpIdcard();
                if (empIdcard != null && !empIdcard.matches(regex)) {
                    employeeMapper.decodeEmployeeIdCardInfo(empIdcard, employee.getEmpId());
                }
            }
            List<EmpHistory> empHistories = empHistoryMapper.findAllEmployeeHistory();
            for (EmpHistory empHistory : empHistories) {
                String empIdcard = empHistory.getEmpIdcard();
                if (empIdcard != null && !empIdcard.matches(regex)) {
                    empHistoryMapper.decodeEmpHistoryIdCardInfo(empHistory.getEmpIdcard(), empHistory.getHistoryId());
                }
            }
        }
        return true;
    }

    @Override
    public List<EmpPicInfoVO> selectEmpPicList(EmpSearchSimpleVO employee) {

        List<Employee> employees = employeeMapper.findEmpPicInfos(employee);
        List<Long> deptIds = employees.stream().map(Employee::getEmpDeptId).collect(Collectors.toList());
        Map<Long, Department> departmentMap = new HashMap<>(16);
        Map<Long, EmployingUnits> unitsMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            List<Department> deptInfos = departmentMapper.findDeptInfosByDeptIds(deptIds);
            departmentMap = deptInfos.stream().collect(Collectors.toMap(Department::getDeptId, Function.identity()));
            List<Long> unitIds = deptInfos.stream().map(Department::getDeptUnitId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(unitIds)) {
                unitsMap = unitsMapper.findUnitInfoByUnitIds(unitIds).stream().
                        collect(Collectors.toMap(EmployingUnits::getUnitId, Function.identity()));
            }
        }
        List<EmpPicInfoVO> empPicInfos = new ArrayList<>();
        for (Employee empInfo : employees) {
            EmpPicInfoVO empPicInfo = new EmpPicInfoVO();
            empPicInfo.setEmpId(empInfo.getEmpId());
            empPicInfo.setEmpName(empInfo.getEmpName());
            if (!StringUtils.isEmpty(empInfo.getEmpIdcard())) {
                empPicInfo.setEmpIdCard(Base64.desensitize(empInfo.getEmpIdcard()));
            }
            Department department = departmentMap.getOrDefault(empInfo.getEmpDeptId(), new Department());
            empPicInfo.setDeptName(department.getDeptName());
            Long deptUnitId = department.getDeptUnitId();
            if (deptUnitId != null) {
                String unitName = unitsMap.getOrDefault(deptUnitId, new EmployingUnits()).getUnitName();
                empPicInfo.setUnitName(unitName);
            }
            empPicInfos.add(empPicInfo);
        }
        return empPicInfos;
    }

    @Override
    public List<ChinaAddressTreeVO> getChinaAddress() {
        List<ChinaAddress> chinaAddresses = chinaAddressMapper.findAll();
        if (CollectionUtils.isEmpty(chinaAddresses)) {
            return new ArrayList<>();
        }

        return buildChinaAddressTree(chinaAddresses);
    }


    private List<ChinaAddressTreeVO> buildChinaAddressTree(List<ChinaAddress> chinaAddresses) {
        List<ChinaAddressTreeVO> chinaAddressList = chinaAddresses.stream().map(s ->
                new ChinaAddressTreeVO(s.getId().intValue(), s.getName(), s.getParentId().intValue())).collect(Collectors.toList());
        Map<Integer, List<ChinaAddressTreeVO>> simpleChinaAddressMap = chinaAddressList.stream().collect(Collectors.
                groupingBy(ChinaAddressTreeVO::getParentId));
        if (!simpleChinaAddressMap.containsKey(0)) {
            return new ArrayList<>();
        }
        simpleChinaAddressMap.get(0).forEach(chinaAddressesTree -> findSimpleChild(simpleChinaAddressMap, chinaAddressesTree));
        return simpleChinaAddressMap.get(1).stream().sorted(Comparator.comparing(ChinaAddressTreeVO::getId)).collect(Collectors.toList());
    }

    private void findSimpleChild(Map<Integer, List<ChinaAddressTreeVO>> simpleChinaAddressMap, ChinaAddressTreeVO chinaAddressesTree) {
        List<ChinaAddressTreeVO> simpleChinaAddressTreeList = simpleChinaAddressMap.get(chinaAddressesTree.getId());
        if (!CollectionUtils.isEmpty(simpleChinaAddressTreeList)) {
            simpleChinaAddressTreeList.sort(Comparator.comparing(ChinaAddressTreeVO::getId));
            chinaAddressesTree.setChildren(simpleChinaAddressTreeList);
            chinaAddressesTree.getChildren().forEach(t -> findSimpleChild(simpleChinaAddressMap, t));
        }
    }

    @Override
    public Employee selectDeletedEmpByEmpName(String empName) {
        List<Employee> employeeList = employeeMapper.selectDeletedEmpByEmpName(empName);
        Employee employee = null;
        if (employeeList != null && employeeList.size() > 0) {
            employee = employeeList.get(0);
        }
        return employee;
    }

    @Override
    public void recoveryDeletedEmp(EmployeeVO vo) {
        employeeMapper.recoveryDeletedEmp(vo);
    }

}
