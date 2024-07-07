package com.yuantu.labor.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import com.yuantu.labor.domain.EmpDocument;
import lombok.Data;

import java.util.Date;

/**
 * 员工健康档案对象 health_records
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
@Data
public class HealthRecordsSimpleVO implements IExcelDataModel, IExcelModel
{
    private static final long serialVersionUID = 1L;




    /** 姓名 */
    @Excel(name = "姓名")
    private String healthEmpName;

    /** 身份证 */
  //  @Excel(name = "身份证")
    private String healthEmpIdcard;

    /** 年度 */
    @Excel(name = "年度")
    private String healthYear;

    /** 体检机构 */
    @Excel(name = "体检机构")
    private String healthMedical;

    /** 体检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "体检时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date healthDate;

    /** 体检结论 */
    @Excel(name = "体检结论")
    private String healthConclusion;

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


}
