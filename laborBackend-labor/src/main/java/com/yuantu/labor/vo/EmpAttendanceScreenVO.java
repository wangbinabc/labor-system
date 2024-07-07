package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 考勤对象 emp_attendance
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class EmpAttendanceScreenVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 考勤ID
     */
    private Long attendId;

    /**
     * 员工id
     */
   // @Excel(name = "员工id")
    private Long attendEmpId;

    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    @Excel(name = "姓名")
    private String attendEmpName;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证")
    @Excel(name = "身份证号")
    private String attendEmpIdcard;

    //@Excel(name = "考勤部门")
    @ApiModelProperty("考勤部门ID")
    private Long departmentId;

    @Excel(name = "考勤部门")
    @ApiModelProperty("考勤部门")
    private String departmentName;

    @ApiModelProperty("人员类别 1=外包,2=全民,3=借工,4=直签")
    @Excel(name = "人员类别", readConverterExp = "1=外包,2=全民,3=借工,4=直签")
    private String empCategory;

    /**
     *考勤日期区间
     */
    @ApiModelProperty("考勤日期区间起始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date attendRecordDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("考勤日期区间结束")
    private Date attendRecordDateEnd;



    /**
     * 是否工作日：0：是，1：否
     */
    @ApiModelProperty("是否工作日")
    @Excel(name = "工作日判定", readConverterExp = "0=是,1=否")
    private String attendIsweekday;

    /**
     * 考勤类型，1，出勤，2，出差，3，休假, 4, 加班
     */

    @ApiModelProperty("考勤类型，1，出勤，2，出差，3，休假, 4, 加班")
    @Excel(name = "考勤状态",readConverterExp = "1=出勤,2=出差,3=休假,4=加班")
    private String attendStatus;

    /**
     * 考勤详细
     */
    @ApiModelProperty("考勤明细")
    @Excel(name = "考勤明细")
    private String attendStatusDetail;

    /**
     * 修改更新时间
     */
    @ApiModelProperty("修改更新时间起始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeStart;

    @ApiModelProperty("修改更新时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeEnd;

    //是否删除
    private Integer  isRelated;
}
