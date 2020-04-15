package com.covid19.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.covid19.tracker")
@ConfigurationPropertiesScan(basePackages = "com.covid19.tracker.config")
public class Covid19TrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19TrackerApplication.class, args);
	}

}
