package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmpPerformGeneralVO {

    private List<EmpPerformYearInfoVO> yearInfos;

    private List<EmpPerformSimpleVO> performSimpleInfos;

}
