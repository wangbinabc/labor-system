package com.yuantu.labor.domain;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.labor.service.impl.EmpInOutServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 员工快照对象 emp_history
 *
 * @author ruoyi
 * @date 2023-09-19
 */
@Data
public class EmpHistory {

    /**
     *
     */
    private Long historyId;

    /**
     * 快照年月,例如：2012-01
     */
    @Excel(name = "快照年月")
    private String historyYearMoth;


    private Long empId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    @ApiModelProperty("员工姓名")
    private String empName;

    /**
     * 员工工号
     */
    // @Excel(name = "员工工号"
    @ApiModelProperty("协同编码")
    private String empCode;

    /**
     * 身份证
     */
    @Excel(name = "身份证")
    @ApiModelProperty("身份证")
    private String empIdcard;


    @Excel(name = "员工年龄")
    @ApiModelProperty("员工年龄")
    private Integer empAge;

    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
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
    @Excel(name = "员工状态")
    @ApiModelProperty("员工状态")
    private String empStatus;

    /**
     * 政治面貌
     */
    @Excel(name = "政治面貌")
    @ApiModelProperty("政治面貌")
    private String empPoliticalStatus;

    /**
     * 性别
     */
    @Excel(name = "性别")
    @ApiModelProperty("性别")
    private String empGender;

    /**
     * 学历
     */
    @Excel(name = "学历")
    @ApiModelProperty("学历")
    private String empEducation;

    /**
     * 专业
     */
    @Excel(name = "专业")
    @ApiModelProperty("专业")
    private String empSpeciality;

    @Excel(name = "学校")
    @ApiModelProperty("学校")
    private String school;


    @Excel(name = "毕业时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("毕业时间")
    private Date graduateTime;

    @Excel(name = "最高学历")
    @ApiModelProperty("最高学历")
    private String highestEducation;

    /**
     * 职称
     */
    @Excel(name = "职称")
    @ApiModelProperty("职称")
    private String empTitle;

    @Excel(name = "取得时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("取得时间")
    private Date acquireTime;

    /**
     * 人员类别
     */
    @Excel(name = "人员类别")
    @ApiModelProperty("人员类别")
    private String empCategory;


    /**
     * 人员类型
     */
    //  @Excel(name = "人员类型", readConverterExp = "1=普通员工,2=特级员工")
    private String empType;

    /**
     * 部门ID
     */
    // @Excel(name = "部门ID")
    private Long empDeptId;


    /**
     * 紧急联系人
     */
//    @Excel(name = "紧急联系人")
    private String empEmergencyContact;

    /**
     * 紧急联系人电话
     */
//    @Excel(name = "紧急联系人电话")
    private String empEmergencyContactPhone;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("用工部门")
    private String empDeptName;

    /**
     * 用工单位
     */
    @Excel(name = "用工单位")
    @ApiModelProperty("用工单位")
    private String empEmployingUnits;

    /**
     * 人力资源公司
     */
    @Excel(name = "人力资源公司")
    @ApiModelProperty("人力资源公司")
    private String empHrCompany;

    /**
     * 岗级
     */
    @Excel(name = "岗级")
    @ApiModelProperty("岗级")
    private String empPositionLevel;

    /**
     * 岗位
     */
    @Excel(name = "岗位")
    @ApiModelProperty("岗位")
    private String empPosition;

    @Excel(name = "任职时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("任职时间")
    private Date onJobTime;


    /**
     * 薪级
     */
    @Excel(name = "薪级")
    @ApiModelProperty("薪级")
    private String empSalaryLevel;

    @Excel(name = "薪级下限")
    @ApiModelProperty("薪级下限")
    private String salaryLevelMin;


    @Excel(name = "薪级上限")
    @ApiModelProperty("薪级上限")
    private String salaryLevelMax;


    /**
     * 联系电话
     */
    @Excel(name = "联系方式")
    @ApiModelProperty("联系方式")
    private String empTelephone;


    /**
     * 推荐人
     */
    @Excel(name = "推荐人")
    @ApiModelProperty("推荐人")
    private String empReference;

    /**
     * 人员变动类型
     */
    // @Excel(name = "人员变动类型")
    private String empChangeType;

    /**
     * 更新时间
     */
    // @JsonFormat(pattern = "yyyy-MM-dd")
    // @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date empUpdateTime;


    private Date empCreateTime;


    private Long empCreateBy;


    private Long empUpdateBy;

    /**
     * 员工头像信息
     */
    //@Excel(name = "员工头像信息")

    private String empAvatarUrl;


    @ApiModelProperty("是否可见 0 可见 1 不可见")
    private Boolean invisible;


    private Date empStatusUpdateTime;

    @Excel(name = "民族")
    @ApiModelProperty("民族")
    private String nation;

    /**
     * 员工到龄时间
     */
    @Excel(name = "预计退休时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("预计退休时间")
    private Date empExpireTime;

    @Excel(name = "退休提醒")
    @ApiModelProperty("退休提醒")
    private String retireReminder;

    @Excel(name = "用工年龄上限日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("用工年龄上限日期")
    private Date hireLimitDate;

    @Excel(name = "部门信息2")
    @ApiModelProperty("部门信息2")
    private String deptInfoTwo;

    @Excel(name = "部门信息3")
    @ApiModelProperty("部门信息3")
    private String deptInfoThree;

    @Excel(name = "人员类别")
    @ApiModelProperty("人员类别")
    private String empPositionCategory;

    @Excel(name = "籍贯")
    @ApiModelProperty("籍贯")
    private String nativePlace;

    /**
     * 家庭地址
     */
    @Excel(name = "现居住地址")
    @ApiModelProperty("现居住地址")
    private String empHomeAddress;


    @Excel(name = "出生地")
    @ApiModelProperty("出生地")
    private String birthPlace;

    @Excel(name = "户籍所在地地址")
    @ApiModelProperty("户籍所在地地址")
    private String domicilePlace;

    @Excel(name = "参加工作时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("参加工作时间")
    private Date attendTime;

    @Excel(name = "断保月数")
    @ApiModelProperty("断保月数")
    private Integer refuseMonth;

    @Excel(name = "进本企业前累计工作时间(月)")
    @ApiModelProperty("进本企业前累计工作时间(月)")
    private Integer accumulativeMonth;

    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进本企业时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("进本企业时间")
    private Date empHiredate;

    @Excel(name = "合同起始", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("合同起始")
    private Date contractStartTime;


    @Excel(name = "合同结束", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("合同结束")
    private Date contractEndTime;

    @Excel(name = "工作电话")
    @ApiModelProperty("工作电话")
    private String workPhone;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String empRemark;


    /**
     * 是否删除 0 未删除 1 已删除
     */
    // @Excel(name = "是否删除 0 未删除 1 已删除")
    private Integer disabled;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Long oldEmpHistoryId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpHistory empHistory = (EmpHistory) o;
        return empId.equals(empHistory.getEmpId()) &&
                empStatus.equals(empHistory.getEmpStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, empStatus);
    }


}
