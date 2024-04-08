package com.github.mat_kubiak.tqs.bus_connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class BusConnectorApplication {
	private static final Logger logger = LoggerFactory.getLogger(BusConnectorApplication.class);

	public static void main(String[] args) {
		logger.info("Application started!");
		SpringApplication.run(BusConnectorApplication.class, args);
	}

}
