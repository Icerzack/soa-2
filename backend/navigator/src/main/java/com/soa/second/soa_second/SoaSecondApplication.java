package com.soa.second.soa_second;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableFeignClients
@RestController
public class SoaSecondApplication {
    public static void main(String[] args) {
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        System.out.println(System.getProperty("javax.net.ssl.keyStore"));
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                    (hostname, sslSession) -> hostname.equals("localhost"));

        SpringApplication.run(SoaSecondApplication.class, args);
    }

    @GetMapping("/")
    public String allWork() {
        return "All works";
    }
}
