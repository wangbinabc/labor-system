package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.vo.*;


/**
 * 培训项目Service接口
 *
 * @author ruoyi
 * @date 2023-09-14
 */
public interface ITrainProjectService {
    public List<TrainProject> selectTrainProjectListByQueryVO(TrainProjectQueryVO vo);


}
