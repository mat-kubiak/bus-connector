package com.github.mat_kubiak.tqs.bus_connector;

import com.github.mat_kubiak.tqs.bus_connector.data.*;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.util.*;

import static com.github.mat_kubiak.tqs.bus_connector.TestUtil.getCurrentYear;
import static com.github.mat_kubiak.tqs.bus_connector.TestUtil.getDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TestManagerServiceImpl {

    private Date today, tomorrow;
    private Weekday todayWeekday, tomorrowWeekday;
    private City tokyo, lodz, moskow;
    private Trip tripToday, tripTomorrow;

    @Mock( lenient = true)
    private CityRepository cityRepository;


    @Mock( lenient = true)
    private TripRepository tripRepository;

    @Mock( lenient = true)
    private TicketRepository ticketRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    @BeforeEach
    public void setUp() {
        tokyo = new City(1L, "Tokyo");
        lodz = new City(2L, "Lodz");
        moskow = new City(3L, "Moskow");

        List<City> allCities = Arrays.asList(tokyo, lodz, moskow);
        Mockito.when(cityRepository.findAll()).thenReturn(allCities);

        Mockito.when(cityRepository.findByCityId(1L)).thenReturn(tokyo);
        Mockito.when(cityRepository.findByCityId(2L)).thenReturn(lodz);
        Mockito.when(cityRepository.findByCityId(3L)).thenReturn(moskow);

        Mockito.when(cityRepository.findByCityId(4L)).thenReturn(null);

        int nextYear = getCurrentYear() + 1; // required to keep the date in the future
        today = getDate(nextYear, 7, 8);
        tomorrow = getDate(nextYear, 7, 9);
        todayWeekday = Weekday.fromDate(today);
        tomorrowWeekday = Weekday.fromDate(tomorrow);

        long epoch = Calendar.getInstance().getTimeInMillis();
        tripToday = new Trip(1L, tokyo, lodz, todayWeekday, new Time(epoch), new Time(epoch), 20, 2);
        tripTomorrow = new Trip(2L, lodz, moskow, tomorrowWeekday, new Time(epoch), new Time(epoch), 20, 2);

        Mockito.when(tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(tokyo, lodz, todayWeekday)).thenReturn(Arrays.asList(tripToday));
        Mockito.when(tripRepository.findAllBySourceCityAndDestinationCityAndWeekday(lodz, moskow, tomorrowWeekday)).thenReturn(Arrays.asList(tripTomorrow));

        Mockito.when(tripRepository.findByTripId(tripToday.getTripId())).thenReturn(tripToday);
        Mockito.when(tripRepository.findByTripId(tripTomorrow.getTripId())).thenReturn(tripTomorrow);

        Ticket ticket1 = new Ticket(1L, tripToday, today, "Joao", "Fereira");
        Ticket ticket2 = new Ticket(2L, tripToday, today, "Joao", "Pereira");
        Ticket ticket3 = new Ticket(3L, tripTomorrow, today, "Joao", "Cadeira");
        List<Ticket> ticketsToday = Arrays.asList(ticket1, ticket2);
        List<Ticket> ticketsTomorrow = Arrays.asList(ticket3);

        Mockito.when(ticketRepository.findAllByTripAndDate(tripToday, today)).thenReturn(ticketsToday);
        Mockito.when(ticketRepository.findAllByTripAndDate(tripToday, tomorrow)).thenReturn(ticketsTomorrow);

        Mockito.when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket(1L, tripTomorrow, tomorrow, "Ana", "Fereira"));
    }

    @Test
    public void getAllCitiesTest() {
        List<City> cities = managerService.getAllCities();
        assertThat(cities).extracting(City::getName).containsExactly("Tokyo", "Lodz", "Moskow");
    }

    @Test
    public void getTripsGivenDateTest() {
        assertThat(managerService.getTrips(tokyo, lodz, today)).containsExactly(tripToday);
        assertThat(managerService.getTrips(lodz, moskow, tomorrow)).containsExactly(tripTomorrow);
    }

    @Test
    public void calculateAvailableSeatsTest() {
        Trip trip = managerService.getTrip(1L).get();
        assertThat(managerService.calculateAvailableSeats(trip, today)).isEqualTo(0);
    }

    @Test
    public void bookTicketTest() {
        assertThat(managerService.bookTicket(tripTomorrow, tomorrow, "Ana", "Fereira").isEmpty()).isEqualTo(false);

        // incompatible weekday
        assertThat(managerService.bookTicket(tripToday, today, "Ana", "Pereira").isEmpty()).isEqualTo(true);

        // not enough space
        assertThat(managerService.bookTicket(tripToday, today, "Ana", "Cadeira").isEmpty()).isEqualTo(true);
    }
}
