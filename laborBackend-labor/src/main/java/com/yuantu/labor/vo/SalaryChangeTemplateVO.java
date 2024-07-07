

package com.yuantu.labor.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.yuantu.common.utils.StringUtils;
import lombok.Data;

import java.util.Date;


/**
 * 员工对象 employee
 *
 * @author ahadon
 * @date 2023-09-06
 */

@Data
public class SalaryChangeTemplateVO {


    /**
     * 员工姓名
     */
    @ExcelProperty(value = "员工姓名")
    private String empName;



    @ExcelProperty(value = "变动年月(yyyy-MM)")
    private String yearMonth;

    /**
     * 备注
     */
    @ExcelProperty(value =  "变动前薪级")
    private String preSalaryLevel;


    @ExcelProperty(value =  "变动后薪级")
    private String afterSalaryLevel;


    @ExcelProperty(value =  "变动类别")
    private String changeType;

    @ExcelProperty(value =  "是否由岗位变动引起")
    private String isPostChange;



}
