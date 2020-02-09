package com.levi.weatherman.service;

import com.levi.weatherman.exception.SolDoesNotExistException;
import com.levi.weatherman.client.NasaRestTemplate;
import com.levi.weatherman.dto.SolDTO;
import com.levi.weatherman.dto.SolTemperatureDTO;
import com.levi.weatherman.extractor.SolExtractor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SolService {

    private NasaRestTemplate nasaRestTemplate;
    private SolExtractor solExtractor;

    public SolService(NasaRestTemplate nasaRestTemplate, SolExtractor solExtractor) {
        this.nasaRestTemplate = nasaRestTemplate;
        this.solExtractor = solExtractor;
    }

    @SuppressWarnings("rawtypes")
    public List<SolTemperatureDTO> findTemperaturesBySol(String sol) throws SolDoesNotExistException {
        HttpEntity<LinkedHashMap> response = nasaRestTemplate.getSolsInformationFromNasa();
        LinkedHashMap body = response.getBody();
        if(!StringUtils.isEmpty(sol)) {
            if(Objects.requireNonNull(body).containsKey(sol)) {
                SolDTO solDTO = solExtractor.extractSol(body, sol);
                return Collections.singletonList(findTemperatureBySol(sol, solDTO));
            } else {
                throw new SolDoesNotExistException("This SOL is not available more");
            }
        } else {
            List<SolTemperatureDTO> solTemperatureDTOS = new ArrayList<>();
            for(Object object : (ArrayList) Objects.requireNonNull(body).get("sol_keys")) {
                SolDTO solDTO = solExtractor.extractSol(body, (String) object);
                solTemperatureDTOS.add(findTemperatureBySol((String) object, solDTO));
            }
            return solTemperatureDTOS;
        }


    }

    public SolTemperatureDTO findTemperatureBySol(String sol, SolDTO solDTO) {
        return SolTemperatureDTO.builder().Sol(sol).averageTemperature(solDTO.getAtDTO().getAv()).build();
    }

}
