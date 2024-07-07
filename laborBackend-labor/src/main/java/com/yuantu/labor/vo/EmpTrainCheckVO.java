package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpTrainCheckVO {
    private String trainEmpName;
    private String trainProjectName;
    private Date trainBeginTime;
    private Integer rowNum;

}
