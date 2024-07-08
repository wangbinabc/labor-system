package com.yuantu.labor.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 家庭关系对象 family_relations
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Data
public class FamilyRelations {

    /**
     * 家庭关系id
     */
    private Long famId;

    /**
     * 称谓
     */
    @Excel(name = "称谓")
    private String famAppellation;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String famName;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private Long famAge;

    /**
     * 工作单位和岗位
     */
    @Excel(name = "工作单位和岗位")
    private String famUnitPost;

    /**
     * 员工ID
     */
    @Excel(name = "员工ID")
    private Long famEmpId;

    /**
     * 员工名称
     */
    @Excel(name = "员工名称")
    private String famEmpName;


    private Integer famEmpSort;

    @Excel(name = "联系方式")
    private String famContactPhone;

}
