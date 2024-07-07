package com.yuantu.labor.vo;


import com.yuantu.common.annotation.Excel;
import lombok.Data;

@Data
public class TrainEmployeeNameVO {

    @Excel(name = "姓名")
    private String name;

}
