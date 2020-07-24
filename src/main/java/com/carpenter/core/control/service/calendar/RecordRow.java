package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.entity.WorkingDay;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class RecordRow {

    private List<RecordRowRepresentative> recordRowRepresentatives = new LinkedList<>();
    private List<LocalDate> monthlyDates;
    public RecordRow(List<LocalDate> monthlyDates) {
        this.monthlyDates = monthlyDates;
    }

//    public void findRecordRow(WorkingDay workingDay) {
//        RecordRow recordRow = recordRows.stream()
//                .filter(rr -> rr.getEmployeeId().equals(workingDay.getEmployee().getId()))
//                .findFirst().orElse(null);
//        if (recordRow == null) {
//            RecordRow rr = new RecordRow(
//                    workingDay.getEmployee().getId(), workingDay.getEmployee().getFirstName(), workingDay.getEmployee().getLastName(), new AtomicInteger(workingDay.getHours()));
//            recordRows.add(rr);
//        }
//    }

    public RecordRowRepresentative insertRecord(EmployeeDto employee, List<WorkingDay> workingDays) {
        RecordRowRepresentative recordRowRepresentative = recordRowRepresentatives.stream().filter(rr -> rr.getEmployeeDto().equals(employee))
                .findFirst().orElse(null);

        WorkingDay workingDay = workingDays.stream().filter(wd -> wd.getEmployee().getId().equals(employee.getId()))
                .findFirst().orElse(null);
        if (recordRowRepresentative == null) {
            int wd = workingDay == null ? 0 : workingDay.getHours();
            RecordRowRepresentative rr = new RecordRowRepresentative(employee, new AtomicInteger(wd), monthlyDates);
            recordRowRepresentatives.add(rr);
            return rr;
        }
        return null;
    }
}
