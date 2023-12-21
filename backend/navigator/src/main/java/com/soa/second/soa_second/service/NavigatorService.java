package com.soa.second.soa_second.service;

import com.soa.second.soa_second.dto.CoordinateDTO;
import com.soa.second.soa_second.dto.GetRoutesResponseDTO;
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
        GetRoutesResponseDTO r = routeClient.getRoutes();
        List<RouteDTO> routes = r.getRoutes();
        int numNodes = routes.size();
        List<List<Dijkstra.Edge>> graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            graph.add(new ArrayList<>());
        }

        for (RouteDTO rd : routes) {
            graph
                    .get(Math.toIntExact(rd.getFrom().getId()))
                    .add(new Dijkstra.Edge(rd.getTo().getId(), rd.getDistance()));
        }

        return Dijkstra.shortestPath(graph, Math.toIntExact(idFrom), Math.toIntExact(idTo));
    }

    public ResponseEntity<RouteDTO> addRoute(Long idFrom, Long idTo) {
        RouteDTO newRoute = new RouteDTO();
        newRoute.setName("Дорога от "+idFrom+" в "+idTo);
        newRoute.setFrom(new LocationDTO()
                .setName("Локация_№"+idFrom)
                .setId(idFrom)
                .setCoordinates(new CoordinateDTO()
                        .setX(1)
                        .setY(1))
        );
        newRoute.setTo(new LocationDTO()
                .setName("Локация_№"+idTo)
                .setId(idTo)
                .setCoordinates(new CoordinateDTO()
                        .setX(2)
                        .setY(2))
        );
        newRoute.setDistance((float) (Math.random() * 100));
        return routeClient.addRoute(newRoute);
    }
}
