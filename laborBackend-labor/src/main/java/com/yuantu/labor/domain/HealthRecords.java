package com.yuantu.labor.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 员工健康档案对象 health_records
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
@Data
public class HealthRecords extends BaseEntity
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
    @Excel(name = "体检日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date healthDate;

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

    private Boolean isChange = true;


}
