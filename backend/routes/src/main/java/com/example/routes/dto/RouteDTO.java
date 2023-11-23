package com.example.routes.dto;

import com.example.routes.entity.RouteEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Accessors(chain = true)
public class RouteDTO {
    @JsonIgnore
    private Long id;
    @NotNull
    @JsonIgnore
    private String creationDate;
    @NotNull
    @NotBlank(message = "The name should not be empty")
    @Max(value = 256, message = "The name must be no more than 256 characters")
    private String name;
    @NotNull
    private LocationDTO from;
    @NotNull
    private LocationDTO to;
    @NotNull
    @Min(value = 1, message = "The distance must be no less than 1")
    private Float distance;
}