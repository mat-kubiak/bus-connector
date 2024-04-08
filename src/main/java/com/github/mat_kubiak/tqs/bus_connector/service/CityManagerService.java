package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityManagerService {

    final CityRepository cityRepository;
    final TripRepository tripRepository;
    final TicketRepository ticketRepository;

    @Autowired
    public CityManagerService(CityRepository cityRepository, TripRepository tripRepository, TicketRepository ticketRepository) {
        this.cityRepository = cityRepository;
        this.tripRepository = tripRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public Optional<Ticket> getTicket(Long id) {
        return ticketRepository.findById(id);
    }

//    public Optional<City> getCityDetails(Long id) {
//        City city = cityRepository.findByCityId(id);
//        if (city == null)
//            return Optional.empty();
//        return Optional.of(city);
//    }

}