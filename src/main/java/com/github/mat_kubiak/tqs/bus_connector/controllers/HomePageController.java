package com.github.mat_kubiak.tqs.bus_connector.controllers;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.service.CityManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.mat_kubiak.tqs.bus_connector.data.City;

@Controller
public class HomePageController {
    private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);
    private final CityManagerService tripService;

    public HomePageController(CityManagerService cityManagerService) {
        this.tripService = cityManagerService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cities", tripService.getAllCities());
        return "index";
    }

//    @RequestMapping(value="/add-city", method = RequestMethod.POST)
//    public String handleSearch() {
//        logger.info("A city has been added!");
//        tripService.save(new City("Thyme"));
//        return "redirect:/";
//    }
}
