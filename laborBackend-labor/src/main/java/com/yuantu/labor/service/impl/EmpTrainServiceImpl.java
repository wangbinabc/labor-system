package com.yuantu.labor.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.excel.EasyExcel;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.entity.SysDictData;
import com.yuantu.common.core.domain.model.LoginUser;
import com.yuantu.common.exception.ServiceException;
import com.yuantu.common.utils.StringUtils;
import com.yuantu.common.utils.bean.BeanUtils;
import com.yuantu.common.utils.file.FileUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.utils.sign.Base64;
import com.yuantu.labor.cenum.FileImportStatusEnum;
import com.yuantu.labor.cenum.FileTypeEnum;
import com.yuantu.labor.cenum.ImportFileTypeEnum;
import com.yuantu.labor.domain.*;
import com.yuantu.labor.handler.DropDownWriteHandler;
import com.yuantu.labor.handler.EmpTrainExcelImportVerifyHandler;
import com.yuantu.labor.mapper.*;
import com.yuantu.labor.service.IEmpTrainService;
import com.yuantu.labor.vo.*;
import com.yuantu.system.mapper.SysDictDataMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 培训记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-22
 */
@Service
public class EmpTrainServiceImpl implements IEmpTrainService {
    @Autowired
    private EmpTrainMapper empTrainMapper;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private TrainProjectMapper trainProjectMapper;
    @Autowired
    private TrainMaterialsMapper trainMaterialsMapper;
    private static final Logger log = LoggerFactory.getLogger(EmpTrainServiceImpl.class);


    /**
     * 查询培训记录列表
     *
     * @param empTrain 培训记录
     * @return 培训记录
     */
    @Override
    public List<EmpTrain> selectEmpTrainList(EmpTrain empTrain) {
        return empTrainMapper.selectEmpTrainList(empTrain);
    }



    @Override
    public List<EmpTrainResultVO> selectEmpTrainByEmpId(Integer empId) {
        return empTrainMapper.selectEmpTrainByEmpId(empId);
    }

    @Override
    public List<PieChartVO> countEmpTrainNatureNumByEmpId(Integer empId) {
        return empTrainMapper.countEmpTrainNatureNumByEmpId(empId);
    }

    @Override
    public List<PieChartVO> countEmpTrainPeriodByEmpId(Integer empId) {
        return empTrainMapper.countEmpTrainPeriodByEmpId(empId);
    }

    @Override
    public List<PieChartVO> countRecentYearsTrainNumByEmpId(Integer empId) {
        return empTrainMapper.countRecentYearsTrainNumByEmpId(empId);
    }

    @Override
    public List<PieChartVO> countRecentYearsTrainPeriodByEmpId(Integer empId) {
        return empTrainMapper.countRecentYearsTrainPeriodByEmpId(empId);
    }

    @Override
    public Map<String, List> recommendTrainProject(Integer empId) {
        //取得专业和岗位
        Employee employee = employeeMapper.selectEmployeeByEmpId(Long.valueOf(empId));
        String empSpeciality = employee.getEmpSpeciality();
        String empPosition = employee.getEmpPosition();
        //电气自动化变电运维=》电气，自动化，变电，运维
        //拆解这些词汇
        Set<String> set = new HashSet<>();
        splitStr(set, empSpeciality);
        splitStr(set, empPosition);
        List<TrainProject> list = new ArrayList<>();
        List<TrainMaterials> matList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(set)) {
            Iterator<String> iter = set.iterator();
            while (iter.hasNext()) {
                String segmentText = iter.next();
                //根据查询条件查询
                List<TrainProject> projectList = trainProjectMapper.selectTrainProjectListByKeyword(segmentText);

                TrainMaterials trainMaterials = new TrainMaterials();
                trainMaterials.setMatName(segmentText);
                List<TrainMaterials> mats = trainMaterialsMapper.selectTrainMaterialsList(trainMaterials);

                list.addAll(projectList);
                matList.addAll(mats);
            }
        }


        //模糊查询这些词汇，并根据这些词汇查询取得项目对象加入到集合中
        //去重
        list = list.stream().distinct().collect(Collectors.toList());

        //处理推荐材料料
        if (list != null && !list.isEmpty()) {
            for (TrainProject project : list) {
                TrainMaterials trainMaterials = new TrainMaterials();
                trainMaterials.setMatProjectId(project.getProjectId());
                List<TrainMaterials> mats = trainMaterialsMapper.selectTrainMaterialsList(trainMaterials);
                matList.addAll(mats);
            }
        }
        matList = matList.stream().distinct().collect(Collectors.toList());
        Map<String, List> recommendMap = new HashMap<>();
        recommendMap.put("recommendProject", list);
        recommendMap.put("recommendMaterial", matList);
        return recommendMap;
    }





    public void splitStr(Set<String> set, String str) {
        //如果str<=3不拆解，
        //如果str>3 按照顺序隔2个拆解
        if (StringUtils.isNotEmpty(str)) {
            while (str.length() > 3) {
                String value = str.substring(0, 2);
                set.add(value);
                str = str.substring(2);
            }
            set.add(str);
        }
    }


}
