package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 员工对象 employee
 *
 * @author ruoyi
 * @date 2023-09-06
 */

@Data
public class EmpDetailHistoryVO {


    private Long empHistoryId;


    private String historyYearMonth;

    /**
     * 员工Id
     */
    @ApiModelProperty("员工Id")
    private Long empId;

    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    @ApiModelProperty("协同编码")
    private String empCode;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdcard;

    @ApiModelProperty("员工年龄")
    private Integer empAge;

    @ApiModelProperty("出生日期")
    private Date birthDate;

    /**
     * 员工状态
     * ：1在职，2
     * 新入职，3
     * 辞职，4
     * 辞退，5
     * 即将到龄
     * ，6到龄
     * ，7返聘
     */
    @ApiModelProperty("员工状态")
    private String empStatus;

    /**
     * 政治面貌
     */
    @ApiModelProperty("政治面貌")
    private String empPoliticalStatus;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String empGender;

    /**
     * 学历
     */
    @ApiModelProperty("学历")
    private String empEducation;

    /**
     * 专业
     */
    @ApiModelProperty("专业")
    private String empSpeciality;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("毕业时间")
    private Date graduateTime;

    @ApiModelProperty("最高学历")
    private String highestEducation;

    /**
     * 职称
     */
    @ApiModelProperty("职称")
    private String empTitle;

    @ApiModelProperty("取得时间")
    private Date acquireTime;

    /**
     * 人员类别
     */
    @ApiModelProperty("人员类别")
    private String empCategory;

    /**
     * 人员类型
     */
    @ApiModelProperty("人员类型")
    private String empType;

    /**
     * 部门ID
     */
    // @Excel(name = "部门ID"）
    @ApiModelProperty("部门id")
    private Long empDeptId;

    /**
     * 部门名称
     */
    @ApiModelProperty("用工部门")
    private String empDeptName;

    /**
     * 用工单位
     */
    @ApiModelProperty("用工公司")
    private String empEmployingUnits;

    /**
     * 人力资源公司
     */
    @ApiModelProperty("劳务公司")
    private String empHrCompany;

    /**
     * 岗级
     */
    @ApiModelProperty("岗级")
    private String empPositionLevel;

    /**
     * 岗位
     */
    @ApiModelProperty("岗位")
    private String empPosition;

    @ApiModelProperty("任职时间")
    private Date onJobTime;

    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("进入企业时间")
    private Date empHiredate;

    /**
     * 薪级
     */
    @ApiModelProperty("薪级")
    private String empSalaryLevel;

    @ApiModelProperty("薪级下限")
    private String salaryLevelMin;

    @ApiModelProperty("薪级上限")
    private String salaryLevelMax;

    /**
     * 紧急联系人
     */
    @ApiModelProperty("紧急联系人")
    private String empEmergencyContact;

    /**
     * 紧急联系人电话
     */
    @ApiModelProperty("紧急联系人电话")
    private String empEmergencyContactPhone;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String empTelephone;

    /**
     * 家庭地址
     */
    @ApiModelProperty("现居住地址")
    private String empHomeAddress;

    /**
     * 推荐人
     */
    @ApiModelProperty("推荐人")
    private String empReference;

    @ApiModelProperty("人员变动类型")
    private String empChangeType;

    @ApiModelProperty("员工头像信息")
    private String empAvatarUrl;

    @ApiModelProperty("员工状态改变时间")
    private Date empStatusUpdateTime;

    @ApiModelProperty("民族")
    private String nation;

    @ApiModelProperty("预计退休时间")
    private Date empExpireTime;

    @ApiModelProperty("退休提醒")
    private String retireReminder;

    @ApiModelProperty("用工年龄上限日期")
    private Date hireLimitDate;

    @ApiModelProperty("部门信息2")
    private String deptInfoTwo;

    @ApiModelProperty("部门信息3")
    private String deptInfoThree;

    @ApiModelProperty("岗位类别")
    private String empPositionCategory;

    @ApiModelProperty("籍贯")
    private List<Long> nativePlace;

    @ApiModelProperty("出生地")
    private List<Long> birthPlace;

    @ApiModelProperty("户籍所在地地址")
    private String domicilePlace;

    @ApiModelProperty("参加工作时间")
    private Date attendTime;

    @ApiModelProperty("断保月数")
    private Integer refuseMonth;

    @ApiModelProperty("进本企业前累计工作时间(月)")
    private Integer accumulativeMonth;

    @ApiModelProperty("合同起始时间")
    private Date contractStartTime;

    @ApiModelProperty("合同结束时间")
    private Date contractEndTime;

    @ApiModelProperty("工作电话")
    private String workPhone;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String empRemark;

    /**
     * 备注
     */
    @ApiModelProperty("员工家庭关系信息")
    private List<EmpFamilyVO> empFamilyInfos;

}
