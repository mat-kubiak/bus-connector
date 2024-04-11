package com.github.mat_kubiak.tqs.bus_connector.unit;

import com.github.mat_kubiak.tqs.bus_connector.TestUtils;
import com.github.mat_kubiak.tqs.bus_connector.data.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DataRepositoriesUT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void findMultipleCities() {
        City tokyo = new City("Tokyo");
        City moscow = new City("Moscow");
        entityManager.persist(tokyo);
        entityManager.persist(moscow);
        entityManager.flush();

        Iterable<City> cities = cityRepository.findAll();
        assertThat(cities).extracting(City::getName).containsExactly("Tokyo", "Moscow");
    }

    @Test
    void findCityById() {
        City lisbon = new City("Lisbon");
        entityManager.persist(lisbon);
        entityManager.flush();

        Optional<City> foundCity = cityRepository.findById(lisbon.getCityId());
        assertThat(foundCity).isPresent();
        assertThat(foundCity.get().getName()).isEqualTo("Lisbon");
    }

    @Test
    void findTripBySourceAndDestinationAndWeekday() {
        City source = new City("SourceCity");
        City destination = new City("DestinationCity");
        entityManager.persist(source);
        entityManager.persist(destination);
        entityManager.flush();

        Trip trip = new Trip(source, destination, Weekday.MONDAY, java.sql.Time.valueOf("08:00:00"), java.sql.Time.valueOf("12:00:00"), 10, 50);
        entityManager.persist(trip);
        entityManager.flush();

        List<Trip> trips = tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(source, destination, Weekday.MONDAY);
        assertThat(trips.size()).isEqualTo(1);
        assertThat(trips.get(0)).isEqualTo(trip);
    }

    @Test
    void findTicketsByTripAndDate() {
        City source = new City("SourceCity");
        City destination = new City("DestinationCity");
        entityManager.persist(source);
        entityManager.persist(destination);
        entityManager.flush();

        Date date = TestUtils.getDate(TestUtils.getCurrentYear() + 1, 5, 20);

        Trip trip = new Trip(source, destination, Weekday.fromDate(date), java.sql.Time.valueOf("08:00:00"), java.sql.Time.valueOf("12:00:00"), 10, 50);
        entityManager.persist(trip);
        entityManager.flush();


        Ticket ticket1 = new Ticket(trip, date, "John", "Doe");
        Ticket ticket2 = new Ticket(trip, date, "Jane", "Doe");

        entityManager.persist(ticket1);
        entityManager.persist(ticket2);
        entityManager.flush();

        List<Ticket> tickets = ticketRepository.findAllByTripAndDate(trip, date);
        assertThat(tickets.size()).isEqualTo(2);
        assertThat(tickets).containsExactly(ticket1, ticket2);
    }

}
