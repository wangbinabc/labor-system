package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpResume;
import com.yuantu.labor.vo.EmpResumeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 履历Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-12
 */
@Mapper
@Repository
public interface EmpResumeMapper {
    /**
     * 查询履历
     *
     * @param resuId 履历主键
     * @return 履历
     */
    public EmpResume selectEmpResumeByResuId(Integer resuId);

    /**
     * 查询履历列表
     *
     * @param empResume 履历
     * @return 履历集合
     */
    public List<EmpResume> selectEmpResumeList(EmpResume empResume);

    /**
     * 新增履历
     *
     * @param empResume 履历
     * @return 结果
     */
    public int insertEmpResume(EmpResume empResume);

    /**
     * 修改履历
     *
     * @param empResume 履历
     * @return 结果
     */
    public int updateEmpResume(EmpResume empResume);

    /**
     * 删除履历
     *
     * @param resuId 履历主键
     * @return 结果
     */
    public int deleteEmpResumeByResuId(Integer resuId);

    /**
     * 批量删除履历
     *
     * @param resuIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpResumeByResuIds(Integer[] resuIds);

    /**
     * 模糊查询指定类型的履历信息
     *
     * @param resumeVO
     * @return
     */
    public List<EmpResume> selectEmpResumeListByWhere(EmpResumeVO resumeVO);

    /**
     * 批量插入履历信息
     *
     * @param empResumes
     * @return
     */
    void batchInsertEmpResumes(@Param("empResumes") List<EmpResume> empResumes);

    /**
     * 根据员工id查询有关履历信息
     *
     * @param empIds
     * @return
     */
    List<EmpResume> findExportInfosByEmpIds(@Param("empIds") List<Long> empIds, @Param("type") String type);

    /**
     * 根据员工id查询有关履历信息
     *
     * @param empIds
     * @return
     */
    List<EmpResume> findInfosByEmpIds(@Param("empIds") List<Long> empIds);



}
