package com.example.routes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteDTO {
    private Long id;
    @NotNull
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