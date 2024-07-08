package com.yuantu.labor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.FileImportRecordMapper;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.service.IFileImportRecordService;

/**
 * 导入记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-10
 */
@Service
public class FileImportRecordServiceImpl implements IFileImportRecordService {
    @Autowired
    private FileImportRecordMapper fileImportRecordMapper;

    /**
     * 查询导入记录
     *
     * @param id 导入记录主键
     * @return 导入记录
     */
    @Override
    public FileImportRecord selectFileImportRecordById(Long id) {
        return fileImportRecordMapper.selectFileImportRecordById(id);
    }

    /**
     * 查询导入记录列表
     *
     * @param fileImportRecord 导入记录
     * @return 导入记录
     */
    @Override
    public List<FileImportRecord> selectFileImportRecordList(FileImportRecord fileImportRecord) {
        return fileImportRecordMapper.selectFileImportRecordList(fileImportRecord);
    }

    /**
     * 新增导入记录
     *
     * @param fileImportRecord 导入记录
     * @return 结果
     */
    @Override
    public int insertFileImportRecord(FileImportRecord fileImportRecord) {
        return fileImportRecordMapper.insertFileImportRecord(fileImportRecord);
    }

    /**
     * 修改导入记录
     *
     * @param fileImportRecord 导入记录
     * @return 结果
     */
    @Override
    public int updateFileImportRecord(FileImportRecord fileImportRecord) {
        return fileImportRecordMapper.updateFileImportRecord(fileImportRecord);
    }

    /**
     * 批量删除导入记录
     *
     * @param ids 需要删除的导入记录主键
     * @return 结果
     */
    @Override
    public int deleteFileImportRecordByIds(Long[] ids) {
        return fileImportRecordMapper.deleteFileImportRecordByIds(ids);
    }

    /**
     * 删除导入记录信息
     *
     * @param id 导入记录主键
     * @return 结果
     */
    @Override
    public int deleteFileImportRecordById(Long id) {
        return fileImportRecordMapper.deleteFileImportRecordById(id);
    }
}
