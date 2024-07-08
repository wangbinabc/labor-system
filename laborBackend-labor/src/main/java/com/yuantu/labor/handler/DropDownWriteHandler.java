package com.yuantu.labor.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropDownWriteHandler implements SheetWriteHandler {

    private Map<Integer, List<String>> downWriteMap;


    private Integer index;


    public DropDownWriteHandler(Map<Integer, List<String>> downWriteMap) {
        this.downWriteMap = downWriteMap;
    }


    public DropDownWriteHandler(Map<Integer, List<String>> downWriteMap, Integer index) {
        this.downWriteMap = downWriteMap;
        this.index = index;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {


    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {


        // 获取Sheet表
        Sheet sheet = writeSheetHolder.getSheet();

        //设置单元格下拉框
        for (Integer column : downWriteMap.keySet()) {
            List<String> dropDown = downWriteMap.get(column);
            // 开始设置 下拉框
            // 定义一个map key是需要添加下拉框的列的index value是下拉框数据
            Map<Integer, String[]> mapDropDown = new HashMap<>(16);
            //性别下拉选项
            String[] downArray = dropDown.toArray(new String[dropDown.size()]); // {"男", "女"};
            //下拉选在Excel中对应的列
            mapDropDown.put(column, downArray);
            //设置下拉框
            DataValidationHelper dvHelper = sheet.getDataValidationHelper();
            for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
                // 起始行、终止行、起始列、终止列  起始行为1即表示表头不设置
                CellRangeAddressList addressList = new CellRangeAddressList(1, 999, entry.getKey(), entry.getKey());
                // 设置下拉框数据 (设置长度为0的数组会报错，所以这里需要判断)
                if (entry.getValue().length > 0) {
                    //创建显式列表约束
                    DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(entry.getValue());
                    // 指定行列约束以及错误信息
                    DataValidation dataValidation = dvHelper.createValidation(constraint, addressList);
                    dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                    dataValidation.setShowErrorBox(true);
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.createErrorBox("提示", "你输入的值未在备选列表中，请下拉选择合适的值");
                    sheet.addValidationData(dataValidation);
                }
            }
        }

        if(index != null){
            Workbook workbook = sheet.getWorkbook();
            CellStyle textStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            textStyle.setDataFormat(format.getFormat("@")); // 设置为文本格式
            sheet.setDefaultColumnStyle(index, textStyle);
        }

    }


}
