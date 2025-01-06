package org.example.elprisappbackend.service;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PowerpriceService {

    /*
    OBS!
    https://www.elprisetjustnu.se/elpris-api
    GET https://www.elprisetjustnu.se/api/v1/prices/[ÅR]/[MÅNAD]-[DAG]_[PRISKLASS].json
    Historisk data kan endast hämtas till och med 1. november 2022.
    Alla priser är utan moms, tillägg och skatter.
    Morgondagens elpris anländer tidigast kl 13 dagen innan.
    */

    @Autowired
    private ApplicationContext context;

    //Skapa en dynamisk URL med dagen datum
    public String generateApiUrl() {
        LocalDate today = LocalDate.now();

        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));

        return "https://www.elprisetjustnu.se/api/v1/prices/" + formattedDate;
    }

    //Skapar en dynamisk URL för morgondagens priser
    public String generateApiUrlForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        String formattedDate = tomorrow.format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));
        return "https://www.elprisetjustnu.se/api/v1/prices/" + formattedDate;
    }

    @Cacheable("elpris-app-backend")
    public String getTodaysPrices(String region) {
        System.out.println("Fetching today data from external API...");
        try {
            String apiUrl = generateApiUrl() + "_SE" + region + ".json";
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.getForObject(apiUrl, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred";
        }
    }

    //tomorrows prices will be avaliable at 13.00
    @Cacheable("elpris-app-backend-tomorrow")
    public String getTomorrowsPrices(String region) {
        System.out.println("Fetching tomorrows data from external API...");
        try {
            String apiUrl = generateApiUrlForTomorrow() + "_SE" + region + ".json";
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.getForObject(apiUrl, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred";
        }
    }

    //Metod som ska hämta dagliga priserna automatiskt varje dag
    @Scheduled(cron = "0 0 15 * * *") //Kör schemlagt vid 15 varje dag
    public void fetchDailyPrices() {
        PowerpriceService proxy = context.getBean(PowerpriceService.class);

        for(int region = 1; region <= 4; region++) { //hämtar från alla 4 regions
            String regionString = String.valueOf(region);
            proxy.getTodaysPrices(regionString);
        }
    }

    @Scheduled(cron = "0 0 13 * * *") //Körs 13:00 varje dag
    public void fetchTomorrowsPrices() {
        PowerpriceService proxy = context.getBean(PowerpriceService.class);

        for(int region = 1; region <= 4; region++) { //hämtar från alla 4 regions
            String regionString = String.valueOf(region);
            proxy.getTomorrowsPrices(regionString);
        }
    }
}
