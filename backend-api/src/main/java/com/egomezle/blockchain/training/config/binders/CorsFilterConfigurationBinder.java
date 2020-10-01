package com.egomezle.blockchain.training.config.binders;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.config")
public class CorsFilterConfigurationBinder {
    private String allowedOrigin;

    private String allowedHeader;

    private String allowedMethod;
}
