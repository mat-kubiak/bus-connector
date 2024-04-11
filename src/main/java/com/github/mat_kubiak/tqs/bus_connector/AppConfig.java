package com.github.mat_kubiak.tqs.bus_connector;

import com.github.mat_kubiak.tqs.bus_connector.service.ExchangeRateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ExchangeRateService exchangeRateService() {
        return new ExchangeRateService();
    }
}
