package com.yuantu.labor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 导入记录对象 file_import_record
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Data
public class FileImportRecord {


    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 成功条数
     */
    @Excel(name = "成功条数")
    private Integer successCount;

    /**
     * 失败条数
     */
    @Excel(name = "失败条数")
    private Integer failureCount;

    /**
     * 导入总条数
     */
    @Excel(name = "导入总条数")
    private Integer totalCount;

    /**
     * 原始文件id
     */
    @Excel(name = "原始文件id")
    private Long originFileId;

    /**
     * 错误信息文件id
     */
    @Excel(name = "错误信息文件id")
    private Long failFileId;


    /**
     * 状态 0正常，1删除
     */
    @Excel(name = "状态 0正常，1删除")
    private Boolean disabled;


    @ApiModelProperty("导入文件类型")
    private String fileType;


    @ApiModelProperty("导入状态")
    private String importStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private Long creatorId;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifiedTime;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    private Long modifierId;


}
