package com.yuantu.labor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * 用于统计岗位变动
 */
@Data
public class PostChangesCountVO {

    private String phAdjustYear;

    private String phAdjustType;

    /**
     * 统计岗位变动结果
     */
    private String countResult;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostChangesCountVO that = (PostChangesCountVO) o;
        return Objects.equals(phAdjustYear, that.phAdjustYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phAdjustYear);
    }
}
