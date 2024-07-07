package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.HealthRecords;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.HealthRecordsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工健康档案Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
@Mapper
@Repository
public interface HealthRecordsMapper 
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
     * @param healthRecordsVO 员工健康档案
     * @return 员工健康档案集合
     */
    public List<HealthRecords> selectHealthRecordsList(HealthRecordsVO healthRecordsVO);

    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<HealthRecords> findExportInfos(@Param("export") ExportVO export);

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
     * 删除员工健康档案
     * 
     * @param healthId 员工健康档案主键
     * @return 结果
     */
    public int deleteHealthRecordsByHealthId(Long healthId);

    /**
     * 批量删除员工健康档案
     * 
     * @param healthIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHealthRecordsByHealthIds(Long[] healthIds);
}
