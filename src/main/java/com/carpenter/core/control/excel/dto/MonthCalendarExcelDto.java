package com.carpenter.core.control.excel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.hpsf.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthCalendarExcelDto {

    private String name;
    private Date date;
    private int sumHours;
}
