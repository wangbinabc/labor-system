package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.vo.EmpTrainQueryVO;
import com.yuantu.labor.vo.TrainProjectQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 培训项目Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-14
 */
@Mapper
@Repository
public interface TrainProjectMapper 
{

     List<TrainProject> selectTrainProjectByProjectName(String projectName);

    /**
     * 筛选查询
     * @author junjia
     * @param vo
     * @return 培训项目集合
     */
     List<TrainProject> selectTrainProjectListByQueryVO(TrainProjectQueryVO vo);

     List<TrainProject> selectTrainProjectListByKeyword(String keyword);

    /**
     * 模糊查询
     * @author junjia
     * @param trainProject
     * @return 培训项目集合
     */
    List<TrainProject> selectTrainProjectList(TrainProject trainProject);

    /**
     * 多选删除
     * @author junjia
     * @param ids
     * @return 删除的培训项目的数量
     */
    int deleteTrainProjectByProjectIds(Integer[] ids);

    /**
     * 新增培训项目
     * @param trainProject
     * @return
     */
    int insertTrainProject(TrainProject trainProject);
}
