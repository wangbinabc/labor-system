package com.yuantu.labor.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于岗位页装载查询条件
 */
@Data
@AllArgsConstructor
public class PostQueryParamsVo {
    private static final long serialVersionUID = 1L;
    /** 名字或者身份证 */
    private String nameOrIdcard;

    /** 调岗变动日期 */
    private String phAdjustDate;

    //是否删除
    private Integer  isRelated;

}
