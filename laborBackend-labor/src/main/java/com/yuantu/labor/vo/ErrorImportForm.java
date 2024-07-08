package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuantu
 */
@Data
public class ErrorImportForm {


    @Excel(name = "员工姓名")
    private String empName;
    @Excel(name = "身份证号")
    private String idCard;
    @Excel(name = "错误原因")
    private String reason;


}
