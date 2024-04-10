package com.github.mat_kubiak.tqs.bus_connector.IT;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.TestUtil;
import com.github.mat_kubiak.tqs.bus_connector.data.*;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Run as a SpringBoot test. The parameters to SpringBootTest could be omitted, but, in this case,
 * we are trying to limit the web context to a simplified web framework, and load the designated application
 */
//@SpringBootTest

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = BusConnectorApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
// adapt AutoConfigureTestDatabase with TestPropertySource to use a real database
// @TestPropertySource(locations = "application-integrationtest.properties")
class RestController_IT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TripRepository tripRepository;

    @AfterEach
    public void resetDb() {
        cityRepository.deleteAll();
        tripRepository.deleteAll();
    }

//    @Test
//    void whenValidInput_thenCreateCity() throws IOException, Exception {
//        City tokyo = new City("Tokyo");
//
//        mvc.perform(get("/api/cities").contentType(MediaType.APPLICATION_JSON).content(toJson(tokyo)));
//
//        List<City> found = cityRepository.findAll();
//        assertThat(found).extracting(City::getName).containsOnly("Tokyo");
//    }

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
    @Disabled
    void givenTrips_whenGetTrips_thenStatus200() throws Exception {
        Date date = TestUtil.getDate(TestUtil.getCurrentYear() + 1, 5, 5);
        createTestTrip("Tokyo", "Moscow", Weekday.fromDate(date), Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), 10, 10);

        mvc.perform(get("/api/trips").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void createTestCity(String name) {
        City city = new City(name);
        cityRepository.saveAndFlush(city);
    }

    private void createTestTrip(String originName, String destinationName, Weekday day, Time departure, Time arrival, int price, int maxSeats) {
        City origin = new City(originName);
        City dest = new City(destinationName);
        cityRepository.saveAndFlush(origin);
        cityRepository.saveAndFlush(dest);
        Trip trip = new Trip(origin, dest, day,departure, arrival,  price, maxSeats);
        tripRepository.saveAndFlush(trip);
    }

}

