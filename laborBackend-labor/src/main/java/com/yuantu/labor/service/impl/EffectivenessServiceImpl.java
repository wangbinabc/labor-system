package com.yuantu.labor.service.impl;

import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.labor.cenum.AttendStatusEnum;
import com.yuantu.labor.cenum.EmpStatusEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.domain.EmpEffectiveness;
import com.yuantu.labor.domain.EmpSalary;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEffectivenessService;
import com.yuantu.labor.vo.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EffectivenessServiceImpl implements IEffectivenessService {
    @Autowired
    private DeptPerformanceMapper deptPerformanceMapper;

    @Autowired
    private EmpAttendanceMapper empAttendanceMapper;

    @Autowired
    private EmpPerformanceMapper empPerformanceMapper;

    @Autowired
    private EmpSalaryMapper empSalaryMapper;

    @Getter
    @Autowired
    private EmpEffectivenessMapper empEffectivenessMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmpCredentialsMapper empCredentialsMapper;

    /**
     * 部门效能
     */
    @Override
    public List<DeptEffectivenessCountVO> selectDeptEffectivenessList(DeptEffectivenessSearchVO deptEffectivenessSearchVO) {

        List<DeptEffectivenessVO> deptAttendanceEffectivenessList = empAttendanceMapper.selectDeptAttendanceEffectivenessList(deptEffectivenessSearchVO);

        List<DeptEffectivenessCountVO> deptList = new ArrayList<>();
        List<EmpEffectivenessCountVO> empEffectivenessList = selectEmpEffectivenessList(null);

        //   Collections.sort(empEffectivenessList, Comparator.comparing(e -> (e.getDeptId())));
        //                "empProfitValue"//员工个人利润值
//                "attendance"//出勤率
//                "performance"//绩效指数
//                "cost" //成本指数
//                "efficiency"//效能指数
        Map<Long, Double> profitValueCount = empEffectivenessList.stream().filter(p -> p.getDeptId() != null).collect(
                Collectors.groupingBy(EmpEffectivenessCountVO::getDeptId,
                        Collectors.averagingDouble(EmpEffectivenessCountVO::getEmpProfitValue)));

        Map<Long, Double> performanceCount = empEffectivenessList.stream().filter(p -> p.getDeptId() != null).collect(
                Collectors.groupingBy(EmpEffectivenessCountVO::getDeptId,
                        Collectors.summingDouble(EmpEffectivenessCountVO::getPerformance)));

        Map<Long, Double> costCount = empEffectivenessList.stream().filter(p -> p.getDeptId() != null).collect(
                Collectors.groupingBy(EmpEffectivenessCountVO::getDeptId,
                        Collectors.summingDouble(EmpEffectivenessCountVO::getCost)));

        Map<Long, Double> efficiencyCount = empEffectivenessList.stream().filter(p -> p.getDeptId() != null).collect(
                Collectors.groupingBy(EmpEffectivenessCountVO::getDeptId,
                        Collectors.averagingDouble(EmpEffectivenessCountVO::getEfficiency)));

        for (Long key : profitValueCount.keySet()) {
            DeptEffectivenessCountVO deptEffectivenessCountVO = new DeptEffectivenessCountVO();
            deptEffectivenessCountVO.setDeptId(key);
            deptEffectivenessCountVO.setDeptProfitValue(profitValueCount.get(key));
            deptEffectivenessCountVO.setPerformance(performanceCount.get(key));
            deptEffectivenessCountVO.setCost(costCount.get(key));
            deptEffectivenessCountVO.setEfficiency(efficiencyCount.get(key));
            deptList.add(deptEffectivenessCountVO);
        }

        for (DeptEffectivenessVO attendance : deptAttendanceEffectivenessList) {

            DeptEffectivenessCountVO deptEffectivenessCountVO = new DeptEffectivenessCountVO();
            deptEffectivenessCountVO.setDeptId(attendance.getDeptId());


            if (deptList.contains(deptEffectivenessCountVO)) {
                int index = deptList.indexOf(deptEffectivenessCountVO);
                deptEffectivenessCountVO = deptList.get(index);
                deptEffectivenessCountVO.setDeptName(attendance.getDeptName());
                deptEffectivenessCountVO.setAttendance(attendance.getResult());
            } else {
                deptEffectivenessCountVO.setDeptName(attendance.getDeptName());
                deptEffectivenessCountVO.setAttendance(attendance.getResult());
                deptList.add(deptEffectivenessCountVO);
            }
        }

        for (DeptEffectivenessCountVO deptEffectivenessCountVO : deptList) {
            String deptName = departmentMapper.selectDepartmentByDeptId(deptEffectivenessCountVO.getDeptId()).getDeptName();
            deptEffectivenessCountVO.setDeptName(deptName);
            deptEffectivenessCountVO.setSearchYearMonth(deptEffectivenessSearchVO != null ? deptEffectivenessSearchVO.getSearchYearMonth() : new SimpleDateFormat("yyyy-MM").format(new Date()));
        }


        // List<DeptEffectivenessVO> deptPerformanceEffectivenessList = deptPerformanceMapper.selectDeptPerformanceEffectivenessList(deptEffectivenessSearchVO);
//        for (EmpEffectivenessCountVO empEffectivenessCountVO: empEffectivenessList){
//            DeptEffectivenessCountVO count = new DeptEffectivenessCountVO();
//            count.setDeptId(empEffectivenessCountVO.getDeptId());
//            for (DeptEffectivenessCountVO deptEffectivenessCountVO :deptList){
//                if (!deptList.contains(count)){
//                    deptEffectivenessCountVO
//                            .setAttendance(empEffectivenessCountVO.getAttendance());
//                    deptEffectivenessCountVO.
//                }
//            }
//
//        }


//        for (DeptEffectivenessVO attendance : deptAttendanceEffectivenessList) {
//            for (DeptEffectivenessVO performance : deptPerformanceEffectivenessList) {
//                if (attendance.getDeptName().equals(performance.getDeptName())) {
//                    Map map = new HashMap<>();
//                    map.put("deptName", attendance.getDeptName());
//                    map.put("attendance", attendance.getResult());
//                    map.put("performance", performance.getResult());
//                    list.add(map);
//                }
//
//            }
//        }
//        for (DeptEffectivenessVO attendance : deptAttendanceEffectivenessList){
//            if(!deptPerformanceEffectivenessList.contains(attendance)){
//                Map map = new HashMap<>();
//                map.put("deptName", attendance.getDeptName());
//                map.put("attendance", attendance.getResult());
//                map.put("performance", "0.0");
//                list.add(map);
//            }
//        }
//
//        for (DeptEffectivenessVO performance : deptPerformanceEffectivenessList){
//            if(!deptAttendanceEffectivenessList.contains(performance)){
//                Map map = new HashMap<>();
//                map.put("deptName", performance.getDeptName());
//                map.put("attendance","0.0" );
//                map.put("performance", performance.getResult());
//                list.add(map);
//            }
//        }
        Collections.sort(deptList, Comparator.comparing(DeptEffectivenessCountVO::getDeptId));
        return deptList;
    }


    @Override
    public List<EmpEffectivenessCountVO> selectEmpEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO) {
        List<EmpEffectivenessVO> empAttendanceEffectivenessList = empAttendanceMapper.selectEmpAttendanceEffectivenessList(empEffectivenessSearchVO);
        //                "empProfitValue"//员工个人利润值
//                "attendance"//出勤率
//                "performance"//绩效指数
//                "cost" //成本指数
//                "efficiency"//效能指数

        if (empEffectivenessSearchVO != null && empEffectivenessSearchVO.getSearchYearMonth() != null) {
            Integer month = Integer.valueOf(empEffectivenessSearchVO.getSearchYearMonth().split("-")[1]);
            if (month <= 3) {
                empEffectivenessSearchVO.setSearchCycle("1");
            } else if (month <= 6) {
                empEffectivenessSearchVO.setSearchCycle("2");
            } else if (month <= 9) {
                empEffectivenessSearchVO.setSearchCycle("3");
            } else if (month <= 12) {
                empEffectivenessSearchVO.setSearchCycle("4");
            }
            empEffectivenessSearchVO.setSearchYear(empEffectivenessSearchVO.getSearchYearMonth().split("-")[0]);
        }


        List<EmpEffectivenessVO> empPerformanceEffectivenessList = empPerformanceMapper.selectEmpPerformanceEffectivenessList(empEffectivenessSearchVO);
        List<EmpEffectivenessCountVO> list = new ArrayList<>();

        EmpEffectiveness empEffectivenessSearch = new EmpEffectiveness();
        Employee employeeSearch = new Employee();
        EmpSalary empSalarySearch = new EmpSalary();
        EmpCredentials empCredentialsSearch = new EmpCredentials();
        if (Objects.nonNull(empEffectivenessSearchVO) && Objects.nonNull(empEffectivenessSearchVO.getSearchYearMonth())) {
            empEffectivenessSearch.setEffYearMonth(empEffectivenessSearchVO.getSearchYearMonth());
            empSalarySearch.setSalaryYearMonth(empEffectivenessSearchVO.getSearchYearMonth());
            employeeSearch.setEmpIdcard(empEffectivenessSearchVO.getEmpIdcard());
            employeeSearch.setEmpName(empEffectivenessSearchVO.getEmpName());
            empCredentialsSearch.setCredEmpIdcard(empEffectivenessSearchVO.getEmpIdcard());
            empCredentialsSearch.setCredEmpName(empEffectivenessSearchVO.getEmpName());
        } else {
            String nowYM = new SimpleDateFormat("yyyy-MM").format(new Date());
            empEffectivenessSearch.setEffYearMonth(nowYM);
            empSalarySearch.setSalaryYearMonth(nowYM);
        }

        List<EmpSalary> empSalaries = empSalaryMapper.selectEmpSalaryList(empSalarySearch);
        Map<Long, List<EmpSalary>> empSalarieGroup = empSalaries.stream().collect(Collectors.groupingBy(EmpSalary::getSalaryEmpId));


        List<EmpEffectiveness> empEffectivenesses = empEffectivenessMapper.selectEmpEffectivenessList(empEffectivenessSearch);
        Map<Long, List<EmpEffectiveness>> empEffectivenessesGroup = empEffectivenesses.stream().collect(Collectors.groupingBy(EmpEffectiveness::getEffEmpId));


        List<Employee> employeeList = employeeMapper.selectEmployeeList(employeeSearch);
        Map<Long, List<Employee>> employeesGroup = employeeList.stream().collect(Collectors.groupingBy(Employee::getEmpId));


        List<EmpCredentials> empCredentialsList = empCredentialsMapper.selectEmpCredentialsList(empCredentialsSearch);
        Map<Long, List<EmpCredentials>> empCredentialsGroup = empCredentialsList.stream().collect(Collectors.groupingBy(EmpCredentials::getCredEmpId));


        for (EmpEffectivenessVO attendance : empAttendanceEffectivenessList) {
            for (EmpEffectivenessVO performance : empPerformanceEffectivenessList) {
                if (attendance.getEmpId().equals(performance.getEmpId())) {
//                    EmpSalary empSalarySearch = new EmpSalary();
//                    empSalarySearch.setSalaryEmpId(attendance.getEmpId());


//                    EmpEffectiveness empEffectivenessSearch = new EmpEffectiveness();
//                    empEffectivenessSearch.setEffEmpId(attendance.getEmpId());
//                    if (Objects.nonNull(empEffectivenessSearchVO)) {
//                        empEffectivenessSearch.setEffYearMonth(empEffectivenessSearchVO.getSearchYearMonth());
//                        empSalarySearch.setSalaryYearMonth(empEffectivenessSearchVO.getSearchYearMonth());
//                    }
//
//
//                    String salaryNetPay   =  empSalaryMapper.selectEmpSalaryList(empSalarySearch).size()>0?
//                            empSalaryMapper.selectEmpSalaryList(empSalarySearch).get(0).getSalaryNetPay():"0.0";
//
//                    String empProfitValue = empEffectivenessMapper.selectEmpEffectivenessList(empEffectivenessSearch).size()>0?
//                            empEffectivenessMapper.selectEmpEffectivenessList(empEffectivenessSearch).get(0).getEffEmpProfitValue():"0.0";

                    Double attendanceCount = attendance.getResult() == null ? 0.0 : attendance.getResult();
                    Double performanceCount = performance.getResult() == null ? 0.0 : performance.getResult();

                    String salaryNetPay = empSalarieGroup != null && empSalarieGroup.get(attendance.getEmpId()) != null &&
                            empSalarieGroup.get(attendance.getEmpId()).size() != 0
                            ? empSalarieGroup.get(attendance.getEmpId()).get(0).getSalaryNetPay() : "0.0";
                    Employee employee = employeesGroup != null && employeesGroup.get(attendance.getEmpId()) != null
                            && employeesGroup.get(attendance.getEmpId()).size() != 0 ? employeesGroup.get(attendance.getEmpId()).get(0) : null;
                    Integer empProfitValue = 0;

                    if (employee != null) {
                        Integer highestEducation = employee.getHighestEducation() != null ? Integer.valueOf(employee.getHighestEducation()) : 0;//学历
                        Integer empTitle = employee.getEmpTitle() != null ? Integer.valueOf(employee.getEmpTitle()) : 0;//职称
                        String nowYear = new SimpleDateFormat("yyyy").format(new Date());
                        String attendYear = employee.getAttendTime() != null ? new SimpleDateFormat("yyyy").format(employee.getAttendTime()) : nowYear;//参加工作年

                        int workYear = Integer.valueOf(nowYear) - Integer.valueOf(attendYear);
                        int workYearNum = 0;
                        switch (workYear) {
                            case 0:
                                workYearNum = 0;
                                break;
                            case 1:
                                workYearNum = 1;
                                break;
                            case 2:
                                workYearNum = 2;
                                break;
                            case 3:
                                workYearNum = 3;
                                break;
                            case 4:
                                workYearNum = 4;
                                break;
                            default:
                                workYearNum = 5;
                        }


                        Integer empCredentialsCount = empCredentialsGroup.get(attendance.getEmpId()) != null ? empCredentialsGroup.get(attendance.getEmpId()).size() : 0;//证书数量
                        int empCredentialsNum = 0;
                        switch (empCredentialsCount) {
                            case 0:
                                empCredentialsNum = 0;
                                break;
                            case 1:
                                empCredentialsNum = 1;
                                break;
                            case 2:
                                empCredentialsNum = 2;
                                break;
                            case 3:
                                empCredentialsNum = 3;
                                break;
                            case 4:
                                empCredentialsNum = 4;
                                break;
                            default:
                                empCredentialsNum = 5;
                        }


                        int educationNum = 0;
                        switch (highestEducation) {
                            case 1:
                                educationNum = 3;
                                break;
                            case 2:
                                educationNum = 6;
                                break;
                            case 3:
                                educationNum = 7;
                                break;
                            case 4:
                                educationNum = 8;
                                break;
                            case 5:
                                educationNum = 9;
                                break;
                            case 6:
                                educationNum = 10;
                                break;
                            default:
                                educationNum = 0;
                        }

                        int titleNum = 0;
                        switch (empTitle) {
                            case 1:
                                titleNum = 1;
                                break;
                            case 2:
                                titleNum = 2;
                                break;
                            case 3:
                                titleNum = 3;
                                break;
                            default:
                                titleNum = 0;
                        }
                        //人员效能=学历得分+职称得分+职业资格证书得分+工作经验得分
                        empProfitValue = workYearNum + empCredentialsNum + educationNum + titleNum;

                    }

                    String profit = empEffectivenessesGroup != null && empEffectivenessesGroup.get(attendance.getEmpId()) != null &&
                            empEffectivenessesGroup.get(attendance.getEmpId()).size() != 0
                            ? empEffectivenessesGroup.get(attendance.getEmpId()).get(0).getEffEmpProfitValue() : "0.0";


                    //新效能指数=人员效能*绩效指数/（出勤率*成本指数）
                    Double efficiency = salaryNetPay.equals("0.0") || empProfitValue.equals(0)
                            || performanceCount.equals(0.0) || attendanceCount.equals(0.0)
                            ? 0.0 :
                            Double.valueOf(empProfitValue) * 100 * performanceCount / (attendanceCount * Double.valueOf(salaryNetPay)) * 10000;


                    EmpEffectivenessCountVO empEffectivenessCountVO = new EmpEffectivenessCountVO();
                    empEffectivenessCountVO.setEmpId(attendance.getEmpId());
                    empEffectivenessCountVO.setEmpName(attendance.getEmpName());
                    empEffectivenessCountVO.setEmpIdcard(attendance.getEmpIdcard());
                    empEffectivenessCountVO.setEmpProfitValue(Double.valueOf(empProfitValue) * 100);
                    empEffectivenessCountVO.setCost(Double.valueOf(salaryNetPay) / 10000);
                    empEffectivenessCountVO.setAttendance(attendanceCount);
                    empEffectivenessCountVO.setPerformance(performanceCount);
                    empEffectivenessCountVO.setEfficiency(efficiency);
                    empEffectivenessCountVO.setProfit(Double.valueOf(profit));
                    empEffectivenessCountVO.setDeptId(attendance.getDeptId());
                    empEffectivenessCountVO.setDeptName(attendance.getDeptName());
                    empEffectivenessCountVO.setSearchYearMonth(empEffectivenessSearchVO != null ? empEffectivenessSearchVO.getSearchYearMonth() : new SimpleDateFormat("yyyy-MM").format(new Date()));
                    list.add(empEffectivenessCountVO);
                }
            }
        }
        Collections.sort(list, Comparator.comparing(EmpEffectivenessCountVO::getEmpId));
        return list;
    }


    @Override
    public TableDataInfo selectDeptEffectiveness(DeptEffectivenessSearchVO deptEffectivenessSearchVO) {

        TableDataInfo dataInfo = new TableDataInfo();
        Date now = new Date();
        List<Department> departments = departmentMapper.findAllDeptInfos();
        if (CollectionUtils.isEmpty(departments)) {
            dataInfo.setCode(200);
            dataInfo.setMsg("查询成功");
            dataInfo.setTotal(0);
            dataInfo.setRows(new ArrayList<>());
            return dataInfo;
        }
        if (deptEffectivenessSearchVO.getDeptName() != null) {
            departments = departments.stream().filter(s -> s.getDeptName() != null).
                    filter(s -> s.getDeptName().contains(deptEffectivenessSearchVO.getDeptName())).collect(Collectors.toList());
        }
        List<DeptEfficiency> deptEfficiencies = new ArrayList<>();
        List<Long> deptIds = departments.stream().map(Department::getDeptId).collect(Collectors.toList());
        Map<Long, List<Employee>> empDeptMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(deptIds)) {
            empDeptMap = employeeMapper.findEmpInfosByEmpDeptIds(deptIds).stream().
                    collect(Collectors.groupingBy(Employee::getEmpDeptId));
        }
        for (Department dept : departments) {
            DeptEfficiency deptEfficiency = new DeptEfficiency();
            deptEfficiency.setDeptId(dept.getDeptId());
            deptEfficiency.setDeptName(dept.getDeptName());
            deptEfficiency.setCreateTime(now);
            List<Employee> employees = empDeptMap.get(dept.getDeptId());
            Date searchTime = null;
            if (deptEffectivenessSearchVO.getSearchYearMonth() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                try {
                    searchTime = dateFormat.parse(deptEffectivenessSearchVO.getSearchYearMonth());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!CollectionUtils.isEmpty(employees)) {
                int newRecruit = employees.stream().filter(s -> EmpStatusEnum.NEW_POSITION.getKey().
                        equals(s.getEmpStatus())).collect(Collectors.toList()).size();
                int resignNum = employees.stream().filter(s -> EmpStatusEnum.RESIGN.getKey().equals(s.getEmpStatus())
                        || EmpStatusEnum.FIRE.getKey().equals(s.getEmpStatus())).collect(Collectors.toList()).size();
                double stable = (employees.size() - ((double) (newRecruit + resignNum) / 2)) / (double) employees.size();
                List<EmpEfficiency> empEfficiencyInfos = getEmpEfficiencyInfos(searchTime, employees);
                double deptProfitSum = 0.0;
                double deptAttendRatioSum = 0.0;
                double deptPerformRatioSum = 0.0;
                double deptCostRatioSum = 0.0;
                double deptEffectRatioSum = 0.0;
                for (EmpEfficiency empEfficiencyInfo : empEfficiencyInfos) {
                    deptProfitSum = deptProfitSum + Double.parseDouble(empEfficiencyInfo.getProfit());
                    deptAttendRatioSum = deptAttendRatioSum + Double.parseDouble(empEfficiencyInfo.getAttendRatio());
                    deptPerformRatioSum = deptPerformRatioSum + Double.parseDouble(empEfficiencyInfo.getPerformRatio());
                    deptCostRatioSum = deptCostRatioSum + Double.parseDouble(empEfficiencyInfo.getCostRatio());
                    deptEffectRatioSum = deptEffectRatioSum + Double.parseDouble(empEfficiencyInfo.getEffectRatio());
                }
                deptEfficiency.setDeptProfit(String.format("%.2f", deptProfitSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptAttendRatio(String.format("%.2f", deptAttendRatioSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptPerformRatio(String.format("%.2f", deptPerformRatioSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptCostRatio(String.format("%.2f", deptCostRatioSum / empEfficiencyInfos.size()));
                deptEfficiency.setDeptEffectRatio(String.format("%.2f", deptEffectRatioSum / empEfficiencyInfos.size() * stable));
            } else {
                deptEfficiency.setDeptProfit("0");
                deptEfficiency.setDeptAttendRatio("0");
                deptEfficiency.setDeptPerformRatio("0");
                deptEfficiency.setDeptCostRatio("0");
                deptEfficiency.setDeptEffectRatio("0");
            }
            deptEfficiencies.add(deptEfficiency);
        }
        List<DeptEffectivenessCountVO> result = getPageResultForDept(deptEfficiencies,
                deptEffectivenessSearchVO.getPageSize(), deptEffectivenessSearchVO.getPageNum());
        dataInfo.setCode(200);
        dataInfo.setMsg("查询成功");
        dataInfo.setTotal(deptEfficiencies.size());
        dataInfo.setRows(result);

        return dataInfo;
    }

    private List<DeptEffectivenessCountVO> getPageResultForDept(List<DeptEfficiency> result, Integer pageSize, Integer pageNum) {


        int beginIndex = 0;
        int endIndex = 0;
        beginIndex = pageSize * (pageNum - 1);
        endIndex = Math.min(pageNum * pageSize, result.size());
        if (beginIndex >= result.size()) {
            //超出范围则无数据
            result = new ArrayList<>();
        } else {
            endIndex = Math.min(result.size(), endIndex);
            result = result.subList(beginIndex, endIndex);
        }
        List<DeptEffectivenessCountVO> pageResult = new ArrayList<>();
        for (DeptEfficiency deptEfficiency : result) {
            DeptEffectivenessCountVO deptEffectivenessCount = new DeptEffectivenessCountVO();
            deptEffectivenessCount.setDeptId(deptEfficiency.getDeptId());
            deptEffectivenessCount.setDeptName(deptEfficiency.getDeptName());
            deptEffectivenessCount.setAttendance(Double.parseDouble(deptEfficiency.getDeptAttendRatio()));
            deptEffectivenessCount.setCost(Double.parseDouble(deptEfficiency.getDeptCostRatio()));
            deptEffectivenessCount.setDeptProfitValue(Double.parseDouble(deptEfficiency.getDeptProfit()));
            deptEffectivenessCount.setPerformance(Double.parseDouble(deptEfficiency.getDeptPerformRatio()));
            deptEffectivenessCount.setEfficiency(Double.parseDouble(deptEfficiency.getDeptEffectRatio()));
            pageResult.add(deptEffectivenessCount);
        }

        return pageResult;

    }

    @Override
    public TableDataInfo selectEmpEffectiveness(EmpEffectivenessSearchVO queryVO) {

        try {
            TableDataInfo dataInfo = new TableDataInfo();
            Date now = null;
            if (queryVO.getSearchYear() != null) {
                String searchYearMonth = queryVO.getSearchYearMonth();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                now = dateFormat.parse(searchYearMonth);
            }
            List<Employee> allEmployees = employeeMapper.findAllEmployees();
            if (CollectionUtils.isEmpty(allEmployees)) {
                dataInfo.setCode(200);
                dataInfo.setMsg("查询成功");
                dataInfo.setTotal(0);
                dataInfo.setRows(new ArrayList<>());
                return dataInfo;
            }
            List<EmpEfficiency> empEfficiencyInfos = getEmpEfficiencyInfos(now, allEmployees);
            List<Long> empIds = new ArrayList<>();
            if (queryVO.getEmpName() != null) {
                empIds = employeeMapper.findInfoByEmpIdCardOrName(queryVO.getEmpName()).stream().
                        map(Employee::getEmpId).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(empIds)) {
                List<Long> finalEmpIds = empIds;
                empEfficiencyInfos = empEfficiencyInfos.stream().filter(s -> finalEmpIds.contains(s.getEmpId())).collect(Collectors.toList());
            }
            List<EmpEffectivenessCountVO> list = getPageResultForEmp(empEfficiencyInfos, queryVO.getPageSize(), queryVO.getPageNum());
            dataInfo.setCode(200);
            dataInfo.setMsg("查询成功");
            dataInfo.setTotal(empEfficiencyInfos.size());
            dataInfo.setRows(list);
            return dataInfo;
        } catch (ParseException e) {
            throw new ServiceException("查询失败");
        }
    }


    private List<EmpEffectivenessCountVO> getPageResultForEmp(List<EmpEfficiency> result, Integer pageSize, Integer pageNum) {

        int beginIndex = 0;
        int endIndex = 0;
        beginIndex = pageSize * (pageNum - 1);
        endIndex = Math.min(pageNum * pageSize, result.size());
        if (beginIndex >= result.size()) {
            //超出范围则无数据
            result = new ArrayList<>();
        } else {
            endIndex = Math.min(result.size(), endIndex);
            result = result.subList(beginIndex, endIndex);
        }
        List<EmpEffectivenessCountVO> pageResult = new ArrayList<>();
        for (EmpEfficiency empEfficiency : result) {
            EmpEffectivenessCountVO empEffectivenessCount = new EmpEffectivenessCountVO();
            empEffectivenessCount.setEmpId(empEfficiency.getEmpId());
            empEffectivenessCount.setEmpName(empEfficiency.getEmpName());
            empEffectivenessCount.setAttendance(Double.parseDouble(empEfficiency.getAttendRatio()));
            empEffectivenessCount.setCost(Double.parseDouble(empEfficiency.getCostRatio()));
            empEffectivenessCount.setProfit(Double.parseDouble(empEfficiency.getProfit()));
            empEffectivenessCount.setPerformance(Double.parseDouble(empEfficiency.getPerformRatio()));
            empEffectivenessCount.setEfficiency(Double.parseDouble(empEfficiency.getEffectRatio()));
            pageResult.add(empEffectivenessCount);
        }
        return pageResult;
    }


    private List<EmpEfficiency> getEmpEfficiencyInfos(Date now, List<Employee> allEmployees) {
        List<Long> empIds = allEmployees.stream().map(Employee::getEmpId).collect(Collectors.toList());
        Map<Long, List<CredentialsDetailsVO>> credentialMap = empCredentialsMapper.findInfosByEmpIds(empIds).stream().
                collect(Collectors.groupingBy(CredentialsDetailsVO::getCredEmpId));
        Map<Long, List<EmpAttendance>> empAttendMap = empAttendanceMapper.findInfosByEmpIdsAndRecordTime(empIds, now).stream().
                collect(Collectors.groupingBy(EmpAttendance::getAttendEmpId));
        Map<Long, List<EmpPerformance>> empPerformanceMap = empPerformanceMapper.findInfosByEmpIdsAndCreateTime(empIds, now).stream().
                collect(Collectors.groupingBy(EmpPerformance::getPerfEmpId));
        Map<Long, List<EmpSalary>> salaryMap = empSalaryMapper.findInfosByEmpIdsAndCurrentTime(empIds, now).stream().
                collect(Collectors.groupingBy(EmpSalary::getSalaryEmpId));
        Map<Long, List<EmpEffectiveness>> empEffectiveMap = empEffectivenessMapper.findInfosByEmpIdsAndCreateTime(empIds, now).
                stream().collect(Collectors.groupingBy(EmpEffectiveness::getEffEmpId));
        List<EmpEfficiency> empEfficiencies = new ArrayList<>();
        String regex = "\\d+";
        for (Employee allEmployee : allEmployees) {
            Long empId = allEmployee.getEmpId();
            int education = 0;
            if (allEmployee.getEmpEducation() != null) {
                String empEducation = allEmployee.getEmpEducation();
                if (empEducation.matches(regex)) {
                    education = 10 + Integer.parseInt(empEducation);
                } else {
                    education = 10;
                }
            } else {
                education = 10;
            }
            int title = 0;
            if (allEmployee.getEmpTitle() != null) {
                String empTitle = allEmployee.getEmpTitle();
                if (empTitle.matches(regex)) {
                    title = 10 + Integer.parseInt(empTitle);
                } else {
                    title = 10;
                }
            } else {
                title = 10;
            }
            List<CredentialsDetailsVO> empCredentials = credentialMap.get(empId);
            int paperNum = 0;
            if (!CollectionUtils.isEmpty(empCredentials)) {
                if (empCredentials.size() > 10) {
                    paperNum = 20;
                } else {
                    paperNum = empCredentials.size() + 10;
                }
            } else {
                paperNum = 10;
            }
            Date attendTime = allEmployee.getAttendTime();
            int workExperience = 0;
            if (attendTime != null) {
                long workYears = DateUtils.calculateDays(attendTime, now) / 365;
                if (workYears > 0) {
                    if (workYears > 10) {
                        workExperience = 20;
                    } else {
                        workExperience = 10 + (int) workYears;
                    }
                }
            } else {
                workExperience = 10;
            }
            List<EmpPerformance> empPerformances = empPerformanceMap.get(empId);
            int performance = 0;
            if (!CollectionUtils.isEmpty(empPerformances)) {
                int score = 0;
                for (EmpPerformance empPerformance : empPerformances) {
                    if ("A".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 10;
                    }
                    if ("B".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 7;
                    }
                    if ("C".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 4;
                    }
                    if ("D".equals(empPerformance.getPerfLevelValue())) {
                        score = score + 1;
                    }
                }
                performance = 10 + score;
            } else {
                performance = 10;
            }
            List<EmpAttendance> empAttendances = empAttendMap.get(empId);
            double attendRatio = 0;
            if (!CollectionUtils.isEmpty(empAttendances)) {
                int onWorkNum = empAttendances.stream().filter(s -> AttendStatusEnum.ONE.getKey().
                        equals(s.getAttendStatus())).collect(Collectors.toList()).size();
                if (empAttendances.size() != 0) {
                    attendRatio = onWorkNum / (double) empAttendances.size();
                }
            }
            List<EmpSalary> empSalaries = salaryMap.get(empId);
            double salary = 0;
            if (!CollectionUtils.isEmpty(empSalaries)) {
                for (EmpSalary empSalary : empSalaries) {
                    String salaryPayableNum = empSalary.getSalaryPayableNum();
                    if (StringUtils.isNotEmpty(salaryPayableNum)) {
                        salary = salary + Double.parseDouble(salaryPayableNum) / 10000;
                    }
                }
            }
            List<EmpEffectiveness> empEffectivenessInfos = empEffectiveMap.get(empId);
            double profit = 0;
            if (!CollectionUtils.isEmpty(empEffectivenessInfos)) {
                for (EmpEffectiveness empEffectivenessInfo : empEffectivenessInfos) {
                    String effEmpProfitValue = empEffectivenessInfo.getEffEmpProfitValue();
                    if (StringUtils.isNotEmpty(effEmpProfitValue)) {
                        profit = profit + Double.parseDouble(effEmpProfitValue);
                    }
                }
            }
            double effect = 0;
            if (salary != 0.0 && attendRatio != 0.0) {
                effect = (education + title + paperNum + workExperience) * performance / (salary + attendRatio);
            }
            EmpEfficiency efficiency = new EmpEfficiency();
            efficiency.setEmpId(empId);
            efficiency.setEmpName(allEmployee.getEmpName());
            efficiency.setProfit(profit + "");
            efficiency.setAttendRatio(String.format("%.2f", attendRatio));
            efficiency.setPerformRatio(performance + "");
            efficiency.setCostRatio(salary + "");
            efficiency.setEffectRatio(String.format("%.2f", effect));
            efficiency.setCreateTime(now);
            empEfficiencies.add(efficiency);

        }
        return empEfficiencies;
    }
}
