package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.domain.EmpExpert;
import lombok.Data;

import java.util.List;

@Data
public class EmpExpertChangeDicVO {


    private String chineseName;

    private List<ExpertExportListVO> exportInfos;

}
