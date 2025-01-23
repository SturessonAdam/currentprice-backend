package org.example.elprisappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

// TODO
// Ta fram de 5 bästa timmarna på dygnet?
// Historisk data
// Bättre felhantering?
// Tester?

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CurrentPriceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentPriceBackendApplication.class, args);
    }

}
