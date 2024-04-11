package com.github.mat_kubiak.tqs.bus_connector.unit;

import com.github.mat_kubiak.tqs.bus_connector.data.Weekday;
import com.github.mat_kubiak.tqs.bus_connector.service.IBusService;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static com.github.mat_kubiak.tqs.bus_connector.TestUtils.getDate;
import static com.github.mat_kubiak.tqs.bus_connector.TestUtils.shiftByDays;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ConvertersUT {

    @Test
    void testDateBefore() {
        Date now = new Date(System.currentTimeMillis());
        Date yesterday = shiftByDays(now, -1);
        Date tomorrow = shiftByDays(now, 1);

        assertThat(IBusService.isDateInPast(now), equalTo(false));
        assertThat(IBusService.isDateInPast(yesterday), equalTo(true));
        assertThat(IBusService.isDateInPast(tomorrow), equalTo(false));
    }

    @Test
    void testWeekday() {
        Date tuesdayDate = getDate(2024, 4, 9);
        assertThat(Weekday.fromDate(tuesdayDate), equalTo(Weekday.TUESDAY));

        Date thursdayDate = getDate(2024, 4, 11);
        assertThat(Weekday.fromDate(thursdayDate), equalTo(Weekday.THURSDAY));
    }
}
