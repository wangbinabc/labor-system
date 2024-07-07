package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class TrainResultVO extends BaseEntity
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

    /** 年度 */
    @Excel(name = "年度")
    private String projectYear;


    /** 责任部门名称 */
    @Excel(name = "责任部门名称")
    private String projectDeptName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @Excel(name = "培训性质(1=管理，2=技能，3=技术）",readConverterExp="1=管理,2=技能,3=技术")
    private String projectNature;

    /** 培训方式(1=内培, 2=外培) */
    @Excel(name = "培训方式(1=内培, 2=外培)",readConverterExp="1=内培,2=外培")
    private String projectMethod;

    /** 项目分类(1=一类，2=二类，3=三类) */
    @Excel(name = "项目分类(1=一类，2=二类，3=三类)",readConverterExp = "1=一类,2=二类,3=三类")
    private String projectClassify;

    private FileVO fileVO;

}
