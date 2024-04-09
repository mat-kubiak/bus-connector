package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.data.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class ManagerService {

    public abstract List<City> getAllCities();

    public abstract Optional<City> getCity(Long id);
    public abstract Optional<Trip> getTrip(Long id);

    public abstract Optional<Ticket> getTicket(Long id);

    public abstract Integer calculateAvailableSeats(Trip trip, Date date);

    public static boolean isDateInPast(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return date.before(new Date(calendar.getTimeInMillis()));
    }

    public abstract List<Trip> getTrips(City from, City to, Date date);

    public abstract Optional<Ticket> bookTicket(Trip trip, Date date, String firstName, String lastName);
}