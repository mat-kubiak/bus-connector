package com.github.mat_kubiak.tqs.bus_connector.frontend;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.data.City;
import com.github.mat_kubiak.tqs.bus_connector.data.Trip;
import com.github.mat_kubiak.tqs.bus_connector.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
public class HomePageController {
    private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);
    private final ManagerService tripService;

    public HomePageController(ManagerService managerService) {
        this.tripService = managerService;
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
        model.addAttribute("dateStr", dateFormat.format(date));

        return "book";
    }

}
