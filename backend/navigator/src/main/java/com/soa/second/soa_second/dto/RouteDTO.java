package com.soa.second.soa_second.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RouteDTO {
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