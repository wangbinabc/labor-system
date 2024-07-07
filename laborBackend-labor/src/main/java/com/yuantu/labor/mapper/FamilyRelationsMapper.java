package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.FamilyRelations;
import com.yuantu.labor.vo.EmpSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 家庭关系Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Repository
public interface FamilyRelationsMapper {
    /**
     * 查询家庭关系
     *
     * @param famId 家庭关系主键
     * @return 家庭关系
     */
    public FamilyRelations selectFamilyRelationsByFamId(Long famId);

    /**
     * 查询家庭关系列表
     *
     * @param familyRelations 家庭关系
     * @return 家庭关系集合
     */
    public List<FamilyRelations> selectFamilyRelationsList(FamilyRelations familyRelations);

    /**
     * 新增家庭关系
     *
     * @param familyRelations 家庭关系
     * @return 结果
     */
    public int insertFamilyRelations(FamilyRelations familyRelations);

    /**
     * 修改家庭关系
     *
     * @param familyRelations 家庭关系
     * @return 结果
     */
    public int updateFamilyRelations(FamilyRelations familyRelations);

    /**
     * 删除家庭关系
     *
     * @param famId 家庭关系主键
     * @return 结果
     */
    public int deleteFamilyRelationsByFamId(Long famId);

    /**
     * 批量删除家庭关系
     *
     * @param famIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFamilyRelationsByFamIds(Long[] famIds);

    /**
     * 根据员工id批量删除家庭关系
     *
     * @param empId
     * @return 结果
     */
    void removeInfoByEmpId(Long empId);

    /**
     * 批量插入员工亲属信息
     *
     * @param familyRelations
     * @return 结果
     */
    void batchInsertFamilyInfos(@Param("list") List<FamilyRelations> familyRelations);

    /**
     * 根据员工Id查询员工亲属信息
     *
     * @param empId
     * @return 结果
     */
    List<FamilyRelations> findFamilyInfoByEmpId(Long empId);

    /**
     * 根据员工Id查询员工亲属信息
     *
     * @param empIds
     * @return 结果
     */
    List<FamilyRelations> selectFamilyInfosByEmpIds(@Param("empIds") List<Long> empIds);

    /**
     * 根据家庭成员手机号和关系查询家庭成员信息
     *
     * @param employee 家庭成员查询信息
     * @return 家庭成员信息
     */
    List<FamilyRelations> searchFamilyInfo(EmpSearchVO employee);

}
