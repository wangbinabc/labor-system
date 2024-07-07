package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class AttendExcelVerifyVO {

    private Long successNum;

    private Long errorNum;

    private Long totalNum;

    private List<LaborDateVO> laborDateList;

    private List<ErrorForm> errorFormList;

}
