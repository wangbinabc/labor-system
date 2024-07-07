package com.yuantu.labor.service;

import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.FamilyRelations;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 员工Service接口
 *
 * @author ruoyi
 * @date 2023-09-06
 */
public interface IEmployeeService {
    /**
     * 查询员工
     *
     * @param empId 员工主键
     * @return 员工
     */
    public Employee selectEmployeeByEmpId(Long empId);

    /**
     * 查询员工详细信息
     *
     * @param empId 员工主键
     * @return 员工
     */
    EmpDetailVO selectEmployeeInfoByEmpId(Long empId);

    /**
     * 查询员工列表
     *
     * @param employee 员工
     * @return 员工集合
     */
    public List<Employee> selectEmployeeList(EmpSearchVO employee);

    /**
     * 查询员工列表
     *
     * @param employee 员工信息
     * @return 员工集合
     */
    List<EmployeeVO> selectEmployees(EmpSearchVO employee);


    /**
     * 查询员工历史信息列表
     *
     * @param employee 员工
     * @return 员工集合
     */
    List<EmpHistory> selectEmployeeHistoryList(EmpSearchVO employee);

    /**
     * 查询员工历史信息列表
     *
     * @param employee 员工
     * @return 员工集合
     */
    List<EmpHistoryVO> selectEmployeeHistory(EmpSearchVO employee);


    /**
     * 查询导出员工信息
     *
     * @param empExport
     * @return 员工集合
     */
    List<Employee> selectEmployeeExportInfos(EmpExportVO empExport);


    /**
     * 查询导出员工历史信息
     *
     * @param empExport
     * @return 员工集合
     */
    List<EmpHistory> selectEmployeeHistoryExportInfos(EmpExportVO empExport);


    /**
     * 表格拆分导出员工信息
     *
     * @param empExport
     */
    void exportDivide(HttpServletResponse response, EmpExportDivideVO empExport);

    /**
     * 表格拆分导出员工历史信息
     *
     * @param empExport
     */
    void exportHistoryDivide(HttpServletResponse response, EmpExportDivideVO empExport);

    /**
     * 新增/修改员工
     *
     * @param flag
     * @param employee 员工
     * @return 结果
     */
    public Long insertEmployee(EmpAddVO employee, Long userId, String userName, Boolean flag);



    /**
     * 批量删除员工
     *
     * @param empIds 需要删除的员工主键集合
     * @return 结果
     */
    public int deleteEmployeeByEmpIds(Long[] empIds);

    /**
     * 批量删除员工亲属信息
     *
     * @param famIds
     * @return 结果
     */
    int deleteEmpFamilyByFamId(Long[] famIds);

    /**
     * 删除员工信息
     *
     * @param empId 员工主键
     * @return 结果
     */
    public int deleteEmployeeByEmpId(Long empId);


    public HashMap<String, Collection<PieChartVO>> selectEmployeeByEmployeeInfoVO(EmployeeInfoVO employeeInfoVO);


    public List<EmpNameCardVO> selectEmpNameAndCard(EmployeeInfoVO employeeInfoVO);

    /**
     * 下载员工信息模板
     *
     * @param response
     * @return 结果
     */
    void downloadExcel(HttpServletResponse response);

    /**
     * 下载员工信息模板
     *
     * @param response
     * @return 结果
     */
    void downloadExcelTemplate(HttpServletResponse response);

    /**
     * 下载员工下载证件模板
     *
     * @param response
     * @return 结果
     */
    void downloadFileTemplate(HttpServletResponse response);

    /**
     * 下载员工证件信息压缩文件
     *
     * @param empFile
     * @param response
     * @return 结果
     */
    void downloadEmployeeFileInfosFile(HttpServletResponse response, EmpFileVO empFile);

    /**
     * 根据上传模版信息匹配员工证件信息
     *
     * @param file
     * @param userId
     * @return 结果
     */
    Boolean matchEmpFileInfosWithinTemplate(MultipartFile file, Long userId);

    /**
     * 根据上传模版信息下载员工证件信息
     *
     * @param file
     * @param response
     * @return 结果
     */
    void downloadEmpFileInfosWithinTemplate(MultipartFile file, HttpServletResponse response);

    /**
     * 上传员工信息
     *
     * @param multipartFile
     * @param userId
     * @return 结果
     */
    ImportResultVO uploadEmployeeInfosFile(MultipartFile multipartFile, Long userId, String username);


    /**
     * 上传员工文件信息
     *
     * @param multipartFile
     * @param username
     * @return 结果
     */
    ImportResultVO uploadEmployeeFileInfosFile(MultipartFile multipartFile, String username, Long userId);

    /**
     * 查询员工亲属信息
     *
     * @param employeeId
     * @return 结果
     */
    List<FamilyRelations> searchEmpFamilyInfos(Long employeeId);

    /**
     * 查询本月员工情况人数
     *
     * @return 结果
     */
    List<EmpSimpleInfoVO> countEmpNum();


    /**
     * 批量离职员工
     *
     * @param empChangeStatus
     * @return 结果
     */
    Boolean batchFireEmployees(List<EmpChangeStatusVO> empChangeStatus);

    /**
     * 查询模板匹配情况列表
     *
     * @param matchSearch
     * @param userId
     * @return 结果
     */
    List<EmpImportHistoryVO> selectMatchTemplateList(MatchSearchVO matchSearch, Long userId);

    /**
     * 修改模板匹配身份证信息
     *
     * @param matchUpdate
     * @param userId
     * @return 结果
     */
    Boolean updateMatchTemplateInfo(MatchUpdateVO matchUpdate, Long userId);

    /**
     * 批量删除匹配信息
     *
     * @param idList
     * @return 结果
     */
    Boolean removeMatchTemplateInfos(List<Long> idList);

    /**
     * 查询匹配数量信息
     *
     * @param userId
     * @return 结果
     */
    MatchResultVO findMatchTemplateNums(Long userId);

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
     * 对员工身份信息做加密处理
     *
     * @param isEncode
     * @return 结果
     */
    Boolean dealEmpIdCardInfo(Boolean isEncode);

    /**
     * 查询员工简要信息
     *
     * @param employee
     * @return 结果
     */
    List<EmpPicInfoVO> selectEmpPicList(EmpSearchSimpleVO employee);

    /**
     * 获取省市区
     *
     * @return 省市区信息
     */
    List<ChinaAddressTreeVO> getChinaAddress();

    /**
     * 查询被删除的员工
     * @param empName
     * @return
     */
    Employee selectDeletedEmpByEmpName(String empName);

    /**
     * 恢复被删除的员工
     * @param vo
     */
    void recoveryDeletedEmp(EmployeeVO vo);



    }
