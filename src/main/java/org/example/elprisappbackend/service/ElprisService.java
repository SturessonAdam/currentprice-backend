package org.example.elprisappbackend.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElprisService {

    /*
    OBS!
    https://www.elprisetjustnu.se/elpris-api
    GET https://www.elprisetjustnu.se/api/v1/prices/[ÅR]/[MÅNAD]-[DAG]_[PRISKLASS].json
    Historisk data kan endast hämtas till och med 1. november 2022.
    Alla priser är utan moms, tillägg och skatter.
    Morgondagens elpris anländer tidigast kl 13 dagen innan.
    */

    public String getElpriser() {
        try {
            String apiUrl = "https://www.elprisetjustnu.se/api/v1/prices/2024/12-12_SE3.json";
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred";
        }
    }
}
