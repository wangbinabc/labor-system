package com.yuantu.labor.vo;

import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

@Data
public class CreateSalaryInfoTemplateVO {

    private Workbook workbook;


    private List<TypeNumVO> typeNumList;


    private List<SegmentVO> segments;


}
