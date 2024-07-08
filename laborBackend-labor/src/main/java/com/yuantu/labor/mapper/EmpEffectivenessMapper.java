package com.yuantu.labor.mapper;

import java.util.Date;
import java.util.List;
import com.yuantu.labor.domain.EmpEffectiveness;
import com.yuantu.labor.vo.DeptEffectivenessCountVO;
import com.yuantu.labor.vo.DeptEffectivenessSearchVO;
import com.yuantu.labor.vo.EmpEffectivenessCountVO;
import com.yuantu.labor.vo.EmpEffectivenessSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 人员效能Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-06
 */
@Mapper
@Repository
public interface EmpEffectivenessMapper 
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
     * 删除人员效能
     * 
     * @param effId 人员效能主键
     * @return 结果
     */
    public int deleteEmpEffectivenessByEffId(Long effId);

    /**
     * 批量删除人员效能
     * 
     * @param effIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpEffectivenessByEffIds(Long[] effIds);

    List<EmpEffectiveness> findInfosByEmpIdsAndCreateTime(@Param("empIds") List<Long> empIds, @Param("now") Date now);

    List<EmpEffectivenessCountVO> selectEmpEffectiveness(EmpEffectivenessSearchVO queryVO);

    List<DeptEffectivenessCountVO> selectDeptEfficiency(DeptEffectivenessSearchVO queryVO);

    void insertBatch(@Param("effectInfos") List<EmpEffectiveness>  empEffectivenessInfos);

    void removeInfosByEmpNameAndTime(@Param("empName") String empName, @Param("yearMonth") String yearMonth);
}
