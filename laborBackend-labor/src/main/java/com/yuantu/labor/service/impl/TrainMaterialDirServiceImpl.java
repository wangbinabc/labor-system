package com.yuantu.labor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.yuantu.common.core.domain.TreeSelect;
import com.yuantu.common.core.domain.entity.SysMenu;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.labor.vo.TreeSelectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.TrainMaterialDirMapper;
import com.yuantu.labor.domain.TrainMaterialDir;
import com.yuantu.labor.service.ITrainMaterialDirService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 材料目录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Service
public class TrainMaterialDirServiceImpl implements ITrainMaterialDirService {
    @Autowired
    private TrainMaterialDirMapper trainMaterialDirMapper;

    /**
     * 查询材料目录
     *
     * @param dirId 材料目录主键
     * @return 材料目录
     */
    @Override
    public TrainMaterialDir selectTrainMaterialDirByDirId(Integer dirId) {
        return trainMaterialDirMapper.selectTrainMaterialDirByDirId(dirId);
    }

    /**
     * 查询材料目录列表
     *
     * @param trainMaterialDir 材料目录
     * @return 材料目录
     */
    @Override
    public List<TrainMaterialDir> selectTrainMaterialDirList(TrainMaterialDir trainMaterialDir) {
        return trainMaterialDirMapper.selectTrainMaterialDirList(trainMaterialDir);
    }



    public List<TrainMaterialDir> buildMenuTree(List<TrainMaterialDir> dirs) {
        List<TrainMaterialDir> returnList = new ArrayList<TrainMaterialDir>();
        List<Integer> tempList = dirs.stream().map(TrainMaterialDir::getDirId).collect(Collectors.toList());
        for (Iterator<TrainMaterialDir> iterator = dirs.iterator(); iterator.hasNext(); ) {
            TrainMaterialDir dir = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dir.getDirParentId())) {
                recursionFn(dirs, dir);
                returnList.add(dir);
            }
        }
        if (returnList.isEmpty()) {
            returnList = dirs;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<TrainMaterialDir> list, TrainMaterialDir t) {
        // 得到子节点列表
        List<TrainMaterialDir> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TrainMaterialDir tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TrainMaterialDir> getChildList(List<TrainMaterialDir> list, TrainMaterialDir t) {
        List<TrainMaterialDir> tlist = new ArrayList();
        Iterator<TrainMaterialDir> it = list.iterator();
        while (it.hasNext()) {
            TrainMaterialDir n = it.next();
            if (n.getDirParentId().intValue() == t.getDirId().intValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TrainMaterialDir> list, TrainMaterialDir t) {
        return getChildList(list, t).size() > 0;
    }
}
