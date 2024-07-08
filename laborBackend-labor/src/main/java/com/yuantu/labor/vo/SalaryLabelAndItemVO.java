package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;
@Data
public class SalaryLabelAndItemVO {
    private String label;

    private List<SalaryLabelAndItemVO> children;

}
