package com.yuantu.labor.service;

import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.domain.EmpDocument;
import com.yuantu.labor.vo.FileUploadVO;
import com.yuantu.labor.vo.FileVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

public interface IFileService {


    /**
     * 上传文件
     *
     * @param fileType
     * @param path
     * @param file
     * @param username
     * @return
     */
    FileVO upLoadEmpFile(String fileType, String path, MultipartFile file, String username);


    /**
     * 上传解压后的文件
     *
     * @param fileType
     * @param path
     * @param file
     * @param username
     * @return
     */
    EmpDocument uploadUnCompressFile(String fileType, String path, File file, String username);

    /**
     * 下载文件
     *
     * @param fileId
     * @param response
     * @return
     */
    void downloadEmpFile(Long fileId, HttpServletResponse response);


    /**
     * 删除文件
     *
     * @param fileId
     * @return
     */
    int removeEmpFile(Long fileId);

    /**
     * 全量保存数据库记录
     */
    Boolean saveAllRecord();

}
