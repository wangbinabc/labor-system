package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StabilityResultVO {

    private String deptName;
    private String unitName;

    private Integer totalNum;

    private Integer exitNum;

    private Integer ageNum;

    private BigDecimal stabilityIndex;
    private Integer yearTenToTwenty;
    private Integer yearGreatTwenty;
    private Integer yearFiveToTen;
    private Integer yearThreeToFive;
    private Integer yearOneToThree;
    private Integer yearLessOne;
}
