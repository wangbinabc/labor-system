package com.yuantu;

import com.yuantu.labor.domain.EmpSalary;
import com.yuantu.labor.domain.EmpTrain;
import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.mapper.EmpSalaryMapper;
import com.yuantu.labor.mapper.EmpTrainMapper;
import com.yuantu.labor.mapper.TrainProjectMapper;
import com.yuantu.labor.vo.EmpTrainProjectVO;
import com.yuantu.labor.vo.TrainProjectQueryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Autowired
    private TrainProjectMapper trainProjectMapper;

    @Autowired
    private EmpTrainMapper empTrainMapper;

    @Test
    public void testSelectTrainProjectList(){
        TrainProject trainProject= new TrainProject();
        List<TrainProject> trainProjects = trainProjectMapper.selectTrainProjectList(trainProject);
        for (TrainProject project : trainProjects) {
            System.out.println(project);
        }
    }

    @Test
    public void testSelectTrainProjectListByQueryVO(){
        TrainProjectQueryVO trainProjectQueryVO = new TrainProjectQueryVO();
        trainProjectQueryVO.setProjectYear("2024");
        List<TrainProject> trainProjects = trainProjectMapper.selectTrainProjectListByQueryVO(trainProjectQueryVO);
        for (TrainProject trainProject : trainProjects) {
            System.out.println(trainProject);
        }
    }

    @Test
    public void testDeleteTrainProjectByProjectIds(){
        Integer[] ids= {27, 28};
        int i = trainProjectMapper.deleteTrainProjectByProjectIds(ids);
        System.out.println(i);
    }


    @Test
    public void testCountTrainProjectByMonth(){
        int i = empTrainMapper.countTrainProjectByMonth("2023-11");
        System.out.println(i);
    }

    @Test
    public void countTrainingByNature(){
        List<EmpTrainProjectVO> empTrainProjectVOS = empTrainMapper.countTrainingByNature("2023-11");
        for (EmpTrainProjectVO empTrainProjectVO : empTrainProjectVOS) {
            String projectNature = empTrainProjectVO.getProjectNature();
            int trainingCount = empTrainProjectVO.getTrainingCount();
            System.out.println(projectNature+" = "+trainingCount);
        }
    }

    @Test
    public void insertTrainProject(){
        TrainProject trainProject= new TrainProject();
        trainProject.setProjectName("翼装飞行");
        trainProject.setProjectYear("2024");
        trainProject.setProjectDeptId(2);
        trainProject.setProjectDeptName("人力资源部");
        trainProject.setProjectMethod("内培");
        trainProject.setProjectClassify("二类");
        trainProject.setProjectContent("10000万高空飞行");
        trainProject.setProjectIsfinish("1");
        int i = trainProjectMapper.insertTrainProject(trainProject);
        System.out.println(i);
        System.out.println(trainProject);
    }

    @Test
    public void updateTrainProject(){
        List<TrainProject> trainProjects = trainProjectMapper.selectTrainProjectByProjectName("翼装飞行");
        TrainProject trainProject = trainProjects.get(0);
        trainProject.setProjectNature("1");
        int i = trainProjectMapper.updateTrainProject(trainProject);
        System.out.println(i);
        System.out.println(trainProject);
    }

}
