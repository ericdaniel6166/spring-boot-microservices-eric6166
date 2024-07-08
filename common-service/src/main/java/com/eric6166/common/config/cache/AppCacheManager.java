package com.eric6166.common.config.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.cache.enabled", havingValue = "true")
@EnableCaching
public class AppCacheManager {

    private final CacheManager cacheManager;

    public Cache.ValueWrapper getCache(String cacheName, Object cacheKey) {
        log.info("get cache, cacheName: {}, cacheKey: {}", cacheName, cacheKey);
        return cacheManager.getCache(cacheName).get(cacheKey);
    }

    public Cache getCache(String cacheName) {
        log.info("getCache, cacheName: {}", cacheName);
        return cacheManager.getCache(cacheName);
    }

    public Collection<String> getCacheNames() {
        log.info("getCacheNames");
        return cacheManager.getCacheNames();
    }

    public void evict(String cacheName, Object cacheKey) {
        log.info("evict cache, cacheName: {}, cacheKey: {}", cacheName, cacheKey);
        cacheManager.getCache(cacheName).evict(cacheKey);
    }

    public void clear(String cacheName) {
        log.info("clear cache, cacheName: {}", cacheName);
        cacheManager.getCache(cacheName).clear();
    }

    public void clear() {
        log.info("clear cache");
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

}
