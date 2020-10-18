package com.carpenter.core.control.excel.service;

import com.carpenter.core.control.excel.dto.MonthCalendarExcelDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
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
        sheet = workbook.createSheet("Miesiąc");

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        setStyleColorYellow(headerCellStyle);
        setFontSize(headerCellStyle, (short) 14, true);
        setBottomBorderDashed(headerCellStyle);
        setRightBorderDashed(headerCellStyle);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
//        dateCellStyle = workbook.createCellStyle();
//        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
    }

    public void setFontSize(CellStyle cellStyle, short size, boolean bold) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(size);
        if (bold) {
            font.setBold(true);
        }
        cellStyle.setFont(font);
    }

    public void createStyleBorders(CellStyle cs) {
        cs.setBorderBottom(BorderStyle.MEDIUM);
        cs.setBorderTop(BorderStyle.MEDIUM);
        cs.setBorderLeft(BorderStyle.MEDIUM);
        cs.setBorderRight(BorderStyle.MEDIUM);
    }

    public void setBottomBorderDashed(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.DASHED);
    }

    public void setRightBorderDashed(CellStyle cellStyle) {
        cellStyle.setBorderRight(BorderStyle.DASHED);
    }


    public void setRightBorderMedium(CellStyle cellStyle) {
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
    }


    public void setLefBorderMedium(CellStyle cs) {
        cs.setBorderLeft(BorderStyle.MEDIUM);

    }

    public void setStyleColorBackgroundRed(HSSFPalette palette, CellStyle cs) {
        HSSFColor myColor = palette.findSimilarColor(245, 24, 0);
        short palIndex = myColor.getIndex();

        cs.setFillForegroundColor(palIndex);
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public void setStyleColorBackGroundGreen(CellStyle cs) {
        try (HSSFWorkbook hwb = new HSSFWorkbook()) {
            HSSFPalette customPalette = hwb.getCustomPalette();
            HSSFColor myColor = customPalette.findSimilarColor(76, 232, 9);
            short palIndex = myColor.getIndex();

            cs.setFillForegroundColor(palIndex);
            cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStyleColorYellow(CellStyle cellStyle) {
        try (HSSFWorkbook hwb = new HSSFWorkbook()) {
            HSSFPalette customPalette = hwb.getCustomPalette();
            HSSFColor myColor = customPalette.findSimilarColor(252, 186, 3);
            short palIndex = myColor.getIndex();

            cellStyle.setFillForegroundColor(palIndex);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStyleColorBlue(CellStyle cellStyle) {
        try (HSSFWorkbook hwb = new HSSFWorkbook()) {
            HSSFPalette customPalette = hwb.getCustomPalette();
            HSSFColor myColor = customPalette.findSimilarColor(14, 190, 235);
            short palIndex = myColor.getIndex();

            cellStyle.setFillForegroundColor(palIndex);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
