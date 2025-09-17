package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.ExchangeRateResponse;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ExchangeRateService;
import com.github.mat_kubiak.tqs.bus_connector.service.IBusService;
import com.github.mat_kubiak.tqs.bus_connector.service.BusServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {
    private final BusServiceImpl tripService;
    private final ExchangeRateService rateService;
    public static final String ORIGIN_PARAM = "originStr";
    public static final String DEST_PARAM = "destinationStr";
    public static final String DATE_PARAM = "dateStr";
    public static final String DATE_ISO_PARAM = "dateISO";
    public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    public final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public WebController(ExchangeRateService rateService, BusServiceImpl tripService) {
        this.rateService = rateService;
        this.tripService = tripService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cities", tripService.getAllCities());
        return "index";
    }

    @GetMapping("/search")
    public String searchTrips(@RequestParam Long from,
                              @RequestParam Long to,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                              Model model) {

        Optional<City> fromCity = tripService.getCity(from);
        Optional<City> toCity = tripService.getCity(to);
        if (fromCity.isEmpty() || toCity.isEmpty()) {
            return index(model);
        }

        model.addAttribute(ORIGIN_PARAM, fromCity.get().getName());
        model.addAttribute(DEST_PARAM, toCity.get().getName());

        List<Trip> trips = tripService.getTrips(fromCity.get(), toCity.get(), date);
        model.addAttribute("trips", trips);

        model.addAttribute(DATE_PARAM, DATE_FORMAT.format(date));
        model.addAttribute(DATE_ISO_PARAM, ISO_FORMAT.format(date));

        if (IBusService.isDateInPast(date)) {
            return index(model);
        }

        return "search";
    }

    @GetMapping("/book")
    public String bookTicket(@RequestParam Long tripId,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                             Model model) {
        Optional<Trip> tripOpt = tripService.getTrip(tripId);
        if (tripOpt.isEmpty() || IBusService.isDateInPast(date)) {
            return index(model);
        }
        Trip trip = tripOpt.get();

        trip.setSeatsAvailable(tripService.calculateAvailableSeats(trip, date));
        model.addAttribute("trip", trip);

        model.addAttribute(ORIGIN_PARAM, trip.getSourceCity().getName());
        model.addAttribute(ORIGIN_PARAM, trip.getDestinationCity().getName());

        model.addAttribute(DATE_PARAM, DATE_FORMAT.format(date));
        model.addAttribute(DATE_ISO_PARAM, ISO_FORMAT.format(date));

        ExchangeRateResponse rateResponse = rateService.getExchangeRates();
        model.addAttribute("rates", rateResponse.getRates());

        return "book";
    }

    @GetMapping("/confirm")
    public String confirmBooking(@RequestParam Long ticketId,
                                 Model model) {
        Optional<Ticket> ticketOpt = tripService.getTicket(ticketId);
        if (ticketOpt.isEmpty()) {
            return "not-found";
        }

        Ticket ticket = ticketOpt.get();
        model.addAttribute("ticket", ticket);

        Trip trip = ticket.getTrip();
        model.addAttribute(ORIGIN_PARAM, trip.getSourceCity().getName());
        model.addAttribute(DEST_PARAM, trip.getDestinationCity().getName());

        Date date = ticket.getDate();
        model.addAttribute(DATE_PARAM, DATE_FORMAT.format(date));
        model.addAttribute(DATE_ISO_PARAM, ISO_FORMAT.format(date));

        return "confirm";
    }

}
