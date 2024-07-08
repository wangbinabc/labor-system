package com.yuantu.labor.vo;

import lombok.Data;

@Data
public class HistoryChangeTypeSearchVO {

    private String startYearMonth;
    private String endYearMonth;
    private String changeType;
    private Integer pageSize = 0;
    private Integer pageNum = 0;
}
