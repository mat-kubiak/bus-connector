package com.github.mat_kubiak.tqs.bus_connector.frontend;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.ExchangeRateResponse;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ExchangeRateService;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HomePageController {
    private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);
    private final ManagerService tripService;
    private final ExchangeRateService rateService;

    public HomePageController(ManagerService managerService, ExchangeRateService rateService) {
        this.tripService = managerService;
        this.rateService = rateService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cities", tripService.getAllCities());
        return "index";
    }

    @GetMapping("/search")
    public String searchTrips(@RequestParam(required = true) Long from,
                              @RequestParam(required = true) Long to,
                              @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                              Model model) {

        City fromCity = tripService.getCity(from);
        City toCity = tripService.getCity(to);
        model.addAttribute("originStr", fromCity.getName());
        model.addAttribute("destinationStr", toCity.getName());

        List<Trip> trips = tripService.getTrips(fromCity, toCity, date);
        model.addAttribute("trips", trips);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("dateStr", dateFormat.format(date));
        model.addAttribute("dateISO", isoFormat.format(date));

        return "search";
    }

    @GetMapping("/book")
    public String bookTicket(@RequestParam(required = true) Long tripId,
                             @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                             Model model) {
        Trip trip = tripService.getTrip(tripId);
        trip.setSeatsAvailable(tripService.calculateAvailableSeats(trip, date));
        model.addAttribute("trip", trip);

        model.addAttribute("originStr", trip.getSourceCity().getName());
        model.addAttribute("destinationStr", trip.getDestinationCity().getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("dateStr", dateFormat.format(date));
        model.addAttribute("dateISO", isoFormat.format(date));

        ExchangeRateResponse rateResponse = rateService.getExchangeRates();
        model.addAttribute("rates", rateResponse.getRates());

        return "book";
    }

    @GetMapping("/confirm")
    public String confirmBooking(@RequestParam(required = true) Long ticketId,
                                 Model model) {
        Optional<Ticket> ticketOpt = tripService.getTicket(ticketId);
        if (ticketOpt.isEmpty()) {
            return "not-found";
        }

        Ticket ticket = ticketOpt.get();
        model.addAttribute("ticket", ticket);

        Trip trip = ticket.getTrip();
        model.addAttribute("originStr", trip.getSourceCity().getName());
        model.addAttribute("destinationStr", trip.getDestinationCity().getName());

        Date date = ticket.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("dateStr", dateFormat.format(date));
        model.addAttribute("dateISO", isoFormat.format(date));

        return "confirm";
    }

}
