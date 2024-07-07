package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author yuantu
 */

public class ErrorForm implements Serializable {
    @Excel(name = "行号", orderNum = "1", width = 10)
    private Integer id;
    @Excel(name = "原因", orderNum = "2", width = 30)
    private String reason;


    public ErrorForm() {
    }

    public Integer getId() {
        return id;
    }

    public ErrorForm(Integer id, String reason) {
        this.id = id;
        this.reason = reason;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
