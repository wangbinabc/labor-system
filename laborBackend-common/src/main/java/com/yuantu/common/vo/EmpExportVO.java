package com.yuantu.common.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class EmpExportVO {

    @NotNull
    private Date date;

    @NotNull
    private List<String> fieldNames;


}
