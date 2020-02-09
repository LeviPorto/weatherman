package com.levi.weatherman.controller;

import com.levi.weatherman.dto.SolTemperatureDTO;
import com.levi.weatherman.service.SolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolController {

    private SolService service;

    public SolController(SolService service) {
        this.service = service;
    }

    @GetMapping("/nasa/temperature")
    public SolTemperatureDTO findTemperatureBySol(@RequestParam(defaultValue = "SOL", required = false) String Sol) {
        return service.findTemperatureBySol(Sol);
    }

}
