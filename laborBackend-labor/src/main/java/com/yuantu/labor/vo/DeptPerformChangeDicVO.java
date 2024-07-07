package com.yuantu.labor.vo;

import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.domain.EmpHistory;
import lombok.Data;

import java.util.List;

@Data
public class DeptPerformChangeDicVO {


    private String chineseName;

    private List<DeptPerformance> exportInfos;

}
