package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.FamilyRelationsHistory;
import com.yuantu.labor.vo.EmpSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tzq
 * @description 针对表【family_relations_history(家庭关系表)】的数据库操作Mapper
 * @createDate 2023-11-07 10:15:52
 * @Entity generator.domain.FamilyRelationsHistory
 */
@Repository
public interface FamilyRelationsHistoryMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FamilyRelationsHistory record);

    int insertSelective(FamilyRelationsHistory record);

    FamilyRelationsHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FamilyRelationsHistory record);

    int updateByPrimaryKey(FamilyRelationsHistory record);

    void batchInsertFamilyRelationsHistory(@Param("relations") List<FamilyRelationsHistory> familyRelationsHistories);

    /**
     * 根据家庭成员手机号和关系查询家庭成员信息
     *
     * @param employee 家庭成员查询信息
     * @return 家庭成员信息
     */
    List<FamilyRelationsHistory> searchFamilyInfo(EmpSearchVO employee);

    /**
     * 根据员工Id查询员工亲属信息
     *
     * @param empIds
     * @return 结果
     */
    List<FamilyRelationsHistory> selectFamilyInfosByEmpIds(@Param("empIds") List<Long> empIds);


    void removeInfosByEmpId(@Param("famEmpId") Long famEmpId, @Param("yearMonth") String yearMonth);


    void batchInsertFamilyInfos(@Param("list") List<FamilyRelationsHistory> familyRelations);

    List<FamilyRelationsHistory> findInfosByEmpIdAndHistoryYearMonth(@Param("empId") Long empId, @Param("historyYearMonth") String historyYearMoth);


    List<FamilyRelationsHistory> selectRecentFamliyHistory(String lastMonth);

    List<FamilyRelationsHistory> findInfosByEmpIdsAndHistoryYearMonth(@Param("empHistoryIds") List<Long> empHistoryIds,
                                                                      @Param("yearMonth") String yearMonth);
}
