package com.yuantu.labor.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 材料目录对象 train_material_dir
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
@Data
public class TrainMaterialDir extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 目录id */
    private Integer dirId;

    /** 目录名称 */
    @Excel(name = "目录名称")
    private String dirName;

    /** 目录级别(1=一级目录, 2=二级目录) */
    @Excel(name = "目录级别",readConverterExp = "1=一级目录, 2=二级目录")
    private String dirLevel;


    /** 父目录 */
    @Excel(name = "父目录")
    private Integer dirParentId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dirUpdateTime;

    /**
     * 文件数量
     */
    @Excel(name = "目录下文件数量")
    private Integer fileNum;

    private List<TrainMaterialDir> children = new ArrayList<TrainMaterialDir>();

}
