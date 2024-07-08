package com.yuantu.labor.vo;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class EmpPicInfoVO {

    private Long empId;

    private String empName;

    private String empIdCard;

    private String unitName;

    private String deptName;

}
