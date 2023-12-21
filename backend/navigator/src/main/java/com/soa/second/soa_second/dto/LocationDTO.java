package com.soa.second.soa_second.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Accessors(chain = true)
public class LocationDTO {
    @JsonProperty("id")
    private Long id;

    @NotNull
    @Valid
    @JsonProperty("coordinates")
    private CoordinateDTO coordinates;

    @NotBlank(message = "The name should not be empty")
    @JsonProperty("name")
    private String name;
}

