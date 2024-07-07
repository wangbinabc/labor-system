package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资格证书对象 emp_credentials
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
public class EmpCredentialSimpleVO {


    /**
     * 证书ID
     */
    private Long credId;


    @ApiModelProperty("证书名称")
    private String credName;

    @Excel(name = "证书类型", readConverterExp = "1=A类,2=B类,3=C类")
    @ApiModelProperty("证书类型")
    private String credType;


    @ApiModelProperty("证书注册时间")
    private Date credRegistTime;


    @ApiModelProperty("资格证获取时间")
    private Date credObtainTime;


    @ApiModelProperty("有效期")
    private String credPeriod;


    @ApiModelProperty("注册证到期时间")
    private Date credExpTime;

    @ApiModelProperty("剩余天数")
    private Integer remainingDays;


    //   @Excel(name = "资格证附件")
    @ApiModelProperty("证书路径")
    private List<String> paperPath;

    @ApiModelProperty("文件信息")
    private List<FileVO> fileInfos;


}
