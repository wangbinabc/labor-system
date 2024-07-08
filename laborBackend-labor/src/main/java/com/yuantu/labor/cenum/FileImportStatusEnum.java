package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
public enum FileImportStatusEnum {

    DURING("1", "导入中"),

    FINISHED("2", "已完成"),

    FAIL("3", "导入失败"),


    ;

    private String key;

    private String value;
}
