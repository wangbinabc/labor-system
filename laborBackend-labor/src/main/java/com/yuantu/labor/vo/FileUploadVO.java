package com.yuantu.labor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadVO {


    private MultipartFile file;

    @ApiModelProperty("文件类型 1 照片 2 身份证 3 最高职称证书 4 最高学历")
    private String fileType;

}
