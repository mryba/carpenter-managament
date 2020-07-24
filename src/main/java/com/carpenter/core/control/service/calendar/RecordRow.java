package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.entity.WorkingDay;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class RecordRow {

    private List<RecordRowRepresentative> recordRowRepresentatives = new LinkedList<>();

    private List<LocalDate> monthlyDates;

    public RecordRow(List<LocalDate> monthlyDates) {
        this.monthlyDates = monthlyDates;
    }

    public RecordRowRepresentative insertRecord(EmployeeDto employee) {
        RecordRowRepresentative recordRowRepresentative = recordRowRepresentatives.stream().filter(rr -> rr.getEmployeeDto().getId().equals(employee.getId()))
                .findFirst().orElse(null);

        if (recordRowRepresentative == null) {
            RecordRowRepresentative rr = new RecordRowRepresentative(employee, monthlyDates);
            recordRowRepresentatives.add(rr);
            return rr;
        }
        return null;
    }
}
