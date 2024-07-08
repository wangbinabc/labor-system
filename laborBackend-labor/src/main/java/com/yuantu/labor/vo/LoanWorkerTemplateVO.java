package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用于查询借工情况列表
 */
@Data
public class LoanWorkerTemplateVO {


    /**
     * 员工姓名
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "员工姓名")
    private String loanEmpName;

    /**
     * 身份证
     */
   // @NotBlank(message = "不能为空")
    //@Excel(name = "身份证号", cellType = Excel.ColumnType.STRING)
   // private String loanEmpIdcard;

    /**
     * 借工单位名称-借工部门名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "借工单位名称-借工部门名称")
    private String loanApplyUnitDeptName;

    /**
     * 借用批次
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "借用批次")
    private String loanBatch;


    /**
     * 拟安排区域
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "拟安排区域")
    private String loanArrageArea;

    /**
     * 新的岗位
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "拟从事岗位")
    private String loanNewPost;

    /**
     * 借调入场时间
     */
    @NotNull(message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借调入场时间")
    private Date loanBeginTime;


}
