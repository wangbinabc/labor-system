package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 员工快照对象 emp_history
 * 
 * @author ruoyi
 * @date 2023-09-19
 */
@Data
public class EmpHistoryQueryParamsVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long historyId;

    /** 快照年月,例如：2012-01 */
    private String historyYearMoth;

    /** 员工ID */
    private Long empId;

    /** 员工工号 */
    private String empCode;

    /** 员工头像信息 */
    private String empAvatarUrl;

    /** 身份证或姓名 */
    private String nameOrIdcard;


    /** 员工年龄 */

    private Long empAge;

    /** 员工状态：1在职，2新入职，3辞职，4辞退，5即将到龄，6到龄，7返聘 */
    private String empStatus;

    /** 政治面貌   1中共党员 2中共预备党 3共青团员 4民革党员 5民盟盟员 6无党派人士 7群众 */
    private String empPoliticalStatus;

    /** 性别  0 男  1 女 */
    private String empGender;

    /** 学历   1高中 2中专 3大专 4本科 5硕士 6博士 */
    private String empEducation;

    /** 专业 */
    private String empSpeciality;

    /** 职称 */
    private String empTitle;

    /** 人员类别 */
    private String empCategory;

    /** 人员类型 */
    private String empType;

    /** 部门ID */
    private Long empDeptId;

    /** 部门名称 */
    private String empDeptName;

    /** 用工单位 */
    private String empEmployingUnits;

    /** 人力资源公司 */
    private String empHrCompany;

    /** 岗级  1岗级A 2岗级B 3岗级C */
    private String empPositionLevel;

    /** 岗位 */
    private String empPosition;

    /** 入职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date empHiredate;

    /** 薪级  1薪级A 2薪级B 3薪级C */
    private String empSalaryLevel;

    /** 备注 */
    private String empRemark;

    /** 紧急联系人 */
    private String empEmergencyContact;

    /** 紧急联系人电话 */
    private String empEmergencContactPhone;

    /** 联系电话 */
    private String empTelephone;

    /** 家庭地址 */
    private String empHomeAddress;

    /** 推荐人 */
    private String empReference;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date empUpdateTime;

    /** 到龄时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date empExpireTime;


}
