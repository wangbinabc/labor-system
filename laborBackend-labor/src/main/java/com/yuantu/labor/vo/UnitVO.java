package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class UnitVO {

    private Long unitId;

    private String unitName;

    private List<UnitDepartmentVO> unitDepartments;

}
