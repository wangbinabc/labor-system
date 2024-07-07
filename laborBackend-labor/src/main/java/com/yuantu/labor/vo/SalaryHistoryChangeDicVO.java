package com.yuantu.labor.vo;

import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.domain.SalaryHistory;
import lombok.Data;

import java.util.List;

@Data
public class SalaryHistoryChangeDicVO {


    private String chineseName;

    private List<SalaryHistory> exportInfos;

}
