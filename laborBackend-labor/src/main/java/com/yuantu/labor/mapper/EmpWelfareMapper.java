package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.vo.ExportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工福利Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
@Mapper
@Repository
public interface EmpWelfareMapper 
{
    /**
     * 查询员工福利
     * 
     * @param welfareId 员工福利主键
     * @return 员工福利
     */
    public EmpWelfare selectEmpWelfareByWelfareId(Long welfareId);

    /**
     * 查询员工福利列表
     * 
     * @param empWelfare 员工福利
     * @return 员工福利集合
     */
    public List<EmpWelfare> selectEmpWelfareList(EmpWelfare empWelfare);


    /**
     * 查询导出列表
     *
     * @return 员工集合
     */
    List<EmpWelfare> findExportInfos(@Param("export") ExportVO export);
    /**
     * 新增员工福利
     * 
     * @param empWelfare 员工福利
     * @return 结果
     */
    public int insertEmpWelfare(EmpWelfare empWelfare);

    /**
     * 修改员工福利
     * 
     * @param empWelfare 员工福利
     * @return 结果
     */
    public int updateEmpWelfare(EmpWelfare empWelfare);

    /**
     * 删除员工福利
     * 
     * @param welfareId 员工福利主键
     * @return 结果
     */
    public int deleteEmpWelfareByWelfareId(Long welfareId);

    /**
     * 批量删除员工福利
     * 
     * @param welfareIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpWelfareByWelfareIds(Long[] welfareIds);
}
