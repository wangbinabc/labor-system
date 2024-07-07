package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class LoanWorkChangeDicVO {


    private String chineseName;

    private List<LoanWorkerExportListVO> exportInfos;

}
