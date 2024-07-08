package com.yuantu.labor.vo;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class BudgetQueryVO extends BaseEntity {
    /** 申请年 */
    private Long budgetYear;
    /**
     * 预算类型
     */

    private String budgetTypeId;
    /**
     * 当前用户
     */
    private String username;
}
