package org.example.elprisappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// TODO
// Cacha elrpis data
// Köra API anrop på schema basis för att hålla elrprisenra uppdaterade
// Berabeta datan innan den skickas till fronten

@SpringBootApplication
@EnableScheduling
public class ElprisAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElprisAppBackendApplication.class, args);
    }

}
