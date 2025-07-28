package org.example.elprisappbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.elprisappbackend.model.FunFactsResponse;
import org.example.elprisappbackend.model.PriceEntry;
import org.example.elprisappbackend.service.PowerpriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class Controller {

    private final PowerpriceService powerpriceService;

    @Autowired
    public Controller(PowerpriceService powerpriceService) {
        this.powerpriceService = powerpriceService;
    }

    @GetMapping("/prices")
    public ResponseEntity<List<PriceEntry>> getPrices(
            @RequestParam(value = "region", defaultValue = "3") String region) {
        
        log.info("Fetching today's prices for region: {}", region);
        
        List<PriceEntry> prices = powerpriceService.getTodaysPrices(region);
        
        if (prices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(prices);
    }

    @GetMapping("/prices/tomorrow")
    public ResponseEntity<List<PriceEntry>> getTomorrowPrices(
            @RequestParam(value = "region", defaultValue = "3") String region) {
        
        log.info("Fetching tomorrow's prices for region: {}", region);
        
        List<PriceEntry> prices = powerpriceService.getTomorrowsPrices(region);
        
        if (prices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic())
                .body(prices);
    }

    @GetMapping("/funfacts")
    public ResponseEntity<FunFactsResponse> getFunFacts(
            @RequestParam(value = "region", defaultValue = "3") String region) {
        
        log.info("Calculating fun facts for region: {}", region);
        
        FunFactsResponse funFacts = powerpriceService.getFunFacts(region);
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES).cachePublic())
                .body(funFacts);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .body("OK");
    }
}
