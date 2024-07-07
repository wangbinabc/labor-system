package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpDocument;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户证件Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-14
 */
@Repository
public interface EmpDocumentMapper {
    /**
     * 查询用户证件
     *
     * @param docId 用户证件主键
     * @return 用户证件
     */
    public EmpDocument selectEmpDocumentByDocId(Long docId);

    /**
     * 查询用户证件列表
     *
     * @param empDocument 用户证件
     * @return 用户证件集合
     */
    public List<EmpDocument> selectEmpDocumentList(EmpDocument empDocument);

    /**
     * 新增用户证件
     *
     * @param empDocument 用户证件
     * @return 结果
     */
    public int insertEmpDocument(EmpDocument empDocument);

    /**
     * 修改用户证件
     *
     * @param empDocument 用户证件
     * @return 结果
     */
    public int updateEmpDocument(EmpDocument empDocument);

    /**
     * 删除用户证件
     *
     * @param docId 用户证件主键
     * @return 结果
     */
    public int deleteEmpDocumentByDocId(Long docId);

    /**
     * 批量删除用户证件
     *
     * @param docIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpDocumentByDocIds(Long[] docIds);

    /**
     * 根据员工id删除员工证件信息
     *
     * @param empId
     * @return 结果
     */
    void removeInfoByEmpId(Long empId);

    /**
     * 删除员工证件信息
     *
     * @param docId
     * @return 结果
     */
    int removeInfoByDocId(Long docId);

    /**
     * 批量删除证件信息
     *
     * @param docIds
     * @return 结果
     */
    int removeInfoByDocIds(Long[] docIds);

    /**
     * 绑定员工与证件信息
     *
     * @param empId
     * @param docIds
     * @return 结果
     */
    void bindEmpInfos(@Param("empId") Long empId, @Param("docIds") List<Long> docIds);

    /**
     * 根据业务id和类型查询证件信息
     *
     * @param docTypes
     * @param empId
     * @return 结果
     */
    List<EmpDocument> findDocInfosByEmpIdAndTypes(@Param("empId") Long empId, @Param("docTypes") List<String> docTypes);


    /**
     * 根据业务id和类型查询证件信息
     *
     * @param docTypes
     * @param empIds
     * @return 结果
     */
    List<EmpDocument> findDocInfosByEmpIdsAndTypes(@Param("empIds") List<Long> empIds, @Param("docTypes") List<String> docTypes);

    /**
     * 批量删除文件信息
     *
     * @param empIdList
     * @param docTypes
     * @return 结果
     */
    void removeInfoByEmpIdsAndDocTypes(@Param("empIdList") List<Long> empIdList, @Param("docTypes") List<String> docTypes);

    /**
     * 批量删除文件信息
     *
     * @param docIds
     * @return 结果
     */
    void removeInfoByDocIdList(@Param("docIds") List<Long> docIds);
}
