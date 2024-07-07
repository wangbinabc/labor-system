package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Data
public class PostHistorySimpleVO implements IExcelDataModel, IExcelModel {


    /**
     * 员工名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "员工名称")
    private String phEmpName;

    /**
     * 身份证
     */
  //  @NotBlank(message = "不能为空")
    @Excel(name = "身份证")
    private String phEmpIdcard;

    /**
     * 原来岗位
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "原来岗位")
    private String phOriginPostName;

    /**
     * 原来岗级
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "原来岗级")
    private String phOriginPostLevel;

    /**
     * 新的岗位
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "新的岗位")
    private String phDestinPostName;

    /**
     * 新的岗级
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "新的岗级")
    private String phDestinPostLevel;

    /**
     * 调岗原因
     */
//    @Excel(name = "调岗原因")
//    private String phAdjustReason;

    /**
     * 调岗日期
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "调岗日期")
    private Date phAdjustDate;

    /**
     * 调岗类型
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "调岗类型")
    private String phAdjustType;

    /**
     * 调岗申请时间
     */
//    @NotNull(message = "不能为空")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "调岗申请时间")
//    private Date phUpdateDate;


    /**
     * 行号
     */
    private int rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostHistorySimpleVO that = (PostHistorySimpleVO) o;
        return Objects.equals(phEmpIdcard, that.phEmpIdcard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phEmpIdcard);
    }
}
