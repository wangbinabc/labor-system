package com.yuantu.labor.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.labor.domain.EmpDocument;
import com.yuantu.labor.domain.TrainMaterialDir;
import com.yuantu.labor.mapper.EmpDocumentMapper;
import com.yuantu.labor.mapper.TrainMaterialDirMapper;
import com.yuantu.labor.service.ITrainMaterialDirService;
import com.yuantu.labor.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import com.yuantu.labor.mapper.TrainMaterialsMapper;
import com.yuantu.labor.domain.TrainMaterials;
import com.yuantu.labor.service.ITrainMaterialsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 培训项目材料Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Service
public class TrainMaterialsServiceImpl implements ITrainMaterialsService {
    @Autowired
    private TrainMaterialsMapper trainMaterialsMapper;

    @Autowired
    private TrainMaterialDirMapper trainMaterialDirMapper;


    /**
     * 查询培训项目材料列表
     *
     * @param trainMaterials 培训项目材料
     * @return 培训项目材料
     */
    @Override
    public List<TrainMaterials> selectTrainMaterialsList(TrainMaterials trainMaterials) {
        List<TrainMaterials> list = trainMaterialsMapper.selectTrainMaterialsList(trainMaterials);
        for(TrainMaterials materials:list){
            if(materials.getMatDirectoryId()!=null){
               List<TrainMaterialDir> dirList = trainMaterialDirMapper.selectDirAndParentById(materials.getMatDirectoryId());
               String dirPath = "";
               for (TrainMaterialDir dir : dirList){
                   dirPath = dir.getDirName()+"/"+dirPath;
               }
               materials.setMatDirectoryName(dirPath);
            }
        }
        return list;
    }
}
