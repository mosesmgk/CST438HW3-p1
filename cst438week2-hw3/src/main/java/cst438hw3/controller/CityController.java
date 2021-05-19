package cst438hw3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cst438hw3.service.CityService;
import cst438hw3.domain.*;

@Controller
public class CityController {

    @Autowired
    CityService cityService;

    @GetMapping("/cities/{city}")
    public String getCityInfo(@PathVariable("city") String cityName, Model model) {

        ResponseEntity<CityInfo> info = cityService.getCityInfo(cityName);
        if (info.getStatusCode() == HttpStatus.NOT_FOUND) {
            return "showerror"; // invalid city name
        }
        CityInfo city = info.getBody();
        model.addAttribute("city", city);
        return "showcity";
    }

    @PostMapping("/cities/reservation")
    public String createReservation(
            @RequestParam("city") String cityName,
            @RequestParam("level") String level,
            @RequestParam("email") String email,
            Model model) {
        model.addAttribute("city", cityName);
        model.addAttribute("level", level);
        model.addAttribute("email", email);
        cityService.requestReservation(cityName, level, email);
        System.out.println(cityName + ";" + level + ";" + email);
        return "request_reservation";
    }
    
}