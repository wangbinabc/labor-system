package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import lombok.Data;

@Data
public class DeptPerformanceParamsVO {

    /** 部门号 */
    private Long dpDeptId;

    /** 部门名 */
    private String dpDeptName;

    /** 绩效年 */
    private String dpYear ;

    /** 绩效周期 */
    private String dpCycle;

    /** 修改起始时间 */
    private String startUpdateTime ;

    /** 修改结束时间 */
    private String endUpdateTime ;
}
