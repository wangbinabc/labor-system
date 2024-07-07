package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 员工家庭关系 empFamily
 *
 * @author ruoyi
 * @date 2023-09-06
 */

@Data
@ExcelTarget("empFamilyVO")
public class EmpFamilyVO {

    /**
     * 员工亲属id
     */

    @ApiModelProperty("员工亲属id")
    private Long famId;

    /**
     * 员工亲属称谓
     */
    @Excel(name = "员工亲属称谓")
    @ApiModelProperty("员工亲属称谓")
    private String famAppellation;

    @Excel(name = "员工亲属联系方式")
    private String famContactPhone;

    /**
     * 员工亲属年龄
     */
    @ApiModelProperty("员工亲属年龄")
    @Excel(name = "员工亲属年龄")
    private String famAge;

    /**
     * 员工亲属工作单位及岗位
     */
    @ApiModelProperty("员工亲属工作单位及岗位")
    @Excel(name = "员工亲属工作单位及岗位")
    private String famUnitPost;

    /**
     * 员工亲属姓名
     */
    @ApiModelProperty("员工亲属姓名")
    private String famName;

    @ApiModelProperty("员工亲属排序")
    private Integer famEmpSort;


}
