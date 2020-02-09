package com.levi.weatherman.controller;

import com.levi.weatherman.exception.SolDoesNotExistException;
import com.levi.weatherman.dto.SolTemperatureDTO;
import com.levi.weatherman.service.SolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SolController {

    private SolService service;

    public SolController(SolService service) {
        this.service = service;
    }

    @GetMapping("/nasa/temperature")
    public List<SolTemperatureDTO> findTemperatureBySol(@RequestParam(value = "SOL", required = false) String Sol)
            throws SolDoesNotExistException {
        return service.findTemperaturesBySol(Sol);
    }

}
