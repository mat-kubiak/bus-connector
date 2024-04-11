package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.mat_kubiak.tqs.bus_connector.data.Weekday;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BusServiceImpl {

    final CityRepository cityRepository;
    final TripRepository tripRepository;
    final TicketRepository ticketRepository;

    @Autowired
    public BusServiceImpl(CityRepository cityRepository, TripRepository tripRepository, TicketRepository ticketRepository) {
        this.cityRepository = cityRepository;
        this.tripRepository = tripRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public Optional<City> getCity(Long id) {
        return Optional.ofNullable(cityRepository.findByCityId(id));
    }
    public Optional<Trip> getTrip(Long id) {
        return Optional.ofNullable(tripRepository.findByTripId(id));
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
        List<Trip> trips = tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(from, to, day);

        for (Trip trip : trips) {
            int available = calculateAvailableSeats(trip, date);
            trip.setSeatsAvailable(available);
        }

        return trips;
    }

    public Optional<Ticket> bookTicket(Trip trip, Date date, String firstName, String lastName) {
        if (Weekday.fromDate(date) != trip.getWeekday()) {
            return Optional.empty();
        }

        if (calculateAvailableSeats(trip, date) == 0 || IBusService.isDateInPast(date)) {
            return Optional.empty();
        }

        Ticket ticket = new Ticket(trip, date, firstName, lastName);
        Ticket newTicket = ticketRepository.save(ticket);
        ticketRepository.flush();
        return Optional.of(newTicket);
    }
}