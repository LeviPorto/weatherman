package com.levi.weatherman.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levi.weatherman.dto.SolDTO;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Objects;

@Component
public class SolExtractor {

    private ObjectMapper objectMapper;

    public SolExtractor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SolDTO extractSol(LinkedHashMap<String, Object> body, String sol) {
       return objectMapper.convertValue(Objects.requireNonNull(body).get(sol), SolDTO.class);
    }

}
