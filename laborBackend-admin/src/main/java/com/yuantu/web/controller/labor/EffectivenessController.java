package com.yuantu.web.controller.labor;

import com.yuantu.common.constant.HttpStatus;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.service.IDeptPerformanceService;
import com.yuantu.labor.service.IEffectivenessService;
import com.yuantu.labor.service.IEmpAttendanceService;
import com.yuantu.labor.service.IEmpPerformanceService;
import com.yuantu.labor.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/labor/effectiveness")
@Api("效能管理")
public class EffectivenessController extends BaseController {
    @Autowired
    private IDeptPerformanceService deptPerformanceService;

    @Autowired
    private IEmpAttendanceService empAttendanceService;


    @Autowired
    private IEmpPerformanceService empPerformanceService;

    @Autowired
    private IEffectivenessService effectivenessService;

    /**
     * 查询人员效能列表
     */
    //  @PreAuthorize("@ss.hasPermi('labor:effectiveness:list')")
    @GetMapping("/empEffectiveness")
    @ApiOperation("统计查询人员效能列表")
    public TableDataInfo empEffectivenessList(EmpEffectivenessSearchVO empEffectivenessSearchVO) {

//        List<EmpEffectivenessVO> empAttendanceEffectivenessList = empAttendanceService.selectEmpAttendanceEffectivenessList(empEffectivenessSearchVO);
//        List<EmpEffectivenessVO> empPerformanceEffectivenessList = empPerformanceService.selectEmpPerformanceEffectivenessList(empEffectivenessSearchVO);
//        startPage();
//        List<Map> list = new ArrayList<>();
//       for (EmpEffectivenessVO attendance : empAttendanceEffectivenessList){
//           for (EmpEffectivenessVO performance : empPerformanceEffectivenessList){
//               if (attendance.getEmpId().equals(performance.getEmpId())){
//                   Map map = new HashMap<>();
//                   map.put("empId",attendance.getEmpId());
//                   map.put("empName",attendance.getEmpName());
//                   map.put("idcard",attendance.getEmpIdcard());
//                   map.put("attendance",attendance.getResult());
//                   map.put("performance",performance.getResult());
//                   list.add(map);
//               }
//           }
//       }
        // startPage();

        //   List<EmpEffectivenessCountVO> list = effectivenessService.selectEmpEffectivenessList(empEffectivenessSearchVO);
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(list);
//        rspData.setTotal(list.size());
//        return rspData;
        return effectivenessService.selectEmpEffectiveness(empEffectivenessSearchVO);
    }

    //  @PreAuthorize("@ss.hasPermi('labor:effectiveness:list')")
    @GetMapping("/deptEffectiveness")
    @ApiOperation("统计查询部门效能列表")
    public TableDataInfo deptEffectivenessList(DeptEffectivenessSearchVO deptEffectivenessSearchVO) {
       // startPage();
        //List<DeptEffectivenessCountVO> list =effectivenessService.selectDeptEffectivenessList(deptEffectivenessSearchVO);
      //  List<DeptEffectivenessCountVO> list = effectivenessService.selectDeptEffectiveness(deptEffectivenessSearchVO);
        return effectivenessService.selectDeptEffectiveness(deptEffectivenessSearchVO);
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(list);
//        rspData.setTotal(list.size());
        //      return rspData;
    }

}
