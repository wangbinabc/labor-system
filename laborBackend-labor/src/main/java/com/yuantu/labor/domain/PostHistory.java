package com.yuantu.labor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;

/**
 * 职位变更对象 post_history
 *
 * @author ruoyi
 * @date 2023-09-19
 */
@Data
public class PostHistory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long phId;

    /**
     * 员工ID
     */
    //  @Excel(name = "员工ID")
    private Long phEmpId;

    /**
     * 员工名称
     */
    @Excel(name = "姓名")
    private String phEmpName;

    /**
     * 身份证
     */
    @Excel(name = "身份证号")
    private String phEmpIdcard;

    /**
     * 原来岗位
     */
    @Excel(name = "变动前岗位名称", readConverterExp = "1=炊事员,2=驾驶员,3=车辆管理,4=后勤服务,5=文员,6=造价(技经管理)员,7=招标员,8=监理部资料员,9=监造(抽检)员,10=设计监理员")
    private String phOriginPostName;

    /**
     * 原来岗级
     */
    @Excel(name = "变动前岗级", readConverterExp = "1=一级,2=二级,3=三级,4=财务辅助,5=财务辅助组长,6=专业管理辅助组长")
    private String phOriginPostLevel;

    /**
     * 新的岗位
     */
    @Excel(name = "变动后岗位名称", readConverterExp = "1=炊事员,2=驾驶员,3=车辆管理,4=后勤服务,5=文员,6=造价(技经管理)员,7=招标员,8=监理部资料员,9=监造(抽检)员,10=设计监理员")
    private String phDestinPostName;

    /**
     * 新的岗级
     */
    @Excel(name = "变动后岗级", readConverterExp = "1=一级,2=二级,3=三级,4=财务辅助,5=财务辅助组长,6=专业管理辅助组长")
    private String phDestinPostLevel;

    /**
     * 调岗原因
     */
    //  @Excel(name = "调岗原因")
    private String phAdjustReason;

    /**
     * 调岗日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "调岗日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date phAdjustDate;

    /**
     * 调岗类型 1.升值，2.平调，3.降职， 4,代理
     */
    @Excel(name = "岗位变动类别", readConverterExp = "1=晋升,2=平调,3=降级")
    private String phAdjustType;

    /**
     * 调岗申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    // @Excel(name = "调岗申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date phUpdateDate;

    /**
     * $column.columnComment
     */
    //  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer disabled;

    /**
     * 记录插入方式
     */
    @ApiModelProperty("0 手动添加 1人员更新管理自动更新 2导入")
    private Long insertType;

    private Boolean isChange = true;

    //是否删除
    private Integer  isRelated;
}
