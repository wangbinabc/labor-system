package com.yuantu.labor.service;

import java.util.List;
import java.util.Map;

import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.domain.EmpWelfare;
import com.yuantu.labor.domain.InsuranceConfiguration;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 员工福利Service接口
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
public interface IEmpWelfareService 
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

    List<EmpWelfare> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);
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
     * 五险配置
     *
     * @param insuranceConfigurations 五险配置对象
     * @return 结果
     */
    int baseConfig(List<InsuranceConfiguration> insuranceConfigurations);

    /**
     * 批量删除员工福利
     * 
     * @param welfareIds 需要删除的员工福利主键集合
     * @return 结果
     */
    public int deleteEmpWelfareByWelfareIds(Long[] welfareIds);

    /**
     * 删除员工福利信息
     * 
     * @param welfareId 员工福利主键
     * @return 结果
     */
    public int deleteEmpWelfareByWelfareId(Long welfareId);




    ImportResultVO uploadWelfareInfosFile(MultipartFile multipartFile, Long userId, String username);

}
