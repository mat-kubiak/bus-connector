package com.github.mat_kubiak.tqs.bus_connector;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.CityRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CityRepository cityRepository;

    @Test
    @Disabled
    void findMultipleCities() {
        entityManager.persistAndFlush(new City(1L, "Tokyo"));
        entityManager.persistAndFlush(new City(2L, "Moscow"));

        assertThat(cityRepository.findAll()).extracting(City::getName).containsExactly("Tokyo", "Moscow");
    }

    @Test
    @Disabled
    void findCityById() {
        entityManager.persistAndFlush(new City(3L, "Lisbon"));

        assertThat(cityRepository.findByCityId(2L).getName()).isEqualTo("Lisbon");
    }
}

