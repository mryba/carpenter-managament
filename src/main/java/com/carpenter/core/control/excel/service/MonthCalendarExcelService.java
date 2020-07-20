package com.carpenter.core.control.excel.service;

import com.carpenter.core.control.excel.dto.MonthCalendarExcelDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MonthCalendarExcelService {

    private List<String> columns;
    private static List<MonthCalendarExcelDto> data = new ArrayList<>();

    private Workbook workbook = new XSSFWorkbook();
    private Sheet sheet;

    public MonthCalendarExcelService(List<String> columns) {
        this.columns = columns;

    }

    CreationHelper creationHelper = workbook.getCreationHelper();
    CellStyle dateCellStyle;

    public void initSheet() {
        // Create a Sheet
        sheet = workbook.createSheet("Month");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
//        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
    }

}
