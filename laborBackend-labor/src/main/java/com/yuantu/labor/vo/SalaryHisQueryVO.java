package com.yuantu.labor.vo;

import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SalaryHisQueryVO extends BaseEntity {
    private String empName;
    private String year;
    private Integer[] hisIds;
    private Integer isRelated;
}
