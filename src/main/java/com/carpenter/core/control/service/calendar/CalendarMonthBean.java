package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.excel.service.MonthCalendarExcelService;
import com.carpenter.core.entity.WorkingDay;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Named("calendarMonthBean")
@ViewScoped
public class CalendarMonthBean extends CalendarBean {

    private static final long serialVersionUID = 7818636965206530503L;

    private List<WorkingDay> workingWeek = new LinkedList<>();
    private List<LocalDate> monthlyDates;

    private List<Map.Entry<EmployeeDto, RecordRow>> recordRowMap;
    private Map<LocalDate, AtomicInteger> columnCount = new LinkedHashMap<>();
    private int lastCellNumber;
    private int sumOfColumns;

    @PostConstruct
    public void init() {
        columnCount.clear();
        workingWeek.clear();
        initCalendarMode(Mode.MONTH);
    }

    public String getCurrentMonthName() {
        return timeManager.getViewStartDate().getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
    }

    public List<Date> getDates() {
        List<Date> result = new LinkedList<>();
        columnCount.clear();
        LocalDateTime startDate = timeManager.getViewStartDate();
        LocalDateTime viewEndDate = timeManager.getViewEndDate();

        workingWeek = workingDayService.getWorkingDaysInScope(timeManager.getStartDate(), timeManager.getEndDate(), principalBean);

        monthlyDates = new LinkedList<>();
        while (startDate.isBefore(viewEndDate)) {
            Date from = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            result.add(from);

            final LocalDate finalStartDate = startDate.toLocalDate();

            monthlyDates.add(finalStartDate);
            startDate = startDate.plusDays(1);
        }
        initSheet();
        return result;
    }

    public void initSheet() {
        Map<EmployeeDto, RecordRow> map = new LinkedHashMap<>();

        List<EmployeeDto> employeeDtos = getEmployees();
        for (EmployeeDto employee : employeeDtos) {

            RecordRow recordRow = map.computeIfAbsent(employee, k -> new RecordRow(monthlyDates));
            recordRow.insertRecord(employee).countDay(workingWeek);

        }

        recordRowMap = new LinkedList<>(map.entrySet());
    }

    public Integer getWorkHours(Long employeeId, RecordRowRepresentative rrr, LocalDate date) {
        if (rrr.getEmployeeDto().getId().equals(employeeId)) {

            Map<LocalDate, AtomicInteger> hourMap = rrr.getHourMap();
            AtomicInteger atomicInteger = hourMap.get(date);

            return atomicInteger.intValue();
        }
        return 0;
    }

    public List<Map.Entry<EmployeeDto, RecordRow>> getRecordRowMap() {
        return recordRowMap;
    }

    public List<LocalDate> getMonthlyDates() {
        return monthlyDates;
    }

    private static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Integer getCountEmployeeHour(Date date) {
        int sumColumn = 0;
        LocalDate localDate = convertDateToLocalDate(date);

        for (Map.Entry<EmployeeDto, RecordRow> entry : recordRowMap) {
            int sum = entry.getValue().getRecordRowRepresentatives()
                    .stream()
                    .map(rrr -> rrr.getHourMap().get(localDate))
                    .mapToInt(AtomicInteger::intValue)
                    .sum();
            sumColumn = sumColumn + sum;
            if (columnCount.get(localDate) != null) {
                columnCount.get(localDate).getAndAdd(sum);
            } else {
                columnCount.putIfAbsent(localDate, new AtomicInteger(sum));
            }
        }
        return sumColumn;
    }

    public Integer getSumOfColumns() {
        sumOfColumns = columnCount.values().stream().mapToInt(AtomicInteger::intValue).sum();
        return sumOfColumns;
    }

    public Integer getRowCount(RecordRowRepresentative rrr) {
        return rrr.getHourMap().values().stream().mapToInt(AtomicInteger::intValue).sum();
    }


    public void renderExcel() {
        LocalDateTime startDate = timeManager.getViewStartDate();
        LocalDateTime viewEndDate = timeManager.getViewEndDate();
        String date = getCurrentMonthName() + " " + startDate.format(DateTimeFormatter.ofPattern("yyyy"));
        List<String> result = new LinkedList<>();
        result.add(date);
        while (startDate.isBefore(viewEndDate)) {
            result.add(startDate.format(DateTimeFormatter.ofPattern("dd.MM")));
            startDate = startDate.plusDays(1);
        }
        result.add("SUMA");

        MonthCalendarExcelService excelService = new MonthCalendarExcelService(result);
        excelService.initSheet(date);


        int rowNum = 1;
        for (Map.Entry<EmployeeDto, RecordRow> entry : recordRowMap) {
            Row row = excelService.getSheet().createRow(rowNum++);

            for (RecordRowRepresentative rrr : entry.getValue().getRecordRowRepresentatives()) {
                int celNum = 0;
                row.createCell(celNum).setCellValue(rrr.getEmployeeDto().getLastName() + " " + rrr.getEmployeeDto().getFirstName());

                CellStyle cellStyle = excelService.getWorkbook().createCellStyle();
                excelService.setFontSize(cellStyle, (short) 14, true);
                excelService.setStyleColorBlue(cellStyle);

                excelService.setBottomBorderDashed(cellStyle);
                excelService.setRightBorderMedium(cellStyle);

                row.getCell(celNum).setCellStyle(cellStyle);
                celNum++;

                for (LocalDate monthlyDate : rrr.getMonthlyDates()) {
                    AtomicInteger hour = rrr.getHourMap().get(monthlyDate);
                    row.createCell(celNum).setCellValue(hour.intValue());

                    CellStyle cs = excelService.getWorkbook().createCellStyle();
                    excelService.setFontSize(cs, (short) 14, false);

                    try (HSSFWorkbook hwb = new HSSFWorkbook()) {
                        HSSFPalette palette = hwb.getCustomPalette();

                        if (hour.intValue() == 0) {
                            excelService.setStyleColorBackgroundRed(palette, cs);
                            excelService.setBottomBorderDashed(cs);
                            excelService.setRightBorderDashed(cs);
                            excelService.centerContent(cs);
                            row.getCell(celNum).setCellStyle(cs);
                        } else {
                            excelService.setStyleColorBackGroundGreen(cs);
                            excelService.setBottomBorderDashed(cs);
                            excelService.setRightBorderDashed(cs);
                            excelService.centerContent(cs);
                            row.getCell(celNum).setCellStyle(cs);

                        }
                    } catch (IOException e) {
                        log.error("Cannot get HSSFWorkBook rendering monthy excel file!");
                    }
                    celNum++;
                }
                row.createCell(celNum).setCellValue(getRowCount(rrr));
                CellStyle cs = excelService.getWorkbook().createCellStyle();
                excelService.setLefBorderMedium(cs);
                excelService.centerContent(cs);
                excelService.setBottomBorderDashed(cs);
                excelService.setFontSize(cs, (short) 14, true);
                excelService.setStyleColorBlue(cs);

                row.getCell(celNum).setCellStyle(cs);
                lastCellNumber = celNum;
            }
        }
        rowNum++;
        Row lastRow = excelService.getSheet().createRow(rowNum);
        lastRow.createCell(lastCellNumber - 1).setCellValue("SUMA");
        lastRow.createCell(lastCellNumber).setCellValue(sumOfColumns);
        CellStyle cs = excelService.getWorkbook().createCellStyle();
        excelService.centerContent(cs);
        excelService.setStyleColorBackGroundGreen(cs);
        excelService.setFontSize(cs, (short) 14, true);
        excelService.setTopBorderMedium(cs);
        excelService.setRightBorderDashed(cs);

        lastRow.getCell(lastCellNumber).setCellStyle(cs);
        lastRow.getCell(lastCellNumber - 1).setCellStyle(cs);

        for (int i = 0; i < excelService.getColumns().size(); i++) {
            excelService.getSheet().autoSizeColumn(i);
        }


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Instant instance = Instant.ofEpochMilli(System.currentTimeMillis());
        LocalDateTime poland = LocalDateTime.ofInstant(instance, ZoneId.of("Poland"));

        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader
                (
                        "Content-Disposition",
                        "attachment; filename=" + getCurrentMonthName() + "-" + startDate.format(DateTimeFormatter.ofPattern("yyyy")) + "- wygenerowano:" + poland.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss")) + ".xlsx"
                );

        try {
            excelService.getWorkbook().write(externalContext.getResponseOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            facesContext.responseComplete();
        }
    }
}

