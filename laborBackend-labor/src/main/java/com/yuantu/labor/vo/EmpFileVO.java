package com.yuantu.labor.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class EmpFileVO {

    @NotNull
    private List<String> fileType;


    private List<Long> empIds;

}
