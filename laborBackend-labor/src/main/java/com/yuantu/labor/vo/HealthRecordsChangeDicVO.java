package com.yuantu.labor.vo;

import com.yuantu.labor.domain.HealthRecords;
import lombok.Data;

import java.util.List;

@Data
public class HealthRecordsChangeDicVO {


    private String chineseName;

    private List<HealthRecords> exportInfos;

}
