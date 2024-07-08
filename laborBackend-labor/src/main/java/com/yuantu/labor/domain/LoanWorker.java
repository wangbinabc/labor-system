package com.yuantu.labor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 借工对象 loan_worker
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Data
public class LoanWorker extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 借调id
     */
    private Long loanId;

    /**
     * 借调员工姓名
     */
    @Excel(name = "姓名")
    private String loanEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String loanEmpIdcard;

    /**
     * 借工单位ID
     */
    //  @Excel(name = "借工单位ID")
    private String loanApplyUnitId;

    /**
     * 借工单位名称
     */
    @Excel(name = "单位")
    private String loanApplyUnitName;

    /**
     * 借工部门ID
     */
    //  @Excel(name = "借工部门ID")
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
     * 年龄
     */
    @Excel(name = "年龄")
    private Integer loanEmpAge;

    /**
     * 性别
     */
    @Excel(name = "性别")
    private String loanEmpGender;

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
     * 借调入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拟入场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loanBeginTime;

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
     * 备注
     */
    @Excel(name = "备注")
    private String loanRemark;


    private Boolean disabled;
}
