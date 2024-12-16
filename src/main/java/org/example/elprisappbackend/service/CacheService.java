package org.example.elprisappbackend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * *") //Rensar cachen vid midnatt varje dag
    public void evictAllCache() {
        cacheManager.getCache("elpris-app-backend").clear();
        System.out.println("Cache cleared at midnight");
    }
}
