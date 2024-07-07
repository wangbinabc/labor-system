package com.yuantu.labor.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeptFlowInfoVO {


    private String type;

    private List<DeptNumVO> deptNumInfos;
}
