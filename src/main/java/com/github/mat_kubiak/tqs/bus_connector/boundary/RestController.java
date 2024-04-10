package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerServiceImpl;
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

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    private final ManagerServiceImpl managerServiceImpl;

    public RestController(ManagerServiceImpl service) {
        this.managerServiceImpl = service;
    }

    @GetMapping(path = "/cities",  produces = "application/json")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = managerServiceImpl.getAllCities();

        if (cities.isEmpty()) {
            logger.info("/cities GET request: no cities found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Successful /cities GET request: returned {} cities", cities.size());
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(path = "/trips",  produces = "application/json")
    public ResponseEntity<List<Trip>> getTripsInfo(@RequestParam Long from,
                                   @RequestParam Long to,
                                   @RequestParam Date date) {

        Optional<City> fromCity = managerServiceImpl.getCity(from);
        Optional<City> toCity = managerServiceImpl.getCity(to);

        if (fromCity.isEmpty() || toCity.isEmpty()) {
            logger.info("Bad /trips GET request: one or more cities with ids {} {} do not exist!", from, to);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Trip> trips = managerServiceImpl.getTrips(fromCity.get(), toCity.get(), date);
        if (trips.isEmpty()) {
            logger.info("Bad /trips GET request: trips not found for destination {} origin {} on date {}", fromCity.get().getName(), toCity.get().getName(), date);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        logger.info("Successful /trips GET request: returned {} trips", trips.size());
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping(path = "/ticket", produces = "application/json")
    public ResponseEntity<Ticket> getTicket(@RequestParam Long id) {
        Optional<Ticket> ticketOpt = managerServiceImpl.getTicket(id);
        if (ticketOpt.isEmpty()) {
            logger.info("Bad /ticket GET request: ticket id {} does not exist!", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Successful /ticket GET request: returned ticket with id {}", id);
        return new ResponseEntity<>(ticketOpt.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/ticket", produces = "application/json")
    public ResponseEntity<Ticket> reserveTicket(@RequestParam Long tripId,
                              @RequestParam Date date,
                              @RequestParam String firstName,
                              @RequestParam String lastName) {

        if (ManagerService.isDateInPast(date)) {
            logger.info("Bad /ticket POST request: date {} is in the past!", date);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Trip> trip = managerServiceImpl.getTrip(tripId);
        if (trip.isEmpty()) {
            logger.info("Bad /ticket POST request: tripId {} does not exist!", tripId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Ticket> ticketOpt = managerServiceImpl.bookTicket(trip.get(), date, firstName, lastName);
        if (ticketOpt.isEmpty()) {
            logger.info("Bad /ticket POST request: not enough seats to reserve for trip {} and date {}", tripId, date);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Ticket ticket = ticketOpt.get();
        logger.info("Successful /ticket POST request: reserved a ticket with id {}", ticket.getTicketId());
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
}
