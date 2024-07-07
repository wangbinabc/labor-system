package com.yuantu.labor.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 薪级变动对象 salary_history
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@Data
public class SalaryHistory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * history_id
     */
    private Integer hisId;

    /**
     * 人员编号
     */
    //@Excel(name = "人员编号")
    private Long hisEmpId;

    /**
     * 人员姓名
     */
    @Excel(name = "姓名")
    private String hisEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String hisEmpIdcard;

    /**
     * 年月
     */
    @Excel(name = "变动年月")
    private String hisYearMonth;

    /**
     * 变动前薪级
     */
    @Excel(name = "变动前薪级")
    private String hisPreviousLevel;

    /**
     * 变动后薪级
     */
    @Excel(name = "变动后薪级")
    private String hisNextLevel;

    /**
     * 变动类别
     */
    @Excel(name = "薪酬变动类别")
    private String hisChangeType;

    /**
     * 是否岗位变动引起的
     */
    @Excel(name = "是否由岗位变动引起")
    private String hisIspostChange;

    private Boolean isChange;


}
