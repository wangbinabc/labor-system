package com.yuantu.labor.vo;

import com.yuantu.labor.domain.SalaryHistory;
import com.yuantu.labor.domain.TrainProject;
import lombok.Data;

import java.util.List;

@Data
public class TrainProjectChangeDicVO {


    private String chineseName;

    private List<TrainProject> exportInfos;

}
