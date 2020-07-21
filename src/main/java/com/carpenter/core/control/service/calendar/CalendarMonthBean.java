package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.excel.service.MonthCalendarExcelService;
import com.carpenter.core.entity.WorkingDay;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Named("calendarMonthBean")
@ViewScoped
public class CalendarMonthBean extends CalendarBean {

    private static final long serialVersionUID = 7818636965206530503L;

    private List<WorkingDay> workingWeek = new LinkedList<>();
    //    private Map<LocalDate, AtomicInteger> dateMap = new LinkedHashMap<>();
    private Map<LocalDate, RowRepresentative> employeeMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        workingWeek.clear();
        initCalendarMode(Mode.MONTH);
    }

    public String getCurrentMonthName() {
        return timeManager.getViewStartDate().getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
    }

    public List<Date> getDates() {
        List<Date> result = new LinkedList<>();

        LocalDateTime startDate = timeManager.getViewStartDate();
        LocalDateTime viewEndDate = timeManager.getViewEndDate();

        workingWeek = workingDayService.getWorkingDaysInScope(timeManager.getStartDate(), timeManager.getEndDate());

        while (startDate.isBefore(viewEndDate)) {
            Date from = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            result.add(from);

            final LocalDate finalStartDate = startDate.toLocalDate();

//            int sum = workingWeek.stream()
//                    .filter(wd -> convertDateToLocalDate(wd.getDay()).equals(finalStartDate))
//                    .mapToInt(WorkingDay::getHours).sum();
//            dateMap.computeIfAbsent(finalStartDate, k -> new AtomicInteger(sum));

            for (WorkingDay workingDay : workingWeek) {
                if (convertDateToLocalDate(workingDay.getDay()).equals(finalStartDate)) {
                    RowRepresentative rowRepresentative = employeeMap.computeIfAbsent(finalStartDate, k -> new RowRepresentative());
                    rowRepresentative.findRecordRow(workingDay);
                }
            }

            startDate = startDate.plusDays(1);
        }
        return result;
    }

    public Integer getWorkHours(Date date, EmployeeDto employeeDto) {
        LocalDate dateAsLocalDate = convertDateToLocalDate(date);
        return workingWeek.stream()
                .filter(w -> w.getEmployee().getId().equals(employeeDto.getId()))
                .filter(wd -> convertDateToLocalDate(wd.getDay()).equals(dateAsLocalDate))
                .map(WorkingDay::getHours)
                .findFirst().orElse(0);
    }

    private static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Integer getCountEmployeeHour(Date date) {
        LocalDate localDate = convertDateToLocalDate(date);
        if (employeeMap.get(localDate) != null) {
            return employeeMap.get(localDate).getRecordRows().stream().mapToInt(rr -> rr.getHours().get()).sum();
        }
        return 0;
    }

    public Integer getSumOfColumns() {
        return employeeMap.values().stream().mapToInt(rr -> rr.getRecordRows().stream().mapToInt(r -> r.getHours().get()).sum()).sum();
    }

    public Integer getRowCount(Long employeeId) {
        List<RecordRow> recordRows = employeeMap.values().stream()
                .map(r -> r.getRecordRows().stream()
                        .filter(rr -> rr.getEmployeeId().equals(employeeId))
                        .findFirst()
                        .orElse(null)).collect(Collectors.toList());

        if (!recordRows.isEmpty()) {
            return recordRows.stream()
                    .filter(Objects::nonNull)
                    .filter(rr -> rr.getHours() != null).mapToInt(rowRepresentative -> rowRepresentative.getHours().get()).sum();
        }
        return 0;
    }

    public void renderExcel() {
        LocalDateTime startDate = timeManager.getViewStartDate();
        LocalDateTime viewEndDate = timeManager.getViewEndDate();

        List<String> result = new LinkedList<>();
        List<LocalDate> resultDate = new LinkedList<>();
        result.add("Imię i nazwisko");
        while (startDate.isBefore(viewEndDate)) {
            String day = startDate.toLocalDate().getDayOfMonth() + " " + startDate.toLocalDate().getMonth().getDisplayName(TextStyle.FULL, FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
            result.add(day);
            resultDate.add(startDate.toLocalDate());
            startDate = startDate.plusDays(1);
        }

        MonthCalendarExcelService excelService = new MonthCalendarExcelService(result);
        excelService.initSheet();


        for (EmployeeDto employee : employeeService.getEmployees()) {
            int cellNum = 1;
            int rowNum = 1;
            while (startDate.isBefore(viewEndDate)) {
                Row row = excelService.getSheet().createRow(rowNum++);
                Integer workHours = getWorkHours(Date.from(startDate.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), employee);
                row.createCell(cellNum).setCellValue(workHours);
            }
            startDate = startDate.plusDays(1);
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

