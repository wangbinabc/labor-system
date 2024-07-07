package com.yuantu.labor.vo;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Data
public class DeptEmpPerformVO {


    private Long orderNum;

    private String empName;

    private String quitDate;

    private Map<String, String> oneQuarterMap;

    private Map<String, String> twoQuarterMap;

    private Map<String, String> threeQuarterMap;

    private Map<String, String> fourQuarterMap;

    private Map<String, String> annualMap;


}
