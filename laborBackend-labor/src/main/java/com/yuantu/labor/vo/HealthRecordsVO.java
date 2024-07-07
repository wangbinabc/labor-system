package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import com.yuantu.labor.domain.EmpDocument;
import lombok.Data;

import java.util.Date;

/**
 * 员工健康档案对象 health_records
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
@Data
public class HealthRecordsVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long healthId;

    /** 员工ID */
 //   @Excel(name = "ID")
    private Long healthEmpId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String healthEmpName;

    /** 身份证 */
    @Excel(name = "身份证号")
    private String healthEmpIdcard;

    /** 年度 */
    @Excel(name = "年度")
    private String healthYear;

    /** 体检机构 */
    @Excel(name = "体检机构")
    private String healthMedical;

    /** 体检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "体检日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String healthDate;

    /** 体检结论 */
    @Excel(name = "体检结论")
    private String healthConclusion;

    /** 附件路径编号 */
  //  @Excel(name = "附件路径编号")
    private Long annexPath;


    /** 附件路径地址 */
  //  @Excel(name = "体检附件")
    private EmpDocument empDocument;


    /** $column.columnComment */
   // @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer disabled;

    //是否删除
    private Integer  isRelated;
}
