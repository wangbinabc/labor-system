package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpPerformance;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 绩效统计对象
 *
 */
@Data
public class CountEmpPerformanceVO {

    private Long perfDeptId ;
    private String perfDeptName ;
    private String perfYear ;
    private String perfCycle ;

    /**
     *统计结果
     * */
    private Long resultA;
    private Long resultB;
    private Long resultC;
    private Long resultD;


    private Map<String, List<EmpPerformance>> map = new HashMap<>();
}
