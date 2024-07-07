package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpWelfare;
import lombok.Data;

import java.util.List;

@Data
public class EmpWelfareChangeDicVO {


    private String chineseName;

    private List<EmpWelfare> exportInfos;

}
