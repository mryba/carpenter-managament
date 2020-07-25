package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.excel.service.MonthCalendarExcelService;
import com.carpenter.core.entity.WorkingDay;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

        workingWeek = workingDayService.getWorkingDaysInScope(timeManager.getStartDate(), timeManager.getEndDate());

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
        Map<EmployeeDto, RecordRow> map = new ConcurrentHashMap<>();

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
        return columnCount.values().stream().mapToInt(AtomicInteger::intValue).sum();
    }

    public Integer getRowCount(RecordRowRepresentative rrr) {
        return rrr.getHourMap().values().stream().mapToInt(AtomicInteger::intValue).sum();
    }

    public void renderExcel() {
        LocalDateTime startDate = timeManager.getViewStartDate();
        LocalDateTime viewEndDate = timeManager.getViewEndDate();

        List<String> result = new LinkedList<>();
        result.add("Imię i nazwisko");
        while (startDate.isBefore(viewEndDate)) {
            String day = startDate.toLocalDate().getDayOfMonth() + " " + startDate.toLocalDate().getMonth().getDisplayName(TextStyle.FULL, FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
            result.add(day);
            startDate = startDate.plusDays(1);
        }

        MonthCalendarExcelService excelService = new MonthCalendarExcelService(result);
        excelService.initSheet();


        int rowNum = 1;
        for (Map.Entry<EmployeeDto, RecordRow> entry : recordRowMap) {

            Row row = excelService.getSheet().createRow(rowNum++);

            for (RecordRowRepresentative rrr : entry.getValue().getRecordRowRepresentatives()) {
                int celNum = 0;
                row.createCell(celNum).setCellValue(rrr.getEmployeeDto().getFirstName() + rrr.getEmployeeDto().getLastName());
                celNum++;
                for (LocalDate monthlyDate : rrr.getMonthlyDates()) {
                    AtomicInteger hour = rrr.getHourMap().get(monthlyDate);
                    row.createCell(celNum).setCellValue(hour.intValue());
                    celNum++;
                }
            }
        }
        for (int i = 0; i < excelService.getColumns().size(); i++) {
            excelService.getSheet().autoSizeColumn(i);
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=raport-miesieczny-" + System.currentTimeMillis() + ".xlsx");

        try {
            excelService.getWorkbook().write(externalContext.getResponseOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            facesContext.responseComplete();
        }
    }
}

