package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmpTrainChangeDicVO {


    private String chineseName;

    private List<EmpTrainResultVO> exportInfos;

}
