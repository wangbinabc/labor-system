package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 履历对象 emp_resume
 *
 * @author ruoyi
 * @date 2023-09-12
 */
@Data
public class ResumeSocialExportVO {


    /**
     * 员工姓名
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "姓名")
    private String resuEmpName;

    /**
     * 身份证
     */

    @Excel(name = "身份证号", cellType = Excel.ColumnType.STRING)
    private String resuEmpIdcard;

    /**
     * 开始日期
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resuBeginDate;

    /**
     * 结束日期
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resuEndDate;


    /**
     * 工作单位名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "工作单位")
    private String resuWorkUnitName;


    /**
     * 履历内容
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "工作内容简述")
    private String resuContext;


}
