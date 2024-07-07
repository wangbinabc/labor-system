package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpPerformance;
import lombok.Data;

import java.util.List;

@Data
public class EmpSalaryCountChangeDicVO {


    private String chineseName;

    private List<EmpSalaryYearCountVO> exportInfos;

}
