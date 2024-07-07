package com.yuantu.labor.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class LaborDateVO {


    private Integer orderNum;

    private String name;

    private String recordDate;

    private String isWorkDay;

    private String attendType;

    private String attendDetail;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LaborDateVO other = (LaborDateVO) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(recordDate, other.recordDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, recordDate);
    }


}
