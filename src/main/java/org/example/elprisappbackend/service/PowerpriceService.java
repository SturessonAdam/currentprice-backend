package org.example.elprisappbackend.service;
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

    //Skapa en dynamisk URL med dagen datum
    public String generateApiUrl() {
        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));

        //return "https://www.elprisetjustnu.se/api/v1/prices/" + year + "/" + String.format("%02d", month) + "/" + String.format("%02d", day) + "_SE3.json";
        return "https://www.elprisetjustnu.se/api/v1/prices/" + formattedDate;
    }

    public String getTodaysPrices(String region) {
        try {
            String apiUrl = generateApiUrl() + "_SE" + region + ".json";
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred";
        }
    }

    //Metod som ska hämta dagliga priserna automatiskt varje dag
    @Scheduled(cron = "0 0 15 * * *") //Kör schemlagt vid 15 varje dag
    public void fetchDailyPrices() {
        String region = "3";
        String prices = getTodaysPrices(region);
        System.out.println(prices);

        //Ska sparas till cache
    }
}
