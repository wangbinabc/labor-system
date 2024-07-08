package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.FileImportRecord;
import org.springframework.stereotype.Repository;

/**
 * 导入记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-26
 */
@Repository
public interface FileImportRecordMapper {
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
     * 删除导入记录
     *
     * @param id 导入记录主键
     * @return 结果
     */
    public int deleteFileImportRecordById(Long id);

    /**
     * 批量删除导入记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFileImportRecordByIds(Long[] ids);
}
