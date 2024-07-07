package com.yuantu.labor.vo;

import com.yuantu.labor.domain.EmpAttendance;
import com.yuantu.labor.domain.EmpCredentials;
import lombok.Data;

import java.util.List;

@Data
public class EmpCredentialsChangeDicVO {


    private String chineseName;

    private List<EmpCredentials> exportInfos;

}
