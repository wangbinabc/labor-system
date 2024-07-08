package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class EmpTrainResultVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 员工培训ID */
    private Integer trainId;

    /** 培训项目id */
    //@Excel(name = "培训项目id")
    private Integer trainProjectId;

    /** 人员ID */
    //@Excel(name = "人员ID")
    private Long trainEmpId;
    /** 姓名 */
    @Excel(name = "姓名")
    private String trainEmpName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String trainEmpIdcard;

    /** 培训项目名称 */
    @Excel(name = "培训项目名称")
    private String trainProjectName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @Excel(name = "培训性质",readConverterExp="1=管理,2=技能,3=技术")
    private String projectNature;



    /** 学时 */
    @Excel(name = "学时")
    private Integer trainPeriod;

    /** 培训费用(元) */
    @Excel(name = "培训费用(元)")
    private BigDecimal trainFee;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trainBeginTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trainEndTime;

    /** 培训地点/方式  */
    @Excel(name = "培训地点/方式 ")
    private String trainAddrMethod;


    /** 考核方式（考评、考试） */
    @Excel(name = "考核方式")
    private String trainExamMethod;


    /** 所属公司ID */
    //@Excel(name = "所属公司ID")
    private Long trainWorkUnitsId;

    /** 所属公司 */
    @Excel(name = "所属公司")
    private String trainWorkUnitsName;

    /** 劳务公司ID */
    //@Excel(name = "劳务公司ID")
    private Long trainHrCompanyId;

    /** 劳务公司 */
    @Excel(name = "劳务公司")
    private String trainHrCompanyName;

    /** 主要培训内容/课程 */
    //@Excel(name = "主要培训内容/课程")
    private String projectContent;

    @ApiModelProperty("有关员工数据 已删除 false 未时除 true")
    private Boolean isChange;

    /** 培训方式(1=内培, 2=外培) */
    private String projectMethod;




}
