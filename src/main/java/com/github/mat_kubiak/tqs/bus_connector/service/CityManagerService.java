package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityManagerService {

    final CityRepository cityRepository;

    @Autowired
    public CityManagerService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City save(City oneCity) {
        return cityRepository.save(oneCity);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public Optional<City> getCityDetails(Long id) {
        City city = cityRepository.findByCityId(id);
        if (city == null)
            return Optional.empty();
        return Optional.of(city);
    }

}