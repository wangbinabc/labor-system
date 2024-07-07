package com.yuantu.labor.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 资格证书对象 emp_credentials
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class EmpCredentials extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 证书ID
     */
    private Long credId;


    /**
     * 员工id
     */
    //  @Excel(name = "员工id")
    private Long credEmpId;

    /**
     * 员工姓名
     */
    @Excel(name = "姓名")
    @ApiModelProperty("姓名")
    private String credEmpName;

    @Excel(name = "身份证号")
    @ApiModelProperty("身份证号")
    private String credEmpIdcard;

    @ApiModelProperty("部门id")
    //@Excel(name = "部门id")
    private Long deptId;

    @Excel(name = "部门名称")
    private String deptName;


    @Excel(name = "证书名称")
    @ApiModelProperty("证书名称")
    private String credName;

    @Excel(name = "证书类型", readConverterExp = "1=A类,2=B类,3=C类")
    @ApiModelProperty("证书类型")
    private String credType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证书注册时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credRegistTime;

    @ApiModelProperty("证书注册时间开始")
    private Date credRegistTimeStart;

    @ApiModelProperty("证书注册时间结束")
    private Date credRegistTimeEnd;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "资格证获取时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credObtainTime;

    @ApiModelProperty("资格证获取时间开始")
    private Date credObtainTimeStart;

    @ApiModelProperty("资格证获取时间结束")
    private Date credObtainTimeEnd;

    @Excel(name = "有效期")
    @ApiModelProperty("有效期")
    private String credPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "注册证到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credExpTime;

    @ApiModelProperty("注册证到期时间开始")
    private Date credExpTimeStart;

    @ApiModelProperty("注册证到期时间结束")
    private Date credExpTimeEnd;

    @ApiModelProperty("剩余天数")
    private Integer remainingDays;


    //   @Excel(name = "资格证附件")
    private Long credAualificationAnnex;

    /**
     * 注册证附件
     * 将doc_id以以A,B,C,D的格式进行保存
     */
    // @Excel(name = "注册证附件")
    private List<Long> credRegistAnnex = new ArrayList<>();

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    //  @Excel(name = "更新日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credUpdateTime;

    /**
     * 逻辑删除，0：正常，1：逻辑删除
     */
    //   @Excel(name = "逻辑删除，0：正常，1：逻辑删除")
    private Integer disabled;

    /**
     * 提醒标识，0：正常，1：接近到期
     */
    private Integer reminder;

    private Integer reminderTime;



    private Boolean isChange = true;

    //是否删除
    private Integer isRelated;
}
