package org.example.elprisappbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(cron = "0 50 14 * * *") // Clear cache at 14:50 every day
    public void evictTodaysCache() {
        try {
            Cache cache = cacheManager.getCache("elpris-app-backend");
            if (cache != null) {
                cache.clear();
                log.info("Successfully cleared today's price cache at 14:50");
            } else {
                log.warn("Today's price cache not found during scheduled eviction");
            }
        } catch (Exception e) {
            log.error("Error clearing today's price cache: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 50 12 * * *") // Clear tomorrow's cache at 12:50 every day
    public void evictTomorrowsCache() {
        try {
            Cache cache = cacheManager.getCache("elpris-app-backend-tomorrow");
            if (cache != null) {
                cache.clear();
                log.info("Successfully cleared tomorrow's price cache at 12:50");
            } else {
                log.warn("Tomorrow's price cache not found during scheduled eviction");
            }
        } catch (Exception e) {
            log.error("Error clearing tomorrow's price cache: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 */6 * * *") // Clear fun facts cache every 6 hours
    public void evictFunFactsCache() {
        try {
            Cache cache = cacheManager.getCache("fun-facts-cache");
            if (cache != null) {
                cache.clear();
                log.info("Successfully cleared fun facts cache");
            } else {
                log.warn("Fun facts cache not found during scheduled eviction");
            }
        } catch (Exception e) {
            log.error("Error clearing fun facts cache: {}", e.getMessage(), e);
        }
    }

    /**
     * Manually evict all caches - useful for maintenance or debugging
     */
    public void evictAllCaches() {
        try {
            cacheManager.getCacheNames().forEach(cacheName -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                    log.info("Manually cleared cache: {}", cacheName);
                }
            });
            log.info("All caches have been manually cleared");
        } catch (Exception e) {
            log.error("Error during manual cache eviction: {}", e.getMessage(), e);
        }
    }

    /**
     * Get cache statistics for monitoring
     */
    public void logCacheStatistics() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                log.info("Cache '{}' is active", cacheName);
            }
        });
    }
}
