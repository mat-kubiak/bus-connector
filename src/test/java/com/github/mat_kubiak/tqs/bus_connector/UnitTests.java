package com.github.mat_kubiak.tqs.bus_connector;

import com.github.mat_kubiak.tqs.bus_connector.data.Weekday;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static com.github.mat_kubiak.tqs.bus_connector.TestUtil.getDate;
import static com.github.mat_kubiak.tqs.bus_connector.TestUtil.shiftDays;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UnitTests {

    @Test
    public void testDateBefore() {
        Date now = new Date(System.currentTimeMillis());
        Date yesterday = shiftDays(now, -1);
        Date tomorrow = shiftDays(now, 1);

        assertThat(ManagerService.isDateInPast(now), equalTo(false));
        assertThat(ManagerService.isDateInPast(yesterday), equalTo(true));
        assertThat(ManagerService.isDateInPast(tomorrow), equalTo(false));
    }

    @Test
    public void testWeekday() {
        Date tuesdayDate = getDate(2024, 4, 9);
        assertThat(Weekday.fromDate(tuesdayDate), equalTo(Weekday.TUESDAY));

        Date thursdayDate = getDate(2024, 4, 11);
        assertThat(Weekday.fromDate(thursdayDate), equalTo(Weekday.THURSDAY));
    }
}
