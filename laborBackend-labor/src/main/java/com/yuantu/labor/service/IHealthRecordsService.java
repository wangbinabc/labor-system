package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.HealthRecords;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.HealthRecordsVO;
import com.yuantu.labor.vo.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 员工健康档案Service接口
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
public interface IHealthRecordsService 
{
    /**
     * 查询员工健康档案
     * 
     * @param healthId 员工健康档案主键
     * @return 员工健康档案
     */
    public HealthRecords selectHealthRecordsByHealthId(Long healthId);

    /**
     * 查询员工健康档案列表
     * 
     * @param healthRecords 员工健康档案
     * @return 员工健康档案集合
     */
    public List<HealthRecords> selectHealthRecordsList(HealthRecordsVO healthRecordsVO);

    List<HealthRecords> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);
    /**
     * 新增员工健康档案
     * 
     * @param healthRecords 员工健康档案
     * @return 结果
     */
    public int insertHealthRecords(HealthRecords healthRecords);

    /**
     * 修改员工健康档案
     * 
     * @param healthRecords 员工健康档案
     * @return 结果
     */
    public int updateHealthRecords(HealthRecords healthRecords);

    /**
     * 批量删除员工健康档案
     * 
     * @param healthIds 需要删除的员工健康档案主键集合
     * @return 结果
     */
    public int deleteHealthRecordsByHealthIds(Long[] healthIds);

    /**
     * 删除员工健康档案信息
     * 
     * @param healthId 员工健康档案主键
     * @return 结果
     */
    public int deleteHealthRecordsByHealthId(Long healthId);


    ImportResultVO uploadHealthRecordsInfosFile(MultipartFile multipartFile, Long userId, String username);

}
