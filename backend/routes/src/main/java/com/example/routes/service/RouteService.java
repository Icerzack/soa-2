package com.example.routes.service;

import com.example.routes.converter.RouteConverter;
import com.example.routes.dto.QueryDTO;
import com.example.routes.dto.RouteDTO;
import com.example.routes.entity.LocationEntity;
import com.example.routes.entity.RouteEntity;
import com.example.routes.exception.EntityNotFoundException;
import com.example.routes.repository.LocationRepository;
import com.example.routes.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.UserTransaction;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private final RouteConverter routeConverter;

    public RouteService(RouteConverter routeConverter) {
        this.routeConverter = routeConverter;
    }

//    @Transactional(readOnly = true)
//    public List<RouteDTO> getAllRoutes(@Valid QueryDTO dto) {
//        FilterService.isValidRequestParams(dto);
////        PageRequest request = PageService.getPageRequest(dto.getLimit(), dto.getOffset(), dto.getSortAsc(), dto.getSort());
//
//        List<RouteEntity> allRoutes = routeRepository.findAllRoutesEntity();
//        List<RouteDTO> allRoutesResponse = new ArrayList<>();
//        for (RouteEntity routeEntity: allRoutes) {
//            allRoutesResponse.add(routeConverter.convertToDTO(routeEntity));
//        }
//        return allRoutesResponse;
//    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getAllRoutes(@Valid QueryDTO dto) {
        FilterService.isValidRequestParams(dto);

        List<RouteEntity> allRoutes = routeRepository.findAllRoutesEntity();

        Sort.Direction sortDirection = dto.getSortDirection() != null ? dto.getSortDirection() : Sort.Direction.ASC;

        String sortField = dto.getSort() != null && !dto.getSort().isEmpty() ? dto.getSort() : "id";

        List<RouteEntity> sortedRoutes = SortService.sortElements(allRoutes, sortField, sortDirection);

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
        if (entity.isPresent()) {
            routeRepository.delete(entity.get());
        }
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
