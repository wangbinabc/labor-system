package com.yuantu.labor.service;

import java.util.List;
import  com.yuantu.labor.domain.FileImportRecord;

/**
 * 导入记录Service接口
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
public interface IFileImportRecordService 
{
    /**
     * 查询导入记录
     * 
     * @param id 导入记录主键
     * @return 导入记录
     */
    public FileImportRecord selectFileImportRecordById(Long id);

    /**
     * 查询导入记录列表
     * 
     * @param fileImportRecord 导入记录
     * @return 导入记录集合
     */
    public List<FileImportRecord> selectFileImportRecordList(FileImportRecord fileImportRecord);

    /**
     * 新增导入记录
     * 
     * @param fileImportRecord 导入记录
     * @return 结果
     */
    public int insertFileImportRecord(FileImportRecord fileImportRecord);

    /**
     * 修改导入记录
     * 
     * @param fileImportRecord 导入记录
     * @return 结果
     */
    public int updateFileImportRecord(FileImportRecord fileImportRecord);

    /**
     * 批量删除导入记录
     * 
     * @param ids 需要删除的导入记录主键集合
     * @return 结果
     */
    public int deleteFileImportRecordByIds(Long[] ids);

    /**
     * 删除导入记录信息
     * 
     * @param id 导入记录主键
     * @return 结果
     */
    public int deleteFileImportRecordById(Long id);
}
