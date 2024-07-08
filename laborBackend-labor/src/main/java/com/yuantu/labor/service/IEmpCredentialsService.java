package com.yuantu.labor.service;

import java.util.List;
import java.util.Map;

import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 资格证书Service接口
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
public interface IEmpCredentialsService 
{

    /**
     * 查询资格证书
     * 
     * @param credId 资格证书主键
     * @return 资格证书
     */
    public EmpCredentials selectEmpCredentialsByCredId(Long credId);


    /**
     * 查询资格证书列表
     * 
     * @param empCredentials 资格证书
     * @return 资格证书集合
     */
    public List<EmpCredentials> selectEmpCredentialsList(EmpCredentials empCredentials);


    List<EmpCredentials> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);
    /**
     * 根据姓名和身份证查询资格证书
     *
     * @param empCredentials 资格证书
     * @return 资格证书
     */
    public List<CredentialsDetailsVO> selectEmpCredentialsVOList(EmpCredentials empCredentials);

    void downloadExcelTemplate(HttpServletResponse response);

    /**
     * 倒入资格证书
     *
     * @param
     * @return CredentialsCountVO
     */
     ImportResultVO uploadPostInfosFile(MultipartFile multipartFile, Long userId, String username);

     /**
     * 根据条件统计查询资格证书
     *
     * @param credentialsCountParamsVO 如参数对象中的变量值为-1表示不需要此项条件查询
     * @return CredentialsCountVO
     */
     Map<String,List<CredentialsCountVO>> countEmpCredentials(CredentialsCountParamsVO credentialsCountParamsVO);

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
     * 批量删除资格证书
     * 
     * @param credIds 需要删除的资格证书主键集合
     * @return 结果
     */
    public int deleteEmpCredentialsByCredIds(Long[] credIds);

    /**
     * 删除资格证书信息
     * 
     * @param credId 资格证书主键
     * @return 结果
     */
    public int deleteEmpCredentialsByCredId(Long credId);



}
