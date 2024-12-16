package org.example.elprisappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

// TODO
// Berabeta datan innan den skickas till fronten

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class ElprisAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElprisAppBackendApplication.class, args);
    }

}
