package com.github.mat_kubiak.tqs.bus_connector.unit;

import com.github.mat_kubiak.tqs.bus_connector.data.*;
import com.github.mat_kubiak.tqs.bus_connector.service.BusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.util.*;

import static com.github.mat_kubiak.tqs.bus_connector.TestUtils.getCurrentYear;
import static com.github.mat_kubiak.tqs.bus_connector.TestUtils.getDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BusServiceUT {

    private Date today, tomorrow;
    private City tokyo, lodz, moscow;
    private Trip tripToday, tripTomorrow;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private BusServiceImpl busService;

    @BeforeEach
    void setUp() {
        tokyo = new City(1L, "Tokyo");
        lodz = new City(2L, "Lodz");
        moscow = new City(3L, "Moscow");

        List<City> allCities = Arrays.asList(tokyo, lodz, moscow);
        Mockito.when(cityRepository.findAll()).thenReturn(allCities);

        Mockito.when(cityRepository.findByCityId(1L)).thenReturn(tokyo);
        Mockito.when(cityRepository.findByCityId(2L)).thenReturn(lodz);
        Mockito.when(cityRepository.findByCityId(3L)).thenReturn(moscow);

        Mockito.when(cityRepository.findByCityId(4L)).thenReturn(null);

        int nextYear = getCurrentYear() + 1; // required to keep the date in the future
        today = getDate(nextYear, 7, 8);
        tomorrow = getDate(nextYear, 7, 9);
        Weekday todayWeekday = Weekday.fromDate(today);
        Weekday tomorrowWeekday = Weekday.fromDate(tomorrow);

        long epoch = Calendar.getInstance().getTimeInMillis();
        tripToday = new Trip(1L, tokyo, lodz, todayWeekday, new Time(epoch), new Time(epoch), 20, 2);
        tripTomorrow = new Trip(2L, lodz, moscow, tomorrowWeekday, new Time(epoch), new Time(epoch), 20, 2);

        Mockito.when(tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(tokyo, lodz, todayWeekday)).thenReturn(Arrays.asList(tripToday));
        Mockito.when(tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(lodz, moscow, tomorrowWeekday)).thenReturn(Arrays.asList(tripTomorrow));

        Mockito.when(tripRepository.findByTripId(tripToday.getTripId())).thenReturn(tripToday);
        Mockito.when(tripRepository.findByTripId(tripTomorrow.getTripId())).thenReturn(tripTomorrow);

        Ticket ticket1 = new Ticket(1L, tripToday, today, "Joao", "Fereira");
        Ticket ticket2 = new Ticket(2L, tripToday, today, "Joao", "Pereira");
        Ticket ticket3 = new Ticket(3L, tripTomorrow, today, "Joao", "Cadeira");
        List<Ticket> ticketsToday = Arrays.asList(ticket1, ticket2);
        List<Ticket> ticketsTomorrow = List.of(ticket3);

        Mockito.when(ticketRepository.findAllByTripAndDate(tripToday, today)).thenReturn(ticketsToday);
        Mockito.when(ticketRepository.findAllByTripAndDate(tripToday, tomorrow)).thenReturn(ticketsTomorrow);

        Mockito.when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket(1L, tripTomorrow, tomorrow, "Ana", "Fereira"));
    }

    @Test
    void getAllCitiesTest() {
        List<City> cities = busService.getAllCities();
        assertThat(cities).extracting(City::getName).containsExactly("Tokyo", "Lodz", "Moscow");
    }

    @Test
    void getTripsGivenDateTest() {
        assertThat(busService.getTrips(tokyo, lodz, today)).containsExactly(tripToday);
        assertThat(busService.getTrips(lodz, moscow, tomorrow)).containsExactly(tripTomorrow);
    }

    @Test
    void calculateAvailableSeatsTest() {
        Optional<Trip> tripOpt = busService.getTrip(1L);
        assertThat(tripOpt).isNotEmpty();
        assertThat(busService.calculateAvailableSeats(tripOpt.get(), today)).isEqualTo(0);
    }

    @Test
    void bookTicketTest() {
        assertThat(busService.bookTicket(tripTomorrow, tomorrow, "Ana", "Fereira").isEmpty()).isEqualTo(false);

        // incompatible weekday
        assertThat(busService.bookTicket(tripToday, today, "Ana", "Pereira").isEmpty()).isEqualTo(true);

        // not enough space
        assertThat(busService.bookTicket(tripToday, today, "Ana", "Cadeira").isEmpty()).isEqualTo(true);
    }
}
