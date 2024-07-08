package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmpTrainQueryVO extends BaseEntity {
    /**
     * 培训员工名称或者身份证
     */
    private String empName;
    private Integer[] trainIds;

    /**
     * 是否是当前月
     */

    private Integer isCurrMonth;
    /**
     * 查询月份
     */

    private String month;
    /**
     * 部门
     */
    private Integer deptId;
    /**
     * 培训状态 0 已完成  1 进行中
     */
    private Integer trainStatus;


    private String statusMonth;

    /**
     * 培训性质
     */
    @ApiModelProperty("培训性质")
    private String projectNature;
    /** 所属公司 */
    @ApiModelProperty("所属公司")
    private String trainWorkUnitsName;
    /** 劳务公司 */
    @ApiModelProperty("劳务公司")
    private String trainHrCompanyName;

    @ApiModelProperty("所属项目名称")
    private String trainProjectName;

    /** 学时 */
    @ApiModelProperty("最小学时")
    private Integer minTrainPeriod;

    @ApiModelProperty("最大学时")
    private Integer maxTrainPeriod;

    /** 培训费用(元) */
    @ApiModelProperty("最小培训费用")
    private BigDecimal minTrainFee;

    @ApiModelProperty("最大培训费用")
    private BigDecimal maxTrainFee;

    @ApiModelProperty("培训地址和方式")
    private String trainAddrMethod;

    /** 开始时间 */
    @ApiModelProperty("开始时间")
    private Date trainBeginTime;

    /** 结束时间 */
    @ApiModelProperty("结束时间")
     private Date trainEndTime;

    /** 考核方式（考评、考试） */
    @ApiModelProperty("考核方式")
    private String trainExamMethod;

    /** 姓名 */
    @ApiModelProperty("姓名")
    private String trainEmpName;

    /** 身份证号 */
    @ApiModelProperty("身份证号")
    private String trainEmpIdcard;

    /** 开始修改时间 */
    @ApiModelProperty("开始修改时间")
    private Date beginUpdateTime;

    /**
     * 结束修改时间
     */
    @ApiModelProperty("结束修改时间")
    private Date endUpdateTime;

    private Integer isRelated;

}
