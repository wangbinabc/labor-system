package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Objects;

@Data
@NoArgsConstructor
public class IdCheckVO {

    private String empIdCard;

    private String name;

    private String empCode;

    private Integer rowNum;

    public IdCheckVO(String empIdCard, String name, Integer rowNum, String empCode) {
        this.empIdCard = empIdCard;
        this.name = name;
        this.rowNum = rowNum;
        this.empCode = empCode;
    }
}
