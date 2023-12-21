package com.soa.second.soa_second;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableFeignClients
public class SoaSecondApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoaSecondApplication.class, args);
    }

}
