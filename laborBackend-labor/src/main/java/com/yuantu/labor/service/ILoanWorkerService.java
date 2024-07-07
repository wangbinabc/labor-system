package com.yuantu.labor.service;

import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.labor.domain.LoanWorker;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 借工Service接口
 *
 * @author ruoyi
 * @date 2023-09-08
 */
public interface ILoanWorkerService {
    /**
     * 查询借工
     *
     * @param loanId 借工主键
     * @return 借工
     */
    public LoanWorker selectLoanWorkerByLoanId(Long loanId);

    /**
     * 查询借工列表
     *
     * @param loanWorker 借工
     * @return 借工集合
     */
    public List<LoanWorker> selectLoanWorkerList(LoanWorker loanWorker);

    /**
     * 新增借工
     *
     * @param username
     * @param loanWorker 借工
     * @return 结果
     */
    public int insertLoanWorker(LoanWorker loanWorker, String username);

    /**
     * 修改借工
     *
     * @param loanWorker 借工
     * @return 结果
     */
    public int updateLoanWorker(LoanWorker loanWorker, String username);

    /**
     * 批量删除借工
     *
     * @param loanIds 需要删除的借工主键集合
     * @return 结果
     */
    public int deleteLoanWorkerByLoanIds(Integer[] loanIds);

    /**
     * 删除借工信息
     *
     * @param loanId 借工主键
     * @return 结果
     */
    public int deleteLoanWorkerByLoanId(Integer loanId);

    /**
     * 根据条件查询借工表
     *
     * @param queryVO
     * @return
     */
    public List<LoanWorkerListVO> selectLoanWorkersListByWhere(LoanWorkerQueryVO queryVO);


    /**
     * 导入借工信息到数据库
     *
     * @param loginUser
     * @param file
     * @return
     */
    ImportResultVO importLoanWorkData(MultipartFile file, LoginUser loginUser);

    /**
     * 统计每个部分借工的人数
     *
     * @return
     */
    public List<PieChartVO> countByDept();

    /**
     * 统计每个单位借工的人数
     *
     * @return
     */
    public List<PieChartVO> countByUnit();

    /**
     * 查询有关借工信息
     *
     * @return
     */
    List<LoanWorkerExportListVO> selectLoanWorkerExportInfos(LoanWorkerExportVO loanWorkerExport);

    /**
     * 拆分导出有关借工信息
     *
     * @param loanWorkerExportDivide
     * @param response
     * @return
     */
    void exportDivide(HttpServletResponse response, LoanWorkerExportDivideVO loanWorkerExportDivide);

    /**
     * 下载借工模板
     *
     * @param response
     */
    public void downloadLoanWorkerExcel(HttpServletResponse response);

    /**
     * 查询借工简要信息
     *
     *  @return
     */
    List<EmpNameCardVO> searchSimpleInfos();

}
