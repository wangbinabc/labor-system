package com.yuantu.labor.domain;

import com.yuantu.labor.vo.FileVO;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 培训成果对象 train_result
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
@Data
public class TrainResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 培训成果名称id */
    private Integer resultId;

    /** 培训成果名称 */
    @Excel(name = "培训成果名称")
    private String resultName;

    /** 所属项目id */
    @Excel(name = "所属项目id")
    private Integer resultProjectId;

    /** 所属项目名称 */
    @Excel(name = "所属项目名称")
    private String resultProjectName;

    /** 成果物附件 */
    @Excel(name = "成果物附件")
    private String resultAnnexPath;

    private FileVO fileVO;


}
