package com.example.routes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties
public class QueryDTO {
    private List<Long> id = null;
    private List<String> name = null;
    private List<String> creationDate = null;
    private List<Integer> locationId = null;
    private List<Integer> coordinatesX = null;
    private List<Float> coordinatesY = null;
    private List<String> locationName = null;
    private List<Float> distance = null;
    private Sort.Direction sortDirection;
    private String sort;
    private Integer page;
    private Integer pagesCount;
}
