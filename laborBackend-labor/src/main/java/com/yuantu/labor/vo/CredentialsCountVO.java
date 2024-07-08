package com.yuantu.labor.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CredentialsCountVO {

    private String nameOrIdcard;
    private Integer deptId;
    private String deptName;
    private String credType;
    private String credName;
    /**
     * 统计结果
     * */
    private Integer countResult;


}
