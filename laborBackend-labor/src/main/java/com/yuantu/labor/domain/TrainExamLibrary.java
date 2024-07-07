package com.yuantu.labor.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 题库对象 train_exam_library
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
@Data
public class TrainExamLibrary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 题目ID */
    private Long examId;

    /** 题目名称 */
    @Excel(name = "题目名称")
    private String examName;

    /** 附件 */
    @Excel(name = "附件ID")
    private Long examAnnexPath;

    /** 逻辑删除(0=正常，1=逻辑删除) */
    @Excel(name = "逻辑删除(0=正常，1=逻辑删除)")
    private Integer disabled;

    /** 附件详情 */
   // @Excel(name = "附件详情")
    private EmpDocument empDocument;
}
