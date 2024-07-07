package com.yuantu.labor.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 培训记录对象 emp_train
 * 
 * @author ruoyi
 * @date 2023-09-22
 */
@Data
public class EmpTrain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工培训ID */
    private Integer trainId;

    /** 培训项目id */
    @Excel(name = "培训项目id")
    private Integer trainProjectId;

    /** 培训项目名称 */
    @Excel(name = "培训项目名称")
    private String trainProjectName;

    /** 学时 */
    @Excel(name = "学时")
    private Integer trainPeriod;

    /** 培训费用(元) */
    @Excel(name = "培训费用(元)")
    private BigDecimal trainFee;

    /** 培训地点/方式  */
    @Excel(name = "培训地点/方式 ")
    private String trainAddrMethod;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trainBeginTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trainEndTime;

    /** 考核方式（考评、考试） */
    @Excel(name = "考核方式", readConverterExp = "考=评、考试")
    private String trainExamMethod;

    /** 人员ID */
    @Excel(name = "人员ID")
    private Long trainEmpId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String trainEmpName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String trainEmpIdcard;

    /** 所属公司ID */
    @Excel(name = "所属公司ID")
    private Long trainWorkUnitsId;

    /** 所属公司 */
    @Excel(name = "所属公司")
    private String trainWorkUnitsName;

    /** 劳务公司ID */
    @Excel(name = "劳务公司ID")
    private Long trainHrCompanyId;

    /** 劳务公司 */
    @Excel(name = "劳务公司")
    private String trainHrCompanyName;


}
