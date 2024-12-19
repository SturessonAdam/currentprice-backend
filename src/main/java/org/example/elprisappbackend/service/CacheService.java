package org.example.elprisappbackend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 50 14 * * *") //Rensar cachen kl 14.50 varje dag
    public void evictAllCache() {
        cacheManager.getCache("elpris-app-backend").clear();
        System.out.println("Cache cleared at midnight");
    }

    @Scheduled(cron = "0 50 12 * * *") //Rensar cachen 12:50 varje dag
    public void evictTomorrowsCache() {
        cacheManager.getCache("elpris-app-backend-tomorrow").clear();
        System.out.println("Cache with tomorrows prices cleared");
    }
}
