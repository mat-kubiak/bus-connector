package com.github.mat_kubiak.tqs.bus_connector;

import com.github.mat_kubiak.tqs.bus_connector.data.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripRepository tripRepository;

    @Test
    void getTripsBySourceAndDestinationAndWeekday() throws Exception {
        City sourceCity = new City(1L, "SourceCity");
        City destinationCity = new City(2L, "DestinationCity");
        Weekday weekday = Weekday.MONDAY;
        Trip trip = new Trip(1L, sourceCity, destinationCity, weekday, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), 10, 50);

        when(tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(sourceCity, destinationCity, weekday))
                .thenReturn(Collections.singletonList(trip));

        mockMvc.perform(get("/trips")
                        .param("sourceCityId", "1")
                        .param("destinationCityId", "2")
                        .param("weekday", "MONDAY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tripId").value(trip.getTripId()))
                .andExpect(jsonPath("$[0].sourceCity.cityId").value(sourceCity.getCityId()))
                .andExpect(jsonPath("$[0].destinationCity.cityId").value(destinationCity.getCityId()))
                .andExpect(jsonPath("$[0].weekday").value(weekday.toString()))
                .andExpect(jsonPath("$[0].departureTime").value(trip.getDepartureTime().toString()))
                .andExpect(jsonPath("$[0].arrivalTime").value(trip.getArrivalTime().toString()))
                .andExpect(jsonPath("$[0].priceEuro").value(trip.getPriceEuro()))
                .andExpect(jsonPath("$[0].seatsTotal").value(trip.getSeatsTotal()));

        verify(tripRepository, times(1))
                .findAllBySourceCityAndDestinationCityAndWeekday(sourceCity, destinationCity, weekday);
    }
}
