package com.yuantu.labor.service;

import java.util.Date;
import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 员工快照Service接口
 *
 * @author ruoyi
 * @date 2023-09-19
 */
public interface IEmpHistoryService {
    /**
     * 查询员工快照
     *
     * @param historyId 员工快照主键
     * @return 员工快照
     */
    public EmpDetailHistoryVO selectEmpHistoryByHistoryId(Long historyId);

    /**
     * 查询员工快照列表
     *
     * @param vo 员工快照
     * @return 员工快照集合
     */
    public List<EmpHistory> selectEmpHistoryList(EmpHistoryQueryParamsVO vo);

    /**
     * 按起始时间查询员工快照列表，并返回结果
     *
     * @return 员工快照集合
     */
    public List<EmpHistoryInfoVO> selectEmpHistoryChangeTypeList(HistoryChangeTypeSearchVO search);

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
    public Boolean updateEmpHistory(EmpDetailHistoryVO empHistory, Long userId);

    /**
     * 批量删除员工快照
     *
     * @param historyIds 需要删除的员工快照主键集合
     * @return 结果
     */
    public int deleteEmpHistoryByHistoryIds(Long[] historyIds);

    /**
     * 删除员工快照信息
     *
     * @param historyId 员工快照主键
     * @return 结果
     */
    public int deleteEmpHistoryByHistoryId(Long historyId);

    /**
     * 上传员工历史信息
     *
     * @param yearMonth
     * @param loginUser
     * @param file
     * @return 员工快照
     */
    ImportResultVO uploadEmpHisInfosFile(String yearMonth, MultipartFile file, LoginUser loginUser);

    /**
     * 复制人员历史信息(按月份)
     *
     * @param monthTime
     * @param loginUser
     *
     * @return 结果
     */
    Boolean empHistoryMonthCopy(MonthTimeVO monthTime, LoginUser loginUser);
}
