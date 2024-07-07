package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpPerformance;
import com.yuantu.labor.domain.EmpResume;
import lombok.Data;

import java.util.List;

@Data
public class EmpResumeChangeDicVO {


    private String chineseName;

    private List<EmpResume> exportInfos;

}
