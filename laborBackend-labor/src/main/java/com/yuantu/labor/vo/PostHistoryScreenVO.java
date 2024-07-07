package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 职位变更对象 post_history
 * 
 * @author ruoyi
 * @date 2023-09-19
 */
@Data
public class PostHistoryScreenVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long phId;

    /** 员工ID */
  //  @Excel(name = "员工ID")
    private Long phEmpId;

    /** 员工名称 */
    @Excel(name = "姓名")
    private String phEmpName;

    /** 身份证 */
    @Excel(name = "身份证号")
    private String phEmpIdcard;

    /** 原来岗位 */
    @Excel(name = "变动前岗位名称")
    private String phOriginPostName;

    /** 原来岗级 */
    @Excel(name = "变动前岗级")
    private String phOriginPostLevel;

    /** 新的岗位 */
    @Excel(name = "变动后岗位名称")
    private String phDestinPostName;

    /** 新的岗级 */
    @Excel(name = "变动后岗级")
    private String phDestinPostLevel;


    /** 调岗日期区间 */

    private Date phAdjustDateStart;
    private Date phAdjustDateEnd;



    /** 调岗类型 1.升值，2.平调，3.降职， 4,代理 */
    @Excel(name = "岗位变动类别",readConverterExp = "1=晋升,2=平调,3=降级")
    private String phAdjustType;


}
