package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导入结果反馈对象
 *
 * @author deng
 * @date 2018/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultVO {
    private Integer totalCount;
    private Integer successCount;
    private Integer errorCount;
    private Long failFileId;
    private String failFileUrl;
}

