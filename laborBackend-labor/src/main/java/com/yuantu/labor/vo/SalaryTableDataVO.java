package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SalaryTableDataVO  {
    private List<EmpSalaryResultVO> salaryMainData;
    private List<Map<String,String>> itemData;
    private List<SalaryLabelAndItemVO> itemTitles;
    private List<Map<String,String>> checkMsgs;
}
