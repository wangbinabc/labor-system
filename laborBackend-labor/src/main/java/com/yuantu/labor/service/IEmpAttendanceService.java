package com.yuantu.labor.service;

import java.util.Date;
import java.util.List;

import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.labor.domain.DeptPerformance;
import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 考勤Service接口
 *
 * @author ruoyi
 * @date 2023-09-20
 */
public interface IEmpAttendanceService {
    /**
     * 查询考勤
     *
     * @param attendId 考勤主键
     * @return 考勤
     */
    public EmpAttendance selectEmpAttendanceByAttendId(Long attendId);

    public List<EmpAttendance> selectEmpAttendanceListByScreen(EmpAttendanceScreenVO empAttendanceScreenVO,
                                                               List<Long> attendIds, Integer query);

    List<EmpAttendance> findExportInfos(ExportVO export);

    void exportDivide(HttpServletResponse response, ExportDivideVO export);

    /**
     * 查询考勤效能
     *
     * @return 考勤
     */
    public List<EmpEffectivenessVO> selectEmpAttendanceEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO);


    List<AttendanceYearVO> countEmpAttendanceByAttendanceYear(String year);

    /**
     * 按年和（身份证或姓名）统计考勤
     *
     * @return 考勤
     */
    public List<AttendanceCountVO> countEmpsAttendanceListByParams(String nameOrIdcard, String year, String month);

    /**
     * 查询考勤列表
     *
     * @param empAttendance 考勤
     * @return 考勤集合
     */
    public List<EmpAttendanceListVO> selectEmpAttendanceList(EmpAttendance empAttendance);


    /**
     * 新增考勤
     *
     * @param empAttendance 考勤
     * @return 结果
     */
    public int insertEmpAttendance(EmpAttendance empAttendance);

    /**
     * 修改考勤
     *
     * @param empAttendance 考勤
     * @return 结果
     */
    public int updateEmpAttendance(EmpAttendance empAttendance);

    /**
     * 批量删除考勤
     *
     * @param attendIds 需要删除的考勤主键集合
     * @return 结果
     */
    public int deleteEmpAttendanceByAttendIds(Long[] attendIds);

    /**
     * 删除考勤信息
     *
     * @param attendId 考勤主键
     * @return 结果
     */
    public int deleteEmpAttendanceByAttendId(Long attendId);

    void downloadExcelTemplate(HttpServletResponse response);

    ImportResultVO uploadAttendanceInfosFile(MultipartFile multipartFile, Long userId, String username, Integer flag);

    TableDataInfo totalCountAttend(String keyword, String yearNum, Integer pageSize, Integer pageNum);

    EmpAttendanceGenVO totalCountDetail(Long empId, Date time, Integer flag);

    void uploadAttendanceInfosFileForSplit(HttpServletResponse response, MultipartFile file);

}
