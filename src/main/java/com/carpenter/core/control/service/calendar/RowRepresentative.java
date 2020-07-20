package com.carpenter.core.control.service.calendar;

import com.carpenter.core.entity.WorkingDay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowRepresentative {

    private List<RecordRow> recordRows = new LinkedList<>();

    public void findRecordRow(WorkingDay workingDay) {
        RecordRow recordRow = recordRows.stream()
                .filter(rr -> rr.getEmployeeId().equals(workingDay.getEmployee().getId()))
                .findFirst().orElse(null);
        if (recordRow == null) {
            RecordRow rr = new RecordRow(
                    workingDay.getEmployee().getId(), workingDay.getEmployee().getFirstName(), workingDay.getEmployee().getLastName(), new AtomicInteger(workingDay.getHours()));
            recordRows.add(rr);
        }
    }
}
