package org.example.elprisappbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.elprisappbackend.model.FunFactsResponse;
import org.example.elprisappbackend.model.PriceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PowerpriceService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    // Energy consumption constants (kWh)
    private static final double EV_KWH = 60.0;
    private static final double HEAT_PUMP_KWH = 20.0;
    private static final double SHOWER_KWH = 8.0 * (10.0/60.0);
    private static final double WASHER_KWH = 1.0;
    private static final double DRYER_KWH = 2.5;
    private static final double DISHWASHER_KWH = 1.5;
    
    @Autowired
    public PowerpriceService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Generate API URL for today's prices
     */
    private String generateApiUrl() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));
        return "https://www.elprisetjustnu.se/api/v1/prices/" + formattedDate;
    }

    /**
     * Generate API URL for tomorrow's prices
     */
    private String generateApiUrlForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String formattedDate = tomorrow.format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));
        return "https://www.elprisetjustnu.se/api/v1/prices/" + formattedDate;
    }

    @Cacheable(value = "elpris-app-backend", key = "#region")
    public List<PriceEntry> getTodaysPrices(String region) {
        log.info("Fetching today's data from external API for region: {}", region);
        try {
            String apiUrl = generateApiUrl() + "_SE" + region + ".json";
            
            String response = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            return objectMapper.readValue(response, new TypeReference<List<PriceEntry>>() {});
        } catch (Exception e) {
            log.error("Error fetching today's prices for region {}: {}", region, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Cacheable(value = "elpris-app-backend-tomorrow", key = "#region")
    public List<PriceEntry> getTomorrowsPrices(String region) {
        log.info("Fetching tomorrow's data from external API for region: {}", region);
        try {
            String apiUrl = generateApiUrlForTomorrow() + "_SE" + region + ".json";
            
            String response = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            return objectMapper.readValue(response, new TypeReference<List<PriceEntry>>() {});
        } catch (Exception e) {
            log.error("Error fetching tomorrow's prices for region {}: {}", region, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Cacheable(value = "fun-facts-cache", key = "#region")
    public FunFactsResponse getFunFacts(String region) {
        log.info("Calculating fun facts for region: {}", region);
        
        List<PriceEntry> prices = getTodaysPrices(region);
        if (prices.isEmpty()) {
            return new FunFactsResponse();
        }

        double avgPrice = prices.stream()
                .filter(Objects::nonNull)
                .mapToDouble(PriceEntry::getSekPerKwh)
                .filter(price -> !Double.isNaN(price))
                .average()
                .orElse(0.0);

        return new FunFactsResponse(
                roundToTwoDecimals(EV_KWH * avgPrice),
                roundToTwoDecimals(HEAT_PUMP_KWH * avgPrice),
                roundToTwoDecimals(SHOWER_KWH * avgPrice),
                roundToTwoDecimals(WASHER_KWH * avgPrice),
                roundToTwoDecimals(DRYER_KWH * avgPrice),
                roundToTwoDecimals(DISHWASHER_KWH * avgPrice)
        );
    }

    /**
     * Async method to fetch data for a single region
     */
    @Async
    public CompletableFuture<Void> fetchPricesAsync(String region, boolean tomorrow) {
        try {
            if (tomorrow) {
                getTomorrowsPrices(region);
            } else {
                getTodaysPrices(region);
            }
            log.info("Successfully fetched {} prices for region {}", 
                    tomorrow ? "tomorrow's" : "today's", region);
        } catch (Exception e) {
            log.error("Failed to fetch {} prices for region {}: {}", 
                    tomorrow ? "tomorrow's" : "today's", region, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Scheduled task to prefetch today's prices for all regions
     */
    @Scheduled(cron = "0 0 15 * * *") // Run at 15:00 every day
    public void fetchDailyPricesForAllRegions() {
        log.info("Starting scheduled fetch of daily prices for all regions");
        
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int region = 1; region <= 4; region++) {
            String regionString = String.valueOf(region);
            futures.add(fetchPricesAsync(regionString, false));
        }
        
        // Wait for all async operations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> log.info("Completed fetching daily prices for all regions"));
    }

    /**
     * Scheduled task to prefetch tomorrow's prices for all regions
     */
    @Scheduled(cron = "0 0 13 * * *") // Run at 13:00 every day
    public void fetchTomorrowsPricesForAllRegions() {
        log.info("Starting scheduled fetch of tomorrow's prices for all regions");
        
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int region = 1; region <= 4; region++) {
            String regionString = String.valueOf(region);
            futures.add(fetchPricesAsync(regionString, true));
        }
        
        // Wait for all async operations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> log.info("Completed fetching tomorrow's prices for all regions"));
    }

    /**
     * Utility method to round to two decimal places
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
