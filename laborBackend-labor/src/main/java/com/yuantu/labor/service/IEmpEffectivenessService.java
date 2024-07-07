package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpEffectiveness;
import com.yuantu.labor.vo.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 人员效能Service接口
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
public interface IEmpEffectivenessService 
{
    /**
     * 查询人员效能
     * 
     * @param effId 人员效能主键
     * @return 人员效能
     */
    public EmpEffectiveness selectEmpEffectivenessByEffId(Long effId);

    /**
     * 查询人员效能列表
     * 
     * @param empEffectiveness 人员效能
     * @return 人员效能集合
     */
    public List<EmpEffectiveness> selectEmpEffectivenessList(EmpEffectiveness empEffectiveness);

    /**
     * 新增人员效能
     * 
     * @param empEffectiveness 人员效能
     * @return 结果
     */
    public int insertEmpEffectiveness(EmpEffectiveness empEffectiveness);

    /**
     * 修改人员效能
     * 
     * @param empEffectiveness 人员效能
     * @return 结果
     */
    public int updateEmpEffectiveness(EmpEffectiveness empEffectiveness);

    /**
     * 批量删除人员效能
     * 
     * @param effIds 需要删除的人员效能主键集合
     * @return 结果
     */
    public int deleteEmpEffectivenessByEffIds(Long[] effIds);

    /**
     * 删除人员效能信息
     * 
     * @param effId 人员效能主键
     * @return 结果
     */
    public int deleteEmpEffectivenessByEffId(Long effId);

    ImportResultVO uploadEmpProfitInfosFile(MultipartFile file,  LoginUser loginUser);
}
