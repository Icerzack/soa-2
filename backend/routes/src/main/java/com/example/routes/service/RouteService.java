package com.example.routes.service;

import com.example.routes.converter.RouteConverter;
import com.example.routes.dto.QueryDTO;
import com.example.routes.dto.RouteDTO;
import com.example.routes.entity.LocationEntity;
import com.example.routes.entity.RouteEntity;
import com.example.routes.exception.EntityNotFoundException;
import com.example.routes.repository.LocationRepository;
import com.example.routes.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private static final double EPSILON = 0.001;
    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private final RouteConverter routeConverter;
    @Autowired
    private PageService pageService;

    public RouteService(RouteConverter routeConverter) {
        this.routeConverter = routeConverter;
    }

    public List<RouteEntity> filterRoutes(QueryDTO query, List<RouteEntity> routeEntities) {
        List<RouteDTO> routeDTOs = routeEntities.stream()
                .map(routeConverter::convertToDTO)
                .collect(Collectors.toList());

        return routeDTOs.stream()
                .filter(route -> checkConditions(route, query))
                .map(routeConverter::convertToEntity)
                .collect(Collectors.toList());
    }

    private boolean checkConditions(RouteDTO route, QueryDTO query) {
        if (query.getFilter() != null) {
            String regex = "^(id|name|creationDate|locationFromId|locationFromCoordinatesX|locationFromCoordinatesY|locationFromName|locationToId|locationToCoordinatesX|locationToCoordinatesY|locationToName|distance)(=|!=|>|<|>=|<=)([0-9a-zA-Zа-яА-Я\\s?!,.'Ёё]+)$";
            Pattern pattern = Pattern.compile(regex);

            return query.getFilter().stream()
                    .map(pattern::matcher)
                    .filter(Matcher::matches)
                    .allMatch(matcher -> {
                        String field = matcher.group(1);
                        String operator = matcher.group(2);
                        String value = matcher.group(3);

                        switch (operator) {
                            case "=":
                                return checkEqual(route, field, value);
                            case "!=":
                                return checkNotEqual(route, field, value);
                            case ">":
                                return checkGreaterThan(route, field, value);
                            case "<":
                                return checkLessThan(route, field, value);
                            case ">=":
                                return checkGreaterThanOrEqual(route, field, value);
                            case "<=":
                                return checkLessThanOrEqual(route, field, value);
                            default:
                                return true;
                        }
                    });
        }

        return true;
    }

    private boolean checkEqual(RouteDTO route, String field, String value) {
        switch (field) {
            case "id":
                return route.getId().equals(Long.valueOf(value));
            case "name":
                return route.getName().equals(value);
            case "creationDate":
                return route.getCreationDate().equals(value);
            case "locationFromId":
                return route.getFrom().getId().equals(Long.valueOf(value));
            case "locationFromCoordinatesX":
                return Math.abs(route.getFrom().getCoordinates().getX() - Float.parseFloat(value)) < EPSILON;
            case "locationFromCoordinatesY":
                return Math.abs(route.getFrom().getCoordinates().getY() - Float.parseFloat(value)) < EPSILON;
            case "locationFromName":
                return route.getFrom().getName().equals(value);
            case "locationToId":
                return route.getTo().getId().equals(Long.valueOf(value));
            case "locationToCoordinatesX":
                return Math.abs(route.getTo().getCoordinates().getX() - Float.parseFloat(value)) < EPSILON;
            case "locationToCoordinatesY":
                return Math.abs(route.getTo().getCoordinates().getY() - Float.parseFloat(value)) < EPSILON;
            case "locationToName":
                return route.getTo().getName().equals(value);
            case "distance":
                return Math.abs(route.getDistance() - Float.parseFloat(value)) < EPSILON;
            default:
                return false;
        }
    }

    private boolean checkNotEqual(RouteDTO route, String field, String value) {
        return !checkEqual(route, field, value);
    }

    private boolean checkGreaterThan(RouteDTO route, String field, String value) {
        switch (field) {
            case "id":
                return compareGreaterThan(route.getId(), Long.valueOf(value));
            case "name":
                return compareGreaterThan(route.getName(), value);
            case "creationDate":
                return compareGreaterThan(route.getCreationDate(), value);
            case "locationFromId":
                return compareGreaterThan(route.getFrom().getId(), Long.valueOf(value));
            case "locationFromCoordinatesX":
                return compareGreaterThan(route.getFrom().getCoordinates().getX(), Integer.parseInt(value));
            case "locationFromCoordinatesY":
                return compareGreaterThan(route.getFrom().getCoordinates().getY(), Float.parseFloat(value));
            case "locationFromName":
                return compareGreaterThan(route.getFrom().getName(), value);
            case "locationToId":
                return compareGreaterThan(route.getTo().getId(), Long.valueOf(value));
            case "locationToCoordinatesX":
                return compareGreaterThan(route.getTo().getCoordinates().getX(), Integer.parseInt(value));
            case "locationToCoordinatesY":
                return compareGreaterThan(route.getTo().getCoordinates().getY(), Float.parseFloat(value));
            case "locationToName":
                return compareGreaterThan(route.getTo().getName(), value);
            case "distance":
                return compareGreaterThan(route.getDistance(), Float.parseFloat(value));
            default:
                return false;
        }
    }

    private <T extends Comparable<T>> boolean compareGreaterThan(T fieldValue, T compareValue) {
        return fieldValue.compareTo(compareValue) > 0;
    }

    private boolean checkLessThan(RouteDTO route, String field, String value) {
        return !(checkGreaterThan(route, field, value) || checkEqual(route, field, value));
    }

    private boolean checkGreaterThanOrEqual(RouteDTO route, String field, String value) {
        return (checkGreaterThan(route, field, value) || checkEqual(route, field, value));
    }

    private boolean checkLessThanOrEqual(RouteDTO route, String field, String value) {
        return checkLessThan(route, field, value) || checkEqual(route, field, value);
    }

    public List<RouteEntity> getPaginatedRoutes(Integer page, Integer elementsCount) {
        Pageable pageable = pageService.createPageRequest(page, elementsCount);
        return routeRepository.findByPageAndElementsCount(pageable);
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getAllRoutes(@Valid QueryDTO dto) {
        FilterService.isValidRequestParams(dto);
        List<RouteEntity> allRoutes = getPaginatedRoutes(dto.getPage(), dto.getElementsCount());
        List<RouteEntity> allFilteredRoutes = filterRoutes(dto, allRoutes);

        Sort.Direction sortDirection = dto.getSortDirection() != null ? dto.getSortDirection() : Sort.Direction.ASC;

        String sortField = dto.getSort() != null && !dto.getSort().isEmpty() ? dto.getSort() : "id";

        List<RouteEntity> sortedRoutes = SortService.sortElements(allFilteredRoutes, sortField, sortDirection);

        List<RouteDTO> allRoutesResponse = new ArrayList<>();
        for (RouteEntity routeEntity : sortedRoutes) {
            allRoutesResponse.add(routeConverter.convertToDTO(routeEntity));
        }

        return allRoutesResponse;
    }

    @Transactional
    public RouteDTO addNewRoute(RouteDTO dto) {
        RouteEntity entity = routeConverter.convertToEntity(dto);
        LocationEntity newLocationFromEntity = entity.getLocationFrom();
        LocationEntity newLocationToEntity = entity.getLocationTo();
        Optional<LocationEntity> locationFromEntity = locationRepository.findLocationEntityByCoordinates(newLocationFromEntity.getCoordinates().getX(), newLocationFromEntity.getCoordinates().getY());
        Optional<LocationEntity> locationToEntity = locationRepository.findLocationEntityByCoordinates(newLocationToEntity.getCoordinates().getX(), newLocationToEntity.getCoordinates().getY());
        if (!locationFromEntity.isPresent()) {
            saveToLocationRepository(newLocationFromEntity);
        } else {
            entity.getLocationFrom().setId(locationFromEntity.get().getId());
        }
        if (!locationToEntity.isPresent()) {
            saveToLocationRepository(newLocationToEntity);
        } else {
            entity.getLocationTo().setId(locationToEntity.get().getId());
        }
        routeRepository.save(entity);
        return routeConverter.convertToDTO(entity);
    }

    @Transactional(readOnly = true)
    public RouteDTO getRouteById(Long id) {
        Optional<RouteEntity> entity = routeRepository.findRouteEntityById(id);
        if (entity.isPresent()) {
            return routeConverter.convertToDTO(entity.get());
        } else {
            throw new EntityNotFoundException(String.format("Can't find route with id=%d", id));
        }
    }

    @Transactional
    public RouteDTO updateRouteById(Long id, RouteDTO dto) {
        Optional<RouteEntity> entity = routeRepository.findRouteEntityById(id);
        if (entity.isPresent()) {
            RouteEntity newEntity = routeConverter.convertToEntity(dto);
            Optional<LocationEntity> locationFromEntity = locationRepository.findLocationEntityByCoordinates(newEntity.getLocationFrom().getCoordinates().getX(), newEntity.getLocationFrom().getCoordinates().getY());
            Optional<LocationEntity> locationToEntity = locationRepository.findLocationEntityByCoordinates(newEntity.getLocationTo().getCoordinates().getX(), newEntity.getLocationTo().getCoordinates().getY());
            if (!locationFromEntity.isPresent()) {
                saveToLocationRepository(newEntity.getLocationFrom());
            } else {
                newEntity.getLocationFrom().setId(locationFromEntity.get().getId());
            }
            if (!locationToEntity.isPresent()) {
                saveToLocationRepository(newEntity.getLocationTo());
            } else {
                newEntity.getLocationTo().setId(locationToEntity.get().getId());
            }
            newEntity.setId(id);
            newEntity = saveToRouteRepository(newEntity);
            return routeConverter.convertToDTO(newEntity);
        } else {
            throw new EntityNotFoundException(String.format("Can't find route with id=%d", id));
        }
    }

    @Transactional
    public void deleteRouteById(Long id) {
        Optional<RouteEntity> entity = routeRepository.findRouteEntityById(id);
        entity.ifPresent(routeEntity -> routeRepository.delete(routeEntity));
    }

    @Transactional
    public void deleteRoutesByDistance(float distance) {
        routeRepository.deleteRoutesByDistance(distance);
    }

    @Transactional(readOnly = true)
    public Float getSumAllDistances() {
        List<RouteEntity> allRoutes = routeRepository.findAllRoutesEntity();
        float sumDistances = 0;
        for (RouteEntity routeEntity: allRoutes) {
            sumDistances += routeEntity.getDistance();
        }
        return sumDistances;
    }

    @Transactional(readOnly = true)
    public int getCountRoutesWithGreaterDistance(float distance) {
        List<RouteEntity> allRoutes = routeRepository.findAllRoutesEntity();
        int countRoutesWithGreaterDistance = 0;
        for (RouteEntity routeEntity: allRoutes) {
            if (routeEntity.getDistance() > distance) {
                countRoutesWithGreaterDistance++;
            }
        }
        return countRoutesWithGreaterDistance;
    }

    @Transactional
    public RouteEntity saveToRouteRepository(RouteEntity routeEntity) {
        return routeRepository.save(routeEntity);
    }

    @Transactional
    public LocationEntity saveToLocationRepository(LocationEntity locationEntity) {
        return locationRepository.save(locationEntity);
    }
}
