package com.soa.second.soa_second.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorDefaultDTO {
    private String message;
}
