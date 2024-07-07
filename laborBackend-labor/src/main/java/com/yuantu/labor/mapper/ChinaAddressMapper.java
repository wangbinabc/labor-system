package com.yuantu.labor.mapper;

import com.yuantu.labor.domain.ChinaAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 全国省份城市地区Mapper接口
 *
 * @author ruoyi
 * @date 2023-11-09
 */
@Mapper
@Repository
public interface ChinaAddressMapper {
    /**
     * 查询全国省份城市地区
     *
     * @param id 全国省份城市地区主键
     * @return 全国省份城市地区
     */
    public ChinaAddress selectChinaAddressById(Long id);

    /**
     * 查询全国省份城市地区列表
     *
     * @param chinaAddress 全国省份城市地区
     * @return 全国省份城市地区集合
     */
    public List<ChinaAddress> selectChinaAddressList(ChinaAddress chinaAddress);

    /**
     * 新增全国省份城市地区
     *
     * @param chinaAddress 全国省份城市地区
     * @return 结果
     */
    public int insertChinaAddress(ChinaAddress chinaAddress);

    /**
     * 修改全国省份城市地区
     *
     * @param chinaAddress 全国省份城市地区
     * @return 结果
     */
    public int updateChinaAddress(ChinaAddress chinaAddress);

    /**
     * 删除全国省份城市地区
     *
     * @param id 全国省份城市地区主键
     * @return 结果
     */
    public int deleteChinaAddressById(Long id);

    /**
     * 批量删除全国省份城市地区
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChinaAddressByIds(Long[] ids);

    /**
     * 获取省市区
     *
     * @return 省市区信息列表
     */
    List<ChinaAddress> findAll();

    ChinaAddress findInfoByNameAndLevel(@Param("name") String name, @Param("level") Integer level);

    ChinaAddress findInfoByNameAndLevelAndParentId(@Param("name") String name, @Param("level") Integer level, @Param("parentId") Long parentId);
}
