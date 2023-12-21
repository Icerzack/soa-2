package com.soa.second.soa_second.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    @Digits(message = "id should be Long number", integer = 15, fraction = 0)
    @JsonProperty("id")
    private Long id;
    @JsonProperty("creationDate")
    private String creationDate;
    @NotNull
    @NotBlank(message = "The name should not be empty")
    @JsonProperty("name")
    private String name;
    @NotNull
    @Valid
    @JsonProperty("from")
    private LocationDTO from;

    @NotNull
    @Valid
    @JsonProperty("to")
    private LocationDTO to;
    @NotNull
    @Min(value = 1, message = "The distance must be no less than 1")
    @JsonProperty("distance")
    private Float distance;
}