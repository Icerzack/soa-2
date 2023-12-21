package com.soa.second.soa_second.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.second.soa_second.dto.ErrorDefaultDTO;
import com.soa.second.soa_second.dto.RouteDTO;
import com.soa.second.soa_second.exception.FirstServiceUnavailableException;
import com.soa.second.soa_second.exception.NotValidParamsException;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class NavigatorController {

    private NavigatorService navigatorService;

    @GET
    @Path("/route/{id-from}/{id-to}/shortest")
    public Response findShortest(@PathParam("id-from") String idFrom, @PathParam("id-to") String idTo) {
        try {
            Long parsedIdFrom = Long.parseLong(idFrom);
            Long parsedIdTo = Long.parseLong(idTo);
            if (parsedIdFrom < 1 || parsedIdTo < 1) {
                throw new NotValidParamsException("невалидные входные данные: ");
            }
            float distance = navigatorService.findShortest(parsedIdFrom, parsedIdTo);
            if (distance == -1f){
                throw new FirstServiceUnavailableException("Недоступен первый сервис: ");
            }
            return Response.ok().build();
        } catch (FeignException e) {
            return e.status() == -1 ? Response.status(Response.Status.GATEWAY_TIMEOUT.getStatusCode())
                    .entity("Service unavailable")
                    .build() : Response.status(e.status()).entity(e.contentUTF8()).build();
        } catch (NumberFormatException e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(404);
            errorDefaultDTO.setMessage("id should be Long Number");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        } catch (NotValidParamsException e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(404);
            errorDefaultDTO.setMessage("id must be positive");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        } catch (FirstServiceUnavailableException e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(500);
            errorDefaultDTO.setMessage("проверьте работоспособность первого сервиса");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        } catch (Exception e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(500);
            errorDefaultDTO.setMessage("Непредвиденная ошибка");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        }
    }

    @POST
    @Path("/route/add/{id-from}/{id-to}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoute(@PathParam("id-from") String idFrom, @PathParam("id-to") String idTo) {
        try {
            Long parsedIdFrom = Long.parseLong(idFrom);
            Long parsedIdTo = Long.parseLong(idTo);
            if (parsedIdFrom < 1 || parsedIdTo < 1) {
                throw new NotValidParamsException("невалидные входные данные: ");
            }
            RouteDTO dto = navigatorService.addRoute(parsedIdFrom, parsedIdTo);
            if (dto == null) {
                throw new FirstServiceUnavailableException("Недоступен первый сервис: ");
            }
            return Response.ok().build();
        } catch (FeignException e) {
            return e.status() == -1 ? Response.status(Response.Status.GATEWAY_TIMEOUT.getStatusCode())
                    .entity("Service unavailable")
                    .build() : Response.status(e.status()).entity(e.contentUTF8()).build();
        } catch (NumberFormatException e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(404);
            errorDefaultDTO.setMessage("id should be Long Number");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        } catch (NotValidParamsException e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(404);
            errorDefaultDTO.setMessage("id must be positive");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        } catch (FirstServiceUnavailableException e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(500);
            errorDefaultDTO.setMessage("проверьте работоспособность первого сервиса");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        } catch (Exception e) {
            System.out.println(e.getClass().getCanonicalName());
            ErrorDefaultDTO errorDefaultDTO = new ErrorDefaultDTO();
            errorDefaultDTO.setCode(500);
            errorDefaultDTO.setMessage("Непредвиденная ошибка");
            ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
            String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            errorDefaultDTO.setTime(formattedCurrentDateTime);
            return Response.status(errorDefaultDTO.getCode()).entity(errorDefaultDTO).build();
        }
    }
}
