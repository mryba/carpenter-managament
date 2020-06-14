package com.carpenter.core.entity.dictionaries;

public enum Week {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private final Integer number;

    Week(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }
}
