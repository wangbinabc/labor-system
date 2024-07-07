package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuantu.common.core.domain.BaseEntity;
import com.yuantu.labor.domain.TrainMaterialDir;
import lombok.Data;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TreeSelectVO   {

    /**
     * 节点ID
     */
    private Integer id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectVO> children;



    public TreeSelectVO(TrainMaterialDir dir) {
        this.id = dir.getDirId();
        this.label = dir.getDirName();
        this.children = dir.getChildren().stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }


}