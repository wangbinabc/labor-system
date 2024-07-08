package com.yuantu.labor.service;


import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.labor.domain.EmpEffectiveness;
import com.yuantu.labor.vo.*;

import java.util.List;
import java.util.Map;

public interface IEffectivenessService {


    List<DeptEffectivenessCountVO> selectDeptEffectivenessList(DeptEffectivenessSearchVO deptEffectivenessSearchVO);

    List<EmpEffectivenessCountVO> selectEmpEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO);


    TableDataInfo selectDeptEffectiveness(DeptEffectivenessSearchVO deptEffectivenessSearchVO);

    TableDataInfo selectEmpEffectiveness(EmpEffectivenessSearchVO queryVO);


}
