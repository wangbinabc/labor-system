package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpHistory;
import com.yuantu.labor.domain.Employee;
import lombok.Data;

import java.util.List;

@Data
public class EmpHistoryChangeDicVO {


    private String chineseName;

    private List<EmpHistory> empHistoryExportInfos;

}
