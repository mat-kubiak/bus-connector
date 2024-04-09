package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final ManagerService managerService;

    public RestController(ManagerService service) {
        this.managerService = service;
    }

    @GetMapping(path = "/cities",  produces = "application/json")
    public List<City> getAllCities() {
        return managerService.getAllCities();
    }

    @GetMapping(path = "/trips",  produces = "application/json")
    public List<Trip> getTripsInfo(@RequestParam(required = true) Long from,
                                   @RequestParam(required = true) Long to,
                                   @RequestParam(required = true) Date date) {

        City fromCity = managerService.getCity(from);
        City toCity = managerService.getCity(to);
        if (fromCity == null || toCity == null) {
            return Collections.emptyList();
        }

        return managerService.getTrips(fromCity, toCity, date);
    }

    @GetMapping(path = "/exchange", produces = "application/json")
    public ExchangeRates getExchangeRates() {
        return new ExchangeRates(0.0, 0.0, 0.0);
    }

    @GetMapping(path = "/ticket", produces = "application/json")
    public Ticket getTicket(@RequestParam(required = true) Long id) {
        return new Ticket();
    }

    @PostMapping(path = "/ticket", produces = "application/json")
    public Long reserveTicket() {
        return 0L;
    }
}
