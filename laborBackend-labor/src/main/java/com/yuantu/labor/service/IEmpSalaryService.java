package com.yuantu.labor.service;

import java.math.BigDecimal;
import java.util.List;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.EmpSalary;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 员工酬薪主Service接口
 *
 * @author ruoyi
 * @date 2023-09-26
 */
public interface IEmpSalaryService {


    /**
     * 查询员工酬薪主列表
     *
     * @param empSalary 员工酬薪主
     * @return 员工酬薪主集合
     */
    public List<EmpSalary> selectEmpSalaryList(EmpSalary empSalary);

    public BigDecimal sumSalaryItemBySalaryType(Integer salaryId, String SalaryType);

    public String sumLastYearNetPay(EmpSalary empSalary);

    List<SalaryChartVO> countPastYearNetPay(EmpSalaryQueryVO queryVO);
    //根据身份证号或者姓名查找

    public List<EmpSalary> selectByNameOrId(EmpSalary empSalary);
}