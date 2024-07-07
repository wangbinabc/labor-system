package com.yuantu.labor.vo;

import com.yuantu.labor.domain.TrainProject;
import lombok.Data;

import java.util.List;
@Data
public class TrainResultChangeDicVO {
    private String chineseName;

    private List<TrainResultDataVO> exportInfos;
}
