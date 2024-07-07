package com.yuantu.labor.mapper;

import java.util.List;

import com.yuantu.labor.domain.AttendCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2023-11-11
 */
@Repository
public interface AttendCountMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AttendCount selectAttendCountById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param attendCount 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AttendCount> selectAttendCountList(AttendCount attendCount);

    /**
     * 新增【请填写功能名称】
     *
     * @param attendCount 【请填写功能名称】
     * @return 结果
     */
    public int insertAttendCount(AttendCount attendCount);

    /**
     * 修改【请填写功能名称】
     *
     * @param attendCount 【请填写功能名称】
     * @return 结果
     */
    public int updateAttendCount(AttendCount attendCount);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAttendCountById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAttendCountByIds(Long[] ids);

    List<AttendCount> findInfoByReferenceParams(@Param("keyword") String keyword, @Param("yearNum") String year);

    void deleteAllInfos();

    void batchInsertInfos(@Param("attendCounts") List<AttendCount> attendCounts);
}
