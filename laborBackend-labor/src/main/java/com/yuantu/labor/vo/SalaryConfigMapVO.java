package com.yuantu.labor.vo;

import com.yuantu.labor.domain.SalaryConfig;
import lombok.Data;

import java.util.List;

@Data
public class SalaryConfigMapVO {

    private String configType;

    private List<SalaryConfig> configInfos;
}
