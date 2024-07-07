package com.yuantu.labor.vo;


import com.yuantu.labor.domain.EmpHistory;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class EmpSimpleVO {


    private Long empId;

    private String empName;

    private String empIdcard;

    private Long empDeptId;

    private String empDeptName;

    private String empStatus;

    private Date createTime;

    private String createTimeStr;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpSimpleVO empSimple = (EmpSimpleVO) o;
        return empId.equals(empSimple.getEmpId()) &&
                empStatus.equals(empSimple.getEmpStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, empStatus);
    }

}