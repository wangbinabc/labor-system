package com.yuantu.labor.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

public class TemplateCellWriteHandler implements CellWriteHandler {

    /**
     * 模板的首行行高 ，通过构造器注入
     */
    private int height;

    public TemplateCellWriteHandler(int height) {
        this.height = height;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }


    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        if (cell.getRowIndex() == 0) {
            font.setFontHeightInPoints((short) 10);
            font.setFontName("宋体");
            //加粗
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置 自动换行
            cellStyle.setWrapText(true);
            Row row = cell.getRow();
            row.setHeightInPoints(height);
        }
        if (cell.getRowIndex() == 1) {
            font.setFontHeightInPoints((short) 11);
            font.setFontName("宋体");
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置 自动换行
            cellStyle.setWrapText(true);
            Row row = cell.getRow();
            row.setHeightInPoints(26);
        }
        cell.setCellStyle(cellStyle);

    }


}
