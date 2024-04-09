package com.github.mat_kubiak.tqs.bus_connector.IT;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.CityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static com.github.mat_kubiak.tqs.bus_connector.TestUtil.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    public void resetDb() {
        cityRepository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateCity() throws IOException, Exception {
        City tokyo = new City("Tokyo");

        mvc.perform(post("/api/cities").contentType(MediaType.APPLICATION_JSON).content(toJson(tokyo)));

        List<City> found = cityRepository.findAll();
        assertThat(found).extracting(City::getName).containsOnly("Tokyo");
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

    private void createTestCity(String name) {
        City city = new City(name);
        cityRepository.saveAndFlush(city);
    }

}

