package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);
    private final ManagerService managerService;

    public RestController(ManagerService service) {
        this.managerService = service;
    }

    @GetMapping(path = "/cities",  produces = "application/json")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = managerService.getAllCities();
        if (cities.isEmpty()) {
            logger.info("/cities GET request: no cities found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        logger.info("Successful /cities GET request: returned " + cities.size() + " cities");
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(path = "/trips",  produces = "application/json")
    public ResponseEntity<List<Trip>> getTripsInfo(@RequestParam(required = true) Long from,
                                   @RequestParam(required = true) Long to,
                                   @RequestParam(required = true) Date date) {

        Optional<City> fromCity = managerService.getCity(from);
        Optional<City> toCity = managerService.getCity(to);

        if (fromCity.isEmpty() || toCity.isEmpty()) {
            logger.info("Bad /trips GET request: one or more cities with ids " + from + " " + to + " do not exist!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<Trip> trips = managerService.getTrips(fromCity.get(), toCity.get(), date);
        if (trips.isEmpty()) {
            logger.info("Bad /trips GET request: trips not found for destination " + fromCity.get().getName() + " origin " + toCity.get().getName() + " on date " + date);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        logger.info("Successful /trips GET request: returned " + trips.size() + " trips");
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping(path = "/ticket", produces = "application/json")
    public ResponseEntity<Ticket> getTicket(@RequestParam(required = true) Long id) {
        Optional<Ticket> ticketOpt = managerService.getTicket(id);
        if (ticketOpt.isEmpty()) {
            logger.info("Bad /ticket GET request: ticket id " + id + " does not exist!");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        logger.info("Successful /ticket GET request: returned ticket with id " + id);
        return new ResponseEntity<>(ticketOpt.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/ticket", produces = "application/json")
    public ResponseEntity<Ticket> reserveTicket(@RequestParam(required = true) Long tripId,
                              @RequestParam(required = true) Date date,
                              @RequestParam(required = true) String firstName,
                              @RequestParam(required = true) String lastName) {

        if (managerService.isDateInPast(date)) {
            logger.info("Bad /ticket POST request: date " + date + " is in the past!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Optional<Trip> trip = managerService.getTrip(tripId);
        if (trip.isEmpty()) {
            logger.info("Bad /ticket POST request: tripId " + tripId + " does not exist!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Optional<Ticket> ticketOpt = managerService.bookTicket(trip.get(), date, firstName, lastName);
        if (ticketOpt.isEmpty()) {
            logger.info("Bad /ticket POST request: not enough seats to reserve for trip " + tripId + " and date " + date);
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        Ticket ticket = ticketOpt.get();
        logger.info("Successful /ticket POST request: reserved a ticket with id " + ticket.getTicketId());
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
}
