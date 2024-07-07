package com.yuantu.labor.vo;


import lombok.Data;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Data
public class EmpPerformYearInfoVO {


    private String year;

    private List<EmpPerformSeasonVO> seasonInfos;

}
