package com.github.mat_kubiak.tqs.bus_connector.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripManagerService {

    final TripRepository tripRepository;

    @Autowired
    public TripManagerService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip save(Trip oneTrip) {
        return tripRepository.save(oneTrip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripDetails(Long id) {
        Trip trip = tripRepository.findByTripId(id);
        if (trip == null)
            return Optional.empty();
        return Optional.of(trip);
    }

}