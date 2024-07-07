package com.yuantu.labor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 emp_expert
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Data
public class EmpExpert extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专家表
     */
    private Integer expertId;


    /**
     * 员工编号
     */
    //@Excel(name = "员工编号")
    private Long expertEmpId;

    /**
     * 员工名称
     */
    @Excel(name = "员工名称")
    private String expertEmpName;

    /**
     * 员工身份证
     */
    @Excel(name = "员工身份证")
    private String expertEmpIdcard;

    /**
     * 专家级别
     */
    @Excel(name = "专家级别")
    private String expertLevel;

    /**
     * 专家称号
     */
    @Excel(name = "专家称号")
    private String expertTitle;

    /**
     * 授予时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "授予时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expertGrantTime;

    /**
     * 解聘时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解聘时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expertDismissTime;

    /**
     * 聘用期
     */
    @Excel(name = "聘用期")
    private String expertPeriod;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    // @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expertUpdateTime;


    private Boolean disabled;


}
