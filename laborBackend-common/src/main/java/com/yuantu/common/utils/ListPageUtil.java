package com.yuantu.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListPageUtil {
    /**
     *内存分页方法
     * Param list:待分页集合 pageSize:每页显示条数 pageNum:当前页码
     * */
    public static List pageResult(List list, int pageSize, int pageNum) {
        int beginIndex = 0;
        int endIndex = 0;
        beginIndex = pageSize * (pageNum - 1);
        endIndex = Math.min(pageNum * pageSize, list.size());
        if (beginIndex >= list.size()) {
            //超出范围则无数据
            list = new ArrayList<>();
        } else {
            endIndex = Math.min(list.size(), endIndex);
            list = list.subList(beginIndex, endIndex);
        }
        return list;
    }
}
