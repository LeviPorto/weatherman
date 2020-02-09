package com.levi.weatherman.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolTemperatureDTO {

    private String Sol;
    private Double averageTemperature;

}
