package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 员工快照Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-19
 */
@Mapper
@Repository
public interface EmpHistoryMapper {
    /**
     * 查询员工快照
     *
     * @param historyId 员工快照主键
     * @return 员工快照
     */
    public EmpHistory selectEmpHistoryByHistoryId(Long historyId);

    /**
     * 查询员工快照列表
     *
     * @param vo 员工快照查询条件
     * @return 员工快照集合
     */
    public List<EmpHistory> selectEmpHistoryList(EmpHistoryQueryParamsVO vo);

    /**
     * 根据快照时间查询员工快照列表
     *
     * @param historyYearMonth 快照时间
     * @return 员工快照集合
     */
    public List<EmpHistoryInfoVO> selectEmpHistoryListByYearMonth(String historyYearMonth);


    /**
     * 查询员工快照信息
     *
     * @param employee
     * @return 员工快照集合
     */
    List<EmpHistory> selectEmployeeInfoHistoryList(EmpSearchVO employee);

    /**
     * 查询员工快照信息
     *
     * @param employee
     * @return 员工快照集合
     */
    List<EmpHistory> selectEmployeeInfoHistory(EmpSearchVO employee);

    /**
     * 根据创建日期查询员工快照信息
     *
     * @param empExport
     * @return 员工快照集合
     */
    List<EmpHistory> selectEmployeeInfoHistoryExportInfos(@Param("empExport") EmpExportVO empExport);


    /**
     * 新增员工快照
     *
     * @param empHistory 员工快照
     * @return 结果
     */
    public int insertEmpHistory(EmpHistory empHistory);

    /**
     * 修改员工快照
     *
     * @param empHistory 员工快照
     * @return 结果
     */
    public int updateEmpHistory(EmpHistory empHistory);

    /**
     * 删除员工快照
     *
     * @param historyId 员工快照主键
     * @return 结果
     */
    public int deleteEmpHistoryByHistoryId(Long historyId);

    /**
     * 批量删除员工快照
     *
     * @param historyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpHistoryByHistoryIds(Long[] historyIds);

    /**
     * 批量插入员工快照
     *
     * @param empHistories
     * @return 结果
     */
    void batchInsertHistoryInfos(@Param("list") List<EmpHistory> empHistories);

    /**
     * 根据创建时间员工快照
     *
     * @param currentTime
     * @return 结果
     */
    List<EmpHistory> findEmpInfosByCreateTime(@Param("currentTime") Date currentTime);

    /**
     * 根据创建时间范围查询员工快照信息
     *
     * @param startTime
     * @param endTime
     * @return 结果
     */
    List<EmpHistory> findEmpInfosByTimeRangeForCreateTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据最远创建时间查询员工快照信息
     *
     * @return 结果
     */
    EmpHistory findEmpInfoWithMinCrateTime();

    /**
     * 更改员工快照信息员工状态
     *
     * @return 结果
     */
    void changeEmpStatusByEmpIdsAndEmpStatus(@Param("empIds") List<Long> empIds, @Param("empStatus") String empStatus, @Param("startTime") Date startTime);


    List<EmpHistory> findAllEmployeeHistory();


    void encodeEmpHistoryIdCardInfo(@Param("empIdcard") String empIdcard, @Param("historyId") Long historyId);

    void decodeEmpHistoryIdCardInfo(@Param("empIdcard") String empIdcard, @Param("historyId") Long historyId);

    void removeCurrentMonthHistory(@Param("now") Date now);

    List<EmpHistory> findEmpInfosByDeptIdAndCreateYear(@Param("deptId") Long deptId, @Param("valueYear") String valueYear);

    void removeEmpHistoryListByYearMonth(String yearMonth);


    List<EmpHistory> selectRecentEmpHistory(String lastMonth);

    String findLatestHistoryYearMonth();

    void deleteEmpInfoByEmpNameAndYearMoth(@Param("empName") String empName, @Param("yearMonth") String yearMonth);

    List<EmpHistory> selectEmpDetailHistoryInfosByYearMonth(@Param("yearMonth") String yearMonth);

    List<EmpHistory> selectEmpDetailHistoryInfosBetweenYearMonth(@Param("beforeMonth") String beforeMonth, @Param("afterMonth") String afterMonth);
}
