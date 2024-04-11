package com.github.mat_kubiak.tqs.bus_connector.IT;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.TestUtil;
import com.github.mat_kubiak.tqs.bus_connector.data.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = BusConnectorApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class RestController_IT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TripRepository tripRepository;

    @AfterEach
    public void resetDb() {
        tripRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    void givenCities_whenGetCities_thenStatus200() throws Exception {
        createTestCity("Tokyo");
        createTestCity("Moscow");

        mvc.perform(get("/api/cities").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", is("Tokyo")))
                .andExpect(jsonPath("$[1].name", is("Moscow")));
    }

    @Test
    void givenTrips_whenGetTrips_thenStatus200() throws Exception {
        Date date = TestUtil.getDate(TestUtil.getCurrentYear() + 1, 5, 5);

        City tokyo = createTestCity("Tokyo");
        City moscow = createTestCity("Moscow");
        createTestTrip(tokyo, moscow, Weekday.fromDate(date), Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), 10, 10);

        String url = String.format("/api/trips?from=%d&to=%d&date=%tF", tokyo.getCityId(), moscow.getCityId(), date);
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.sourceCity.name", is("Tokyo")))
                .andExpect(jsonPath("$.destinationCity.name", is("Moscow")))
                .andExpect(jsonPath("$.priceEuro", is(10)))
                .andExpect(jsonPath("$.seatsTotal", is(10)));
    }

    private City createTestCity(String name) {
        City city = new City(name);
        cityRepository.saveAndFlush(city);
        return city;
    }

    private void createTestTrip(City origin, City destination, Weekday day, Time departure, Time arrival, int price, int maxSeats) {
        Trip trip = new Trip(origin, destination, day,departure, arrival,  price, maxSeats);
        tripRepository.saveAndFlush(trip);
    }

}
