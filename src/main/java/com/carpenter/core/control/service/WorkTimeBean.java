package com.carpenter.core.control.service;

import com.carpenter.core.entity.dictionaries.Day;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ViewScoped
@Named("workTimeBean")
public class WorkTimeBean implements Serializable {

    private Long clientId;
    private Date dateTime;
    private Integer hours;

    public List<Day> getHour() {
        return Stream.of(Day.values()).collect(Collectors.toList());
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
