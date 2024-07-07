package com.yuantu.labor.domain;

import lombok.Data;
import java.util.Date;

/**
 * 
 * @TableName loan_worker_history
 */
@Data
public class LoanWorkerHistory  {
    /**
     * 
     */
    private Long historyId;

    /**
     * 快照年月,例如：2012-01
     */
    private String historyYearMonth;

    /**
     * 借工id
     */
    private Long loanId;

    /**
     * 借工员工姓名
     */
    private String loanEmpName;

    /**
     * 身份证号
     */
    private String loanEmpIdcard;

    /**
     * 借工单位id
     */
    private String loanApplyUnitId;

    /**
     * 借工单位名称
     */
    private String loanApplyUnitName;

    /**
     * 借工部门id
     */
    private Long loanApplyDeptId;

    /**
     * 借工部门名称
     */
    private String loanApplyDeptName;

    /**
     * 科室
     */
    private String loanApplyOffice;

    /**
     * 性别
     */
    private String loanEmpGender;

    /**
     * 学历
     */
    private String loanEmpEducation;

    /**
     * 年龄
     */
    private Integer loanEmpAge;

    /**
     * 专业
     */
    private String loanEmpSpeciality;

    /**
     * 职称
     */
    private String loanEmpTitle;

    /**
     * 资格证书
     */
    private String loanEmpCredentials;

    /**
     * 借调入场时间
     */
    private Date loanBeginTime;

    /**
     * 拟安排区域
     */
    private String loanArrageArea;

    /**
     * 新的岗位
     */
    private String loanNewPost;

    /**
     * 备注
     */
    private String loanRemark;

    /**
     * 0 正常 1已删除
     */
    private Boolean disabled;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String updateBy;

    /**
     * 
     */
    private Date updateTime;


}