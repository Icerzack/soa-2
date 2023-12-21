package com.soa.second.soa_second.service;

import com.soa.second.soa_second.dto.CoordinateDTO;
import com.soa.second.soa_second.dto.GetRoutesResponseDTO;
import com.soa.second.soa_second.dto.LocationDTO;
import com.soa.second.soa_second.dto.RouteDTO;
import com.soa.second.soa_second.feign.RouteClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class NavigatorService {
    @Inject
    private RouteClient routeClient;

    public Float findShortest(Long idFrom, Long idTo) {
        try {
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
        } catch (Exception e) {
            return -1f;
        }
    }

    public RouteDTO addRoute(Long idFrom, Long idTo) {
        try {
            GetRoutesResponseDTO r = routeClient.getRoutes();
            List<RouteDTO> routes = r.getRoutes();
            LocationDTO locationFrom = null;
            LocationDTO locationTo = null;
            for (RouteDTO routeDTO : routes) {
                if (locationFrom != null && locationTo != null) {
                    break;
                }
                if (routeDTO.getFrom().getId() == idFrom) {
                    locationFrom = routeDTO.getFrom();
                }
                if (routeDTO.getTo().getId() == idTo) {
                    locationTo = routeDTO.getTo();
                }
            }
            RouteDTO newRoute = new RouteDTO();
            newRoute.setName("Route from " + locationFrom.getName() + " to " + locationTo.getName());
            newRoute.setFrom(locationFrom);
            newRoute.setTo(locationTo);
            newRoute.setDistance((float) calculateDistance(
                    locationFrom.getCoordinates().getX(),
                    locationFrom.getCoordinates().getY(),
                    locationTo.getCoordinates().getX(),
                    locationTo.getCoordinates().getY()));
            return routeClient.addRoute(newRoute);
        } catch (Exception e) {
            return null;
        }
    }

    public static double calculateDistance(int x1, Float y1, int x2, Float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
