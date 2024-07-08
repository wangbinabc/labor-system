package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PieChartVO {
    private String id;
    private String name;
    private Integer value;

    public PieChartVO() {

    }

    public PieChartVO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public PieChartVO(String id, String name, Integer value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
