package com.example.routes.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PageService {
    private static final int DEFAULT_LIMIT = 3;
    private static final int DEFAULT_OFFSET = 1;


    public static PageRequest getPageRequest(Integer limitParam, Integer offsetParam, boolean sortAsc, List<String> sortingFields) {
        int limit = limitParam == null ? DEFAULT_LIMIT : limitParam;
        int offset = offsetParam == null ? DEFAULT_OFFSET : offsetParam;
        int page = offset / limit;
        List<String> resultSort = new ArrayList<>();
        for (String s : sortingFields) {
            if (s.equals("x")) {
                resultSort.add("coordinatesX");
            } else if (s.equals("y")) {
                resultSort.add("coordinatesY");
            } else if (s.equals("name")) {
                resultSort.add("name");
            } else if (s.equals("locationName")) {
                resultSort.add("locationName");
            } else if (s.equals("creationDate")) {
                resultSort.add("creationDate");
            } else if (s.equals("distance")) {
                resultSort.add("distance");
            } else if (s.equals("locationId")) {
                resultSort.add("locationId");
            } else {
                resultSort.add(s);
            }
        }
        String sorts = resultSort.stream().map(Object::toString).collect(Collectors.joining(","));
        return sorts.isEmpty() ? PageRequest.of(page, limit)
                : PageRequest.of(page, limit, JpaSort.unsafe( sortAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sorts));
    }
}

