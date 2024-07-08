package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 专家查询页面VO
 */
@Data
public class ExpertExportListVO {


    /**
     * 员工编号
     */
    // @Excel(name = "员工编号")
    private Long expertEmpId;

    /**
     * 员工名称
     */
    @Excel(name = "姓名")
    private String expertEmpName;

    /**
     * 员工身份证
     */
    @Excel(name = "身份证号")
    private String expertEmpIdcard;

    @Excel(name = "年龄")
    private Integer empAge;

    /**
     * 性别
     */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String empGender;


    /**
     * 专家表
     */
    private Integer expertId;

    /**
     * 专家级别
     */
    @Excel(name = "专家级别", readConverterExp = "1=一级,2=二级,3=三级,4=四级,5=五级")
    private String expertLevel;

    /**
     * 专家称号
     */
    @Excel(name = "专家人才称号")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    //  @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date expertUpdateTime;
}
