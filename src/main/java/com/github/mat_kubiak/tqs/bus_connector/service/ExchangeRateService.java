package com.github.mat_kubiak.tqs.bus_connector.service;

import com.github.mat_kubiak.tqs.bus_connector.data.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    @Value("${exchange.rate.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("exchangeRates")
    public ExchangeRateResponse getExchangeRates() {
        return restTemplate.getForObject(apiUrl, ExchangeRateResponse.class);
    }
}
