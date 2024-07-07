package com.yuantu.labor.service;

import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.Employee;
import com.yuantu.labor.domain.SalaryHistory;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 薪级变动Service接口
 * 
 * @author ruoyi
 * @date 2023-10-07
 */
public interface ISalaryHistoryService 
{
    /**
     * 查询薪级变动列表
     * 
     * @param vo 薪级变动
     * @return 薪级变动集合
     */
    public List<SalaryHistory> selectSalaryHistoryList(SalaryHisQueryVO vo);


}
