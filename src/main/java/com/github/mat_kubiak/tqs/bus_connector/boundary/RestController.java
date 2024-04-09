package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.ExchangeRateResponse;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ExchangeRateService;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final ManagerService managerService;
    private final ExchangeRateService rateService;

    public RestController(ManagerService service, ExchangeRateService rateService) {
        this.managerService = service;
        this.rateService = rateService;
    }

    @GetMapping(path = "/cities",  produces = "application/json")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = managerService.getAllCities();
        if (cities.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(path = "/trips",  produces = "application/json")
    public ResponseEntity<List<Trip>> getTripsInfo(@RequestParam(required = true) Long from,
                                   @RequestParam(required = true) Long to,
                                   @RequestParam(required = true) Date date) {

        City fromCity = managerService.getCity(from);
        City toCity = managerService.getCity(to);
        if (fromCity == null || toCity == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<Trip> trips = managerService.getTrips(fromCity, toCity, date);
        if (trips.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping(path = "/exchange-rates", produces = "application/json")
    public ResponseEntity<ExchangeRateResponse> getExchangeRates() {
        ExchangeRateResponse response = rateService.getExchangeRates();
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/ticket", produces = "application/json")
    public ResponseEntity<Ticket> getTicket(@RequestParam(required = true) Long id) {
        Optional<Ticket> ticketOpt = managerService.getTicket(id);
        if (ticketOpt.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ticketOpt.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/ticket", produces = "application/json")
    public ResponseEntity<Ticket> reserveTicket(@RequestParam(required = true) Long tripId,
                              @RequestParam(required = true) Date date,
                              @RequestParam(required = true) String firstName,
                              @RequestParam(required = true) String lastName) {

        Trip trip = managerService.getTrip(tripId);
        if (trip == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Ticket ticket = managerService.bookTicket(trip, date, firstName, lastName);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
}
