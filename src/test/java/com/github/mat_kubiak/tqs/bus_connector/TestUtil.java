package com.github.mat_kubiak.tqs.bus_connector;

import java.sql.Date;
import java.util.Calendar;

public class TestUtil {

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    public static Date shiftDays(Date date, int shift) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_YEAR, shift);

        return new Date(calendar.getTimeInMillis());
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
