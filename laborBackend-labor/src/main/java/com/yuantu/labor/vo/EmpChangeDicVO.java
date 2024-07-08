package com.yuantu.labor.vo;

import com.yuantu.labor.domain.Employee;
import lombok.Data;

import java.util.List;

@Data
public class EmpChangeDicVO {


    private String chineseName;

    private List<Employee> empExportInfos;

}
