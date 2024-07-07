package com.yuantu.labor.vo;

import com.yuantu.common.core.page.TableDataInfo;
import lombok.Data;

import java.util.List;

@Data
public class TableDataInfoVO extends TableDataInfo{
    private static final long serialVersionUID = 1L;
    private List<?> columns;
    private List<?> colData;

    private List<?> checkMsgs;
}
