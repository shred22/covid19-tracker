package com.covid19.tracker.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "endpoint.covid")
@Getter
@Setter
@NoArgsConstructor
public class EndpointConfig {

    private String confirmedCases;

}
