package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 用于查询借工情况列表
 */
@Data
public class LoanWorkerExportListVO {

    private Long loanId;

    @Excel(name="单位")
    private String loanApplyUnitId;

    /**
     * 员工姓名
     */
    @Excel(name = "姓名")
    private String loanEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String loanEmpIdcard;


    private Long loanApplyDeptId;

    /**
     * 借工部门名称
     */
    @Excel(name = "用工部门")
    private String loanApplyDeptName;
    /**
     * 科室
     */
    @Excel(name="处/室")
    private String loanApplyOffice;

    /**
     * 性别
     */
    @Excel(name = "性别")
    private String loanEmpGender;
    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private Integer loanEmpAge;
    /**
     * 学历
     */
    @Excel(name = "学历")
    private String loanEmpEducation;


    /**
     * 专业
     */
    @Excel(name = "专业")
    private String loanEmpSpeciality;

    /**
     * 职称
     */
    @Excel(name = "职称")
    private String loanEmpTitle;

    /**
     * 资格证书
     */
    @Excel(name = "执业资格证书")
    private String loanEmpCredentials;



    /**
     * 拟安排区域
     */
    @Excel(name = "拟派驻现场(片区)")
    private String loanArrageArea;

    /**
     * 新的岗位
     */
    @Excel(name = "拟从事岗位")
    private String loanNewPost;

    /**
     * 借调入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拟入场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loanBeginTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String loanRemark;
    /**
     * 借调更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "借调更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date loanUpdateDate;
}
