package com.soa.second.soa_second.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.second.soa_second.dto.RouteDTO;
import com.soa.second.soa_second.service.NavigatorService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@RestController
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class NavigatorController {

    private NavigatorService navigatorService;

    @GET
    @Path("/route/{id-from}/{id-to}/shortest")
    public Response findShortest(@PathParam("id-from") Long idFrom, @PathParam("id-to") Long idTo) {
        try {
            return Response.ok(navigatorService.findShortest(idFrom, idTo)).build();
        } catch (FeignException e) {
            return e.status() == -1 ? Response.status(Response.Status.GATEWAY_TIMEOUT.getStatusCode())
                    .entity("Service unavailable")
                    .build() : Response.status(e.status()).entity(e.contentUTF8()).build();
        }
    }

    @POST
    @Path("/route/add/{id-from}/{id-to}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoute(@PathParam("id-from") Long idFrom, @PathParam("id-to") Long idTo) {
        try {
            return Response.ok(navigatorService.addRoute(idFrom, idTo)).build();
        } catch (FeignException e) {
            System.out.println("FeignException: " + e.getMessage());
            return e.status() == -1 ? Response.status(Response.Status.GATEWAY_TIMEOUT.getStatusCode())
                    .entity("Service unavailable")
                    .build() : Response.status(e.status()).entity(e.contentUTF8()).build();
        }
    }
}
