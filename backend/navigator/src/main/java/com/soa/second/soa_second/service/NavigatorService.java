package com.soa.second.soa_second.service;

import com.soa.second.soa_second.dto.CoordinateDTO;
import com.soa.second.soa_second.dto.LocationDTO;
import com.soa.second.soa_second.dto.RouteDTO;
import com.soa.second.soa_second.feign.RouteClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class NavigatorService {
    private RouteClient routeClient;

    public Float findShortest(Long idFrom, Long idTo) {
        List<RouteDTO> routes = routeClient.getRoutes();

        int numNodes = routes.size();
        List<List<Dijkstra.Edge>> graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            graph.add(new ArrayList<>());
        }

        for (RouteDTO r : routes) {
            graph
                    .get(Math.toIntExact(r.getFrom().getId()))
                    .add(new Dijkstra.Edge(r.getTo().getId(), r.getDistance()));
        }

        return Dijkstra.shortestPath(graph, Math.toIntExact(idFrom), Math.toIntExact(idTo));
    }

    public ResponseEntity<RouteDTO> addRoute(Long idFrom, Long idTo) {
        RouteDTO newRoute = new RouteDTO();
        newRoute.setName("Нонейм дорога");
        newRoute.setFrom(new LocationDTO()
                .setName("Локация_4")
                .setId(idFrom)
                .setCoordinates(new CoordinateDTO()
                        .setX(88)
                        .setY(90))
        );
        newRoute.setTo(new LocationDTO()
                .setName("Локация_5")
                .setId(idTo)
                .setCoordinates(new CoordinateDTO()
                        .setX(100)
                        .setY(909))
        );
        newRoute.setDistance((float) (Math.random() * 100));
        return routeClient.addRoute(newRoute);
    }
}
