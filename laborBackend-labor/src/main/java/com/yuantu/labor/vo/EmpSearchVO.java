package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
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
public class EmpSearchVO {


    /**
     * 员工工号
     */
    @ApiModelProperty("选择月份")
    private Date month;


    private List<Long> empIds;


    private String keyword;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdcard;

    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    /**
     * 最小年龄
     */
    @ApiModelProperty("最小年龄")
    private Integer minAge;

    /**
     * 最大年龄
     */
    @ApiModelProperty("最大年龄")
    private Integer maxAge;

    @ApiModelProperty("用工单位名称")
    private String empEmployingUnits;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String empDeptName;


    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long empDeptId;


    /**
     * 人力资源公司
     */
    @ApiModelProperty("人力资源公司名称")
    private String empHrCompany;

    /**
     * 岗位
     */
    @ApiModelProperty("岗位")
    private String empPosition;

    /**
     * 专业
     */
    @ApiModelProperty("专业")
    private String empSpeciality;


    /**
     * 入职开始日期
     */
    @ApiModelProperty("入职开始日期")
    private Date minEmpHireDate;

    /**
     * 入职结束日期
     */
    @ApiModelProperty("入职结束日期")
    private Date maxEmpHireDate;

    /**
     * 到龄开始日期
     */
    @ApiModelProperty("到龄开始日期")
    private Date minEmpExpireTime;

    /**
     * 到龄结束日期
     */
    @ApiModelProperty("到龄结束日期")
    private Date maxEmpExpireTime;

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
     * 性别
     */
    @ApiModelProperty("员工性别")
    private String empGender;

    /**
     * 岗级
     */
    @ApiModelProperty("岗级")
    private String empPositionLevel;

    /**
     * 薪级
     */
    @Excel(name = "薪级")
    private String empSalaryLevel;

    /**
     * 政治面貌
     */
    @ApiModelProperty("政治面貌")
    private String empPoliticalStatus;

    @ApiModelProperty("学历")
    private String empEducation;

    @ApiModelProperty("职称")
    private String empTitle;

    @ApiModelProperty("人员类型")
    private String empType;


    @ApiModelProperty("人员类别")
    private String empCategory;

    /**
     * 可选展示参数
     */
//    @ApiModelProperty("可选展示参数")
//    private List<String> params;


    @ApiModelProperty("备注")
    private String empRemark;

    @ApiModelProperty("出生日期开始时间")
    private Date birthdayStart;
    @ApiModelProperty("出生日期结束时间")
    private Date birthdayEnd;

    @ApiModelProperty("用工年龄上限日期开始时间")
    private Date hireLimitDateStart;
    @ApiModelProperty("用工年龄上限日期结束时间")
    private Date hireLimitDateEnd;

    @ApiModelProperty("协同编码")
    private String empCode;

    @ApiModelProperty("部门信息2")
    private String deptInfoTwo;

    @ApiModelProperty("部门信息3")
    private String deptInfoThree;

    @ApiModelProperty("任职时间开始时间")
    private Date onJobTimeStart;
    @ApiModelProperty("任职时间结束时间")
    private Date onJobTimeEnd;

    @ApiModelProperty("籍贯")
    private List<String> nativePlace;

    private String nativePlaceStr;

    @ApiModelProperty("出生地")
    private List<String> birthPlace;

    private String birthPlaceStr;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("毕业时间开始时间")
    private Date graduateTimeStart;
    @ApiModelProperty("毕业时间结束时间")
    private Date graduateTimeEnd;

    @ApiModelProperty("参加工作时间开始时间")
    private Date attendTimeStart;
    @ApiModelProperty("参加工作时间结束时间")
    private Date attendTimeEnd;

    @ApiModelProperty("断保月数最小值")
    private Integer refuseMonthMin;
    @ApiModelProperty("断保月数最大值")
    private Integer refuseMonthMax;

    @ApiModelProperty("进本企业前累计工作时间(月)最小值")
    private Integer accumulativeMonthMin;
    @ApiModelProperty("进本企业前累计工作时间(月)最大值")
    private Integer accumulativeMonthMax;

    @ApiModelProperty("合同时间开始时间")
    private Date contractTimeStart;
    @ApiModelProperty("合同时间结束时间")
    private Date contractTimeEnd;

    @ApiModelProperty("联系方式")
    private String empTelephone;

    @ApiModelProperty("工作电话")
    private String workPhone;

    @ApiModelProperty("推荐人")
    private String empReference;

    @ApiModelProperty("家庭成员信息-联系方式1")
    private String contactPhone1;

    @ApiModelProperty("家庭成员信息-联系方式2")
    private String contactPhone2;

    @ApiModelProperty("最高学历")
    private String highestEducation;

    @ApiModelProperty("家庭成员信息-与本人关系1")
    private String famAppellation1;

    @ApiModelProperty("家庭成员信息-与本人关系2")
    private String famAppellation2;




}
