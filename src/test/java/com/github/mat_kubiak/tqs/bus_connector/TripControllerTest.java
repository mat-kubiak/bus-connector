//package com.github.mat_kubiak.tqs.bus_connector;
//
//import com.github.mat_kubiak.tqs.bus_connector.boundary.RestController;
//import com.github.mat_kubiak.tqs.bus_connector.data.*;
//import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
//import com.github.mat_kubiak.tqs.bus_connector.service.ManagerServiceImpl;
//import io.restassured.module.mockmvc.RestAssuredMockMvc;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.sql.Date;
//import java.sql.Time;
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//import static org.mockito.Mockito.when;
//
//@WebMvcTest(RestController.class)
//class TripControllerTest {
//
//    @MockBean
//    private TripRepository tripRepository;
//
//    @MockBean
//    ManagerService service;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void getTripsBySourceAndDestinationAndWeekday() throws Exception {
//        City sourceCity = new City(1L, "SourceCity");
//        City destinationCity = new City(2L, "DestinationCity");
//
//        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = TestUtil.getDate(TestUtil.getCurrentYear() + 1, 10, 10);
//        Weekday weekday = Weekday.fromDate(date);
//
//        Trip trip = new Trip(1L, sourceCity, destinationCity, weekday, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), 10, 50);
//
//        when(tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(sourceCity, destinationCity, weekday))
//            .thenReturn(Collections.singletonList(trip));
//
//        RestAssuredMockMvc
//        .given()
//            .param("from", "1")
//            .param("to", "2")
//            .param("date", isoFormat.format(date))
//
//        .when()
//            .get("/trips")
//
//        .then()
//            .statusCode(200)
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body("[0].tripId", equalTo(trip.getTripId().intValue()))
//            .body("[0].sourceCity.cityId", equalTo(sourceCity.getCityId().intValue()))
//            .body("[0].destinationCity.cityId", equalTo(destinationCity.getCityId().intValue()))
//            .body("[0].weekday", equalTo(weekday.toString()))
//            .body("[0].departureTime", equalTo(trip.getDepartureTime().toString()))
//            .body("[0].arrivalTime", equalTo(trip.getArrivalTime().toString()))
//            .body("[0].priceEuro", equalTo(trip.getPriceEuro()))
//            .body("[0].seatsTotal", equalTo(trip.getSeatsTotal()));
//    }
//}
