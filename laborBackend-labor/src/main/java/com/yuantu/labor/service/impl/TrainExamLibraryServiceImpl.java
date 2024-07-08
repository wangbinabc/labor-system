package com.yuantu.labor.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.labor.domain.EmpCredentials;
import com.yuantu.labor.domain.EmpDocument;
import com.yuantu.labor.mapper.EmpDocumentMapper;
import com.yuantu.labor.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.TrainExamLibraryMapper;
import com.yuantu.labor.domain.TrainExamLibrary;
import com.yuantu.labor.service.ITrainExamLibraryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 题库Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-25
 */
@Service
public class TrainExamLibraryServiceImpl implements ITrainExamLibraryService 
{
    @Autowired
    private TrainExamLibraryMapper trainExamLibraryMapper;

    @Autowired
    private EmpDocumentMapper empDocumentMapper;

    /**
     * 查询题库列表
     * 
     * @param trainExamLibrary 题库
     * @return 题库
     */
    @Override
    public List<TrainExamLibrary> selectTrainExamLibraryList(TrainExamLibrary trainExamLibrary)
    {
        List<TrainExamLibrary> list = trainExamLibraryMapper.selectTrainExamLibraryList(trainExamLibrary);
        for (TrainExamLibrary t:list){
            EmpDocument ed =  empDocumentMapper.selectEmpDocumentByDocId(t.getExamAnnexPath());
            t.setEmpDocument(ed);
        }
        return list;
    }



}
