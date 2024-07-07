package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpImportHistory;
import com.yuantu.labor.vo.EmpImportHistoryVO;
import com.yuantu.labor.vo.MatchSearchVO;
import com.yuantu.labor.vo.MatchUpdateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户证件Mapper接口
 *
 * @author ahadon
 * @date 2023-10-08
 */
@Repository
public interface EmpImportHistoryMapper {
    /**
     * 查询用户证件
     *
     * @param id 用户证件主键
     * @return 用户证件
     */
    public EmpImportHistory selectEmpImportHistoryById(Long id);

    /**
     * 查询用户证件列表
     *
     * @param empImportHistory 用户证件
     * @return 用户证件集合
     */
    public List<EmpImportHistory> selectEmpImportHistoryList(EmpImportHistory empImportHistory);

    /**
     * 新增用户证件
     *
     * @param empImportHistory 用户证件
     * @return 结果
     */
    public int insertEmpImportHistory(EmpImportHistory empImportHistory);

    /**
     * 修改用户证件
     *
     * @param empImportHistory 用户证件
     * @return 结果
     */
    public int updateEmpImportHistory(EmpImportHistory empImportHistory);

    /**
     * 删除用户证件
     *
     * @param id 用户证件主键
     * @return 结果
     */
    public int deleteEmpImportHistoryById(Long id);

    /**
     * 批量删除用户证件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpImportHistoryByIds(Long[] ids);

    /**
     * 根据创建人删除信息
     *
     * @param createId
     * @return 结果
     */
    void removeInfoByCreateId(Long createId);

    /**
     * 批量插入信息
     *
     * @param empImportHistoryList
     * @return 结果
     */
    void batchInsertInfos(@Param("list") List<EmpImportHistory> empImportHistoryList);

    /**
     * 查询列表信息
     *
     * @param matchSearch
     * @param userId
     * @return 结果
     */
    List<EmpImportHistoryVO> selectMatchInfos(@Param("matchSearch") MatchSearchVO matchSearch, @Param("createId") Long userId);

    /**
     * 批量删除列表信息
     *
     * @param idList
     * @return 结果
     */
    void removeInfosByIds(@Param("ids") List<Long> idList);

    /**
     * 根据创建人查询总数
     *
     * @param userId
     * @return 结果
     */
    Long findTotalNums(Long userId);

    /**
     * 根据创建人查询未匹配数量
     *
     * @param userId
     * @return 结果
     */
    Long findUnmatchedNums(Long userId);
}
