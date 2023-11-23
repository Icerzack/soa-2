package com.soa.second.soa_second.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {
        packages("com.soa.second.soa_second.controller");
        register(CorsResponseFilter.class);
    }
}
