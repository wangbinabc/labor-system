package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 员工对象 employee
 *
 * @author ruoyi
 * @date 2023-09-06
 */

@Data
public class EmpBaseInfoVO {


    /**
     * 员工ID
     */
    private Long empId;


    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String empName;

    @ApiModelProperty("员工性别")
    private String empGender;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证号")
    private String empIdcard;


    @ApiModelProperty("年龄")
    private Integer empAge;

    /**
     * 学历
     */
    @ApiModelProperty("学历")
    private String empEducation;

    /**
     * 专业
     */
    @Excel(name = "专业")
    private String empSpeciality;

    /**
     * 职称
     */
    @ApiModelProperty("职称")
    private String empTitle;

    /**
     * 人员类别
     */
    @ApiModelProperty("人员类别")
    private String empCategory;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门id")
    private Long empDeptId;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String empDeptName;

    @ApiModelProperty("单位id")
    private Long empUnitId;

    @ApiModelProperty("单位名称")
    private String empUnitName;

    /**
     * 岗位
     */
    @ApiModelProperty("岗位")
    private String empPosition;

    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    private Date empHiredate;

    /**
     * 员工头像信息
     */
    @ApiModelProperty("员工头像信息")
    private String empAvatarUrl;

    @ApiModelProperty("出勤率")
    private String attendanceRatio;

    @ApiModelProperty("出勤天数")
    private Integer attendanceNum;

    @ApiModelProperty("休假天数")
    private Integer holidayNum;

    @ApiModelProperty("加班天数")
    private Integer overTimeNum;

    @ApiModelProperty("单位加班排名")
    private Integer overTimeRank;

    @ApiModelProperty("单位加班平均天数")
    private Integer avgOverTimeNum;

    @ApiModelProperty("加班相较平均水平")
    private String compareOverTime;

    @ApiModelProperty("出差天数")
    private Integer onBusinessNum;

    @ApiModelProperty("单位出差排名")
    private Integer onBusinessRank;

    @ApiModelProperty("单位出差平均天数")
    private Integer avgOnBusinessNum;

    @ApiModelProperty("出差相较平均水平")
    private String compareOnBusiness;

    @ApiModelProperty("参加培训课程数")
    private Integer trainCourseNum;

    @ApiModelProperty("单位平均参加培训课程数")
    private Integer avgTrainCourseNum;

    @ApiModelProperty("参加培训课程相较平均水平")
    private String compareTrainCourse;

    @ApiModelProperty("参加培训小时数")
    private Integer trainHourNum;

    @ApiModelProperty("单位平均参加培训小时数")
    private Integer avgTrainHourNum;

    @ApiModelProperty("参加培训小时数相较平均水平")
    private String compareTrainHour;


}
