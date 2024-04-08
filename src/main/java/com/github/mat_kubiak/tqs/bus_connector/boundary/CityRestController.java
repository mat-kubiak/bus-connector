package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.service.CityManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CityRestController {

    private final CityManagerService managerService;

    public CityRestController(CityManagerService service) {
        this.managerService = service;
    }

    @PostMapping("/cities") public ResponseEntity<City> createTrip(@RequestBody City oneCity) {
        HttpStatus status = HttpStatus.CREATED;
        City saved = managerService.save(oneCity);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping(path = "/cities",  produces = "application/json")
    public List<City> getAllTrips() {
        return managerService.getAllCities();
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCarById(@PathVariable(value = "id") Long id)
            throws RuntimeException {
        City city = managerService.getCityDetails(id)
                .orElseThrow(() -> new RuntimeException("City not found for id: " + id));
        return ResponseEntity.ok().body(city);
    }

}
