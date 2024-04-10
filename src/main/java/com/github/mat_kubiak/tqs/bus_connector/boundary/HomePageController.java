package com.github.mat_kubiak.tqs.bus_connector.boundary;

import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.ExchangeRateResponse;
import com.github.mat_kubiak.tqs.bus_connector.data.Ticket;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ExchangeRateService;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
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

    private final ManagerServiceImpl tripService;

    private final ExchangeRateService rateService;

    private String originParam = "originStr";
    private String destParam = "destinationStr";
    private String dateParam = "dateStr";
    private String dateIsoParam = "dateISO";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    private SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");

    public HomePageController(ExchangeRateService rateService, ManagerServiceImpl tripService) {
        this.rateService = rateService;
        this.tripService = tripService;
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

        Optional<City> fromCity = tripService.getCity(from);
        Optional<City> toCity = tripService.getCity(to);
        if (fromCity.isEmpty() || toCity.isEmpty()) {
            return "index";
        }

        model.addAttribute(originParam, fromCity.get().getName());
        model.addAttribute(destParam, toCity.get().getName());

        List<Trip> trips = tripService.getTrips(fromCity.get(), toCity.get(), date);
        model.addAttribute("trips", trips);

        model.addAttribute(dateParam, dateFormat.format(date));
        model.addAttribute(dateIsoParam, isoFormat.format(date));

        model.addAttribute("isInPast", ManagerService.isDateInPast(date));
        return "search";
    }

    @GetMapping("/book")
    public String bookTicket(@RequestParam(required = true) Long tripId,
                             @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                             Model model) {
        Optional<Trip> tripOpt = tripService.getTrip(tripId);
        if (tripOpt.isEmpty() || ManagerService.isDateInPast(date)) {
            return index(model);
        }
        Trip trip = tripOpt.get();

        trip.setSeatsAvailable(tripService.calculateAvailableSeats(trip, date));
        model.addAttribute("trip", trip);

        model.addAttribute(originParam, trip.getSourceCity().getName());
        model.addAttribute(originParam, trip.getDestinationCity().getName());

        model.addAttribute(dateParam, dateFormat.format(date));
        model.addAttribute(dateIsoParam, isoFormat.format(date));

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
        model.addAttribute(originParam, trip.getSourceCity().getName());
        model.addAttribute(destParam, trip.getDestinationCity().getName());

        Date date = ticket.getDate();
        model.addAttribute("dateStr", dateFormat.format(date));
        model.addAttribute("dateISO", isoFormat.format(date));

        return "confirm";
    }

}
