package com.soa.second.soa_second.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.second.soa_second.feign.RouteClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FeignClientsConfiguration {
    private final ObjectMapper objectMapper;
    private final RouteConfiguration routeConfiguration;

    @Bean
    public RouteClient getRouteClient() {
        return getBuilderWithCoders().target(RouteClient.class, routeConfiguration.getUrl());
    }

    private Feign.Builder getBuilderWithCoders() {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper));
    }
}
