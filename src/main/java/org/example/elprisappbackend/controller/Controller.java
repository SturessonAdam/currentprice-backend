package org.example.elprisappbackend.controller;

import org.example.elprisappbackend.service.PowerpriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class Controller {

    @Autowired
    private PowerpriceService powerpriceService;

    @GetMapping("/prices")
    public String getPrices(@RequestParam(value = "region", defaultValue = "3") String region) {
        return powerpriceService.getTodaysPrices(region);
    }

    @GetMapping("/prices/tomorrow")
    public String getTomorrowPrices(@RequestParam(value = "region", defaultValue = "3") String region) {
        return powerpriceService.getTomorrowsPrices(region);
    }

    @GetMapping("/funfacts")
    public String getFunFacts(@RequestParam(value = "region", defaultValue = "3") String region) {
        return powerpriceService.getFunFacts(region);
    }
}
