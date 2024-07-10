package com.yuantu;

import com.yuantu.labor.domain.TrainProject;
import com.yuantu.labor.mapper.TrainProjectMapper;
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

    @Test
    public void testSelectTrainProjectList(){
        TrainProject trainProject= new TrainProject();
        trainProject.setProjectName("700kv");
        List<TrainProject> trainProjects = trainProjectMapper.selectTrainProjectList(trainProject);
        for (TrainProject project : trainProjects) {
            System.out.println(project);
        }
    }
}
