package com.github.mat_kubiak.tqs.bus_connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableJpaRepositories
public class BusConnectorApplication {
	private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);

	public static void main(String[] args) {
		logger.info("Application started!");
		SpringApplication.run(BusConnectorApplication.class, args);
	}

}
