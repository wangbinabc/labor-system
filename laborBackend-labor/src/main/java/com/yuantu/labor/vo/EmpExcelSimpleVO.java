package com.yuantu.labor.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmpExcelSimpleVO {

    @Excel(name = "员工姓名")
    private String empName;
    @NotBlank
    @Excel(name = "身份证号")
    private String idCard;


}
