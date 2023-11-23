package com.soa.second.soa_second.controller;

import com.soa.second.soa_second.service.NavigatorService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response addRoute(@PathParam("id-from") Long idFrom, @PathParam("id-to") Long idTo) {
        try {
            return Response.ok(navigatorService.addRoute(idFrom, idTo)).build();
        } catch (FeignException e) {
            return e.status() == -1 ? Response.status(Response.Status.GATEWAY_TIMEOUT.getStatusCode())
                    .entity("Service unavailable")
                    .build() : Response.status(e.status()).entity(e.contentUTF8()).build();
        }
    }
}
