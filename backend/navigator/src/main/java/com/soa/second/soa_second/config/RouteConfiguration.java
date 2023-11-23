package com.soa.second.soa_second.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("routes")
@Getter
@Setter
public class RouteConfiguration {
    private String url;
}
