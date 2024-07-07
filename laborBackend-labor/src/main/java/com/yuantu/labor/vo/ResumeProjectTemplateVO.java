package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
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
public class ResumeProjectTemplateVO {

    /**
     * 员工姓名
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "姓名")
    private String resuEmpName;

    /**
     * 身份证
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "身份证号", cellType = Excel.ColumnType.STRING)
    private String resuEmpIdcard;
    ;

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
     * 项目名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目名称")
    private String resuProjectName;

    /**
     * 项目规模
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目规模")
    private String resuProjectScale;


    /**
     * 项目类型
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目类型", readConverterExp = "1=A类,2=B类,3=C类")
    private String resuProjectType;

    /**
     * 项目职位
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "项目职位")
    private String resuProjectJob;


    /**
     * 证明人
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "证明人")
    private String resuProjectCertifier;


    /**
     * 履历内容
     */
    // @Excel(name = "工作内容简述")
    private String resuContext;


}
