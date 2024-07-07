package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmpTrainCountVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 学时 */
    @Excel(name = "学时")
    private Integer trainPeriod;

    /** 培训费用(元) */
    @Excel(name = "培训费用(元)")
    private BigDecimal trainFee;

    /** 人员ID */
    @Excel(name = "人员ID")
    private Long trainEmpId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String trainEmpName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String trainEmpIdcard;

    /** 所属公司 */
    @Excel(name = "所属公司")
    private String trainWorkUnitsName;

    /** 劳务公司 */
    @Excel(name = "劳务公司")
    private String trainHrCompanyName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @Excel(name = "培训性质(1=管理，2=技能，3=技术）",readConverterExp="1=管理,2=技能,3=技术")
    private String projectNature;
}
