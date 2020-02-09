package com.levi.weatherman.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolTemperatureDTO {

    private String sol;
    private Double averageTemperature;

    @Override
    public boolean equals(Object object) {
        SolTemperatureDTO solTemperature = (SolTemperatureDTO) object;
        return this.averageTemperature.equals(solTemperature.averageTemperature) && this.sol.equals(solTemperature.sol);
    }

}
