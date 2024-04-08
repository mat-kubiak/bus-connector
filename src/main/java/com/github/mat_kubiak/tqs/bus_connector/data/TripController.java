package com.github.mat_kubiak.tqs.bus_connector.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TripController {

    private final TripManagerService managerService;

    public TripController(TripManagerService service) {
        this.managerService = service;
    }

    @PostMapping("/trips") public ResponseEntity<Trip> createTrip(@RequestBody Trip oneTrip) {
        HttpStatus status = HttpStatus.CREATED;
        Trip saved = managerService.save(oneTrip);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping(path = "/trips",  produces = "application/json")
    public List<Trip> getAllTrips() {
        return managerService.getAllTrips();
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<Trip> getCarById(@PathVariable(value = "id") Long id)
            throws RuntimeException {
        Trip trip = managerService.getTripDetails(id)
                .orElseThrow(() -> new RuntimeException("Trip not found for id: " + id));
        return ResponseEntity.ok().body(trip);
    }

}
