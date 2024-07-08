package com.yuantu.labor.domain;

import com.yuantu.labor.vo.EmpAttendNumVO;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】对象 attend_count
 *
 * @author ruoyi
 * @date 2023-11-11
 */
@Data
public class AttendCount {


    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 员工id
     */
    @Excel(name = "员工id")
    private Long empId;

    private String empName;

    /**
     * 年份
     */
    @Excel(name = "年份")
    private String currentYear;


    /**
     * 连续上班最大天数
     */
    @Excel(name = "连续上班最大天数")
    private Integer linkDays;

    /**
     * 全年出差天数
     */
    // @Excel(name = "全年出差天数")
    //private Integer businessDays;

    /**
     * 全年累计上班天数
     */
    //@Excel(name = "全年累计上班天数")
    //private Integer totalDays;

    /**
     * 全年加班天数
     */
    //@Excel(name = "全年加班天数")
    //private Integer overTimeDays;

    /**
     * 全年休假天数
     */
    //@Excel(name = "全年休假天数")
    //private Integer holidayDays;

    private Map<String, Integer> attendanceCount;

    private List<EmpAttendNumVO> attendNumList;


    private Integer flag;


}
