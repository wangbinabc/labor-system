package com.yuantu.labor.vo;

import com.yuantu.labor.domain.FamilyRelationsHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 快照员工信息
 *
 * @author syw
 * @since 2023-11-09
 **/
@Data
@ApiModel("快照员工信息")
public class EmpHistoryVO {
    @ApiModelProperty("快照id")
    private Long historyId;

    @ApiModelProperty(name = "快照年月, 例如：2012-01")
    private String historyYearMoth;

    @ApiModelProperty("员工ID")
    private Long empId;

    @ApiModelProperty("员工姓名")
    private String empName;

    @ApiModelProperty("协同编码")
    private String empCode;

    @ApiModelProperty("身份证")
    private String empIdcard;

    @ApiModelProperty("员工年龄")
    private Integer empAge;

    @ApiModelProperty("出生日期")
    private Date birthDate;

    @ApiModelProperty("员工状态: 1在职,2新入职,3辞职,4辞退,5即将到龄,6到龄,7返聘")
    private String empStatus;

    @ApiModelProperty("政治面貌")
    private String empPoliticalStatus;

    @ApiModelProperty("性别")
    private String empGender;

    @ApiModelProperty("学历")
    private String empEducation;

    @ApiModelProperty("所学专业")
    private String empSpeciality;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("毕业时间")
    private Date graduateTime;

    @ApiModelProperty("最高学历")
    private String highestEducation;

    @ApiModelProperty("职称")
    private String empTitle;

    @ApiModelProperty("取得时间")
    private Date acquireTime;

    @ApiModelProperty("人员类别")
    private String empCategory;

    @ApiModelProperty("人员类型: 1=普通员工,2=特级员工")
    private String empType;

    @ApiModelProperty("部门ID")
    private Long empDeptId;

    @ApiModelProperty("用工部门")
    private String empDeptName;

    @ApiModelProperty("用工公司")
    private String empEmployingUnits;

    @ApiModelProperty("劳务公司")
    private String empHrCompany;

    @ApiModelProperty("岗级")
    private String empPositionLevel;

    @ApiModelProperty("岗位名称")
    private String empPosition;

    @ApiModelProperty("任职时间")
    private Date onJobTime;

    @ApiModelProperty("薪级")
    private String empSalaryLevel;

    @ApiModelProperty("薪级下限")
    private String salaryLevelMin;

    @ApiModelProperty("薪级上限")
    private String salaryLevelMax;

    @ApiModelProperty("紧急联系人")
    private String empEmergencyContact;

    @ApiModelProperty("紧急联系人电话")
    private String empEmergencyContactPhone;

    @ApiModelProperty("联系方式")
    private String empTelephone;

    @ApiModelProperty("推荐人")
    private String empReference;

    @ApiModelProperty("人员变动类型")
    private String empChangeType;

    @ApiModelProperty("更新时间")
    private Date empUpdateTime;

    @ApiModelProperty("创建时间")
    private Date empCreateTime;

    @ApiModelProperty("创建人id")
    private Long empCreateBy;

    @ApiModelProperty("更新人id")
    private Long empUpdateBy;

    @ApiModelProperty("员工头像信息")
    private String empAvatarUrl;

    @ApiModelProperty("是否删除 0 未删除 1 已删除")
    private Boolean disabled;

    @ApiModelProperty("是否可见 0 可见 1 不可见")
    private Boolean invisible;

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

    @ApiModelProperty("现居住地址")
    private String empHomeAddress;

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

    @ApiModelProperty("进本企业时间")
    private Date empHiredate;

    @ApiModelProperty("合同起始时间")
    private Date contractStartTime;

    @ApiModelProperty("合同结束时间")
    private Date contractEndTime;

    @ApiModelProperty("工作电话")
    private String workPhone;

    @ApiModelProperty("备注")
    private String empRemark;

    @ApiModelProperty("家庭成员信息")
    private List<FamilyRelationsHistory> empFamilyInfos;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
