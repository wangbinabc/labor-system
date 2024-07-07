package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.service.IEmployeeService;
import com.yuantu.labor.service.IFileService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 员工Controller
 *
 * @author ruoyi
 * @date 2023-09-06
 */
@Api("文件管理")
@RestController
@RequestMapping("/labor/file")
public class FileController extends BaseController {

    @Autowired
    @Qualifier("localFile")
    private IFileService fileService;

    /**
     * 上传文件
     */
    @ApiOperation("上传文件 文件类型 1 照片 2 身份证 3 最高职称证书 4 最高学历")
    @PostMapping(value = "/emp/upload")
    public AjaxResult uploadEmpFile(@RequestParam String fileType, @RequestParam String path, @RequestBody MultipartFile file) {
        String username = getUsername();
        return success(fileService.upLoadEmpFile(fileType, path, file, username));
    }


    @ApiOperation("下载文件")
    @GetMapping(value = "/download")
    public void uploadEmpFile(@RequestParam Long fileId, HttpServletResponse response) {
        fileService.downloadEmpFile(fileId, response);
    }


    @ApiOperation("删除文件")
    @GetMapping(value = "/remove")
    public void removeEmpFile(@RequestParam Long fileId) {
        fileService.removeEmpFile(fileId);
    }



    /**
     * 全量保存数据库记录
     */
    // @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @GetMapping("/save/all")
    public AjaxResult saveAllRecord() {
        return toAjax(fileService.saveAllRecord());
    }


}
