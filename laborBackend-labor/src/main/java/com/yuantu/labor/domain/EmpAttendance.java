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
 * 考勤对象 emp_attendance
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class EmpAttendance extends BaseEntity {
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
    @Excel(name = "姓名")
    private String attendEmpName;
    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String attendEmpIdcard;

    @Excel(name = "考勤部门")
    private String departmentName;

    // @Excel(name = "考勤部门")
    private String departmentId;

    @ApiModelProperty("人员类别 1=外包,2=全民,3=借工,4=直签")
    @Excel(name = "人员类别", readConverterExp = "1=外包,2=全民,3=借工,4=直签")
    private String empCategory;
    /**
     * 考勤日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "考勤日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date attendRecordDate;
    /**
     * 是否工作日：0：是，1：否
     */
    @Excel(name = "工作日判定", readConverterExp = "0=是,1=否")
    private String attendIsweekday;
    /**
     * 考勤类型，1，出勤，2，出差，3，休假, 4, 加班
     */
    @Excel(name = "考勤状态", readConverterExp = "1=出勤,2=出差,3=休假,4=加班")
    private String attendStatus;
    /**
     * 考勤详细
     */
    @Excel(name = "考勤明细")
    private String attendStatusDetail;
    /**
     * 修改更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    // @Excel(name = "修改更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date attendUpdateTime;

    /**
     * 逻辑删除：0：正常，1：删除
     */
    // @Excel(name = "逻辑删除：0：正常，1：删除")
    private Integer disabled;


    private Boolean isChange = true;

    //是否删除
    private Integer isRelated;


    private Integer flag;
}
