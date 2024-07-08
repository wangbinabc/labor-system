package com.yuantu.labor.domain;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 培训项目对象 train_project
 * 
 * @author ruoyi
 * @date 2023-09-14
 */
@Data
public class TrainProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 培训项目id */
    private Integer projectId;

    /** 培训项目名称 */
    @Excel(name = "培训项目")
    private String projectName;

    /** 主要培训内容/课程 */
    @Excel(name = "主要培训内容/课程")
    private String projectContent;

    /** 年度 */
    @Excel(name = "年度")
    private String projectYear;

    /** 责任部门 */
    //@Excel(name = "责任部门ID")
    private Integer projectDeptId;

    /** 责任部门名称 */
    @Excel(name = "责任部门")
    private String projectDeptName;

    /** 培训性质(1=管理，2=技能，3=技术） */
    @Excel(name = "培训性质")
    private String projectNature;

    /** 培训方式(1=内培, 2=外培) */
    @Excel(name = "培训方式")
    private String projectMethod;

    /** 项目分类(1=一类，2=二类，3=三类) */
    @Excel(name = "项目分类")
    private String projectClassify;

    /** 是否完成 */
    @Excel(name = "是否完成")
    private String projectIsfinish;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date projectUpdateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainProject project = (TrainProject) o;
        return Objects.equals(projectId, project.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }

    /**
     * 项目id集合
     */
    private Integer[] projectIds;

}
