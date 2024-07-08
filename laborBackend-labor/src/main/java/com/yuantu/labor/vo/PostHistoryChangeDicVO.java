package com.yuantu.labor.vo;

import com.yuantu.labor.domain.PostHistory;
import lombok.Data;

import java.util.List;

@Data
public class PostHistoryChangeDicVO {


    private String chineseName;

    private List<PostHistory> exportInfos;

}
