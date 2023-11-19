package com.example.routes.service;

import com.example.routes.entity.RouteEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SortService {
    private static final Pattern SORT_FIELD_PATTERN = Pattern.compile("^<{0,1}(id|name|creationDate|locationFrom\\.id|locationFrom\\.coordinates\\.x|locationFrom\\.coordinates\\.y|locationFrom\\.name|locationTo\\.id|locationTo\\.coordinates\\.x|locationTo\\.coordinates\\.y|locationTo\\.name|distance)$");

    public static Sort.Direction getSortDirection(String sortField) {
        Matcher matcher = SORT_FIELD_PATTERN.matcher(sortField);
        if (matcher.matches()) {
            return sortField.startsWith("<") ? Sort.Direction.DESC : Sort.Direction.ASC;
        } else {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
    }

    public static List<RouteEntity> sortElements(List<RouteEntity> allRoutes, String sortField, Sort.Direction sortDirection) {
        Comparator<RouteEntity> comparator = getComparator(sortField, sortDirection);

        return allRoutes.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private static Comparator<RouteEntity> getComparator(String sortField, Sort.Direction sortDirection) {
        sortField = sortField.startsWith("<") ? sortField.substring(1) : sortField;

        Comparator<RouteEntity> comparator;

        switch (sortField) {
            case "id":
                comparator = Comparator.comparing(route -> route.getId());
                break;
            case "name":
                comparator = Comparator.comparing(RouteEntity::getName);
                break;
            case "creationDate":
                comparator = Comparator.comparing(RouteEntity::getCreationDate);
                break;
            case "locationFrom.id":
                comparator = Comparator.comparing(route -> route.getLocationFrom().getId());
                break;
            case "locationFrom.coordinates.x":
                comparator = Comparator.comparing(route -> route.getLocationFrom().getCoordinates().getX());
                break;
            case "locationFrom.coordinates.y":
                comparator = Comparator.comparing(route -> route.getLocationFrom().getCoordinates().getY());
                break;
            case "locationFrom.name":
                comparator = Comparator.comparing(route -> route.getLocationFrom().getName());
                break;
            case "locationTo.id":
                comparator = Comparator.comparing(route -> route.getLocationTo().getId());
                break;
            case "locationTo.coordinates.x":
                comparator = Comparator.comparing(route -> route.getLocationTo().getCoordinates().getX());
                break;
            case "locationTo.coordinates.y":
                comparator = Comparator.comparing(route -> route.getLocationTo().getCoordinates().getY());
                break;
            case "locationTo.name":
                comparator = Comparator.comparing(route -> route.getLocationTo().getName());
                break;
            case "distance":
                comparator = Comparator.comparing(RouteEntity::getDistance);
                break;
            default:
                throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }

        if (sortDirection == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

}
