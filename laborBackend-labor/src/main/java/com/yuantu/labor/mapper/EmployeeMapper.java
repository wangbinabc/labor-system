package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 员工Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Mapper
@Repository
public interface EmployeeMapper {
    /**
     * 查询员工
     *
     * @param empId 员工主键
     * @return 员工
     */
    public Employee selectEmployeeByEmpId(Long empId);

    /**
     * 查询员工列表
     *
     * @param employee 员工
     * @return 员工集合
     */
    public List<Employee> selectEmployeeList(Employee employee);

    /**
     * 查询员工列表
     *
     * @param employee 员工
     * @return 员工集合
     */
    List<Employee> selectEmployeeInfoList(EmpSearchVO employee);

    /**
     * 查询员工列表
     *
     * @param employee 员工
     * @return 员工集合
     */
    List<Employee> selectEmployees(EmpSearchVO employee);


    /**
     * 查询员工列表
     *
     * @param empIds
     * @return 员工集合
     */
    List<Employee> selectEmployeeInfosByIds(@Param("empIds") List<Long> empIds);


    /**
     * 查询员工导出列表
     *
     * @param empExport
     * @return 员工集合
     */
    List<Employee> findEmpExportInfos(@Param("empExport") EmpExportVO empExport);

    /**
     * 新增员工
     *
     * @param employee 员工
     * @return 结果
     */
    public int insertEmployee(Employee employee);

    /**
     * 修改员工
     *
     * @param employee 员工
     * @return 结果
     */
    public int updateEmployee(Employee employee);

    /**
     * 恢复删除员工
     * @param employee
     * @return
     */
    public int updateRecoveryEmployee(Employee employee);

    /**
     * 删除员工
     *
     * @param empId 员工主键
     * @return 结果
     */
    public int deleteEmployeeByEmpId(Long empId);

    /**
     * 批量删除员工
     *
     * @param empIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmployeeByEmpIds(@Param("empIds") Long[] empIds, @Param("now") Date now);

    /**
     * 根据EmployeeInfoVO条件查询员工
     *
     * @param employeeInfoVO
     * @return 员工集合
     */
    public List<Employee> selectEmployeeByEmployeeInfoVO(EmployeeInfoVO employeeInfoVO);


    public List<EmpNameCardVO> selectEmpNameAndCard(EmployeeInfoVO employeeInfoVO);

    /**
     * 根据身份证号查询员工信息
     *
     * @param empIdCard
     * @return 结果
     */
    Employee findInfoByEmpIdCard(String empIdCard);


    Employee findInfoByHistoryEmpIdCard(String loanEmpIdcard);

    Employee findInfoByHistoryEmpName(String empName);

    /**
     * 根据更新时间查询当月员工信息
     *
     * @return 结果
     */
    List<Employee> findCurrentInfos();

    /**
     * 更新员工头像信息
     *
     * @param employee
     * @return 结果
     */
    int updateEmpAvatarUrl(Employee employee);



    int findCurrentMonthStatusNum(@Param("empStatus") String empStatus);

    /**
     * 查询本月新入职人数
     *
     * @return 结果
     */
    int findAttendJobNumWithCurrentMonth();

    /**
     * 查询本月辞职人数
     *
     * @return 结果
     */
    int findResignNumWithCurrentMonth();

    /**
     * 查询本月辞退人数
     *
     * @return 结果
     */
    int findFireNumWithCurrentMonth();

    /**
     * 查询本月即将到龄人数
     *
     * @return 结果
     */
    int findSoonExpiredNumWithCurrentMonth();

    /**
     * 查询本月到龄人数
     *
     * @return 结果
     */
    int findExpiredNumWithCurrentMonth();


    /**
     * 查询本月返聘人数
     *
     * @return 结果
     */
    int findReEmployNumWithCurrentMonth();

    /**
     * 更改员工状态
     *
     * @param empIds
     * @param empStatus
     * @return 结果
     */
    void changeEmpStatusByEmpIdsAndEmpStatus(@Param("empIds") List<Long> empIds, @Param("empStatus") String empStatus, @Param("now") Date now);


    void changeEmpInvisibleByEmpIdsAndInvisible(@Param("empIds") List<Long> empIds, @Param("invisible") Boolean invisible);

    /**
     * 查询所有员工信息
     *
     * @return 结果
     */
    List<Employee> findAllEmployees();

    /**
     * 更新员工到龄时间
     *
     * @return 结果
     */
    int updateExpireTimeByEmpId(@Param("expireTime") Date expireTime, @Param("empId") Long empId);

    /**
     * 根据身份证号查询员工信息
     *
     * @param idInfos
     * @return 结果
     */
    List<Employee> findInfoByIdCards(@Param("idInfos") List<String> idInfos);

    List<Employee> findInfoEmpNames(@Param("empNames") List<String> empNames);


    List<String> findNamesByEmpNames(@Param("empNames") List<String> employNames);

    /**
     * 根据ids查询员工信息
     *
     * @param empIds
     * @return 结果
     */
    List<Employee> findEmpInfosByIdsWithoutDisabled(@Param("empIds") List<Long> empIds);

    /**
     * 根据创建时间查询员工信息
     *
     * @param currentTime
     * @return 结果
     */
    List<Employee> findEmpInfosByCreateTime(@Param("currentTime") String currentTime);

    /**
     * 根据部门id查询员工信息
     *
     * @param deptIds
     * @return 结果
     */
    List<Employee> findEmpInfosByEmpDeptIds(@Param("deptIds") List<Long> deptIds);

    void updateRetireReminder(@Param("empId") Long empId, @Param("retireReminder") String retireReminder);


    /**
     * 部门稳定度分析
     *
     * @param queryVO
     * @return
     */
    List<StabilityResultVO> getDeptStabilityData(StabilityQueryVO queryVO);

    /**
     * 单位稳定度分析
     *
     * @param queryVO
     * @return
     */
    List<StabilityResultVO> getUnitStabilityData(StabilityQueryVO queryVO);

    /**
     * 根据员工姓名查询员工信息
     *
     * @param empName
     * @return
     */
    Employee findEmpInfoByEmpName(@Param("empName") String empName);

    void encodeEmployeeIdCardInfo(@Param("empIdcard") String empIdcard, @Param("empId") Long empId);

    void decodeEmployeeIdCardInfo(@Param("empIdcard") String empIdcard, @Param("empId") Long empId);

    /**
     * 查询有关员工信息
     *
     * @param employee
     * @return
     */
    List<Employee> findEmpPicInfos(@Param("empInfo") EmpSearchSimpleVO employee);

    List<Employee> findInfoByEmpIdCardOrName(@Param("empIdCard") String empIdcard);

    void updateHireLimitDateByEmpId(@Param("hireLimitDate") Date hireLimitDate, @Param("empId") Long empId);

    void updateEmpSalaryLevel(Employee emp);

    List<Employee> findEmployeeInfosByDeptIdWithStatus(@Param("deptId") Long deptId, @Param("valueYear") String valueYear);

    List<Employee> selectDeletedEmpByEmpName(String empName);

    /**
     * 恢复被删除的员工
     * @param vo
     */
    void recoveryDeletedEmp(EmployeeVO vo);

    Employee findDeletedEmpInfoByEmpName(@Param("empName") String empName);


    List<Long> findEmpIdsByParams(@Param("empName") String empName, @Param("isRelated") Integer isRelated,
                                  @Param("empCategory") String empCategory,
                                  @Param("deptId") Long deptId);

    List<Long> findInfosByStatusInfos(@Param("statusInfos") List<String> statusList);


}
