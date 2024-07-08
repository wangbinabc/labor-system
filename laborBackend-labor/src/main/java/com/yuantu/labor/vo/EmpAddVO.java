package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;


/**
 * 员工对象 employee
 *
 * @author ruoyi
 * @date 2023-09-06
 */

@Data
public class EmpAddVO {


    /**
     * 员工Id
     */
    @ApiModelProperty("员工Id")
    private Long empId;

    /**
     * 员工姓名 q
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    /**
     * 身份证 q
     */
    @ApiModelProperty("身份证号")
    private String empIdcard;

    /**
     * 员工状态 q
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
     * q
     */
    @ApiModelProperty("民族")
    private String nation;

    /**
     * q
     */
    @ApiModelProperty("性别")
    private String empGender;

    /**
     * q
     */
    @ApiModelProperty("年龄")
    private Integer empAge;

    @ApiModelProperty("出生日期")
    private Date birthDate;

    @ApiModelProperty("预计退休日期")
    private Date empExpireTime;

    @ApiModelProperty("用工年龄上限日期")
    private Date hireLimitDate;

    /**
     * 政治面貌
     */
    @ApiModelProperty("政治面貌")
    private String empPoliticalStatus;

    /**
     * 性别
     */
    // @ApiModelProperty("性别")
    //private String empGender;

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
    //  @ApiModelProperty("人员类型")
    //  private String empType;

    /**
     * 部门ID
     */
    // @Excel(name = "部门ID"）
    @ApiModelProperty("部门id")
    private Long empDeptId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
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

    /**
     * 岗位
     */
    @ApiModelProperty("岗位类别")
    private String empPositionCategory;

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


    /**
     * 紧急联系人
     */
//    @ApiModelProperty("紧急联系人")
//    private String empEmergencyContact;

    /**
     * 紧急联系人电话
     */
//    @ApiModelProperty("紧急联系人电话")
//    private String empEmergencyContactPhone;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系方式")
    private String empTelephone;

    /**
     * 家庭地址
     */
    @ApiModelProperty("现居住地地址")
    private String empHomeAddress;

    /**
     * 推荐人
     */
    @ApiModelProperty("推荐人")
    private String empReference;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String empRemark;

    /**
     * 备注
     */
    @ApiModelProperty("家庭成员信息")
    private List<EmpFamilyVO> empFamilyInfos;


    @ApiModelProperty("协同编码")
    private String empCode;

    @ApiModelProperty("任职时间")
    @JsonFormat(pattern = "yyyy-MM")
    private Date onJobTime;


    @ApiModelProperty("部门信息2")
    private String deptInfoTwo;

    @ApiModelProperty("部门信息3")
    private String deptInfoThree;
    @ApiModelProperty("籍贯")
    private List<Long> nativePlace;
    @ApiModelProperty("户籍所在地地址")
    private String domicilePlace;
    @ApiModelProperty("出生地")
    private List<Long> birthPlace;
    @ApiModelProperty("学校")
    private String school;
    @ApiModelProperty("最高学历")
    private String highestEducation;
    @ApiModelProperty("毕业时间")
    private Date graduateTime;
    @ApiModelProperty("参加工作时间")
    private Date attendTime;
    @ApiModelProperty("断供月数")
    private Integer refuseMonth;
    @ApiModelProperty("进本企业前累计工作时间(月数)")
    private Integer accumulativeMonth;
    @ApiModelProperty("合同起始时间")
    private Date contractStartTime;
    @ApiModelProperty("合同结束时间")
    private Date contractEndTime;
    @ApiModelProperty("工作电话")
    private String workPhone;

    @ApiModelProperty("薪级下限")
    private String salaryLevelMin;

    @ApiModelProperty("薪级上限")
    private String salaryLevelMax;

    /**
     * 员工头像文件id
     */
    private Long avatarDocId;

    /**
     * 员工身份文件id
     */
    private List<Long> idCardDocList;

    /**
     * 员工学历文件id
     */
    private List<Long> educateDocList;

    /**
     * 员工职称文件id
     */
    private List<Long> paperDocList;

    /**
     * 员工档案文件id
     */
    private List<Long> recordDocList;

    /**
     * 其他文件id
     */
    private List<Long> otherDocList;

}
