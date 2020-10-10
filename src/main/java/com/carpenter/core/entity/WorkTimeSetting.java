package com.carpenter.core.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "WORK_TIME_SETTINGS")
public class WorkTimeSetting {

    private static final long serialVersionUID = 4437366168761136L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATIC_HOUR")
    private Integer staticHour;

    @Column(name = "ACTIVE")
    private Boolean mechanismActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkTimeSetting workTime = (WorkTimeSetting) o;
        return Objects.equals(id, workTime.id) &&
                Objects.equals(staticHour, workTime.staticHour) &&
                Objects.equals(mechanismActive, workTime.mechanismActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, staticHour, mechanismActive);
    }
}
