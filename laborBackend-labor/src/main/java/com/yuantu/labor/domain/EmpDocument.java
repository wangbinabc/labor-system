package com.yuantu.labor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 用户证件对象 emp_document
 *
 * @author ruoyi
 * @date 2023-09-14
 */
@Data
public class EmpDocument {


    /**
     * $column.columnComment
     */
    private Long docId;

    /**
     * 附件类型 1 照片 2 身份证 3 最高职称证书 4 最高学历
     */
    @Excel(name = "附件类型 1 照片 2 身份证 3 最高职称证书 4 最高学历")
    private String docType;

    /**
     * 附件名称
     */
    @Excel(name = "附件名称")
    private String docName;

    /**
     * 附件路径
     */
    @Excel(name = "附件路径")
    private String docAnnexPath;

    /**
     * 员工ID
     */
    @Excel(name = "员工ID")
    private Long docEmpId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date docUpdateTime;


    private String createBy;


    private Boolean disabled;


    private Boolean isCompress;

}
