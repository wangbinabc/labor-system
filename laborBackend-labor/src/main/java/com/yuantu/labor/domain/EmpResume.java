package com.yuantu.labor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 履历对象 emp_resume
 *
 * @author ruoyi
 * @date 2023-09-12
 */
@Data
public class EmpResume extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 履历ID
     */
    private Integer resuId;

    /**
     * 员工id
     */
    // @Excel(name = "员工id")
    private Long resuEmpId;

    /**
     * 员工姓名
     */
    @Excel(name = "姓名")
    private String resuEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String resuEmpIdcard;

    /**
     * 履历类别
     * 1.社会，2.项目，3.本单位
     */
    @Excel(name = "履历类别", readConverterExp = "1=社会,2=项目,3=本单位")
    private String resuType;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resuBeginDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resuEndDate;

    /**
     * 工作单位名称
     */
    @Excel(name = "工作单位")
    private String resuWorkUnitName;

    /**
     * 公司岗位
     */
    @Excel(name = "从事岗位")
    private String resuPosition;


    /**
     * 部门
     */
    @Excel(name = "部门")
    private String resuDept;


    private String resuDeptName;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String resuProjectName;

    /**
     * 项目规模
     */
    @Excel(name = "项目规模")
    private String resuProjectScale;


    /**
     * 项目类型
     */
    @Excel(name = "项目类型", readConverterExp = "1=A类,2=B类,3=C类")
    private String resuProjectType;

    /**
     * 项目职位
     */
    @Excel(name = "项目职位")
    private String resuProjectJob;


    /**
     * 证明人
     */
    @Excel(name = "证明人")
    private String resuProjectCertifier;


    /**
     * 履历内容
     */
    @Excel(name = "工作内容简述")
    private String resuContext;


    /**
     * 更新时间
     */
    private Date resuUpdateTime;


    private Boolean disabled;

    @ApiModelProperty("有关员工数据已删除 false 未删除 true")
    private Boolean isChange;

}
