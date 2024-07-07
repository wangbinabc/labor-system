package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 省市区树
 *
 * @author syw
 * @since 2023-11-09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("省市区信息")
public class ChinaAddressTreeVO {
    private Integer id;

    private String name;

    private Integer parentId;

    private List<ChinaAddressTreeVO> children;

    public ChinaAddressTreeVO(Integer value, String label, Integer parentId) {
        this.id = value;
        this.name = label;
        this.parentId = parentId;
    }

}
