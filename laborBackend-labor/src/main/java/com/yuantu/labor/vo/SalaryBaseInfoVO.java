package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class SalaryBaseInfoVO {

    private String name;

    private List<SalaryValueVO> values;

}
