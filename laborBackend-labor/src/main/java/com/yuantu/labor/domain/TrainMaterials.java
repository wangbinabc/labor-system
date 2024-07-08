package com.yuantu.labor.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.labor.vo.FileVO;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 培训项目材料对象 train_materials
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
@Data
public class TrainMaterials extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 材料id */
    private Integer matId;

    /** 材料名称 */
    @Excel(name = "材料名称")
    private String matName;

    /** 所属项目ID */
    @Excel(name = "所属项目ID")
    private Integer matProjectId;

    /** 所属项目 */
    @Excel(name = "所属项目")
    private String matProjectName;

    /** 附件地址 */
    @Excel(name = "附件地址")
    private String matAnnexPath;

    /** 所属目录 */
    @Excel(name = "所属目录")
    private Integer matDirectoryId;
    /**
     * 目录路径     *
     */
    @Excel(name = "培训材料目录")
    private String matDirectoryName;



    /** 材料更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "材料更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date matUpdateTime;

    private Integer[] matIds;

    private FileVO fileVO;

}
