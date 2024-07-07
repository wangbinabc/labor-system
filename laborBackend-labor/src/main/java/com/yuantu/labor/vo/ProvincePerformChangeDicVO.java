package com.yuantu.labor.vo;

import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.domain.ProvicePerformance;
import lombok.Data;

import java.util.List;

@Data
public class ProvincePerformChangeDicVO {


    private String chineseName;

    private List <ProvicePerformance> exportInfos;

}
