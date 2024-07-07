package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpDocument;
import com.yuantu.labor.domain.TrainResult;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 培训成果Service接口
 *
 * @author ruoyi
 * @date 2023-09-25
 */
public interface ITrainResultService {


    /**
     * 查询培训成果列表
     *
     * @param trainResult 培训成果
     * @return 培训成果集合
     */
    public List<TrainResult> selectTrainResultList(TrainResult trainResult);


}
