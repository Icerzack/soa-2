package com.example.routes.controller;

import com.example.routes.dto.QueryDTO;
import com.example.routes.dto.RouteDTO;
import com.example.routes.dto.RoutesWithPagingDTO;
import com.example.routes.dto.SpecialOfferQueryDTO;
import com.example.routes.service.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Api(tags = "Route", description = "Эндпоинты для взаимодействия с route")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @GetMapping("/api/v1/routes")
    @ApiOperation(value = "Получить список из элементов Route")
    public ResponseEntity<RoutesWithPagingDTO> getAllRoutes(@Valid QueryDTO dto) {
        return ResponseEntity.status(200).body(routeService.getAllRoutes(dto));
    }

    @PostMapping("/api/v1/routes")
    @ApiOperation(value = "Добавить новый Route")
    public ResponseEntity<RouteDTO> addNewRoute(@RequestBody RouteDTO dto) {
        return ResponseEntity.status(200).body(routeService.addNewRoute(dto));
    }

    @GetMapping("/api/v1/routes/{id}")
    @ApiOperation(value = "Получить Route по id")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable("id") Long id) {
        return ResponseEntity.status(200).body(routeService.getRouteById(id));
    }

    @PutMapping("/api/v1/routes/{id}")
    @ApiOperation(value = "Обновить Route по id")
    public ResponseEntity<RouteDTO> updateRouteById(@PathVariable("id") Long id, @RequestBody RouteDTO dto) {
        return ResponseEntity.status(200).body(routeService.updateRouteById(id, dto));
    }

    @DeleteMapping("/api/v1/routes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Удалить Route по id")
    public void deleteRouteById(@PathVariable("id") Long id) {
        routeService.deleteRouteById(id);
    }

    @DeleteMapping("/api/v1/routes/distances/{distance}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Удалить все Routes, значение поля distance которых равно заданному")
    public void deleteRoutesByDistance(@PathVariable("distance") Float distance) {
        routeService.deleteRoutesByDistance(distance);
    }

    @GetMapping("/api/v1/routes/distances/sum")
    @ApiOperation(value = "Рассчитать сумму всех distance для всех Route")
    public ResponseEntity<Float> getSumAllDistances() {
        return ResponseEntity.status(200).body(routeService.getSumAllDistances());
    }

    @GetMapping("/api/v1/routes/distances/{distance}/count/greater")
    @ApiOperation(value = "Вернуть количество объектов, значение поля distance которых больше заданного")
    public ResponseEntity<Integer> getCountRoutesWithGreaterDistance(@PathVariable("distance") float distance) {
        return ResponseEntity.status(200).body(routeService.getCountRoutesWithGreaterDistance(distance));
    }

    @GetMapping("/api/v1/routes/special-offer")
    @ApiOperation(value = "Вернуть выгодный билет для заданного маршрута")
    public ResponseEntity<Map<String, Object>> getSpecialOffers(SpecialOfferQueryDTO dto) {
        return ResponseEntity.status(200).body(routeService.getSpecialOffers(dto));
    }
}
