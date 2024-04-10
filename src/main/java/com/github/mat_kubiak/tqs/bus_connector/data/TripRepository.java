package com.github.mat_kubiak.tqs.bus_connector.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllBySourceCityAndDestinationCityAndWeekday(City sourceCity, City destinationCity, Weekday weekday);
    Trip findByTripId(Long tripId);
}
