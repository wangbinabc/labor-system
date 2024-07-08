package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.vo.CredentialsCountParamsVO;
import com.yuantu.labor.vo.CredentialsCountVO;
import com.yuantu.labor.vo.CredentialsDetailsVO;
import com.yuantu.labor.vo.ExportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 资格证书Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
@Mapper
@Repository
public interface EmpCredentialsMapper 
{
    /**
     * 查询资格证书
     * 
     * @param credId 资格证书主键
     * @return 资格证书
     */
    public EmpCredentials selectEmpCredentialsByCredId(Long credId);




    /**
     * 根据部门统计查询资格证书
     *
     * @param credentialsCountParamsVO
     * @return CredentialsCountVO
     */
    public List<CredentialsCountVO> countEmpCredentialsByDept(CredentialsCountParamsVO credentialsCountParamsVO);

    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<EmpCredentials> findExportInfos(@Param("export") ExportVO export);

    /**
     * 根据证书名字统计查询资格证书
     *
     * @param credentialsCountParamsVO
     * @return CredentialsCountVO
     */
    public List<CredentialsCountVO> countEmpCredentialsByCredName(CredentialsCountParamsVO credentialsCountParamsVO);

    /**
     * 查询资格证书详情
     *
     * @param empCredentials 资格证书
     * @return 资格证书
     */
    public List<CredentialsDetailsVO> selectEmpCredentialsVOList(@Param("credential") EmpCredentials empCredentials);

    /**
     * 查询资格证书列表
     * 
     * @param empCredentials 资格证书
     * @return 资格证书集合
     */
    public List<EmpCredentials> selectEmpCredentialsList(EmpCredentials empCredentials);

    /**
     * 新增资格证书
     * 
     * @param empCredentials 资格证书
     * @return 结果
     */
    public int insertEmpCredentials(EmpCredentials empCredentials);

    /**
     * 修改资格证书
     * 
     * @param empCredentials 资格证书
     * @return 结果
     */
    public int updateEmpCredentials(EmpCredentials empCredentials);

    /**
     * 删除资格证书
     * 
     * @param credId 资格证书主键
     * @return 结果
     */
    public int deleteEmpCredentialsByCredId(Long credId);

    /**
     * 批量删除资格证书
     * 
     * @param credIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpCredentialsByCredIds(Long[] credIds);

    /**
     * 根据员工id查询资格证书信息
     *
     * @param empIds
     * @return 结果
     */
    List<CredentialsDetailsVO> findInfosByEmpIds(@Param("empIds") List<Long> empIds);
}
