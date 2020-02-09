package com.levi.weatherman.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SolDTO {

    @JsonProperty("AT")
    private AtDTO atDTO;

}
