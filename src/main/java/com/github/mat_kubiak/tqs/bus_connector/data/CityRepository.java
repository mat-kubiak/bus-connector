package com.github.mat_kubiak.tqs.bus_connector.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    public List<City> findAll();

    public City findByCityId(Long id);
}
