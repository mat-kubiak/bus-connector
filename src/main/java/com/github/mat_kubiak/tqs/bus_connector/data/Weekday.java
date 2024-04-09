package com.github.mat_kubiak.tqs.bus_connector.data;

import java.util.Calendar;
import java.util.Date;

public enum Weekday {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    public static Weekday fromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Weekday.values()[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }
}
