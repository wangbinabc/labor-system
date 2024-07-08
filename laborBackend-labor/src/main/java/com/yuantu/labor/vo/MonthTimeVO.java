package com.yuantu.labor.vo;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MonthTimeVO {

    @NotNull
    private Date copyMonth;

    @NotNull
    private Date beforeMonth;

    @NotNull
    private Date afterMonth;


}
