package com.soa.second.soa_second.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class GetRoutesResponseDTO {
    @NotNull
    private List<RouteDTO> routes;
    @JsonIgnore
    private int page;
    @JsonIgnore
    private int elementsCount;

}
