package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.boundary.ExchangeRates;
import com.github.mat_kubiak.tqs.bus_connector.data.*;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.mat_kubiak.tqs.bus_connector.data.Weekday;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);

    final CityRepository cityRepository;
    final TripRepository tripRepository;
    final TicketRepository ticketRepository;

    @Autowired
    public ManagerService(CityRepository cityRepository, TripRepository tripRepository, TicketRepository ticketRepository) {
        this.cityRepository = cityRepository;
        this.tripRepository = tripRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCity(Long id) {
        return cityRepository.findByCityId(id);
    }
    public Trip getTrip(Long id) {
        return tripRepository.findByTripId(id);
    }

    public Optional<Ticket> getTicket(Long id) {
        return ticketRepository.findById(id);
    }

    public Integer calculateAvailableSeats(Trip trip, Date date) {
        List<Ticket> tickets = ticketRepository.findAllByTripAndDate(trip, date);
        return trip.getSeatsTotal() - tickets.size();
    }

    public List<Trip> getTrips(City from, City to, Date date) {
        Weekday day = Weekday.fromDate(date);
        logger.info("Weekday: " + day);
        logger.info("number: " + day.ordinal());

        List<Trip> trips = tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(from, to, day);
        logger.info("trips: " + trips);

        for (Trip trip : trips) {
            int available = calculateAvailableSeats(trip, date);
            trip.setSeatsAvailable(available);
        }

        return trips;
    }

}