package com.saus.bgt.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("bgt")
public class BgtServiceConfiguration {
    private BggConfiguration bgg;

    @Getter
    @Setter
    public static class BggConfiguration {
        private String baseUrl;
    }
}

