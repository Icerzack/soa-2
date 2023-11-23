package com.soa.second.soa_second.feign;

import com.soa.second.soa_second.dto.RouteDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface RouteClient {
    @RequestMapping(
            value = "/routes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<RouteDTO> getRoutes();

    @RequestMapping(
            value = "/routes",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<RouteDTO> addRoute(RouteDTO route);
}
