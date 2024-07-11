package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmpTrainProjectVO {
    //EmpTrain
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


    //TrainProject
    /** 培训项目id */
    private Integer projectId;

    /** 培训项目名称 */
    @Excel(name = "培训项目")
    private String projectName;

    /** 主要培训内容/课程 */
    @Excel(name = "主要培训内容/课程")
    private String projectContent;

    /** 年度 */
    @Excel(name = "年度")
    private String projectYear;

    /** 责任部门 */
    //@Excel(name = "责任部门ID")
    private Integer projectDeptId;

    /** 责任部门名称 */
    @Excel(name = "责任部门")
    private String projectDeptName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @Excel(name = "培训性质")
    private String projectNature;

    /** 培训方式(1=内培, 2=外培) */
    @Excel(name = "培训方式")
    private String projectMethod;

    /** 项目分类(1=一类，2=二类，3=三类) */
    @Excel(name = "项目分类")
    private String projectClassify;

    /** 是否完成 */
    @Excel(name = "是否完成")
    private String projectIsfinish;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date projectUpdateTime;

    /** 统计培训项目的数量 */
    private int trainingCount;
}
