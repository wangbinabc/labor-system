package com.yuantu.labor.service;

import java.util.List;
import java.util.Map;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpTrain;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 培训记录Service接口
 *
 * @author ruoyi
 * @date 2023-09-22
 */
public interface IEmpTrainService {


    /**
     * 查询培训记录列表
     *
     * @param empTrain 培训记录
     * @return 培训记录集合
     */
    public List<EmpTrain> selectEmpTrainList(EmpTrain empTrain);

    public List<EmpTrainResultVO> selectEmpTrainByEmpId(Integer empId);

    public List<PieChartVO> countEmpTrainNatureNumByEmpId(Integer empId);

    public List<PieChartVO> countEmpTrainPeriodByEmpId(Integer empId);


    public List<PieChartVO> countRecentYearsTrainNumByEmpId(Integer empId);

    public List<PieChartVO> countRecentYearsTrainPeriodByEmpId(Integer empId);

    public Map<String,List> recommendTrainProject(Integer empId);



}
