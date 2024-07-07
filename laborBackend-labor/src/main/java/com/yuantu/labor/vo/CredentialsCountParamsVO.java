package com.yuantu.labor.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CredentialsCountParamsVO {

    private String nameOrIdcard;
    private Integer deptId;
    private String credType;
    private String credName;

    /** 证书注册时间区间 */
    private Date credRegistTimeStart;
    private Date credRegistTimeEnd;

    /** 证书获取时间区间 */
    private Date credObtainTimeStart;
    private Date credObtainTimeEnd;

    /** 注册证到期时间区间 */
    private Date credExpTimeStart;
    private Date credExpTimeEnd;


    /** 剩余时间区间 */
    private Integer remainderStart;
    private Integer remainderEnd;


    /**
     * 提醒标识，0：正常，1：接近到期
     */
    private Integer reminder = 0;

    /**
     * 提醒标识，0：正常，1：接近到期
     */
    private Integer reminderTime;

}
