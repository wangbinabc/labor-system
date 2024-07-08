package com.yuantu.labor.service.impl;

import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.labor.domain.EmpDocument;
import com.yuantu.labor.mapper.EmpDocumentMapper;
import com.yuantu.labor.service.IFileService;
import com.yuantu.labor.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service(value = "localFile")
public class FileService implements IFileService {


    @Value("${ruoyi.profile}")
    private String basePath;


    @Autowired
    private EmpDocumentMapper empDocumentMapper;


    private FileVO upLoadFile(MultipartFile file, String path, String fileName, String docType, String username) {

        FileUtils.upload(file, path);
        EmpDocument empDocument = new EmpDocument();
        empDocument.setDocType(docType);
        empDocument.setDocName(fileName);
        path = path.replaceFirst(basePath, "");
        empDocument.setDocAnnexPath(path);
        empDocument.setDocUpdateTime(new Date());
        empDocument.setDisabled(false);
        empDocument.setCreateBy(username);
        empDocumentMapper.insertEmpDocument(empDocument);
        FileVO fileInfo = new FileVO();
        fileInfo.setFileId(empDocument.getDocId());
        fileInfo.setFileType(docType);
        fileInfo.setFileName(fileName);
        fileInfo.setFileUrl(empDocument.getDocAnnexPath());
        return fileInfo;
    }


    private void downLoadFile(String url, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            FileUtils.writeBytes(url, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public FileVO upLoadEmpFile(String fileType, String filePath, MultipartFile file, String username) {
        String fileName = file.getOriginalFilename();
        int length = fileName.length();
        if (length >= 200) {
            throw new ServiceException("文件名称过长");
        }
        String prefix = File.separator + filePath + File.separator + LocalDate.now() + File.separator;
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String rand = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = rand + suffixName; // 新文件名
        String path = basePath + prefix + fileName;
        return upLoadFile(file, path, file.getOriginalFilename(), fileType, username);
    }

    @Override
    public EmpDocument uploadUnCompressFile(String fileType, String filePath, File file, String username) {

        String prefix = File.separator + filePath + File.separator + LocalDate.now() + File.separator;
        String fileName = file.getName();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String rand = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = rand + suffixName; // 新文件名
        String path = basePath + prefix + fileName;
        FileUtils.upload(file, path);
        EmpDocument empDocument = new EmpDocument();
        empDocument.setDocType(fileType);
        empDocument.setDocName(file.getName());
        path = path.replaceFirst(basePath, "");
        empDocument.setDocAnnexPath(path);
        empDocument.setDocUpdateTime(new Date());
        empDocument.setCreateBy(username);
        empDocument.setDisabled(false);
        empDocument.setIsCompress(true);
        empDocumentMapper.insertEmpDocument(empDocument);
        return empDocument;
    }

    @Override
    public void downloadEmpFile(Long fileId, HttpServletResponse response) {
        EmpDocument empDocument = empDocumentMapper.selectEmpDocumentByDocId(fileId);
        if (empDocument == null) {
            throw new ServiceException("此文件已删除或不存在");
        }
        String url = empDocument.getDocAnnexPath();
        url = basePath + url;
        downLoadFile(url, response);
    }

    @Override
    public int removeEmpFile(Long fileId) {

        return empDocumentMapper.removeInfoByDocId(fileId);
    }

    @Override
    public Boolean saveAllRecord() {


//        // 指定备份的数据库信息
//        String hostIP = "localhost";
//        String username = "root";
//        String password = "zzy";
//        String database = "exam";
//
//        // 指定备份文件保存的文件夹路径和文件名
//        String backupFolder = "D:\\backup\\folder\\";
//
//
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String nowStr = now.format(formatter);
//        String backupFileName = "backup_" + nowStr + ".sql";
//        File backupFolderFile = new File(backupFolder);
//        if (!backupFolderFile.exists()) {
//            backupFolderFile.mkdirs();
//        }
//
//        File backupFile = new File(backupFolder + backupFileName);
//
//        // 删除三天前的备份文件
//        LocalDateTime threeDaysAgo = now.minusDays(3);
//        String threeDaysAgoStr = threeDaysAgo.format(formatter);
//        File[] backupFiles = backupFolderFile.listFiles();
//        for (File file : backupFiles) {
//            if (file.isFile()) {
//                String fileName = file.getName();
//                int start = fileName.indexOf("_");
//                int end = fileName.indexOf(".");
//                String fileDateStr = fileName.substring(start + 1, end);
//                if (threeDaysAgoStr.compareTo(fileDateStr) > 0 || threeDaysAgoStr.equals(fileDateStr)) {
//                    file.delete();
//                }
//            }
//        }
//
//        FileWriter fileWriter = null;
//        BufferedReader bufferedReader = null;
//        try {
//            String cmd1 = "/bin/sh";
//            String cmd2 = "-c";
//            String os_name = System.getProperty("os.name");
//            // 判断是否是windows系统
//            if (os_name.toLowerCase().startsWith("win")) {
//                cmd1 = "cmd";
//                cmd2 = "/c";
//            }
//            fileWriter = new FileWriter(backupFile);
//            String stmt = "mysqldump -h" + hostIP + " -u" + username + " -p" + password + " --set-charset=UTF8 " + database;
//            String[] cmd = {cmd1, cmd2, stmt};
//            Process process = Runtime.getRuntime().exec(cmd);
//            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
//            bufferedReader = new BufferedReader(inputStreamReader);
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                fileWriter.write(line);
//            }
//            fileWriter.flush();
//            if (process.waitFor() == 0) {//0 表示线程正常终止。
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bufferedReader != null) {
//                    bufferedReader.close();
//                }
//                if (fileWriter != null) {
//                    fileWriter.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;

        String command = "/bin/bash /home/yuantu/yqc_labor/mysql_backup.sh";
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("脚本执行成功");
            } else {
                System.out.println("脚本执行失败");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}

