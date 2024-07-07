package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import lombok.Data;

import java.util.*;

/**
 * 考勤统计对象
 *
 * @date 2023-09-06
 */
@Data
public class AttendanceCountVO {

    /** 员工id */
    private Long attendEmpId;

    /** 员工姓名 */
    private String attendEmpName;

    /** 身份证 */
    private String attendEmpIdcard;

    /** 统计的年份 */
    private String workingYear;

    /** 统计的月份,值为"-1"表示统计全年 */
    private String workingMonth = "-1";

    /**
     * 累计上班天数(包括加班和出差)
     *
     */
    private Integer workingDays;


    /**
     * 连续上班最大天数
     *
     */
    private Integer MaximumConsecutiveWorkingDays;

    /**
     * 正常出勤天数
     *
     */
    private Integer attendanceDays = 0;

    /**
     *  出差天数
     */
    private Integer  businessTripDays = 0;

    /**
     *  加班天数
     */

    private Integer overtimeDays = 0;

    /**
     *  休假天数
     */
    private Integer vacationDays = 0;

    /**
     *  考勤详细
     */
    private Map<String, LinkedList> detail = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceCountVO that = (AttendanceCountVO) o;
        return Objects.equals(attendEmpIdcard, that.attendEmpIdcard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendEmpIdcard);
    }
}
