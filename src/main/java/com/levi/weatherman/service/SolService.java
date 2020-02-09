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

    private NasaRestTemplate nasaRestTemplate;
    private SolExtractor solExtractor;

    public SolService(NasaRestTemplate nasaRestTemplate, SolExtractor solExtractor) {
        this.nasaRestTemplate = nasaRestTemplate;
        this.solExtractor = solExtractor;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SolTemperatureDTO> findTemperaturesBySol(String sol) throws SolDoesNotExistException {
        HttpEntity<LinkedHashMap> response = nasaRestTemplate.getSolsInformationFromNasa();
        LinkedHashMap<String, Object> body = response.getBody();
        if(!StringUtils.isEmpty(sol)) {
            return getSpecificSolIfExist(sol, body);
        } else {
            return getAllAvailableSolsTemperature(body);
        }
    }

    private List<SolTemperatureDTO> getSpecificSolIfExist(String sol, LinkedHashMap<String, Object> body) {
        if(Objects.requireNonNull(body).containsKey(sol)) {
            return Collections.singletonList(constructSolTemperatureDTO(body, sol));
        } else {
            throw new SolDoesNotExistException("This SOL is not available more");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<SolTemperatureDTO> getAllAvailableSolsTemperature(LinkedHashMap<String, Object> body) {
        ArrayList<Object> availableSols = (ArrayList) Objects.requireNonNull(body).get("sol_keys");
        return availableSols.stream().map(object -> constructSolTemperatureDTO(body, (String) object)).collect(Collectors.toList());
    }

    private SolTemperatureDTO constructSolTemperatureDTO(LinkedHashMap<String, Object> body, String object) {
        return SolTemperatureDTO.builder().Sol(object)
                .averageTemperature(solExtractor.extractSol(body, object).getAtDTO().getAv()).build();
    }


}
