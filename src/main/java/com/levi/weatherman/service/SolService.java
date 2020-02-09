package com.levi.weatherman.service;

import com.levi.weatherman.exception.SolDoesNotExistException;
import com.levi.weatherman.client.NasaRestTemplate;
import com.levi.weatherman.dto.SolTemperatureDTO;
import com.levi.weatherman.extractor.SolExtractor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SolService {

    public static final String SOL_KEYS_PARAMETER = "sol_keys";

    private NasaRestTemplate nasaRestTemplate;
    private SolExtractor extractor;

    public SolService(NasaRestTemplate nasaRestTemplate, SolExtractor solExtractor) {
        this.nasaRestTemplate = nasaRestTemplate;
        this.extractor = solExtractor;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SolTemperatureDTO> findTemperaturesBySol(String sol) throws SolDoesNotExistException {
        HttpEntity<LinkedHashMap> response = nasaRestTemplate.getSolsInformationFromNasa();
        LinkedHashMap<String, Object> body = response.getBody();
        if(!StringUtils.isEmpty(sol)) {
            return getSpecificSolTemperatureIfExist(sol, body);
        } else {
            return getAllAvailableSolsTemperature(body);
        }
    }

    private List<SolTemperatureDTO> getSpecificSolTemperatureIfExist(String sol, LinkedHashMap<String, Object> body) {
        if(Objects.requireNonNull(body).containsKey(sol)) {
            return Collections.singletonList(constructSolTemperature(body, sol));
        } else {
            throw new SolDoesNotExistException();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<SolTemperatureDTO> getAllAvailableSolsTemperature(LinkedHashMap<String, Object> body) {
        ArrayList<Object> availableSols = (ArrayList) Objects.requireNonNull(body).get(SOL_KEYS_PARAMETER);
        return availableSols.stream().map(sol -> constructSolTemperature(body, (String) sol)).collect(Collectors.toList());
    }

    private SolTemperatureDTO constructSolTemperature(LinkedHashMap<String, Object> body, String sol) {
        return SolTemperatureDTO.builder().sol(sol)
                .averageTemperature(extractor.extractSol(body, sol).getAtDTO().getAv()).build();
    }


}
