package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.BusConnectorApplication;
import com.github.mat_kubiak.tqs.bus_connector.data.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);

    @Value("${exchange.rate.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(cacheNames = "exchangeRates")
    public ExchangeRateResponse getExchangeRates() {
        return restTemplate.getForObject(apiUrl, ExchangeRateResponse.class);
    }

    @Scheduled(fixedRate = 100000)
    @CacheEvict(cacheNames = "exchangeRates", allEntries = true)
    public void emptyCurrenciesCache() {
        logger.info("Clearing exchange rate cache");
    }
}
