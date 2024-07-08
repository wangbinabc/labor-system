package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import com.yuantu.labor.domain.EmpDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资格证书对象 emp_credentials
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class CredentialsDetailsVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 证书ID
     */
    private Long credId;

    /**
     * 证书名称
     */
    @Excel(name = "证书名称")
    private String credName;

    /**
     * 员工id
     */
    @Excel(name = "员工id")
    private Long credEmpId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String credEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证")
    private String credEmpIdcard;


    /**
     * 部门名和部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    @Excel(name = "所属部门名称")
    private String deptName;


    /**
     * 证书类型
     */
    @Excel(name = "证书类型")
    private String credType;

    /**
     * 证书注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证书注册时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credRegistTime;

    /**
     * 资格证获取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "资格证获取时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credObtainTime;

    /**
     * 有效期
     */
    @Excel(name = "有效期")
    private String credPeriod;

    /**
     * 注册证到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "注册证到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date credExpTime;

    /**
     * 资格证附件
     */
    @Excel(name = "资格证附件")
    private Long credAualificationAnnex;


//    /** 资格证附件 */
//    @Excel(name = "资格证附件")
//    private String credAualificationAnnexPath;

    /**
     * 资格证附件详情
     */
    private EmpDocument credAualificationDocument;

    /**
     * 注册证附件
     * 将doc_id以以A,B,C,D的格式进行保存
     */
    @Excel(name = "注册证附件")
    private List<Long> credRegistAnnex = new ArrayList<>();

    private String credRegistAnnexStr;

    /**
     * 注册证附件详情
     */
    private List<EmpDocument> credRegistAnnexDocuments = new ArrayList<>();
    ;
//    /** 注册证附件
//     * 将doc_id以以A,B,C,D的格式进行保存
//     * */
//    @Excel(name = "注册证附件")
//    private List<String> credRegistAnnexPaths = new ArrayList<>();

    @ApiModelProperty("剩余天数")
    private Integer remainingDays;

    /**
     * 提醒标识，0：正常，1：接近到期
     */
    private Integer reminder = 0;

    /**
     * 逻辑删除，0：正常，1：逻辑删除
     */
    private Integer disabled;

    @ApiModelProperty("有关员工数据已删除 false 未删除 true")
    private Boolean isChange;

}
