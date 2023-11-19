package com.example.routes.service;

import com.example.routes.dto.QueryDTO;
import com.example.routes.exception.NotValidParamsException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.example.routes.service.SortService.getSortDirection;

@Service
@AllArgsConstructor
public class FilterService {
    public static void isValidRequestParams(QueryDTO dto) {
        if (dto.getId() != null) {
            dto.getId().forEach(id -> checkNumberIsNatural("id", id));
        }
        if (dto.getName() != null) {
            dto.getName().forEach(name -> validateName(name));
        }
        if (dto.getCreationDate() != null) {
            dto.getCreationDate().forEach(creationDate -> validateCreationDate(creationDate));
        }
        if (dto.getLocationId() != null) {
            dto.getLocationId().forEach(locationId -> checkNumberIsNatural("location_id", locationId));
        }
        if (dto.getCoordinatesX() != null) {
            dto.getCoordinatesX().forEach(x -> validateCoordinateX(x));
        }
        if (dto.getCoordinatesY() != null) {
            dto.getCoordinatesY().forEach(y -> validateCoordinateY(y));
        }
        if (dto.getLocationName() != null) {
            dto.getLocationName().forEach(locationName -> validateName(locationName));
        }
        if (dto.getDistance() != null) {
            dto.getDistance().forEach(distance -> validateDistance(distance));
        }
        if (dto.getPage() != null) {
            checkNumberIsNatural("page", dto.getPage());
        }
        if (dto.getPagesCount() != null) {
            checkNumberIsNatural("pagesCount", dto.getPagesCount());
        }
        if (dto.getSort() != null && !dto.getSort().isEmpty()) {
            try {
                Sort.Direction sortDirection = getSortDirection(dto.getSort());
                dto.setSortDirection(sortDirection);
            } catch (IllegalArgumentException e) {
                throw new NotValidParamsException("Invalid sort field: " + dto.getSort());
            }
        }
    }

    public static void validateName(String name) {
        checkEmpty(name);
        if (name.length() > 256) {
            throw new NotValidParamsException("Name length should be no more than 256");
        }
        if (name.isEmpty()) {
            throw new NotValidParamsException("Name length should be greater than 0");
        }
    }

    public static void checkNumberIsNatural(String nameOfField, Number number) {
        if (number.longValue() <= 0) {
            throw new NotValidParamsException(nameOfField + " should be a natural number");
        }
    }

    public static void checkEmpty(String value) {
        if (value == null || value.isEmpty()) {
            throw new NotValidParamsException("Fields should not be empty");
        }
    }
    public static void validateCreationDate(String creationDate) {
        String correctDataExample = "2023-09-12T00:00:00Z";
        String dateRegex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z";

        if (!Pattern.matches(dateRegex, creationDate)) {
            throw new NotValidParamsException("Creation date " + creationDate + " cannot be parsed.\n" + "Correct format: " + correctDataExample);
        }
    }
    public static void validateCoordinateX(Integer x) {
    }

    public static void validateCoordinateY(Float y) {
        if (y > 488) {
            throw new NotValidParamsException("Y coordinate should not be greater than 488");
        }
    }

    public static void validateDistance(Float distance) {
    }
}
